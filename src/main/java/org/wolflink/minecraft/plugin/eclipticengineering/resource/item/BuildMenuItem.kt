package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticstructure.extension.parsePapi

object BuildMenuItem: InteractiveItem(
    Material.SPYGLASS.createSpecialItem(
        SpecialItemType.SPECIAL_ITEM,
        Quality.EXQUISITE,
        "幽光万华镜",
        true,
        listOf("    ${PRIMARY_TEXT_COLOR}窥视深渊的万华镜，","    ${PRIMARY_TEXT_COLOR}能够看到些什么呢？")
    )
) {
    /**
     * 打开建筑菜单
     */
    private fun openBuildMenu(player: Player) {
        if(player.abilityTable.checkAbilityWithNotice(Ability.BUILDING,2)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.buildMenuCmd.parsePapi(player))
        }
    }
    override fun onInteract(player: Player) {
        openBuildMenu(player)
    }
}