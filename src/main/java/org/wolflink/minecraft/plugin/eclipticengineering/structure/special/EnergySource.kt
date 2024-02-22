package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureZoneRelationRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.ZoneRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Zone
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class EnergySource private constructor(blueprint: Blueprint, builder: Builder) : GameStructure(blueprint, builder, 1),
    IStructureListener {
    override val tags = setOf(
        GameStructureTag.AMOUNT_LIMITED,
        GameStructureTag.ENERGY_SUPPLY
    )
    override val customListeners by lazy { listOf(this) }

    companion object : StructureCompanion<EnergySource>() {
        // 影响半径(格)
        private const val EFFECT_RADIUS = 35
        private const val STRUCTURE_NAME = "幽光能量发生场"
        override val clazz: Class<EnergySource> = EnergySource::class.java
        override fun supplier(blueprint: Blueprint, builder: Builder): EnergySource {
            return EnergySource(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                STRUCTURE_NAME,
                60,
                3000,
                ItemRequirement("需要 1 圆石", ItemStack(Material.COBBLESTONE)),
                ItemRequirement("需要 1 铁锭", ItemStack(Material.IRON_INGOT)),
                VirtualRequirement("需要 15 石料", VirtualResourceType.STONE, 15),
                VirtualRequirement("需要 40 木材", VirtualResourceType.WOOD, 40)
            )
        )
    }

    /**
     * 影响其它建筑结构
     */
    private suspend fun effectStructures() {
        val center = builder.buildLocation
        val effectZone = Zone(
            center.world.name,
            center.blockX - EFFECT_RADIUS..center.blockX + EFFECT_RADIUS,
            center.blockY - EFFECT_RADIUS..center.blockY + EFFECT_RADIUS,
            center.blockZ - EFFECT_RADIUS..center.blockZ + EFFECT_RADIUS
        )
        while (true) {
            // 能源中心生效时对范围内建筑供能
            if (available) {
                // 在影响范围内的建筑结构
                val effectedStructures = ZoneRepository.findByOverlap(effectZone)
                    .mapNotNull(StructureZoneRelationRepository::find1)
                    .toSet()
                // 不在影响范围内的建筑结构
                val notEffectedStructures = StructureRepository.findAll() - effectedStructures
                notEffectedStructures.forEach {
                    it.doDamage(5 + (0.03 * (blueprint.maxDurability - durability)), DamageSource.LACK_OF_ENERGY, this)
                }
            }
            // 能源中心失效，所有建筑开始失去耐久
            else {
                StructureRepository.findAll().forEach {
                    it.doDamage(5 + (0.03 * (blueprint.maxDurability - durability)), DamageSource.LACK_OF_ENERGY, this)
                }
            }

            // 每秒检查一次
            delay(1000)
        }
    }

    private var effectJob: Job? = null
    override fun onAvailable(e: StructureAvailableEvent) {
        if (effectJob == null || effectJob!!.isCompleted) effectJob = EEngineeringScope.launch { effectStructures() }
    }
}