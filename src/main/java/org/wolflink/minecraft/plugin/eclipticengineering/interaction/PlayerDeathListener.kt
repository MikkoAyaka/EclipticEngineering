package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import java.time.Duration

object PlayerDeathListener: Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun on(e: PlayerDeathEvent) {
        e.isCancelled = true
        e.player.gameMode = GameMode.SPECTATOR
        e.player.playSound(e.player, Sound.ENTITY_WOLF_DEATH,1f,0.7f)
        e.player.showTitle(Title.title("<red>死".toComponent(),"<white>你已阵亡，请寻找最近的重生信标以复活".toComponent(),
            Title.Times.times(
                Duration.ofMillis(500),
                Duration.ofMillis(1000),
                Duration.ofMillis(500)
            )
        ))
        Bukkit.broadcast("$MESSAGE_PREFIX <red>玩家 <white>${e.player.name} <red>阵亡了！".toComponent())
    }
}