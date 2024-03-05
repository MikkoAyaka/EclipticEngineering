package org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai.customgoals

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.AABB
import org.bukkit.event.entity.EntityTargetEvent
import java.util.*
import java.util.function.Predicate

/**
 * 透视索敌
 */
open class XrayNearestAttackableTargetGoal<T : LivingEntity>(
    mob: Mob,
    protected val targetType: Class<T>,
    reciprocalChance: Int,
    checkCanNavigate: Boolean,
    targetPredicate: Predicate<LivingEntity?>?
) : TargetGoal(mob, false, checkCanNavigate) {
    protected val randomInterval: Int
    protected var target: LivingEntity? = null
    protected var targetConditions: TargetingConditions

    constructor(mob: Mob, targetClass: Class<T>) : this(mob, targetClass, DEFAULT_RANDOM_INTERVAL, false, null)
    constructor(mob: Mob, targetClass: Class<T>, targetPredicate: Predicate<LivingEntity?>?) : this(
        mob,
        targetClass,
        DEFAULT_RANDOM_INTERVAL,
        false,
        targetPredicate
    )

    constructor(mob: Mob, targetClass: Class<T>, checkCanNavigate: Boolean) : this(
        mob,
        targetClass,
        DEFAULT_RANDOM_INTERVAL,
        checkCanNavigate,
        null
    )

    init {
        randomInterval = reducedTickDelay(reciprocalChance)
        this.setFlags(EnumSet.of(Flag.TARGET))
        targetConditions = TargetingConditions.forCombat().range(this.followDistance).selector(targetPredicate)
        targetConditions.ignoreLineOfSight()
        if (mob.level().paperConfig().entities.entitiesTargetWithFollowRange) targetConditions.useFollowRange() // Paper
    }

    override fun canUse(): Boolean {
        return if (randomInterval > 0 && this.mob.getRandom().nextInt(randomInterval) != 0) {
            false
        } else {
            findTarget()
            target != null
        }
    }

    private fun getTargetSearchArea(distance: Double): AABB {
        return this.mob.boundingBox.inflate(distance, 4.0, distance)
    }

    private fun findTarget() {
        target = if (targetType != Player::class.java && targetType != ServerPlayer::class.java) {
            this.mob.level().getNearestEntity(
                this.mob.level()
                    .getEntitiesOfClass(targetType, getTargetSearchArea(this.followDistance)) { true },
                targetConditions,
                this.mob,
                this.mob.x,
                this.mob.eyeY,
                this.mob.z
            )
        } else {
            this.mob.level()
                .getNearestPlayer(targetConditions, this.mob, this.mob.x, this.mob.eyeY, this.mob.z)
        }
    }

    override fun start() {
        if(target == null) {
            mob.target = null
        } else {
            mob.setTarget(
                target!!,
                if (target is ServerPlayer) EntityTargetEvent.TargetReason.CLOSEST_PLAYER else EntityTargetEvent.TargetReason.CLOSEST_ENTITY,
                true
            ) // CraftBukkit - reason
        }
        super.start()
    }

    companion object {
        private const val DEFAULT_RANDOM_INTERVAL = 10
    }
}
