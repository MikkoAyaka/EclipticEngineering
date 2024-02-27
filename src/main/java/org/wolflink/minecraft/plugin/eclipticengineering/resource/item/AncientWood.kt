package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Material
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticstructure.config.PRIMARY_TEXT_COLOR

object AncientWood: SpecialItem(
    Material.STRIPPED_OAK_LOG.createSpecialItem(
    SpecialItemType.SPECIAL_RESOURCE,
    Quality.RARE,
    "永翠木",
    false,
    listOf(
        "    ${PRIMARY_TEXT_COLOR}吸收了世界的精华，",
        "    ${PRIMARY_TEXT_COLOR}历经千年仍绿意盎然。"
    )
))