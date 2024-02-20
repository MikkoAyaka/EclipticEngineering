package org.wolflink.minecraft.plugin.eclipticengineering.stage

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.monster.StrategyDecider
import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.GoalHolder
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage

class GameStage(stageHolder: StageHolder): Stage("游戏阶段",stageHolder) {
    override fun onEnter() {
        Bukkit.getOnlinePlayers()
            .filter { it.gameMode == GameMode.ADVENTURE }
            .forEach { it.teleport(Config.gameLocation) }

        // 启用刷怪
        StrategyDecider.enable()
        Bukkit.getScheduler().runTaskLater(EclipticEngineering.instance, Runnable {
            GoalHolder.init()
        },20 * 3)
    }
    override fun onLeave() {
        StrategyDecider.disable()
    }
}