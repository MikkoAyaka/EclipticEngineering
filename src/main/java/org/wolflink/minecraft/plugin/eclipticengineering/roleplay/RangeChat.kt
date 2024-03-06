package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.wolflink.minecraft.plugin.eclipticengineering.extension.onlinePlayers
import org.wolflink.minecraft.plugin.eclipticengineering.utils.RandomAPI
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

object RangeChat: Listener {
    private const val CHAT_RANGE = 50
    @EventHandler(priority = EventPriority.HIGHEST)
    fun on(e: AsyncChatEvent) {
        Bukkit.getConsoleSender().sendMessage("<white>${e.player.name} <gray>说 <white>".toComponent().append(e.message()))
        e.isCancelled = true
        val chatter = e.player
        onlinePlayers
            .filter { it.world == chatter.world }
            .filter { it.location.distance(chatter.location) <= CHAT_RANGE }
            .forEach {
                if(it.hasLineOfSight(chatter) || it == chatter || it.location.distance(chatter.location) < CHAT_RANGE * 0.5) {
                    it.sendMessage(chatter.displayName().append(" <gray>说 <white>".toComponent()).append(e.message()))
                } else {
                    val msgLoss = it.location.distance(chatter.location) / CHAT_RANGE - 0.5
                    val newMessageChars = PlainTextComponentSerializer.plainText().serialize(e.message()).toCharArray()
                    for (i in newMessageChars.indices) {
                        if(RandomAPI.nextDouble() < msgLoss) newMessageChars[i] = '?'
                    }
                    it.sendMessage("<yellow>???</yellow> <gray>说 <white>".toComponent().append(newMessageChars.concatToString().toComponent()))
                }
            }
    }
}