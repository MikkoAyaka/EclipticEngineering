package org.wolflink.minecraft.plugin.eclipticengineering.metadata

import org.bukkit.entity.Damageable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.META_PROJECTILE_EXTRA_DAMAGE

object MetadataHandler: Listener {
    /**
     * 抛射物额外伤害
     */
    @EventHandler
    fun onProjectileHit(e: ProjectileHitEvent) {
        val hitEntity = e.hitEntity
        // 计算总附加伤害
        val extraDamages = e.entity.getMetadata(META_PROJECTILE_EXTRA_DAMAGE).map { it.asDouble() }.reduce(Double::plus)
        if(hitEntity is Damageable) hitEntity.damage(extraDamages)
    }
}