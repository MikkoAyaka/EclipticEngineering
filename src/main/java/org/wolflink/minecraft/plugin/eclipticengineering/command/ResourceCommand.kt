package org.wolflink.minecraft.plugin.eclipticengineering.command

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.ResourceMapper
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.SpecialItem

/**
 * 商店资源指令
 * /eer special get PioneerBook 发放特殊资源
 */
object ResourceCommand: CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ee-resource")?.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false
        if(!sender.isOp) return false
        if(args.getOrNull(0) == "special" && args.getOrNull(1) == "get") {
            val specialResource = ResourceMapper.mapToSpecialResource(args.getOrNull(2))
            if(specialResource == null) {
                sender.playSound(sender, Sound.ENTITY_VILLAGER_NO,1f,1f)
                sender.sendMessage("$MESSAGE_PREFIX 未找到匹配的特殊资源类型：${args.getOrNull(2)}}")
                return false
            }
            specialResource.give(sender)
            return true
        }
        return false
    }
}