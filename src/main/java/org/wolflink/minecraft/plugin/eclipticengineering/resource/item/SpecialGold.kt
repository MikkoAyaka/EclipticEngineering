package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Material
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticstructure.config.PRIMARY_TEXT_COLOR

object SpecialGold: SpecialItem(
    Material.RAW_GOLD.createSpecialItem(
        SpecialItemType.SPECIAL_RESOURCE,
        Quality.EXQUISITE,
        "闪金矿石",
        false,
        listOf("    ${PRIMARY_TEXT_COLOR}携带着星辰的记忆，","    ${PRIMARY_TEXT_COLOR}每一粒金砂都闪烁着过往光年的辉煌。")
    )
)