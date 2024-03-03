package org.wolflink.minecraft.plugin.eclipticengineering.structure.resource

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
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
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureCompletedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class LoggingPlace private constructor(blueprint: GameStructureBlueprint, builder: Builder) :
    GameStructure(StructureType.LOGGING_PLACE, blueprint, builder, 1),IStructureListener {
    override val customListeners: List<IStructureListener> = listOf(this)
    // 区域内有效木头
    private val logLocations = mutableSetOf<Location>()
    override fun completed(e: StructureCompletedEvent) {
        // 绑定木头坐标
        zone.forEach { world, x, y, z ->
            with(world.getBlockAt(x, y, z)) {
                if(type == Material.OAK_LOG) logLocations.add(this.location)
            }
        }
    }
    private fun generateResource(location: Location) {
        Bukkit.getScheduler().runTaskLater(EclipticEngineering.instance, Runnable {
            if(available) {
                location.block.type = Material.OAK_LOG
                location.world.playSound(location, Sound.ENTITY_ITEM_PICKUP,1.5f,1.2f)
            }
        },20L * RandomAPI.nextInt(10,20))
    }
    override fun onBlockBreak(e: BlockBreakEvent) {
        if(e.block.location in logLocations && e.block.type == Material.OAK_LOG) {
            generateResource(e.block.location)
            e.isCancelled = false
            e.isDropItems = true
        }
    }
    companion object : StructureCompanion<LoggingPlace>() {
        private const val STRUCTURE_NAME = "伐木场"
        override fun supplier(blueprint: Blueprint, builder: Builder): LoggingPlace {
            return LoggingPlace(blueprint as GameStructureBlueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                STRUCTURE_NAME,
                30,
                3000,
                setOf(
                    GameStructureTag.AMOUNT_LIMITED,
                    GameStructureTag.COMMON_RESOURCE_GENERATOR
                ),
                setOf(VirtualRequirement(VirtualResourceType.WOOD, 12)),
                setOf(VirtualRequirement(VirtualResourceType.WOOD, 90),
                    VirtualRequirement(VirtualResourceType.STONE, 45),
                    AbilityCondition(Ability.BUILDING,2))
            )
        )
    }
}