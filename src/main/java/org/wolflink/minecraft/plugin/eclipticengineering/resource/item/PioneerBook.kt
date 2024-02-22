package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem

object PioneerBook {
    val defaultItem: ItemStack = Material.BOOK.createSpecialItem(
        SpecialItemType.SPECIAL_ITEM,
        Quality.UNIQUE,
        "开拓者 帮助手册",
        false,
        listOf(
            "    ${PRIMARY_TEXT_COLOR}任务目标：建立并不断发展据点。",
            "",
            "    ${PRIMARY_TEXT_COLOR}开拓者们，这里有一个非常不幸的消息，",
            "    ${PRIMARY_TEXT_COLOR}幽匿伪装者正潜伏在你们之中，",
            "    ${PRIMARY_TEXT_COLOR}也许是一位，也许...是多个。",
            "    ${PRIMARY_TEXT_COLOR}不论如何，一定要在第5天到来之前抓住他。",
        )
    )
}