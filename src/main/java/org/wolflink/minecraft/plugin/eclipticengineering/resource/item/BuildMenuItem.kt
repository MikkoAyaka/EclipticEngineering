package org.wolflink.minecraft.plugin.eclipticengineering.resource.item

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.Quality
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.SpecialItemType
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.extension.createSpecialItem
import org.wolflink.minecraft.plugin.eclipticengineering.papi.PlayerStructurePapi
import org.wolflink.minecraft.plugin.eclipticstructure.META_STRUCTURE_ID
import org.wolflink.minecraft.plugin.eclipticstructure.config.PRIMARY_TEXT_COLOR
import org.wolflink.minecraft.plugin.eclipticstructure.esLogger
import org.wolflink.minecraft.plugin.eclipticstructure.extension.parsePapi
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure

object BuildMenuItem: InteractiveItem(
    "幽光万华镜",
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

    /**
     * 打开单个建筑的详细信息菜单
     */
    private fun openStructureMenu(player: Player,structure: Structure) {
        if(player.abilityTable.checkAbilityWithNotice(Ability.BUILDING,2)) {
            // 更新变量
            PlayerStructurePapi.updateRelation(player,structure)
            // 打开菜单
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.structureMenuCmd.parsePapi(player))
        }
    }

    override fun onLeftClick(e: PlayerInteractEvent) {
        if(e.clickedBlock?.hasMetadata(META_STRUCTURE_ID) != true) return
        val structure = StructureRepository.find(e.clickedBlock!!.getMetadata(META_STRUCTURE_ID).first().asInt())
        if(structure == null) {
            esLogger.warning("玩家与带有建筑结构标签的方块交互，但是未找到建筑结构。")
            return
        }
        openStructureMenu(e.player,structure)
    }
    override fun onRightClick(e: PlayerInteractEvent) {
        openBuildMenu(e.player)
    }
}