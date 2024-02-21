package org.wolflink.minecraft.plugin.eclipticengineering.structure.survival

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureUnavailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class LargeCampfire private constructor(
    blueprint: Blueprint, builder: Builder
) : GameStructure(blueprint, builder),IStructureListener {
    override val tags: Set<GameStructureTag> = setOf()
    override val customListeners = listOf(this)
    companion object : StructureCompanion<LargeCampfire>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): LargeCampfire {
            return LargeCampfire(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "大型营火",
                5,
                3000,
                ItemRequirement("需要 1 圆石", ItemStack(Material.COBBLESTONE)),
                ItemRequirement("需要 1 铁锭", ItemStack(Material.COBBLESTONE))
            )
        )
        override val clazz: Class<LargeCampfire> = LargeCampfire::class.java
    }

    private suspend fun healTask() {
        while (available) {
            builder.buildLocation.getNearbyEntitiesByType(Player::class.java,10.0).forEach {
                it.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION,20 * 5,0))
            }
            delay(5 * 1000)
        }
    }
    private var job: Job? = null
    override fun onAvailable(e: StructureAvailableEvent) {
         if(job == null || job?.isCompleted == true) job = EEngineeringScope.launch { healTask() }
    }
    override fun onUnavailable(e: StructureUnavailableEvent) {
    }
}