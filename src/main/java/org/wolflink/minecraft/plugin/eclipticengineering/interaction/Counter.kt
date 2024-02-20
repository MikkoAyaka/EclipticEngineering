package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticstructure.extension.*
import java.time.Duration

/**
 * 倒计时计数器
 */
object Counter {
    suspend fun count(timeInSeconds: Int,mainTitle: Component = "".toComponent()) {
        var count = timeInSeconds
        repeat(timeInSeconds) {
            val title = Title.title(
                mainTitle,
                "${(RED_COLOR to GREEN_COLOR ofGradient count.toDouble()/timeInSeconds).toHexFormat()}<bold>$count".toComponent(),
                Title.Times.times(
                    Duration.ofMillis(150),
                    Duration.ofMillis(700),
                    Duration.ofMillis(150)
                )
            )
            EclipticEngineering.runTask {
                Bukkit.getOnlinePlayers().forEach {
                    it.showTitle(title)
                    it.playSound(it, Sound.BLOCK_NOTE_BLOCK_BELL,1f,1.4f)
                }
            }
            count--
            delay(1000)
        }
    }
}