package org.wolflink.minecraft.plugin.eclipticengineering.metadata

import org.bukkit.entity.Entity
import org.bukkit.entity.Projectile
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffect
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.META_PROJECTILE_EXTRA_DAMAGE
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.META_PROJECTILE_POTION_EFFECT

object MetadataModifier {
    fun modifyEffect(projectile: Projectile,potion: PotionEffect) {
        projectile.setMetadata(
            META_PROJECTILE_POTION_EFFECT,
            FixedMetadataValue(EclipticEngineering.instance,potion)
        )
    }
    fun modifyDamage(projectile: Entity, damage: Double) {
        projectile.setMetadata(
            META_PROJECTILE_EXTRA_DAMAGE,
            FixedMetadataValue(EclipticEngineering.instance,damage)
        )
    }
    fun modifyDamage(projectile: Entity, damage: Int) {
        modifyDamage(projectile,damage.toDouble())
    }
}