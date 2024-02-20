package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticstructure.extension.parsePapi

object MainMenuItem: InteractiveItem(
    Material.SCULK_SHRIEKER.createSpecialItem(
        SpecialItemType.SPECIAL_ITEM,
        Quality.EXQUISITE,
        "幽光回响终端",
        true,
        listOf("    ${PRIMARY_TEXT_COLOR}改造后的幽匿尖啸体，","    ${PRIMARY_TEXT_COLOR}能够发送并接收来自另一端的幽光信号。")
    )
){
    private fun openMainMenu(player: Player) {
        player.playSound(player, Sound.BLOCK_SCULK_SHRIEKER_SHRIEK,0.4f,1f)
        player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_RESONATE,1f,1f)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.mainMenuCmd.parsePapi(player))
    }
    override fun onInteract(player: Player) {
        openMainMenu(player)
    }
}