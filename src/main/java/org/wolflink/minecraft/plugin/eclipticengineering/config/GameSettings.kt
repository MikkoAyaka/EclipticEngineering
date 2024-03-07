package org.wolflink.minecraft.plugin.eclipticengineering.config

import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.EEDifficulty

object GameSettings {
    var difficulty = EEDifficulty.NORMAL

    /**
     * 总计游戏天数(结束第x天后刷新BOSS)
     */
    val totalDays = 3

    // 内鬼所需完成目标数
    val disguiserWinGoalAmount = 1
}