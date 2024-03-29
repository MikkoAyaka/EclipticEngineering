package org.wolflink.minecraft.plugin.eclipticengineering.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.wolflink.minecraft.plugin.eclipticengineering.stage.StageHolder
import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.GoalHolder

/**
 * 关于游戏阶段的变量
 *
 * %ees_stage% 当前阶段
 * %ees_goal% 当前目标
 * %ees_special_location% 当前特殊行动坐标
 * %ees_foothold_location% 当前据点坐标
 * %ees_prepare_time% 下一阶段剩余准备时间
 */
object StagePapi: PlaceholderExpansion() {
    override fun getIdentifier() = "ees"

    override fun getAuthor() = "MikkoAyaka"

    override fun getVersion() = "1.0.0"

    override fun onRequest(player: OfflinePlayer?, params: String): String {
        if(params == "stage") return StageHolder.thisStage?.displayName ?: "未初始化"
        if(params == "goal") return GoalHolder.nowGoal?.displayName ?: "未初始化"
        if(params == "special_location") return GoalHolder.specialLocation?.toVector()?.toString() ?: "暂无"
        if(params == "foothold_location") return GoalHolder.footholdLocation?.toVector()?.toString() ?: "暂无"
        if(params == "prepare_time") return "${GoalHolder.prepareTimeLeft}"
        return "未知变量"
    }
}