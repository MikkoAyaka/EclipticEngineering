package org.wolflink.minecraft.plugin.eclipticengineering.structure.tower

import org.bukkit.Location
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
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.metadata.MetadataModifier
import org.wolflink.minecraft.plugin.eclipticengineering.particle.withParticle
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.ItemRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.VirtualRequirement
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialIron
import org.wolflink.minecraft.plugin.eclipticengineering.utils.RandomAPI
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureCompanion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

class BlazeTurret private constructor(blueprint: ElementalTurretBlueprint, builder: Builder) :
    ElementalTurret(StructureType.TURRET_BLAZE, blueprint, builder) {
    private val displayEntityRelativeVector = blueprint.displayEntityRelativeVector
    override fun playShootSound() {
        displayEntity.location.world.playSound(displayEntity.location, Sound.ENTITY_BLAZE_SHOOT, 2f, 1f)
    }

    override fun shoot(from: Location, vector: Vector, damage: Int) {
        from.world.spawnEntity(
            from,
            EntityType.SMALL_FIREBALL
        ).apply {
            this as SmallFireball
            direction = vector.normalize().multiply(1.2)
            yield = 0f
            MetadataModifier.modifyDamage(this, damage)
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

    companion object : StructureCompanion<BlazeTurret>() {
        override fun supplier(blueprint: Blueprint, builder: Builder): BlazeTurret {
            return BlazeTurret(blueprint as ElementalTurretBlueprint, builder)
        }

        override val blueprints = listOf(
            ElementalTurretBlueprint(
                1,
                "烈焰防御塔台",
                60,
                6000,
                Vector(0, 11, 0),
                2,
                4..6,
                24,
                VirtualRequirement("需要 30 木材", VirtualResourceType.WOOD, 30),
                VirtualRequirement("需要 50 石料", VirtualResourceType.STONE, 50),
                VirtualRequirement("需要 30 金属", VirtualResourceType.METAL, 30),
                ItemRequirement("需要 5 陨铁矿石", SpecialIron.defaultItem.clone().apply { amount = 5 }),
                ItemRequirement("需要 5 熔岩桶", ItemStack(Material.LAVA_BUCKET, 5))
            )
        )
    }
}