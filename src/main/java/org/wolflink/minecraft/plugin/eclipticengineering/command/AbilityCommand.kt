package org.wolflink.minecraft.plugin.eclipticengineering.command

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toHex
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toHexFormat
import java.lang.Exception

object AbilityCommand: CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ee-ability")?.setExecutor(this)
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false
        return when (args.getOrNull(0)) {
            "set" -> {
                if(!sender.isOp) return false
                val abilityName = args.getOrNull(1) ?: return false
                val abilityLevel = args.getOrNull(2)?.toInt() ?: return false
                val playerName = args.getOrNull(3) ?: sender.name
                val ability = try { Ability.valueOf(abilityName.uppercase()) } catch (_: Exception){ return false }
                val player = Bukkit.getOfflinePlayer(playerName).apply { abilityTable.setAbility(ability,abilityLevel) }
                sender.sendMessage("$MESSAGE_PREFIX <white>${player.name} 的 ${ability.color.toHexFormat()}$ability <white>等级已被设置为 <green>$abilityLevel".toComponent())
                sender.playSound(sender,Sound.ENTITY_PLAYER_LEVELUP,1f,1.75f)
                true
            }
            "add" -> {
                val abilityName = args.getOrNull(1) ?: return false
                val ability = try { Ability.valueOf(abilityName.uppercase()) } catch (_: Exception){ return false }
                if(!sender.abilityTable.hasPoint()) {
                    sender.sendActionBar("$MESSAGE_PREFIX 你没有足够的能力点数了。".toComponent())
                    sender.playSound(sender,Sound.ENTITY_VILLAGER_NO,1f,0.7f)
                    return false
                }
                val abilityLevel = sender.abilityTable.getLevel(ability)
                if(abilityLevel >= ability.maxLevel) {
                    sender.sendActionBar("$MESSAGE_PREFIX <white>你的 ${ability.color.toHexFormat()}$ability <white>等级已达到上限，最大为 <green>${sender.abilityTable.getLevel(ability)}".toComponent())
                    sender.playSound(sender,Sound.ENTITY_VILLAGER_NO,1f,0.7f)
                    return false
                }
                sender.abilityTable.addAbility(ability)
                sender.sendActionBar("$MESSAGE_PREFIX <white>你的 ${ability.color.toHexFormat()}$ability <white>等级已达到 <green>${sender.abilityTable.getLevel(ability)}".toComponent())
                sender.playSound(sender,Sound.ENTITY_PLAYER_LEVELUP,1f,1.75f)
                true
            }
            else -> false
        }
    }
}