package org.wolflink.minecraft.plugin.eclipticengineering.stage

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticstructure.extension.*
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage
import java.time.Duration

class ReadyStage(stageHolder: StageHolder) : Stage("准备阶段", stageHolder) {
    override fun onEnter() {
        Bukkit.getOnlinePlayers().forEach {
            it.gameMode = GameMode.ADVENTURE
        }
        EEngineeringScope.launch {
            val maxCount = 30
            var count = maxCount
            repeat(maxCount) {
                val title = Title.title(
                    "".toComponent(),
                    "${(RED_COLOR to GREEN_COLOR ofGradient count.toDouble()/maxCount).toHexFormat()}<bold>$count".toComponent(),
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
            EclipticEngineering.runTask { stageHolder.next() }
        }
    }

    override fun onLeave() {
    }
}