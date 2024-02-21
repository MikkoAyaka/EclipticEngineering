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

object BuildCommand:CommandExecutor {
    fun register() {
        Bukkit.getPluginCommand("ee-build")?.setExecutor(this)
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false
        if(args.getOrNull(0) == "build") {
            val structureTypeName = args.getOrNull(1)?.uppercase() ?: return false
            val structureLevel = args.getOrNull(2)?.toIntOrNull() ?: return false
            val structureMeta = StructureRegistry.get(structureTypeName)
            val builder = Builder(structureLevel,structureMeta,sender.location,false)
            builder.build(sender)
            return true
        }
        return false
    }
}