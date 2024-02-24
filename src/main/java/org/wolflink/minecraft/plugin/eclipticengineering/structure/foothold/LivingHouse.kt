package org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.data.type.Door
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructureTag
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureCompletedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureDestroyedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.register
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.unregister
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class LivingHouse private constructor(
    blueprint: Blueprint, builder: Builder
) : GameStructure(StructureType.LIVING_HOUSE, blueprint, builder,1),IStructureListener,Listener {
    override val customListeners = listOf<IStructureListener>(this)
    private val doorOwners = mutableMapOf<Player,Block>()
    // 夜晚效果
    private suspend fun effectNightBuff() {
        while (available) {
            EclipticEngineering.runTask {
                gamingPlayers.filter { it in zone }.forEach{
                    it.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20 * 10,1))
                }
            }
            delay(1000 * 10)
        }
    }
    override fun completed(e: StructureCompletedEvent) {
        this.register(EclipticEngineering.instance)
        EEngineeringScope.launch { effectNightBuff() }
    }

    override fun destroyed(e: StructureDestroyedEvent) {
        this.unregister()
    }
    @EventHandler
    fun on(e: PlayerInteractEvent) {
        if (e.hasBlock()) {
            val block: Block = e.clickedBlock!!
            // 与建筑区域的铁门交互
            if (block.type == Material.IRON_DOOR && block.location in zone) {
                val doorBlock = doorOwners[e.player]
                // 交互的自己的门
                if(block == doorBlock || block.getRelative(0,1,0) == doorBlock || block.getRelative(0,-1,0) == doorBlock) {
                    block.blockData = (block.blockData as Door).apply { isOpen = !isOpen }
                }
                // 玩家还没有绑定铁门
                else if(doorBlock == null) {
                    doorOwners[e.player] = block
                    e.player.sendMessage("$MESSAGE_PREFIX 你使用钥匙打开了这扇门，这个房间是你的了。".toComponent())
                    e.player.playSound(e.player, Sound.ENTITY_VILLAGER_YES,1f,1.5f)
                }
                // 交互别人的门
                else {
                    val owner = doorOwners.filter { it.value == doorBlock }.map { it.key }.firstOrNull()
                    if(owner == null) e.player.sendMessage("$MESSAGE_PREFIX 你没有匹配的钥匙打开这扇门。".toComponent())
                    else e.player.sendMessage("$MESSAGE_PREFIX 你无法打开这扇门，这是 ${owner.name} 的房间。".toComponent())
                    e.player.playSound(e.player, Sound.ENTITY_VILLAGER_NO,1f,1.2f)
                }
            }
        }
    }


    companion object : StructureCompanion<LivingHouse>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): LivingHouse {
            return LivingHouse(blueprint, builder)
        }

        override val blueprints = listOf(
            ConditionBlueprint(
                1,
                "居住屋",
                120,
                20000,
                setOf(
                    GameStructureTag.AMOUNT_LIMITED
                ),
                VirtualRequirement(VirtualResourceType.STONE, 90),
                VirtualRequirement(VirtualResourceType.WOOD, 90),
                AbilityCondition(Ability.BUILDING,3)
            )
        )
    }
}