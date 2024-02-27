package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Material
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticstructure.config.PRIMARY_TEXT_COLOR

object SpecialDiamond: SpecialItem(
    Material.DIAMOND.createSpecialItem(
    SpecialItemType.SPECIAL_RESOURCE,
    Quality.EPIC,
    "辉晶矿石",
    true,
    listOf("    ${PRIMARY_TEXT_COLOR}每一面切割都精准地捕捉到了天际的璀璨，","    ${PRIMARY_TEXT_COLOR}仿佛夜空最明亮的星辰凝聚而成。")
))