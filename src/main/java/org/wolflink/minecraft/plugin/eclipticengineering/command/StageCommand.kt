package org.wolflink.minecraft.plugin.eclipticengineering.command

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.stage.StageHolder
import org.wolflink.minecraft.plugin.eclipticengineering.stage.WaitStage
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

object StageCommand: CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ee-stage")?.setExecutor(this)
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false
        if(!sender.isOp) return false
        if(args.getOrNull(0) == "start") {
            return if(StageHolder.thisStage is WaitStage) {
                sender.sendMessage("$MESSAGE_PREFIX 你已触发开始游戏。".toComponent())
                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,1f,1f)
                StageHolder.next()
                true
            } else {
                sender.sendMessage("$MESSAGE_PREFIX <yellow>当前不处于准备阶段，无法开始游戏。".toComponent())
                sender.playSound(sender, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,1f,1f)
                false
            }
        }
        return false
    }
}