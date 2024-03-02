package org.wolflink.minecraft.plugin.eclipticengineering.roleplay

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.wolflink.minecraft.plugin.eclipticengineering.extension.onlinePlayers
import org.wolflink.minecraft.plugin.eclipticengineering.utils.RandomAPI
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

object RangeChat: Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun on(e: AsyncChatEvent) {
        e.isCancelled = true
        val chatter = e.player
        onlinePlayers
            .filter { it.location.distance(chatter.location) <= 20 }
            .forEach {
                if(it.hasLineOfSight(chatter) || it == chatter) {
                    it.sendMessage(chatter.displayName().append(" <gray>说 <white>".toComponent()).append(e.message()))
                } else {
                    val newMessageChars = PlainTextComponentSerializer.plainText().serialize(e.message()).toCharArray()
                    for (i in newMessageChars.indices) {
                        if(RandomAPI.nextDouble() < 0.5) newMessageChars[i] = '?'
                    }
                    it.sendMessage("<yellow>???</yellow> <gray>说 <white>".toComponent().append(newMessageChars.concatToString().toComponent()))
                }
            }
    }
}