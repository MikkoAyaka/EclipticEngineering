package org.wolflink.minecraft.plugin.eclipticengineering.roleplay.disguise

import me.libraryaddict.disguise.DisguiseAPI
import me.libraryaddict.disguise.disguisetypes.Disguise
import me.libraryaddict.disguise.disguisetypes.DisguiseType
import me.libraryaddict.disguise.disguisetypes.MobDisguise
import org.bukkit.entity.Player
import java.util.concurrent.atomic.AtomicInteger

object DisguiseHandler {
    private val availableDisguises = setOf(
        DisguiseType.VILLAGER,
        DisguiseType.COW,
        DisguiseType.SHEEP,
        DisguiseType.LLAMA,
        DisguiseType.PILLAGER,
        DisguiseType.SNOWMAN,
        DisguiseType.TRADER_LLAMA,
        DisguiseType.WANDERING_TRADER,
        DisguiseType.VINDICATOR,
        DisguiseType.WITCH,
        DisguiseType.ZOMBIFIED_PIGLIN,
        DisguiseType.PILLAGER
    ).map { MobDisguise(it) }.associateWith { AtomicInteger(0) }
    fun disguise(player: Player) {
        val minValue = availableDisguises.minOf { it.value.get() }
        val disguisers = availableDisguises.filter { it.value.get() == minValue }
        DisguiseAPI.disguiseEntity(player,disguisers.keys.random())
    }
}