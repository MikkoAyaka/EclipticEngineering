package org.wolflink.minecraft.plugin.eclipticengineering.stage.story

import kotlinx.coroutines.delay
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import java.time.Duration

class Story(vararg text: String) {
    private val textList = text.toList()
    suspend fun broadcast() {
        for (text in textList) {
            val delayMills = 300 + 60 * text.length + 500
            EclipticEngineering.runTask {
                Bukkit.getOnlinePlayers().forEach {
                    it.playSound(it, Sound.ENTITY_VILLAGER_AMBIENT,1.2f,0.8f)
                    it.showTitle(Title.title("".toComponent(),text.toComponent(), Title.Times.times(Duration.ofMillis(150),
                        Duration.ofMillis(50L * text.length), Duration.ofMillis(150))))
                }
            }
            delay(delayMills.toLong())
        }
    }
}