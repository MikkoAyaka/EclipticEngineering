package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.disguise

import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.extension.asDisguiser
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.extension.onlinePlayers

object ToBeDisguiser {
    private val wannaBeDisguisers = mutableSetOf<Player>()

    /**
     * 自愿成为内鬼
     */
    fun toBeDisguiser(player: Player) {
        wannaBeDisguisers.add(player)
    }

    /**
     * 选择内鬼
     */
    fun applyDisguiser() {
        if(wannaBeDisguisers.isNotEmpty()) {
            wannaBeDisguisers.random().asDisguiser()
        } else {
            onlinePlayers.random().asDisguiser()
        }
    }
}