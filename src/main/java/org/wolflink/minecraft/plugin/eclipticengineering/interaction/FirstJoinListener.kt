package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.extension.reset
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.MainMenuItem
import org.wolflink.minecraft.wolfird.framework.bukkit.attribute.ModifierData
import java.util.UUID

object FirstJoinListener: Listener {
    // 缓存列表，玩家进入服务器后会被记录到该列表，直至服务器重启
    private val cacheSet = mutableSetOf<UUID>()
    private fun firstJoin(player: Player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.run {
            if(getModifier(UUID.fromString("C1BE8DB2-148F-CA78-7C32-7C7D206C3DD2")) == null) {
                addModifier(AttributeModifier(UUID.fromString("C1BE8DB2-148F-CA78-7C32-7C7D206C3DD2"),"额外初始生命值",20.0,AttributeModifier.Operation.ADD_NUMBER))
            }
        }
        player.reset()
        player.teleport(Config.lobbyLocation)
        player.gameMode = GameMode.ADVENTURE
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