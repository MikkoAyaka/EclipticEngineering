package org.wolflink.minecraft.plugin.eclipticengineering.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.VirtualResourceType
import org.wolflink.minecraft.plugin.eclipticengineering.resource.VirtualTeamInventory

/**
 * 关于虚拟资源的变量
 *
 * %eevr_wood% 当前阶段
 * %eevr_stone% 当前目标
 * %eevr_xxx% 名称为xxx的虚拟资源的数量
 */
object VirtualResourcePapi: PlaceholderExpansion()  {
    override fun getIdentifier() = "eevr"

    override fun getAuthor() = "MikkoAyaka"

    override fun getVersion() = "1.0.0"

    override fun onRequest(player: OfflinePlayer?, params: String): String {
        try {
            val type = VirtualResourceType.valueOf(params.uppercase())
            return "${VirtualTeamInventory.get(type)}"
        } catch (_: IllegalArgumentException){}
        return "未知虚拟资源"
    }
}