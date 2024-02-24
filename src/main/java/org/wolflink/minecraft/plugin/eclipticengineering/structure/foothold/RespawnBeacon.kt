package org.wolflink.minecraft.plugin.eclipticengineering.structure.foothold

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.RespawnBeaconBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class RespawnBeacon private constructor(blueprint: RespawnBeaconBlueprint, builder: Builder) :
    GameStructure(StructureType.RESPAWN_BEACON,blueprint, builder, 1), IStructureListener {
    override val customListeners by lazy { listOf(this) }
    private val chargeSeconds = blueprint.chargeSeconds
    private val maxChargeAmount = blueprint.chargeAmount
    private var nowChargeAmount = 0

    companion object : StructureCompanion<RespawnBeacon>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): RespawnBeacon {
            return RespawnBeacon(blueprint as RespawnBeaconBlueprint, builder)
        }

        override val blueprints = listOf(
            RespawnBeaconBlueprint(
                1,
                "幽光充能信标",
                120,
                10000,
                180,
                4,
                VirtualRequirement(VirtualResourceType.STONE, 150),
                VirtualRequirement(VirtualResourceType.METAL,50),
                AbilityCondition(Ability.BUILDING,3)
            )
        )
    }

    private suspend fun charge() {
        while (available) {
            delay(1000L * chargeSeconds)
            if (nowChargeAmount < maxChargeAmount) nowChargeAmount++
        }
    }

    private suspend fun respawnCheck() {
        while (available) {
            delay(1000)
            // 重生信标是否有重生次数
            if (nowChargeAmount == 0) continue
            val respawnPlayers = Bukkit.getOnlinePlayers()
                .filter {
                    it.gameMode == GameMode.SPECTATOR // 观察者模式
                            && it.location.distance(builder.buildLocation) < 8 // 靠近重生信标
                            && it.location.block.getRelative(0, -1, 0).type == Material.END_PORTAL_FRAME // 脚下是末地门框架
                }
            Bukkit.getScheduler().runTask(EclipticEngineering.instance, Runnable {
                respawnPlayers.forEach { respawn(it) }
            })
        }

    }

    private fun respawn(player: Player) {
        if (nowChargeAmount > 0) {
            player.level = 0
            player.exp = 0.0f
            player.inventory.clear()
            player.clearActivePotionEffects()
            player.gameMode = GameMode.SURVIVAL
            player.location.world.playSound(player.location, Sound.ITEM_TOTEM_USE, 2f, 1.2f)
            // TODO 复活粒子效果
            nowChargeAmount--
        }
    }

    private var chargeJob: Job? = null
    private var respawnCheckJob: Job? = null
    override fun onAvailable(e: StructureAvailableEvent) {
        if (chargeJob == null || chargeJob!!.isCompleted) chargeJob = EEngineeringScope.launch { charge() }
        if (respawnCheckJob == null || respawnCheckJob!!.isCompleted) EEngineeringScope.launch { respawnCheck() }
    }
}