package org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai.customgoals

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Creeper

/**
 * 苦力怕英勇自爆目标，靠近目标一定距离无视掩体爆炸
 */
class SelfExplosionGoal<T : LivingEntity>(mob: Creeper, targetClass: Class<T>) :
    XrayNearestAttackableTargetGoal<T>(mob, targetClass) {
    private val creeper: Creeper

    init {
        creeper = mob
    }

    override fun requiresUpdateEveryTick(): Boolean {
        return true
    }

    override fun tick() {
        if (creeper.target != null && creeper.distanceTo(creeper.target!!) <= SELF_EXPLOSION_RADIUS) {
            creeper.ignite()
        }
    }

    companion object {
        private const val SELF_EXPLOSION_RADIUS = 6
    }
}
