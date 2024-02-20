package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.MainMenuItem
import java.util.UUID

object FirstJoinListener: Listener {
    // 缓存列表，玩家进入服务器后会被记录到该列表，直至服务器重启
    private val cacheSet = mutableSetOf<UUID>()
    private fun firstJoin(player: Player) {
        player.exp = 0f
        player.level = 0
        player.teleport(Config.lobbyLocation)
        player.gameMode = GameMode.ADVENTURE
        player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value!!
        player.foodLevel = 20
        player.inventory.clear()
        player.activePotionEffects.forEach { player.removePotionEffect(it.type) }
        MainMenuItem.give(player)
    }
    @EventHandler
    fun on(e: PlayerJoinEvent) {
        val uuid = e.player.uniqueId
        if(!cacheSet.contains(uuid)) {
            cacheSet.add(uuid)
            firstJoin(e.player)
        }
    }
}