package org.wolflink.minecraft.plugin.eclipticengineering.stage

import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.GameMode
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.monster.StrategyDecider
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.BuildMenuItem
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.GoalHolder
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage

class GameStage(stageHolder: StageHolder): Stage("游戏阶段",stageHolder) {
    private fun initGameWorld() {
        val gameWorld = Config.gameWorld
        gameWorld.difficulty = Difficulty.NORMAL
        gameWorld.worldBorder.size = 800.0
        gameWorld.worldBorder.setCenter(0.0,0.0)
    }
    override fun onEnter() {
        initGameWorld()
        Bukkit.getOnlinePlayers()
            .filter { it.gameMode == GameMode.ADVENTURE }
            .forEach {
                // 传送到游戏世界
                it.teleport(Config.gameLocation)
                // 游戏模式改为生存
                it.gameMode = GameMode.SURVIVAL
                // 清理药水效果
                it.activePotionEffects.forEach { potion -> it.removePotionEffect(potion.type) }
                it.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20 * 10,4,false,false))
                it.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION,20 * 10,4,false,false))
            }

        // 启用刷怪
        StrategyDecider.enable()
        Bukkit.getScheduler().runTaskLater(EclipticEngineering.instance, Runnable {
            DayNightHandler.start()
        },20 * 5)
        Bukkit.getScheduler().runTaskLater(EclipticEngineering.instance, Runnable {
            GoalHolder.init()
        },20 * 8)
    }
    override fun onLeave() {
        StrategyDecider.disable()
    }
}