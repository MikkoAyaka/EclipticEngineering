package org.wolflink.minecraft.plugin.eclipticengineering.metadata

import org.bukkit.entity.Damageable
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.potion.PotionEffect
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.META_PROJECTILE_EXTRA_DAMAGE
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.META_PROJECTILE_POTION_EFFECT

object MetadataHandler: Listener {
    /**
     * 抛射物额外伤害
     */
    @EventHandler
    fun extraDamage(e: ProjectileHitEvent) {
        val hitEntity = e.hitEntity
        // 计算总附加伤害
        val extraDamages = e.entity.getMetadata(META_PROJECTILE_EXTRA_DAMAGE).map { it.asDouble() }.reduceOrNull(Double::plus) ?: return
        if(hitEntity is Damageable) hitEntity.damage(extraDamages)
    }
    @EventHandler
    fun potionEffect(e: ProjectileHitEvent) {
        val hitEntity = e.hitEntity
        val potionEffects = e.entity.getMetadata(META_PROJECTILE_POTION_EFFECT).map { it as PotionEffect }
        if(potionEffects.isEmpty()) return
        if(hitEntity is LivingEntity) hitEntity.addPotionEffects(potionEffects)
    }
}