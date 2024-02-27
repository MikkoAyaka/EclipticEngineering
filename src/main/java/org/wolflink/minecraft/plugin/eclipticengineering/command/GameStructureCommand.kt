package org.wolflink.minecraft.plugin.eclipticengineering.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.eeLogger
import org.wolflink.minecraft.plugin.eclipticengineering.papi.PlayerStructurePapi
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository

/**
 * /eegs repair 修理建筑
 * /eegs destroy 摧毁建筑
 */
object GameStructureCommand: CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ee-gamestructure")?.setExecutor(this)
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false
        // 从变量中获取玩家当前正在查看的建筑ID
        val structureId = PlayerStructurePapi.getStructureId(sender) ?: return false
        val structure = StructureRepository.find(structureId) ?: return false
        if(structure !is GameStructure) {
            eeLogger.warning("${sender.name} 尝试与非 GameStructure 类型交互以进行修理/摧毁等行为，这是不应该发生的。")
            return false
        }
        when(args.getOrElse(0){""}.lowercase()) {
            // 获取建筑ID
            // 修理建筑
            "repair" -> {
                structure.repair(sender)
                return true
            }
            // 摧毁建筑
            "destroy" -> {
                structure.destroy(sender)
                return true
            }
        }
        sender.sendMessage("$MESSAGE_PREFIX 不匹配的 GameStructure 命令。".toComponent())
        return false
    }
}