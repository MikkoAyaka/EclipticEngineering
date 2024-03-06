package org.wolflink.minecraft.plugin.eclipticengineering.stage

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.extension.asDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.monster.StrategyDecider
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.BuildMenuItem
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.PioneerBook
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.disguise.ToBeDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.stage.goal.GoalHolder
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage

class GameStage(stageHolder: StageHolder): Stage("游戏阶段",stageHolder) {
    private fun initGameWorld() {
        val gameWorld = Config.gameWorld
        gameWorld.difficulty = Difficulty.NORMAL
        gameWorld.worldBorder.size = 800.0
        gameWorld.worldBorder.setCenter(0.0,0.0)
    }

    /**
     * 时间奖励
     * 每2分钟为玩家等级提升1级
     */
    private suspend fun timeRewards() {
        while (stageHolder.thisStage == this) {
            delay(1000 * 60 * 2)
            EclipticEngineering.runTask {
                gamingPlayers.forEach {
                    it.level += 1
                    it.playSound(it,Sound.ENTITY_PLAYER_LEVELUP,0.5f,1f)
                }
            }
        }
    }
    override fun onEnter() {
        initGameWorld()
        EEngineeringScope.launch { timeRewards() }
        // 随机一名玩家作为内鬼
        ToBeDisguiser.applyDisguiser()
        Bukkit.getOnlinePlayers()
            .filter { it.gameMode == GameMode.ADVENTURE }
            .forEach {
                // 传送到游戏世界
                it.teleport(Config.gameLocation)
                // 游戏模式改为生存
                it.gameMode = GameMode.SURVIVAL
                // 发放身份手册
                it.inventory.addItem(PioneerBook.defaultItem)
                // 达到建筑2级发放建造工具
                if(it.abilityTable.hasAbility(Ability.BUILDING,2)) BuildMenuItem.give(it)
                // 清理药水效果
                it.activePotionEffects.forEach { potion -> it.removePotionEffect(potion.type) }
                it.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20 * 10,4,false,false))
                it.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION,20 * 10,4,false,false))
                // 设置权限，对接 RPG-PRO 插件
                if(it.abilityTable.hasAbility(Ability.COMBAT,3)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user ${it.name} permission set quantumrpg.class.战斗III true")
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user ${it.name} permission unset quantumrpg.class.战斗III")
                }
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
    }
}