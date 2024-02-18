package org.wolflink.minecraft.plugin.eclipticengineering

import kotlinx.coroutines.cancel
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.wolflink.minecraft.plugin.eclipticengineering.command.AbilityCommand
import org.wolflink.minecraft.plugin.eclipticengineering.command.BuildCommand
import org.wolflink.minecraft.plugin.eclipticengineering.command.StageCommand
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.AuxiliaryBlockListener
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.MenuListener
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.HitMonsterListener
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.WorkingToolListener
import org.wolflink.minecraft.plugin.eclipticengineering.metadata.MetadataHandler
import org.wolflink.minecraft.plugin.eclipticengineering.monster.CreatureSpawnListener
import org.wolflink.minecraft.plugin.eclipticengineering.papi.StagePapi
import org.wolflink.minecraft.plugin.eclipticengineering.papi.VirtualResourcePapi
import org.wolflink.minecraft.plugin.eclipticengineering.resource.VirtualTeamInventory
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.TaskBook
import org.wolflink.minecraft.plugin.eclipticengineering.stage.StageHolder
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.TowerArrow
import org.wolflink.minecraft.plugin.eclipticstructure.extension.register

class EclipticEngineering : JavaPlugin() {
    companion object {
        lateinit var instance: EclipticEngineering
        fun runTask(block: ()->Unit) {
            Bukkit.getScheduler().runTask(instance,block)
        }
    }
    override fun onLoad() {
        instance = this
    }
    override fun onEnable() {
        // 注册指令
        BuildCommand.register()
        AbilityCommand.register()
        StageCommand.register()
        // 注册可用的建筑结构
        StructureType.entries.forEach(StructureType::register)
        // 注册事件监听器
        MenuListener.register(this)
        WorkingToolListener.register(this)
        AuxiliaryBlockListener.register(this)
        HitMonsterListener.register(this)
        MetadataHandler.register(this)
        CreatureSpawnListener.register(this)
        // 注册防御塔监听器
        TowerArrow.BukkitListener.register(this)
        // 注册背包监听器
        VirtualTeamInventory.register(this)
        // 注册任务书监听器
        TaskBook.register(this)
        // 注册变量
        StagePapi.register()
        VirtualResourcePapi.register()
        // 初始化阶段
        StageHolder.init()
    }

    override fun onDisable() {
        // 取消注册可用的建筑结构
        StructureType.entries.forEach(StructureType::unregister)

        EEngineeringScope.cancel()
    }
}
