package org.wolflink.minecraft.plugin.eclipticengineering.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.forge.ForgeHandler

/**
 * /eef weapon  锻造武器
 * /eef repair  锻造修复材料
 */
object ForgeCommand: CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ee-forge")?.setExecutor(this)
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false
        when(args.getOrNull(0)) {
            "weapon" -> {
                ForgeHandler.forgeWeapon(sender)
                return true
            }
            "repair" -> {
                ForgeHandler.forgeRepair(sender)
                return true
            }
        }
        return false
    }
}