package org.wolflink.minecraft.plugin.eclipticengineering.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.wolflink.minecraft.plugin.eclipticengineering.extension.toDeluxeMenuLores
import org.wolflink.minecraft.plugin.eclipticengineering.forge.ForgeHandler

object ForgePapi: PlaceholderExpansion() {
    override fun getIdentifier() = "eef"

    override fun getAuthor() = "MikkoAyaka"

    override fun getVersion() = "1.0.0"

    override fun onRequest(player: OfflinePlayer?, params: String): String {
        if(player == null) return "未知玩家"
        if (params == "weapon_conditions") return ForgeHandler.weaponConditions.toDeluxeMenuLores(player)
        if (params == "repair_conditions") return ForgeHandler.repairConditions.toDeluxeMenuLores(player)
        return "未知变量"
    }
}