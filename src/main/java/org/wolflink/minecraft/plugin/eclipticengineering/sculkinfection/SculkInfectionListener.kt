package org.wolflink.minecraft.plugin.eclipticengineering.sculkinfection

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.wolflink.minecraft.bukkit.wolfblockspread.SpreadType
import org.wolflink.minecraft.bukkit.wolfblockspread.WolfBlockSpreadAPI
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.utils.LocationAPI
import org.wolflink.minecraft.plugin.eclipticengineering.utils.RandomAPI
import org.wolflink.minecraft.plugin.eclipticengineering.utils.SculkSpawnBox
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.wolfird.framework.bukkit.WolfirdListener
import java.util.*

internal class SculkInfectionListener(private val manager: OrnamentSculkInfection) : WolfirdListener() {
    val milkPlayers: MutableSet<UUID> = HashSet<UUID>()
    private val milkCDSet: MutableSet<UUID> = HashSet<UUID>()

    @EventHandler
    fun breakSculk(event: BlockBreakEvent) {
        if (!manager.sculkTypes.contains(event.block.type)) return
        manager.addInfectionValue(event.player, 10)
    }

    @EventHandler
    fun drinkMilk(event: PlayerItemConsumeEvent) {
        val item = event.item
        val player = event.player
        if (item.type != Material.MILK_BUCKET) return
        // 玩家喝了牛奶
        if (milkCDSet.contains(player.uniqueId)) return
        if (manager.getInfectionValue(player.uniqueId) >= 300) {
            milkCDSet.add(player.uniqueId)
            milkPlayers.add(player.uniqueId)
            player.setCooldown(Material.MILK_BUCKET, 20 * 480)
            // 5分钟有效期，期间感染值不会增高
            subScheduler.runTaskLater(Runnable {
                milkPlayers.remove(player.uniqueId)
                player.sendMessage("$MESSAGE_PREFIX 牛奶的效果减退了。".toComponent())
            }, 20 * 300L)
            // 8分钟冷却
            subScheduler.runTaskLater(Runnable {
                milkCDSet.remove(player.uniqueId)
                if (player.isOnline) {
                    player.sendMessage("$MESSAGE_PREFIX 你可以再次饮用牛奶了。".toComponent())
                }
            }, 20 * 480L)
            manager.addInfectionValue(player, -500)
            player.sendMessage("$MESSAGE_PREFIX 喝了牛奶之后你感觉好多了。".toComponent())
        }
    }

    @EventHandler
    fun sculkSpread(event: EntityDeathEvent) {
        if(event.entity.world != Config.gameWorld) return
        Bukkit.getScheduler().runTaskAsynchronously(EclipticEngineering.instance, Runnable {
            // 15%几率生成地基
            if (RandomAPI.nextDouble() <= 0.2) {
                val sculkSpawnBox = SculkSpawnBox(event.entity.location.clone())
                if (sculkSpawnBox.isAvailable) {
                    Bukkit.getScheduler().runTask(EclipticEngineering.instance, sculkSpawnBox::spawn)
                }
            }
        })
    }

    private val r = Random()
    override fun onEnable() {
        subScheduler.runTaskTimerAsync({
            val secs = r.nextInt(180)
            subScheduler.runTaskLater({ autoSculkSpread() }, (20 * secs).toLong())
        }, (20 * 60 * 4).toLong(), (20 * 60 * 6).toLong())
    }

    private fun autoSculkSpread() {
        subScheduler.runTaskAsync {
            val player = gamingPlayers.randomOrNull()
            if (player == null || !player.isOnline) return@runTaskAsync
            for (i in 0..2) {
                var solidLoc: Location = LocationAPI.getLocationByAngle(player.location, r.nextInt(360) - 180.0, 10.0)
                if (!solidLoc.block.type.isSolid()) {
                    solidLoc = LocationAPI.getNearestSolid(solidLoc, 7) ?: continue
                }
                val finalSolidLoc = solidLoc
                Bukkit.getScheduler().runTask(EclipticEngineering.instance,
                    Runnable { WolfBlockSpreadAPI.start(SPREAD_BLUEPRINT_ID, finalSolidLoc) })
                break
            }
        }
    }

    companion object {
        private val SPREAD_BLUEPRINT_ID: Int =
            WolfBlockSpreadAPI.create(Material.SCULK, SpreadType.SINGLE_SPREAD, 20, 40)
    }
}
