package org.wolflink.minecraft.plugin.eclipticengineering.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler

/**
 * %eegs_xxxxx_x_conditions% 类型名称为 xxxxx 等级为 x 的建筑的建造条件
 * %eegs_xxxxx_tags% 建筑类型为 xxxxx 的标签
 */
object GameStructurePapi: PlaceholderExpansion()  {
    override fun getIdentifier() = "eegs"

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