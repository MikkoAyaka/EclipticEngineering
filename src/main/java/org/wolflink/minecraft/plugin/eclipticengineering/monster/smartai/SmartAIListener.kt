package org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.entity.player.Player
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.wolflink.minecraft.plugin.eclipticengineering.GameRoom
import org.wolflink.minecraft.plugin.eclipticengineering.monster.SpecialSpawnEntityEvent
import org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai.customgoals.SelfExplosionGoal
import org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai.customgoals.XrayNearestAttackableTargetGoal

/**
 * 更聪明的怪物AI
 */
object SmartAIListener : Listener {
    @EventHandler
    fun on(event: SpecialSpawnEntityEvent) {
        val craftEntity: Entity = (event.entity as CraftEntity).handle
        // 仇恨透视 + 仇恨距离增加
        if (craftEntity is Mob) {
            addXrayAbility(craftEntity)
            fartherFollowDistance(craftEntity)
        }
        // 向据点进攻
        if(craftEntity is PathfinderMob) {
            footholdRush(craftEntity)
        }
        if (craftEntity is Zombie) {
            enhanceZombie(craftEntity)
        } else if (craftEntity is Creeper) {
            enhanceCreeper(craftEntity)
        }
    }

    /**
     * 向玩家的据点发起进攻
     */
    private fun footholdRush(mob: PathfinderMob) {
        val footholdLocation: Location = GameRoom.getFootholdLocation() ?: return
        mob.movingTarget = BlockPos(footholdLocation.blockX,footholdLocation.blockY,footholdLocation.blockZ)

    }
    private fun enhanceZombie(zombie: Zombie) {
//        zombie.goalSelector.addGoal(1, ZombieBlockGoal(zombie))
    }

    /**
     * 为怪物增加额外32格的仇恨距离
     */
    private fun fartherFollowDistance(mob: Mob) {
        mob.getAttribute(Attributes.FOLLOW_RANGE)?.addPermanentModifier(AttributeModifier("",32.0,AttributeModifier.Operation.ADDITION))
    }

    private fun addXrayAbility(mob: Mob) {
        mob.targetSelector.addGoal(1, XrayNearestAttackableTargetGoal(mob, Player::class.java))
    }

    private fun enhanceCreeper(creeper: Creeper) {
        creeper.goalSelector.addGoal(1, SelfExplosionGoal(creeper, Player::class.java))
    }
}
