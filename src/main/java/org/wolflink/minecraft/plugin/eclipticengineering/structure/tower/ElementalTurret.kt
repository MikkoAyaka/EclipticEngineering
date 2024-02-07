package org.wolflink.minecraft.plugin.eclipticengineering.structure.tower

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ElementalTurretBlueprint
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureDestroyedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureUnavailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder
import java.util.Calendar

abstract class ElementalTurret(blueprint: ElementalTurretBlueprint, builder: Builder): Structure(blueprint, builder),IStructureListener {
    override val customListener by lazy { this }
    private val attackRange = blueprint.attackRange
    private val damageRange = blueprint.attackDamage
    private val attackCooldownSeconds = blueprint.attackCooldownSeconds
    protected val displayEntity by lazy { spawnDisplayEntity() }
    /**
     * 炮台攻击逻辑
     */
    abstract fun attack(enemy: Entity,damage: Int)

    /**
     * 炮台生成展示实体
     */
    abstract fun spawnDisplayEntity(): Entity
    // 上次发起攻击的时间
    private var lastAttackTime = 0L
    // 索敌任务
    private var job: Job? = null

    /**
     * 搜索敌人，每秒尝试寻找一次
     */
    private suspend fun searchEnemy() {
        while (true) {
            delay(500)
            // 攻击冷却中
            if(Calendar.getInstance().timeInMillis - lastAttackTime < attackCooldownSeconds * 1000) continue
            // 重置CD
            lastAttackTime = Calendar.getInstance().timeInMillis
            Bukkit.getScheduler().runTask(EclipticEngineering.instance, Runnable {
                // 随机选取范围内的一个敌人进行攻击
                val enemy = displayEntity.location.getNearbyLivingEntities(attackRange.toDouble())
                    // 排除玩家与动物以及没有AI的展示实体
                    .filterNot { it is Player || it is Animals || !it.hasAI() }
                    .randomOrNull() ?: return@Runnable
                attack(enemy,damageRange.random())
            })
        }
    }
    override fun onAvailable(e: StructureAvailableEvent) {
        job = EEngineeringScope.launch { searchEnemy() }
    }

    override fun onUnavailable(e: StructureUnavailableEvent) {
        if(job != null) job!!.cancel()
    }

    override fun destroyed(e: StructureDestroyedEvent) {
        displayEntity.remove()
    }
}