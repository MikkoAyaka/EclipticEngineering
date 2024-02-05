package org.wolflink.minecraft.plugin.eclipticengineering.interaction

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable

object HitMonsterListener: Listener {
    @EventHandler
    fun onHit(e: EntityDamageByEntityEvent) {
        if(e.damager is Player) {
            val player = e.damager as Player
            if(!player.abilityTable.checkAbilityWithNotice(Ability.COMBAT)) {
                e.isCancelled = true
                return
            }
        }
    }
}