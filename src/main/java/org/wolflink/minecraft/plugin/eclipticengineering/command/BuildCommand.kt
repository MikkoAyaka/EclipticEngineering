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
            if(structureMeta.blueprints.firstOrNull() is ConditionBlueprint) {
                val conditionText = mutableListOf<String>()
                val blueprint = structureMeta.blueprints[structureLevel-1] as ConditionBlueprint
                var pass = true
                for (condition in blueprint.conditions) {
                    if(!condition.isSatisfy(sender)) {
                        pass = false
                        conditionText.add(condition.description)
                    }
                }
                if(pass) blueprint.conditions.forEach { if(it is Requirement)it.delivery(sender) }
                else {
                    sender.sendMessage("$MESSAGE_PREFIX 无法建造${blueprint.structureName} <hover:show_text:'<newline>${conditionText.joinToString(separator = "<newline>")}<newline><newline>'><yellow>[详情]".toComponent())
                    return false
                }
            } else Bukkit.getLogger().warning("$MESSAGE_PREFIX 建筑结构 $structureTypeName 的蓝图类型为非资源型蓝图，这是不应该的，请检查代码。")
            val builder = Builder(structureLevel,structureMeta,sender.location,false)
            builder.build(sender)
            return true
        }
        return false
    }
}