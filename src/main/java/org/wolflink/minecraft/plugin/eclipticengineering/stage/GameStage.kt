package org.wolflink.minecraft.plugin.eclipticengineering.stage

import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.GameMode
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.monster.StrategyDecider
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.BuildMenuItem
import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.GoalHolder
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage

class GameStage(stageHolder: StageHolder): Stage("游戏阶段",stageHolder) {
    override fun onEnter() {
        Bukkit.getWorld(Config.gameWorldName)?.difficulty = Difficulty.NORMAL
        Bukkit.getOnlinePlayers()
            .filter { it.gameMode == GameMode.ADVENTURE }
            .forEach {
                // 传送到游戏世界
                it.teleport(Config.gameLocation)
                // 游戏模式改为生存
                it.gameMode = GameMode.SURVIVAL
                // 清理药水效果
                it.activePotionEffects.forEach { potion -> it.removePotionEffect(potion.type) }
                // 达到建筑2级发放建造工具
                if(it.abilityTable.hasAbility(Ability.BUILDING,2)) BuildMenuItem.give(it)
            }

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