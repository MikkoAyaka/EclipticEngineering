package org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai.customgoals

import net.minecraft.core.SectionPos.*
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.Goal
import org.bukkit.Location
import java.util.*


class MoveToLocationGoal(private val mob: PathfinderMob,private val targetLocation: Location): Goal() {
    // 是否已经接近过目标地点
    private var reached = false
    init {
        this.setFlags(EnumSet.of(Flag.MOVE))
    }
    override fun canUse(): Boolean {
        return !mob.isPathFinding && !reached
    }
    override fun canContinueToUse(): Boolean {
        return !isNearDestination()
    }
    override fun start() {
        mob.navigation.moveTo(targetLocation.x,targetLocation.y,targetLocation.z, 1.2)
    }
    private fun isNearDestination(): Boolean {
        return mob.bukkitMob.location.distance(targetLocation) < 10
    }

}