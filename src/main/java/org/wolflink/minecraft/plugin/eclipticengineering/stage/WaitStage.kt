package org.wolflink.minecraft.plugin.eclipticengineering.stage

import org.bukkit.Bukkit
import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage

class WaitStage(stageHolder: StageHolder): Stage("等待阶段",stageHolder) {
    private var taskId = 0
    override fun onEnter() {
        initGameRule()
        taskId = Bukkit.getScheduler().runTaskTimer(EclipticEngineering.instance, Runnable {
            Bukkit.getOnlinePlayers().forEach {
                it.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION,20 * 3,4))
                it.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20 * 3,4))
                it.addPotionEffect(PotionEffect(PotionEffectType.SATURATION,1,0))
            }
        },20,20).taskId
    }
    override fun onLeave() {
        Bukkit.getScheduler().cancelTask(taskId)
        taskId = 0
    }
    private fun initGameRule() {
        Bukkit.getWorlds().forEach {
            it.setGameRule(GameRule.DO_FIRE_TICK,false) // 防火
            it.setGameRule(GameRule.RANDOM_TICK_SPEED,0) // 防止自然生长
            it.setGameRule(GameRule.KEEP_INVENTORY,true) // 保留背包(插件接管
            it.setGameRule(GameRule.MOB_GRIEFING,false) // 怪物不破坏地形
            it.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK,true) // 禁用鞘翅检测
            it.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true) // 死亡自动复活
            it.setGameRule(GameRule.DO_INSOMNIA,false) // 无幻翼
            it.setGameRule(GameRule.DO_MOB_SPAWNING,false) // 无自然刷怪
            it.setGameRule(GameRule.SHOW_DEATH_MESSAGES,false) // 无死亡通知(插件接管
            it.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE,200) // 睡觉无法跳过夜晚
            it.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false)// 禁止昼夜更替
            it.difficulty = Difficulty.PEACEFUL
        }
    }
}