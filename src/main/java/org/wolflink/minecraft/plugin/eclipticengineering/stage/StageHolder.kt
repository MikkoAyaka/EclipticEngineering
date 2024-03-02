package org.wolflink.minecraft.plugin.eclipticengineering.stage

import org.wolflink.minecraft.wolfird.framework.gamestage.stageholder.LinearStageHolder

object StageHolder: LinearStageHolder(false) {
    fun init() {
        bindStages(arrayOf(
            WaitStage(this),
            ReadyStage(this),
            PreGameStage(this),
            GameStage(this),
            EndGameStage(this),
            FinalStage(this)
        ))
        next()
    }
}