package org.wolflink.minecraft.plugin.eclipticengineering.extension

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.ability.AbilityTree
import java.util.UUID

private val abilityTreeMap = mutableMapOf<UUID,AbilityTree>()
val UUID.abilityTree
    get() = abilityTreeMap.getOrPut(this) { AbilityTree() }
val Player.abilityTree
    get() = this.uniqueId.abilityTree
val OfflinePlayer.abilityTree
    get() = this.uniqueId.abilityTree