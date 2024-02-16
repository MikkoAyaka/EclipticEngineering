package org.wolflink.minecraft.plugin.eclipticengineering.stage

import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.GoalHolder
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage

class GameStage(stageHolder: StageHolder): Stage("游戏阶段",stageHolder) {
    override fun onEnter() {
        GoalHolder.init()
    }
    override fun onLeave() {
    }
}