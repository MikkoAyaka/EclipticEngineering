package org.wolflink.minecraft.plugin.eclipticengineering.stage

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.extension.onlinePlayers
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.Counter
import org.wolflink.minecraft.plugin.eclipticstructure.extension.*
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage
import java.time.Duration

class ReadyStage(stageHolder: StageHolder) : Stage("准备阶段", stageHolder) {
    override fun onEnter() {
        onlinePlayers.forEach {
            it.gameMode = GameMode.ADVENTURE
        }
        EEngineeringScope.launch {
            if(Config.debugMode) Counter.count(5,"<green>准备搜刮物资吧！".toComponent())
            else Counter.count(15,"<green>准备搜刮物资吧！".toComponent())
            EclipticEngineering.runTask { stageHolder.next() }
        }
    }

    override fun onLeave() {
    }
}