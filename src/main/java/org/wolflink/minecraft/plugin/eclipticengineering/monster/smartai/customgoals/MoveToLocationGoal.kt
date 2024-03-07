package org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai.customgoals

import net.minecraft.core.SectionPos.*
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.Goal
import org.bukkit.Location
import java.util.*


class MoveToLocationGoal(private val mob: PathfinderMob,private val targetLocation: Location): Goal() {
    // 从怪物指向目的地坐标的向量(自然化后翻10倍)
    private val vector = targetLocation.subtract(mob.bukkitEntity.location).toVector().normalize().multiply(10)
    // 是否已经接近过目标地点
    private var reached = false
    init {
        this.setFlags(EnumSet.of(Flag.MOVE))
    }
    override fun canUse(): Boolean {
        return !mob.isPathFinding && !reached
    }
    override fun canContinueToUse(): Boolean {
        return !isNearDestination() && mob.level().getNearestPlayer(mob, 10.0) == null
    }
    override fun start() {
    }
    override fun tick() {
        mob.navigation.moveTo(vector.x,vector.y,vector.z, 1.2)
    }
    private fun isNearDestination(): Boolean {
        val result = mob.bukkitMob.location.distance(targetLocation) < 10
        if(result && !reached) reached = true
        return result
    }

}