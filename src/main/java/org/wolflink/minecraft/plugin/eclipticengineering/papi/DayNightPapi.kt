package org.wolflink.minecraft.plugin.eclipticengineering.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

/**
 * %eedn_days%
 * %eedn_status%
 */
object DayNightPapi: PlaceholderExpansion()  {
    override fun getIdentifier() = "eedn"

    override fun getAuthor() = "MikkoAyaka"

    override fun getVersion() = "1.0.0"

    override fun onRequest(player: OfflinePlayer?, params: String): String {
        when(params) {
            "days" -> {
                return "${DayNightHandler.days}"
            }
            "status" -> {
                return DayNightHandler.status.displayName
            }
        }
        return "未知变量"
    }
}