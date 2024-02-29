package org.wolflink.minecraft.plugin.eclipticengineering.extension

import org.bukkit.OfflinePlayer
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Condition

/**
 * 将条件集合转为格式化的 DeluxeMenu 描述文本
 */
fun Collection<Condition>.toDeluxeMenuLores(player: OfflinePlayer) =
    this.joinToString(separator = "\n") {
        if(it.isSatisfy(player.player)) "    &#33FF33☑"+it.description
        else "    &#FF3333☒"+it.description
    }