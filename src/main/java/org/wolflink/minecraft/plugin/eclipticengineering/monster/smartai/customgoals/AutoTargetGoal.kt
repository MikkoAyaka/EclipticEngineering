package org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai.customgoals

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.Goal
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import java.lang.Exception


class AutoTargetGoal(private val mob: PathfinderMob): Goal() {
    override fun canUse(): Boolean {
        // 空闲状态自动追击玩家
        return !mob.isPathFinding && mob.target == null
    }
    override fun canContinueToUse(): Boolean {
        return false
    }
    override fun start() {
        try {
            mob.target = gamingPlayers.filter { it.world == mob.bukkitEntity.world }.random() as LivingEntity
        } catch (_: Exception) {}
    }
}