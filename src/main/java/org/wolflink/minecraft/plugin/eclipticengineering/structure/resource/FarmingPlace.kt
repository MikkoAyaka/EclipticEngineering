package org.wolflink.minecraft.plugin.eclipticengineering.structure.resource

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.citizensnpcs.trait.Age
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.data.Ageable
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GameStructureBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticengineering.utils.RandomAPI
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureCompletedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class FarmingPlace private constructor(blueprint: GameStructureBlueprint, builder: Builder) :
    GameStructure(StructureType.FARMING_PLACE, blueprint, builder, 1),IStructureListener {
    override val customListeners: List<IStructureListener> = listOf(this)
    private var job: Job? = null
    // 区域内有效作物
    private val cropLocations = mutableSetOf<Location>()
    private suspend fun timerTask() {
        while (available) {
            delay(1000 * 15)
            // 为范围内 30% 的作物生长一次
            cropLocations.map { it.block }.filter { it.blockData is Ageable && RandomAPI.nextDouble() < 0.3 }.forEach {
                EclipticEngineering.runTask {
                    it.blockData = (it.blockData as Ageable).apply { if(age < maximumAge) age++ }
                }
            }
        }
    }

    override fun completed(e: StructureCompletedEvent) {
        // 绑定作物坐标
        zone.forEach { world, x, y, z ->
            with(world.getBlockAt(x, y, z)) {
                if(blockData is Ageable) cropLocations.add(this.location)
            }
        }
    }
    override fun onAvailable(e: StructureAvailableEvent) {
        if(job == null || job?.isCompleted == true) {
            job = EEngineeringScope.launch { timerTask() }
        }
    }
    private fun generateResource(location: Location,material: Material) {
        Bukkit.getScheduler().runTaskLater(EclipticEngineering.instance, Runnable {
            if(available) {
                location.block.type = material
                location.world.playSound(location, Sound.ITEM_CROP_PLANT,1.5f,1.2f)
            }
        },20L * RandomAPI.nextInt(10,20))
    }
    override fun onBlockBreak(e: BlockBreakEvent) {
        if(e.block.location in cropLocations && e.block.blockData is Ageable) {
            if((e.block.blockData as Ageable).run { age != maximumAge }) return
            generateResource(e.block.location,e.block.type)
            e.isCancelled = false
            e.isDropItems = true
        }
    }

    companion object : StructureCompanion<FarmingPlace>() {
        private const val STRUCTURE_NAME = "农场"
        override fun supplier(blueprint: Blueprint, builder: Builder): FarmingPlace {
            return FarmingPlace(blueprint as GameStructureBlueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                STRUCTURE_NAME,
                60,
                3000,
                setOf(
                    GameStructureTag.AMOUNT_LIMITED,
                    GameStructureTag.COMMON_RESOURCE_GENERATOR
                ),
                setOf(VirtualRequirement(VirtualResourceType.WOOD, 15)),
                setOf(ItemRequirement("需要 96 泥土", ItemStack(Material.DIRT, 96)),
                    VirtualRequirement(VirtualResourceType.WOOD, 60),
                    AbilityCondition(Ability.BUILDING,2))
            )
        )
    }
}