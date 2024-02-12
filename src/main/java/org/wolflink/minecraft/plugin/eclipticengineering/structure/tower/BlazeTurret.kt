package org.wolflink.minecraft.plugin.eclipticengineering.structure.tower

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.SmallFireball
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ElementalTurretBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.metadata.MetadataModifier
import org.wolflink.minecraft.plugin.eclipticengineering.particle.withParticle
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class BlazeTurret private constructor(blueprint: ElementalTurretBlueprint,builder: Builder) : ElementalTurret(blueprint, builder) {
    private val displayEntityRelativeVector = blueprint.displayEntityRelativeVector
    override fun attack(enemy: Entity,damage: Int) {
        displayEntity.location.world.playSound(displayEntity.location, Sound.ENTITY_BLAZE_SHOOT,2f,1f)
        // 从展示实体指向敌人的向量
        val vector = enemy.location.add(-displayEntity.location.x,-1-displayEntity.location.y,-displayEntity.location.z).toVector()
        displayEntity.world.spawnEntity(
            displayEntity.location.clone().add(0.0,2.0,0.0),
            EntityType.SMALL_FIREBALL
        ).apply {
            this as SmallFireball
            direction = vector.normalize().multiply(1.2)
            yield = 0f
            MetadataModifier.modifyDamage(this,damage)
            withParticle(Particle.SMALL_FLAME)
        }
    }
    override fun spawnDisplayEntity(): Entity {
        val spawnLocation = builder.buildLocation.clone().add(displayEntityRelativeVector)
        return spawnLocation.world.spawnEntity(spawnLocation, EntityType.BLAZE).apply {
            (this as LivingEntity)
            setAI(false) // 取消AI
            isInvulnerable = true // 无敌
            isCollidable = false // 防止推动
        }
    }

    companion object : StructureCompanion<BlazeTurret>(){
        override val clazz: Class<BlazeTurret> = BlazeTurret::class.java
        override val blueprints = listOf(
            ElementalTurretBlueprint(
                1,
                "烈焰防御塔台",
                5,
                1000,
                Vector(0,11,0),
                1,
                4..6,
                20,
                ItemStack(Material.COBBLESTONE, 128),
                ItemStack(Material.IRON_INGOT, 16),
                ItemStack(Material.GOLD_INGOT, 8)
            )
        )
    }
}