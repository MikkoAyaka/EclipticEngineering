package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Material
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem

object SpecialIron: SpecialItem(
    Material.RAW_IRON.createSpecialItem(
        SpecialItemType.SPECIAL_RESOURCE,
        Quality.RARE,
        "陨铁矿石",
        false,
        listOf("    ${PRIMARY_TEXT_COLOR}质地坚硬无比，","    ${PRIMARY_TEXT_COLOR}散发着淡淡的星光。")
    )
)