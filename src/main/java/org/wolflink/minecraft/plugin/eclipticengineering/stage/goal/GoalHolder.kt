package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import kotlinx.coroutines.launch
import org.bukkit.Location
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.extension.asDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.BuildMenuItem
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.PioneerBook
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.TaskBook

object GoalHolder {
    // 当前目标
    var nowGoal: Goal? = null
    // 特殊行动坐标
    var specialLocation: Location? = null
    // 据点坐标
    var footholdLocation: Location? = null
    // 剩余准备时间
    val prepareTimeLeft get() = nowGoal?.prepareTimeLeft ?: -1
    fun init() {
        nowGoal = EstablishFoothold
        // 发放任务书给随机一名正在游戏中的玩家
        TaskBook.give(gamingPlayers.random())
        // 发放身份手册
        gamingPlayers.forEach{
            it.inventory.addItem(PioneerBook.defaultItem)
        }
        // 随机一名玩家作为内鬼
        gamingPlayers.random().asDisguiser()
        // 达到建筑2级发放建造工具
        gamingPlayers.forEach{
            if(it.abilityTable.hasAbility(Ability.BUILDING,2)) BuildMenuItem.give(it)
        }
        nowGoal!!.into()
    }
    fun next() {
        val gameWorldBorder = Config.gameWorld.worldBorder
        gameWorldBorder.setSize(gameWorldBorder.size + 100,15)
        EEngineeringScope.launch {
            nowGoal?.leave()
            nowGoal = nowGoal?.nextGoal
            nowGoal?.into()
        }
    }
}