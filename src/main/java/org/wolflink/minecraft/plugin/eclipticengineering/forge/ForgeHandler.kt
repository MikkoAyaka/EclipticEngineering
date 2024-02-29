package org.wolflink.minecraft.plugin.eclipticengineering.forge

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config.forgeMenuCmd
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.*
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialDiamond
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialGold
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialIron
import org.wolflink.minecraft.plugin.eclipticengineering.utils.toRandomDistribution
import org.wolflink.minecraft.plugin.eclipticstructure.extension.parsePapi
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

object ForgeHandler {
    val weaponConditions = setOf(
        SpecialItemRequirement(SpecialIron,3),
        SpecialItemRequirement(SpecialGold,3),
        SpecialItemRequirement(SpecialDiamond,1),
        AbilityCondition(Ability.SMELTING,2)
    )
    val repairConditions = setOf<Condition>(
        SpecialItemRequirement(SpecialIron,2),
        SpecialItemRequirement(SpecialGold,1),
        AbilityCondition(Ability.SMELTING,2)
    )
    private val randomWeaponCommand = mapOf(
        "itemgenerator give %player_name% 武器_普通" to 15,
        "itemgenerator give %player_name% 武器_优秀" to 30,
        "itemgenerator give %player_name% 武器_稀有" to 35,
        "itemgenerator give %player_name% 武器_史诗" to 15,
        "itemgenerator give %player_name% 武器_传说" to 4,
        "itemgenerator give %player_name% 武器_奥义" to 1
    ).toRandomDistribution()
    private val randomRepairCommand = mapOf(
        "rpgrepair give %player_name% sample_tool 1" to 5,
        "rpgrepair give %player_name% sample_tool 2" to 25,
        "rpgrepair give %player_name% sample_tool 3" to 35,
        "rpgrepair give %player_name% sample_tool 4" to 25,
        "rpgrepair give %player_name% sample_tool 5" to 10,
    ).toRandomDistribution()
    fun openForgeMenu(player: Player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),forgeMenuCmd.parsePapi(player))
    }
    fun forgeWeapon(player: Player) {
        // 不满足条件
        if(weaponConditions.any { !it.isSatisfy(player) }) {
            player.sendMessage("$MESSAGE_PREFIX 你不满足锻造所需的条件。".toComponent())
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1f,1f)
            return
        }
        weaponConditions.forEach { if(it is Requirement) it.delivery(player) }
        player.sendMessage("$MESSAGE_PREFIX 锻造成功。".toComponent())
        player.playSound(player, Sound.ENTITY_VILLAGER_YES,1f,1f)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), randomWeaponCommand.random().parsePapi(player))
    }
    fun forgeRepair(player: Player) {
        if(repairConditions.any{ !it.isSatisfy(player) }) {
            player.sendMessage("$MESSAGE_PREFIX 你不满足锻造所需的条件。".toComponent())
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1f,1f)
            return
        }
        repairConditions.forEach { if(it is Requirement) it.delivery(player) }
        player.sendMessage("$MESSAGE_PREFIX 锻造成功。".toComponent())
        player.playSound(player, Sound.ENTITY_VILLAGER_YES,1f,1f)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), randomRepairCommand.random().parsePapi(player))
    }
}