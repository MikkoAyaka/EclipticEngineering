package org.wolflink.minecraft.plugin.eclipticengineering.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

object BuildCommand:CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ecliptic-engineering")?.setExecutor(this)
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false
        if(args.getOrNull(0) == "build" && args.getOrElse(1) { "" }.isNotEmpty()) {
            val structureTypeName = args[1].uppercase()
            val builder = Builder(structureTypeName,sender.location,false)
            builder.build(sender)
            return true
        }
        return false
    }
}