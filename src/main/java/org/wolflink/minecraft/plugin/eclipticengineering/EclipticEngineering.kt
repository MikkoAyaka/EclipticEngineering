package org.wolflink.minecraft.plugin.eclipticengineering

import kotlinx.coroutines.cancel
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.wolflink.minecraft.plugin.eclipticengineering.ability.OccupationHandler
import org.wolflink.minecraft.plugin.eclipticengineering.command.*
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.*
import org.wolflink.minecraft.plugin.eclipticengineering.metadata.MetadataHandler
import org.wolflink.minecraft.plugin.eclipticengineering.monster.CreatureSpawnListener
import org.wolflink.minecraft.plugin.eclipticengineering.monster.MonsterDropItemListener
import org.wolflink.minecraft.plugin.eclipticengineering.monster.smartai.SmartAIListener
import org.wolflink.minecraft.plugin.eclipticengineering.papi.*
import org.wolflink.minecraft.plugin.eclipticengineering.resource.virtual.VirtualTeamInventory
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.BuildMenuItem
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.DisguiserBook
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.MainMenuItem
import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.TaskBook
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DayListener
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.DisguiserNotice
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.RangeChat
import org.wolflink.minecraft.plugin.eclipticengineering.roleplay.playergoal.PlayerGoalHolder
import org.wolflink.minecraft.plugin.eclipticengineering.stage.StageHolder
import org.wolflink.minecraft.plugin.eclipticengineering.structure.listener.BuilderListener
import org.wolflink.minecraft.plugin.eclipticengineering.structure.defense.TowerArrow
import org.wolflink.minecraft.plugin.eclipticengineering.utils.WorldAPI
import org.wolflink.minecraft.plugin.eclipticstructure.extension.register

class EclipticEngineering : JavaPlugin() {
    companion object {
        lateinit var instance: EclipticEngineering
        fun runTaskTimer(delay:Long,period:Long,block: ()->Unit) {
            Bukkit.getScheduler().runTaskTimer(instance,block,delay,period)
        }
        fun runTask(block: ()->Unit) {
            Bukkit.getScheduler().runTask(instance,block)
        }
        fun runTaskLater(delay: Long,block: ()->Unit) {
            Bukkit.getScheduler().runTaskLater(instance,block,delay)
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
        VoteCommand.register()
        GameStructureCommand.register()
        ForgeCommand.register()
        ResourceCommand.register()
        DebugCommand.register()
        // 注册可用的建筑结构
        StructureType.entries.forEach(StructureType::register)
        // 注册事件监听器
        WorkingToolListener.register(this)
        HitMonsterListener.register(this)
        MetadataHandler.register(this)
        CreatureSpawnListener.register(this)
        SmartAIListener.register(this)
        FertilizeListener.register(this)
        PlayerDeathListener.register(this)
        PlayerJoinListener.register(this)
        BuilderListener.register(this)
        PlayerGoalHolder.register(this)
        DisguiserNotice.register(this)
        AuxiliaryBlockListener.register(this)
        StepFarmlandListener.register(this)
        DayListener.register(this)
        MonsterDropItemListener.register(this)
        RangeChat.register(this)
        // 注册防御塔监听器
        TowerArrow.BukkitListener.register(this)
        // 注册背包监听器
        VirtualTeamInventory.register(this)
        // 注册任务书监听器
        TaskBook.register(this)
        MainMenuItem.register(this)
        BuildMenuItem.register(this)
        DisguiserBook.register(this)
        // 注册变量
        StagePapi.register()
        VirtualResourcePapi.register()
        AbilityPapi.register()
        DayNightPapi.register()
        GameStructurePapi.register()
        PlayerStructurePapi.register()
        ForgePapi.register()
        // 初始化职业效果
        OccupationHandler.initEffects()
        // 初始化阶段
        StageHolder.init()
    }

    override fun onDisable() {
        // 取消注册可用的建筑结构
        StructureType.entries.forEach(StructureType::unregister)

        EEngineeringScope.cancel()
    }
}
