package org.wolflink.minecraft.plugin.eclipticengineering.structure.tower

import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.SmallFireball
import org.bukkit.entity.Snowball
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ElementalTurretBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.metadata.MetadataModifier
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class SnowGolemTurret private constructor(blueprint: ElementalTurretBlueprint, builder: Builder) : ElementalTurret(blueprint, builder) {
    private val displayEntityRelativeVector = blueprint.displayEntityRelativeVector
    override fun attack(enemy: Entity,damage: Int) {
        // 从展示实体指向敌人的向量
        val vector = enemy.location.add(-displayEntity.location.x,-displayEntity.location.y,-displayEntity.location.z).toVector()
        displayEntity.world.spawnEntity(
            displayEntity.location.clone().add(0.0,2.0,0.0),
            EntityType.SNOWBALL
        ).apply {
            this as Snowball
            setGravity(false)
            velocity = vector.normalize().multiply(0.8)
            MetadataModifier.modifyEffect(this, PotionEffect(PotionEffectType.SLOW,40,1,false,false))
        }
    }
    override fun spawnDisplayEntity(): Entity {
        val spawnLocation = builder.buildLocation.clone().add(displayEntityRelativeVector)
        return spawnLocation.world.spawnEntity(spawnLocation, EntityType.SNOWMAN).apply {
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
                "寒冰防御塔台",
                5,
                1000,
                Vector(0,10,0),
                2,
                6..8,
                20,
                ItemStack(Material.COBBLESTONE, 128),
                ItemStack(Material.IRON_INGOT, 16),
                ItemStack(Material.GOLD_INGOT, 8)
            )
        )

        fun create(structureLevel: Int, builder: Builder): SnowGolemTurret {
            val blueprint = blueprints.getOrNull(structureLevel - 1)
                ?: throw IllegalArgumentException("不支持的建筑等级：${structureLevel}")
            return SnowGolemTurret(blueprint, builder)
        }
    }
}