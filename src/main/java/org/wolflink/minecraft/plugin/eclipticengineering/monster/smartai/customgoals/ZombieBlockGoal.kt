package org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai.customgoals

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.phys.Vec3
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import java.util.stream.Stream
import kotlin.math.sqrt

class ZombieBlockGoal(zombie: Zombie) : Goal() {
    private val zombieEntity: Entity
    private val zombie: Zombie
    override fun isInterruptable(): Boolean {
        return false
    }

    override fun canUse(): Boolean {
        return zombie.target != null && zombie.target!!.position().y - zombie.position().y >= 2
    }

    private var stayTicks = 8
    override fun canContinueToUse(): Boolean {
        val result = stayTicks-- > 0
        if (!result) stayTicks = 8
        return result
    }

    init {
        this.zombie = zombie
        zombieEntity = zombie.bukkitEntity
    }

    override fun start() {
        Bukkit.getScheduler().runTaskAsynchronously(EclipticEngineering.instance, Runnable {
            breakNearestBlocks()
            val locBlock = zombieEntity.location.block
            val headBlock = locBlock.getRelative(0, 2, 0)
            breakBlock(headBlock)
            jumpAndPlaceBlock()
            dragToTarget()
        })
    }

    /**
     * 强行牵引怪物到目标
     */
    private fun dragToTarget() {
        val livingEntity: LivingEntity? = zombie.target
        if (livingEntity != null) {
            val vector: Vec3 = livingEntity.position().add(zombie.position().reverse())
            val x = if (vector.x == 0.0) 0.0 else if (vector.x > 0) PULL_DISTANCE else -PULL_DISTANCE
            val z = if (vector.z == 0.0) 0.0 else if (vector.z > 0) PULL_DISTANCE else -PULL_DISTANCE
            EclipticEngineering.runTask {
                zombieEntity.velocity = Vector(x, 0.0, z)
            }
        }
    }

    /**
     * 破坏紧邻的方块
     */
    private fun breakNearestBlocks() {
        // 没有目标，跳过
        if (zombie.target == null) return
        // 高度差大于2格，不触发墙体破坏
        if (zombie.target!!.position().y - zombieEntity.location.blockY > 2) return
        val locBlock = zombieEntity.location.block
        Stream.of(
            locBlock.getRelative(-1, 0, 0),
            locBlock.getRelative(1, 0, 0),
            locBlock.getRelative(0, 0, -1),
            locBlock.getRelative(0, 0, 1),
            locBlock.getRelative(-1, 1, 0),
            locBlock.getRelative(1, 1, 0),
            locBlock.getRelative(0, 1, -1),
            locBlock.getRelative(0, 1, 1)
        ).filter { block: Block ->
            val deltaX = block.location.blockX + 0.5 - zombieEntity.location.x
            val deltaZ = block.location.blockZ + 0.5 - zombieEntity.location.z
            val distance = sqrt(deltaX * deltaX + deltaZ * deltaZ)
            distance <= 1.0
        }
            .forEach { block: Block -> breakBlock(block) }
    }

    private fun jumpAndPlaceBlock() {
        val footBlock = zombieEntity.location.block
        zombie.jumpControl.jump()
        EEngineeringScope.launch {
            if(zombieEntity.isDead) return@launch
            if (footBlock.getRelative(0, -1, 0).isSolid) {
                delay(250)
                placeBlock(footBlock.location)
            }
            delay(1000 * 15)
            if(footBlock.type == Material.COBBLESTONE) breakBlock(footBlock)
        }
    }
    private fun placeBlock(location: Location) {
        EclipticEngineering.runTask {
            location.block.type = Material.COBBLESTONE
            location.world.playSound(location,Sound.BLOCK_STONE_PLACE, 0.5f, 1f)
        }
    }
    private fun breakBlock(block: Block) {
        EclipticEngineering.runTask {
            if (block.isSolid) {
                block.breakNaturally()
                block.world.playSound(block.location, block.blockSoundGroup.breakSound, 1f, 1f)
            }
        }
    }

    companion object {
        private const val PULL_DISTANCE = 0.05
    }
}
