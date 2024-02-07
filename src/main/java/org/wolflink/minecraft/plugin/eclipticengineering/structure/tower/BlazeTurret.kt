package org.wolflink.minecraft.plugin.eclipticengineering.structure.tower

import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.SmallFireball
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ElementalTurretBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.metadata.MetadataModifier
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class BlazeTurret(blueprint: ElementalTurretBlueprint,builder: Builder) : ElementalTurret(blueprint, builder) {
    override fun attack(enemy: Entity,damage: Int) {
        // 从展示实体指向敌人的向量
        val vector = enemy.location.add(-displayEntity.location.x,-displayEntity.location.y,-displayEntity.location.z).toVector()
        displayEntity.world.spawnEntity(
            displayEntity.location.clone().add(0.0,2.0,0.0),
            EntityType.SMALL_FIREBALL
        ).apply {
            this as SmallFireball
            direction = vector.normalize().multiply(0.8)
            yield = 0f
            MetadataModifier.modifyProjectile(this,damage)
        }
    }
    override fun spawnDisplayEntity(): Entity {
        val spawnLocation = builder.buildLocation.clone().add(0.0,9.0,0.0)
        return spawnLocation.world.spawnEntity(spawnLocation, EntityType.BLAZE).apply {
            (this as LivingEntity)
            setAI(false) // 取消AI
            isInvulnerable = true // 无敌
            isCollidable = false // 防止推动
        }
    }

    companion object {
        val blueprints = listOf(
            ElementalTurretBlueprint(
                1,
                "烈焰防御塔台",
                5,
                1000,
                5,
                6..8,
                12,
                ItemStack(Material.COBBLESTONE, 128),
                ItemStack(Material.IRON_INGOT, 16),
                ItemStack(Material.GOLD_INGOT, 8)
            )
        )

        fun create(structureLevel: Int, builder: Builder): BlazeTurret {
            val blueprint = blueprints.getOrNull(structureLevel - 1)
                ?: throw IllegalArgumentException("不支持的建筑等级：${structureLevel}")
            return BlazeTurret(blueprint, builder)
        }
    }
}