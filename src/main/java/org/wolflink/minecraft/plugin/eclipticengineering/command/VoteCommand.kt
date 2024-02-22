package org.wolflink.minecraft.plugin.eclipticengineering.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.MeetingHandler
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent

object VoteCommand: CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ee-vote")?.setExecutor(this)
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false
        if(args.getOrNull(0) == "vote") {
            val beenVotedName = args.getOrNull(1) ?: run {
                sender.sendMessage("$MESSAGE_PREFIX 请输入你要投票的玩家ID。".toComponent())
                return false
            }
            val beenVoted = Bukkit.getPlayer(beenVotedName) ?: run {
                sender.sendMessage("$MESSAGE_PREFIX 未找到玩家 $beenVotedName 。".toComponent())
                return false
            }
            if(beenVoted == sender) {
                sender.sendMessage("$MESSAGE_PREFIX 你不能对自己投票。".toComponent())
                return false
            }
            MeetingHandler.vote(sender,beenVoted)
            return true
        }
        if(args.getOrNull(0) == "abstain") {
            MeetingHandler.abstain(sender)
            return true
        }
        return false
    }
}