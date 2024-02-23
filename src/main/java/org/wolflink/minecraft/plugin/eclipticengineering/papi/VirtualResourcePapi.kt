package org.wolflink.minecraft.plugin.eclipticengineering.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.resource.virtual.VirtualTeamInventory
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toHex

/**
 * 关于虚拟资源的变量
 *
 * %eevr_wood%
 * %eevr_stone%
 * %eevr_xxx% 名称为xxx的虚拟资源的数量
 */
object VirtualResourcePapi: PlaceholderExpansion()  {
    override fun getIdentifier() = "eevr"

    override fun getAuthor() = "MikkoAyaka"

    override fun getVersion() = "1.0.0"

    override fun onRequest(player: OfflinePlayer?, params: String): String {
        try {
            val type = VirtualResourceType.valueOf(params.uppercase())
            return "&#E8E8E8${VirtualTeamInventory.get(type)} &#${type.color.toHex()}${type.displayName}"
        } catch (_: IllegalArgumentException){}
        return "未知虚拟资源"
    }
}