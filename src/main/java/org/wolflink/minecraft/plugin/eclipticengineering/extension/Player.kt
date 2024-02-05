package org.wolflink.minecraft.plugin.eclipticengineering.extension

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.ability.AbilityTable
import java.util.UUID

private val abilityTableMap = mutableMapOf<UUID,AbilityTable>()
val UUID.abilityTable
    get() = abilityTableMap.getOrPut(this) { AbilityTable() }
val Player.abilityTable
    get() = this.uniqueId.abilityTable
val OfflinePlayer.abilityTable
    get() = this.uniqueId.abilityTable