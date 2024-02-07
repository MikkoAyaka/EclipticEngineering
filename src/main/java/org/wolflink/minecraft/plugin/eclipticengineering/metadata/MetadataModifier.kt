package org.wolflink.minecraft.plugin.eclipticengineering.metadata

import org.bukkit.entity.Entity
import org.bukkit.metadata.FixedMetadataValue
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.META_PROJECTILE_EXTRA_DAMAGE

object MetadataModifier {
    const val META_PROJECTILE_EXTRA_DAMAGE = "EclipticStructure-ExtraDamage"
    fun modifyProjectile(projectile: Entity,damage: Double) {
        projectile.setMetadata(
            META_PROJECTILE_EXTRA_DAMAGE,
            FixedMetadataValue(EclipticEngineering.instance,damage)
        )
    }
    fun modifyProjectile(projectile: Entity,damage: Int) {
        modifyProjectile(projectile,damage.toDouble())
    }
}