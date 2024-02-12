package org.wolflink.minecraft.plugin.eclipticengineering.structure.tower

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.TowerArrowBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.metadata.MetadataModifier
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureInitializedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureZoneRelationRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.ZoneRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder
import java.util.Random

class TowerArrow private constructor(blueprint: TowerArrowBlueprint, builder: Builder) : Structure(blueprint, builder),
    IStructureListener {
    override val customListener by lazy { this }
    private val extraDamageRange = blueprint.extraDamageRange
    private val maxShooterAmount = blueprint.maxShooterAmount
    private val returnArrowPercent = blueprint.returnArrowPercent
    object BukkitListener: Listener {
        @EventHandler
        fun onShoot(e: EntityShootBowEvent) {
            // 玩家事件
            if(e.entityType == EntityType.PLAYER) {
                // 获取玩家所处的建筑结构
                val player = e.entity as Player
                val arrowTower = ZoneRepository.findByLocation(player.location)
                    .map { StructureZoneRelationRepository.find1(it) }
                    .filterIsInstance<TowerArrow>()
                    .firstOrNull() ?: return
                arrowTower.playerShoot(player,e.shouldConsumeItem(),e.projectile)
            }
        }
    }
    companion object : StructureCompanion<TowerArrow>(){
        override val clazz: Class<TowerArrow> = TowerArrow::class.java
        val random = Random()
        override val blueprints = listOf(
            TowerArrowBlueprint(
                1,
                "弓箭高台",
                5,
                1000,
                2..5,
                1,
                0.5,
                ItemStack(Material.COBBLESTONE, 128),
                ItemStack(Material.IRON_INGOT, 16),
                ItemStack(Material.GOLD_INGOT, 8)
            )
        )
    }
    private fun inValidZone(player: Player) = (player.location.y - builder.buildLocation.y) in 2.0..5.0
    // 正在该防御塔进行射击的玩家 - 被记录的箭矢
    private val shooters = mutableSetOf<Player>()
        @Synchronized get
    fun playerShoot(player: Player,consumeArrow: Boolean,projectile: Entity) {
        if(shooters.size >= maxShooterAmount) return
        // 玩家是否在正确的区域射击
        if(!inValidZone(player)) return
        // 添加到记录
        if(!shooters.contains(player)) shooters.add(player)
        // 概率判定，返还箭矢
        if(consumeArrow && random.nextDouble() <= returnArrowPercent) {
            Bukkit.getScheduler().runTask(EclipticEngineering.instance, Runnable {
                player.inventory.addItem(ItemStack(Material.ARROW))
            })
        }
        // 修改箭矢实体，附加伤害标签
        MetadataModifier.modifyDamage(projectile,extraDamageRange.random())
    }

    private suspend fun shooterLeaveCheck() {
        while (!destroyed) {
           shooters
               // 离开有效区域的射击者
               .filter { it.location.distance(builder.buildLocation.clone().add(0.0,3.0,0.0)) >= 4 }
               // 从记录中清除
               .forEach { shooters.remove(it) }
            delay(3000)
        }
    }

    override fun initialized(e: StructureInitializedEvent) {
        EEngineeringScope.launch { shooterLeaveCheck() }
    }
}