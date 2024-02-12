package org.wolflink.minecraft.plugin.eclipticengineering.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.wolflink.minecraft.plugin.eclipticengineering.stage.StageHolder
import org.wolflink.minecraft.plugin.eclipticengineering.stage.WaitStage

object StageCommand: CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ee-stage")?.setExecutor(this)
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(!sender.isOp) return false
        if(args.getOrNull(0) == "start") {
            if(StageHolder.thisStage is WaitStage) {
                StageHolder.next()
                return true
            }
            else {
                sender.sendMessage("当前不处于准备阶段，无法开始游戏。")
                return false
            }
        }
        return false
    }
}