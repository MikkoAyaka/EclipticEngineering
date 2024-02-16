package org.wolflink.minecraft.plugin.eclipticengineering.structure.special

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.RespawnBeaconBlueprint
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class RespawnBeacon private constructor(blueprint: RespawnBeaconBlueprint,builder: Builder): Structure(blueprint,builder),IStructureListener {
    override val customListener: IStructureListener by lazy { this }
    private val chargeSeconds = blueprint.chargeSeconds
    private val maxChargeAmount = blueprint.chargeAmount
    private var nowChargeAmount = 0
    companion object : StructureCompanion<RespawnBeacon>(){
        override fun supplier(blueprint: Blueprint, builder: Builder): RespawnBeacon {
            return RespawnBeacon(blueprint as RespawnBeaconBlueprint, builder)
        }
        override val blueprints = listOf(RespawnBeaconBlueprint(
            1,
            "幽光充能信标",
            5,
            1000,
            180,
            4,
            ItemStack(Material.COBBLESTONE,128),
            ItemStack(Material.IRON_INGOT,16),
            ItemStack(Material.GOLD_INGOT,8)
        ))
        override val clazz: Class<RespawnBeacon> = RespawnBeacon::class.java
    }

    private suspend fun charge() {
        while (available) {
            delay(1000L * chargeSeconds)
            if(nowChargeAmount < maxChargeAmount) nowChargeAmount++
        }
    }
    private suspend fun respawnCheck() {
        while (available) {
            delay(1000)
            // 重生信标是否有重生次数
            if(nowChargeAmount == 0) continue
            val respawnPlayers = Bukkit.getOnlinePlayers()
                .filter {
                    it.gameMode == GameMode.SPECTATOR // 观察者模式
                        && it.location.distance(builder.buildLocation) < 8 // 靠近重生信标
                        && it.location.block.getRelative(0,-1,0).type == Material.END_PORTAL_FRAME // 脚下是末地门框架
                }
            Bukkit.getScheduler().runTask(EclipticEngineering.instance, Runnable {
                respawnPlayers.forEach { respawn(it) }
            })
        }

    }
    private fun respawn(player: Player) {
        if(nowChargeAmount > 0) {
            player.level = 0
            player.exp = 0.0f
            player.inventory.clear()
            player.clearActivePotionEffects()
            player.gameMode = GameMode.ADVENTURE
            player.location.world.playSound(player.location, Sound.ITEM_TOTEM_USE,2f,1.2f)
            // TODO 复活粒子效果
            nowChargeAmount--
        }
    }
    private var chargeJob: Job? = null
    private var respawnCheckJob: Job? = null
    override fun onAvailable(e: StructureAvailableEvent) {
        if(chargeJob == null || chargeJob!!.isCompleted) chargeJob = EEngineeringScope.launch { charge() }
        if(respawnCheckJob == null || respawnCheckJob!!.isCompleted) EEngineeringScope.launch { respawnCheck() }
    }
}