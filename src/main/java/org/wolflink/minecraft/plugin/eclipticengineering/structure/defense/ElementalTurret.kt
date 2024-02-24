package org.wolflink.minecraft.plugin.eclipticengineering.structure.defense

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ElementalTurretBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.EnergyRequiredListener
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.utils.RandomAPI
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureDestroyedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureUnavailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder
import java.util.*

abstract class ElementalTurret(type:StructureType,blueprint: ElementalTurretBlueprint, builder: Builder) :
    GameStructure(type,blueprint, builder),IStructureListener {
    override val customListeners by lazy { listOf(EnergyRequiredListener(this),this) }
    private val attackRange = blueprint.attackRange
    private val damageRange = blueprint.attackDamage
    private val attackCooldownSeconds = blueprint.attackCooldownSeconds
    protected val displayEntity by lazy { spawnDisplayEntity() }

    /**
     * 炮台攻击逻辑
     */
    private fun attack(enemy: Entity, damage: Int) {
        repeat(5) {
            // 从展示实体指向敌人的向量
            val vector =
                enemy.location.clone().add(
                    RandomAPI.nextDouble(-1.5, 1.5) - displayEntity.location.x,
                    RandomAPI.nextDouble(-1.5, 1.5) - 1 - displayEntity.location.y,
                    RandomAPI.nextDouble(-1.5, 1.5) - displayEntity.location.z
                )
                    .toVector()
            shoot(displayEntity.location.clone().add(0.0, 2.0, 0.0), vector.normalize().multiply(1.2), damage)
        }
    }
    protected abstract fun playShootSound()
    protected abstract fun shoot(from: Location, vector: Vector, damage: Int)

    /**
     * 炮台生成展示实体
     */
    abstract fun spawnDisplayEntity(): Entity

    // 上次发起攻击的时间
    private var lastAttackTime = 0L

    /**
     * 搜索敌人，每秒尝试寻找一次
     */
    private suspend fun searchEnemy() {
        while (true) {
            delay(500)
            // 攻击冷却中
            if (Calendar.getInstance().timeInMillis - lastAttackTime < attackCooldownSeconds * 1000) continue
            // 重置CD
            lastAttackTime = Calendar.getInstance().timeInMillis
            Bukkit.getScheduler().runTask(EclipticEngineering.instance, Runnable {
                // 随机选取范围内的一个敌人进行攻击
                val enemy = displayEntity.location.getNearbyLivingEntities(attackRange.toDouble())
                    // 排除玩家与动物以及没有AI的展示实体
                    .filterNot { it is Player || it is Animals || !it.hasAI() }
                    .randomOrNull() ?: return@Runnable
                attack(enemy, damageRange.random())
            })
        }
    }
    // 索敌任务
    private var job: Job? = null
    override fun onAvailable(e: StructureAvailableEvent) {
        job = EEngineeringScope.launch { searchEnemy() }
    }

    override fun onUnavailable(e: StructureUnavailableEvent) {
        if (job != null) job!!.cancel()
    }

    override fun destroyed(e: StructureDestroyedEvent) {
        displayEntity.remove()
    }
}