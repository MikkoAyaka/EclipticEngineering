package org.wolflink.minecraft.plugin.eclipticengineering.structure.tower

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Snowball
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ElementalTurretBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.metadata.MetadataModifier
import org.wolflink.minecraft.plugin.eclipticengineering.particle.withParticle
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialIron
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class SnowGolemTurret private constructor(blueprint: ElementalTurretBlueprint, builder: Builder) :
    ElementalTurret(StructureType.TURRET_SNOW_GOLEM,blueprint, builder) {
    private val displayEntityRelativeVector = blueprint.displayEntityRelativeVector
    override fun attack(enemy: Entity, damage: Int) {
        displayEntity.location.world.playSound(displayEntity.location, Sound.ENTITY_SNOWBALL_THROW, 2f, 1f)
        // 从展示实体指向敌人的向量
        val vector =
            enemy.location.add(-displayEntity.location.x, -1 - displayEntity.location.y, -displayEntity.location.z)
                .toVector()
        displayEntity.world.spawnEntity(
            displayEntity.location.clone().add(0.0, 2.0, 0.0),
            EntityType.SNOWBALL
        ).apply {
            this as Snowball
            setGravity(false)
            velocity = vector.normalize().multiply(1.2)
            withParticle(Particle.SNOWFLAKE)
            MetadataModifier.modifyEffect(this, PotionEffect(PotionEffectType.SLOW, 40, 1, false, false))
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

    companion object : StructureCompanion<SnowGolemTurret>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): SnowGolemTurret {
            return SnowGolemTurret(blueprint as ElementalTurretBlueprint, builder)
        }

        override val blueprints = listOf(
            ElementalTurretBlueprint(
                1,
                "寒冰防御塔台",
                60,
                6000,
                Vector(0, 11, 0),
                1,
                4..6,
                24,
                VirtualRequirement("需要 30 木材", VirtualResourceType.WOOD, 30),
                VirtualRequirement("需要 50 石料", VirtualResourceType.STONE, 50),
                VirtualRequirement("需要 30 金属", VirtualResourceType.METAL, 30),
                ItemRequirement("需要 5 陨铁矿石", SpecialIron.defaultItem.clone().apply { amount = 5 }),
                ItemRequirement("需要 30 雪块", ItemStack(Material.SNOW_BLOCK, 30))
            )
        )
    }
}