package org.wolflink.minecraft.plugin.eclipticengineering.extension

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.ability.AbilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import java.util.UUID

private val abilityTableMap = mutableMapOf<UUID,AbilityTable>()
val UUID.abilityTable
    get() = abilityTableMap.getOrPut(this) { AbilityTable(this) }
val Player.abilityTable
    get() = this.uniqueId.abilityTable
val OfflinePlayer.abilityTable
    get() = this.uniqueId.abilityTable

fun Player.isGaming() = gameMode == GameMode.SURVIVAL && world.name != Config.lobbyWorldName
val gamingPlayers: Collection<Player> get() = onlinePlayers.filter { it.isGaming() }
val onlinePlayers: Collection<Player> get() = Bukkit.getOnlinePlayers()