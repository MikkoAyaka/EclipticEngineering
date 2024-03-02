package org.wolflink.minecraft.plugin.eclipticengineering.structure.resource

import kotlinx.coroutines.delay
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.block.BlockBreakEvent
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GameStructureBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.isOre
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.resource.virtual.VirtualTeamInventory
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticengineering.utils.RandomAPI
import org.wolflink.minecraft.plugin.eclipticengineering.utils.toRandomDistribution
import org.wolflink.minecraft.plugin.eclipticstructure.coroutine.SingletonJob
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureCompletedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class MiningPlace private constructor(blueprint: GameStructureBlueprint, builder: Builder) :
    GameStructure(StructureType.MINING_PLACE, blueprint, builder, 1), IStructureListener {
    override val customListeners: List<IStructureListener> = listOf(this)
    private val blockResourceJob = SingletonJob {
        while (available) {
            delay(1000 * RandomAPI.nextLong(6, 12))
            val location =
                stoneLocations.filter { it.block.type == Material.STONE }.randomOrNull() ?: return@SingletonJob
            EclipticEngineering.runTask {
                location.block.type = oreDistribution.random()
                location.world.playSound(location, Sound.BLOCK_STONE_PLACE, 1.5f, 1.2f)
            }
        }
    }
    private val virtualResourceJob = SingletonJob {
        while (available) {
            delay(1000 * 60)
            VirtualTeamInventory.add(VirtualResourceType.METAL, 10.0)
        }
    }
    private val stoneLocations = mutableSetOf<Location>()
    override fun completed(e: StructureCompletedEvent) {
        // 绑定作物坐标
        zone.forEach { world, x, y, z ->
            with(world.getBlockAt(x, y, z)) {
                if (type == Material.STONE) stoneLocations.add(this.location)
            }
        }
    }

    override fun onBlockBreak(e: BlockBreakEvent) {
        if (e.block.location in stoneLocations && e.block.type.isOre()) {
            e.isCancelled = false
            e.isDropItems = true
            Bukkit.getScheduler().runTaskLater(EclipticEngineering.instance, Runnable {
                e.block.type = Material.STONE
            }, 1)
        }
    }

    override fun onAvailable(e: StructureAvailableEvent) {
        blockResourceJob.tryLaunch(EEngineeringScope)
        virtualResourceJob.tryLaunch(EEngineeringScope)
    }

    companion object : StructureCompanion<MiningPlace>() {
        private val oreDistribution = mapOf(
            Material.COAL_ORE to 8,
            Material.COPPER_ORE to 5,
            Material.IRON_ORE to 12,
            Material.GOLD_ORE to 7,
            Material.REDSTONE_ORE to 4,
            Material.LAPIS_ORE to 4,
            Material.DIAMOND_ORE to 2
        ).toRandomDistribution()
        private const val STRUCTURE_NAME = "采矿场"
        override fun supplier(blueprint: Blueprint, builder: Builder): MiningPlace {
            return MiningPlace(blueprint as GameStructureBlueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                STRUCTURE_NAME,
                60,
                4500,
                true,
                setOf(
                    GameStructureTag.AMOUNT_LIMITED,
                    GameStructureTag.COMMON_RESOURCE_GENERATOR
                ),
                setOf(
                    VirtualRequirement(VirtualResourceType.WOOD, 8),
                    VirtualRequirement(VirtualResourceType.STONE, 8)
                ),
                setOf(
                    VirtualRequirement(VirtualResourceType.WOOD, 30),
                    VirtualRequirement(VirtualResourceType.STONE, 30),
                    VirtualRequirement(VirtualResourceType.METAL, 30),
                    AbilityCondition(Ability.BUILDING, 2)
                )
            )
        )
    }
}