package org.wolflink.minecraft.plugin.eclipticengineering.particle

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Particle
import org.bukkit.entity.Projectile
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope

fun Projectile.withParticle(particle: Particle) {
    val world = this.world
    val projectile = this
    EEngineeringScope.launch {
        while (!projectile.isDead) {
            world.spawnParticle(particle,location,1,0.0,0.0,0.0)
            delay(50)
        }
    }
}