package org.wolflink.minecraft.plugin.eclipticengineering.structure.survival

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
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
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureUnavailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class LargeCampfire private constructor(
    blueprint: GameStructureBlueprint, builder: Builder
) : GameStructure(StructureType.LARGE_CAMPFIRE, blueprint, builder), IStructureListener {
    override val customListeners = listOf(this)

    companion object : StructureCompanion<LargeCampfire>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): LargeCampfire {
            return LargeCampfire(blueprint as GameStructureBlueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "大型营火",
                30,
                5000,
                setOf(),
                setOf(VirtualRequirement(VirtualResourceType.WOOD, 15)),
                setOf(VirtualRequirement(VirtualResourceType.WOOD, 60),
                    ItemRequirement("需要 1 打火石", ItemStack(Material.FLINT_AND_STEEL)),
                    AbilityCondition(Ability.BUILDING,2))
            )
        )
    }

    private suspend fun healTask() {
        while (available) {
            EclipticEngineering.runTask {
                builder.buildLocation.getNearbyEntitiesByType(Player::class.java, 16.0).forEach {
                    it.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 0))
                }
            }
            delay(5 * 1000)
        }
    }

    private var job: Job? = null
    override fun onAvailable(e: StructureAvailableEvent) {
        if (job == null || job?.isCompleted == true) job = EEngineeringScope.launch { healTask() }
    }

    override fun onUnavailable(e: StructureUnavailableEvent) {
    }
}