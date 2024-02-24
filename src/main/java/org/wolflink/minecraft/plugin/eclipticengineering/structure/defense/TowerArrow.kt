package org.wolflink.minecraft.plugin.eclipticengineering.structure.defense

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
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.TowerArrowBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.metadata.MetadataModifier
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.AbilityCondition
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureInitializedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureZoneRelationRepository
import org.wolflink.minecraft.plugin.eclipticstructure.repository.ZoneRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder
import java.util.Random

class TowerArrow private constructor(blueprint: TowerArrowBlueprint, builder: Builder
) : GameStructure(StructureType.TOWER_ARROW,blueprint, builder),
    IStructureListener {
    override val customListeners by lazy { listOf(this) }
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
                    .mapNotNull { StructureZoneRelationRepository.find1(it) }
                    .filterIsInstance<TowerArrow>()
                    .firstOrNull() ?: return
                arrowTower.playerShoot(player,e.shouldConsumeItem(),e.projectile)
            }
        }
    }
    companion object : StructureCompanion<TowerArrow>(){
        override fun supplier(blueprint: Blueprint, builder: Builder): TowerArrow {
            return TowerArrow(blueprint as TowerArrowBlueprint,builder)
        }
        val random = Random()
        override val blueprints = listOf(
            TowerArrowBlueprint(
                1,
                "弓箭高台",
                60,
                5000,
                2..5,
                1,
                0.5,
                VirtualRequirement(VirtualResourceType.WOOD,60),
                VirtualRequirement(VirtualResourceType.STONE,20),
                AbilityCondition(Ability.BUILDING,2)
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
    private suspend fun shooterNotice() {
        while (available) {
            shooters.forEach {
                it.sendActionBar("$MESSAGE_PREFIX 弓箭高台正在为你提供增益。".toComponent())
            }
            delay(3000)
        }
    }
    private suspend fun shooterLeaveCheck() {
        while (available) {
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
        EEngineeringScope.launch { shooterNotice() }
    }
}