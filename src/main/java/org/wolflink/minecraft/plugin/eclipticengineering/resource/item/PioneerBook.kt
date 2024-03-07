package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.config.GameSettings
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayNightHandler
import org.wolflink.minecraft.plugin.eclipticstructure.config.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticstructure.config.SPLITER_COLOR

object PioneerBook: SpecialItem(
    "开拓者 帮助手册",
    Material.BOOK.createSpecialItem(
        SpecialItemType.SPECIAL_ITEM,
        Quality.UNIQUE,
        "开拓者 帮助手册",
        false,
        listOf(
            "   ${SPLITER_COLOR}│ <#CCFFCC>目标",
            "",
            "    ${PRIMARY_TEXT_COLOR}建立并完善据点，利用防御塔巩固基地",
            "    ${PRIMARY_TEXT_COLOR}比末影龙还要强大的 <#FF3333>BOSS</#FF3333> 将在 <yellow>第 ${GameSettings.totalDays+1} 天</yellow> 降临",
            "    ${PRIMARY_TEXT_COLOR}如果不能及时找出幽匿伪装者",
            "    ${PRIMARY_TEXT_COLOR}<#FF3333>BOSS</#FF3333> 将获得 <#7F00FF>史诗级强化</#7F00FF>",
            "",
            "   ${SPLITER_COLOR}│ <#FFFFFF>简介",
            "",
            "    ${PRIMARY_TEXT_COLOR}开拓者们，本次任务艰巨，",
            "    ${PRIMARY_TEXT_COLOR}幽匿伪装者正潜伏在你们之中，",
            "    ${PRIMARY_TEXT_COLOR}也许是一位，也许...是多个。",

            "    ${PRIMARY_TEXT_COLOR}不论如何，一定要在第 ${GameSettings.totalDays+1} 天到来之前抓住他！",
            "",
            "   ${SPLITER_COLOR}│ <#66B2FF>提示",
            "",
            "    ${PRIMARY_TEXT_COLOR}幽匿伪装者会有一些 异于常人 的行为需要留意。",
            "    ${PRIMARY_TEXT_COLOR}手持 木头、石头、金属 等资源，",
            "    ${PRIMARY_TEXT_COLOR}蹲下并丢出即可提交到团队共享仓库。",
            "",
        )
    )
)