package org.wolflink.minecraft.plugin.eclipticengineering.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTree
import java.lang.Exception

object AbilityCommand: CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ee-ability")?.setExecutor(this)
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false
        if(!sender.isOp) return false
        return when {
            args.getOrNull(0) == "ability" -> {
                when {
                    args.getOrNull(1) == "set" -> {
                        val abilityName = args.getOrNull(2) ?: return false
                        val abilityLevel = args.getOrNull(3)?.toInt() ?: return false
                        val ability = try { Ability.valueOf(abilityName.uppercase()) } catch (_: Exception){ return false }
                        sender.abilityTree.setAbility(ability,abilityLevel)
                        println("你的 $ability 等级已被设置为 $abilityLevel")
                        true
                    }
                    else -> false
                }
            }
            else -> false
        }
    }
}