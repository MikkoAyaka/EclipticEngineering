package org.wolflink.minecraft.plugin.eclipticengineering

import kotlinx.coroutines.cancel
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.wolflink.minecraft.plugin.eclipticengineering.command.AbilityCommand
import org.wolflink.minecraft.plugin.eclipticengineering.command.BuildCommand
import org.wolflink.minecraft.plugin.eclipticengineering.command.StageCommand
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.*
import org.wolflink.minecraft.plugin.eclipticengineering.metadata.MetadataHandler
import org.wolflink.minecraft.plugin.eclipticengineering.monster.CreatureSpawnListener
import org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai.SmartAIListener
import org.wolflink.minecraft.plugin.eclipticengineering.papi.AbilityPapi
import org.wolflink.minecraft.plugin.eclipticengineering.papi.StagePapi
import org.wolflink.minecraft.plugin.eclipticengineering.papi.VirtualResourcePapi
import org.wolflink.minecraft.plugin.eclipticengineering.resource.VirtualTeamInventory
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.BuildMenuItem
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.MainMenuItem
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.TaskBook
import org.wolflink.minecraft.plugin.eclipticengineering.stage.StageHolder
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.TowerArrow
import org.wolflink.minecraft.plugin.eclipticengineering.utils.WorldAPI
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
        WorldAPI.regen(Config.gameWorldName)
        // 注册指令
        BuildCommand.register()
        AbilityCommand.register()
        StageCommand.register()
        // 注册可用的建筑结构
        StructureType.entries.forEach(StructureType::register)
        // 注册事件监听器
        WorkingToolListener.register(this)
        AuxiliaryBlockListener.register(this)
        HitMonsterListener.register(this)
        MetadataHandler.register(this)
        CreatureSpawnListener.register(this)
        SmartAIListener.register(this)
        FertilizeListener.register(this)
        PlayerDeathListener.register(this)
        FirstJoinListener.register(this)
        BlockPlaceListener.register(this)
        // 注册防御塔监听器
        TowerArrow.BukkitListener.register(this)
        // 注册背包监听器
        VirtualTeamInventory.register(this)
        // 注册任务书监听器
        TaskBook.register(this)
        MainMenuItem.register(this)
        BuildMenuItem.register(this)
        // 注册变量
        StagePapi.register()
        VirtualResourcePapi.register()
        AbilityPapi.register()
        // 初始化阶段
        StageHolder.init()
    }

    override fun onDisable() {
        // 取消注册可用的建筑结构
        StructureType.entries.forEach(StructureType::unregister)

        EEngineeringScope.cancel()
    }
}
