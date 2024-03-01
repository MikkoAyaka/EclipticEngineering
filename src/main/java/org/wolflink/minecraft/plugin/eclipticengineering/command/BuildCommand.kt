package org.wolflink.minecraft.plugin.eclipticengineering.command

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
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
    private val buildRequestMap = mutableMapOf<Player,(Location)->Unit>()
    // 挂起建造请求
    private fun hangupBuildRequest(player: Player,buildTask:(Location)->Unit) {
        buildRequestMap[player] = buildTask
        player.sendMessage("$MESSAGE_PREFIX <green>建造任务已挂起，请在 15 秒内使用建造工具左键以指定建造点。".toComponent())
    }
    fun hasBuildRequest(player: Player) = buildRequestMap[player] != null
    fun continueBuildRequest(player: Player,location: Location) {
        val buildTask = buildRequestMap[player]!!
        buildTask.invoke(location)
        buildRequestMap.remove(player)
    }
    private fun cancelBuildRequest(player: Player) {
        buildRequestMap.remove(player)
        player.sendMessage("$MESSAGE_PREFIX <yellow>没有在指定时间内选择建造点，建造任务已取消。".toComponent())
    }
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
            // 挂起建造请求，等待玩家选择建造地点
            hangupBuildRequest(sender) {
                val builder = Builder(structureLevel,structureMeta,it)
                builder.build(sender,broadcast,floorCheck,playerCheck,zoneCheck)
            }
            // 超时检测
            EEngineeringScope.launch {
                delay(1000 * 15)
                if(buildRequestMap.containsKey(sender)) cancelBuildRequest(sender)
            }
            return true
        } catch (e: Exception) {
            sender.sendMessage("$MESSAGE_PREFIX <red>执行指令时发生错误，请检查指令格式。".toComponent())
        }
        return false
    }
}