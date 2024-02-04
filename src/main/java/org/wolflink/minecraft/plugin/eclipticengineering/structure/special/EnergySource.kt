package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureZoneRelationRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.ZoneRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Zone
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class EnergySource private constructor(blueprint: Blueprint,builder: Builder): Structure(blueprint,builder),IStructureListener{
    override val customListener by lazy { this }
    companion object {
        // 影响半径(格)
        private const val EFFECT_RADIUS = 30
        private const val STRUCTURE_NAME = "幽光能量发生场"
        val blueprints = listOf(
            Blueprint(
                1,
                STRUCTURE_NAME,
                5,
                3000,
                ItemStack(Material.COBBLESTONE, 128),
                ItemStack(Material.IRON_INGOT, 16),
                ItemStack(Material.GOLD_INGOT, 8)
            )
        )

        fun create(structureLevel: Int, builder: Builder): EnergySource {
            val blueprint = blueprints.getOrNull(structureLevel)
                ?: throw IllegalArgumentException("不支持的建筑等级：${structureLevel}")
            return EnergySource(blueprint, builder)
        }
    }

    /**
     * 影响其它建筑结构
     */
    private suspend fun effectStructures() {
        val center = builder.buildLocation
        val effectZone = Zone(
            center.world.name,
            center.blockX-EFFECT_RADIUS..center.blockX+EFFECT_RADIUS,
            center.blockY-EFFECT_RADIUS..center.blockY+EFFECT_RADIUS,
            center.blockZ-EFFECT_RADIUS..center.blockZ+EFFECT_RADIUS
        )
        while (true) {
            // 能源中心生效时对范围内建筑供能
            if(available) {
                // 在影响范围内的建筑结构
                val effectedStructures = ZoneRepository.findByOverlap(effectZone)
                    .map(StructureZoneRelationRepository::find1)
                    .toSet()
                // 不在影响范围内的建筑结构
                val notEffectedStructures = StructureRepository.findAll() - effectedStructures
                notEffectedStructures.forEach {
                    it.doDamage(5 + (0.03 * (blueprint.maxDurability - durability)),DamageSource.LACK_OF_ENERGY,this)
                }
            }
            // 能源中心失效，所有建筑开始失去耐久
            else {
                StructureRepository.findAll().forEach {
                    it.doDamage(5 + (0.03 * (blueprint.maxDurability - durability)),DamageSource.LACK_OF_ENERGY,this)
                }
            }

            // 每秒检查一次
            delay(1000)
        }
    }
    private var effectJob: Job? = null
    override fun onAvailable(e: StructureAvailableEvent) {
        if(effectJob == null || effectJob!!.isCompleted) effectJob = EEngineeringScope.launch { effectStructures() }
    }
}