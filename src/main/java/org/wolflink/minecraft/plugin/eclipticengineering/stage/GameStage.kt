package org.wolflink.minecraft.plugin.eclipticengineering.stage

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.GoalHolder
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage

class GameStage(stageHolder: StageHolder): Stage("游戏阶段",stageHolder) {
    override fun onEnter() {
        Bukkit.getOnlinePlayers()
            .filter { it.gameMode != GameMode.ADVENTURE }
            .forEach { it.gameMode = GameMode.SPECTATOR }
        GoalHolder.init()
    }
    override fun onLeave() {
    }
}