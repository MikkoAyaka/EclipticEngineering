package org.wolflink.minecraft.plugin.eclipticengineering.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Requirement
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder
import org.wolflink.minecraft.plugin.eclipticstructure.structure.registry.StructureRegistry

/**
 *      结构名         等级 是否播报 地板检测 玩家检测 空间检测
 * /eeb energy_source 1   false   true   true    true
 */
object BuildCommand:CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ee-build")?.setExecutor(this)
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false
        try {
            val structureTypeName = args.getOrNull(0)?.uppercase() ?: return false
            val structureLevel = args.getOrNull(1)?.toIntOrNull() ?: return false
            val broadcast = args.getOrElse(2){"false"}.toBooleanStrict()
            val floorCheck = args.getOrElse(3){"true"}.toBooleanStrict()
            val playerCheck = args.getOrElse(4){"true"}.toBooleanStrict()
            val zoneCheck = args.getOrElse(5){"true"}.toBooleanStrict()
            val structureMeta = StructureRegistry.get(structureTypeName)
            val builder = Builder(structureLevel,structureMeta,sender.location)
            builder.build(sender,broadcast,floorCheck,playerCheck,zoneCheck)
            return true
        } catch (e: Exception) {
            sender.sendMessage("$MESSAGE_PREFIX <red>执行指令时发生错误，请检查指令格式。".toComponent())
        }
        return false
    }
}