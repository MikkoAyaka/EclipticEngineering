package org.wolflink.minecraft.plugin.eclipticengineering

import kotlinx.coroutines.cancel
import org.bukkit.plugin.java.JavaPlugin
import org.wolflink.minecraft.plugin.eclipticengineering.command.AbilityCommand
import org.wolflink.minecraft.plugin.eclipticengineering.command.BuildCommand
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.AuxiliaryBlockListener
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.BuildToolListener
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.HitMonsterListener
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.WorkingToolListener
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.TowerArrow
import org.wolflink.minecraft.plugin.eclipticstructure.coroutine.EStructureScope
import org.wolflink.minecraft.plugin.eclipticstructure.extension.register
import org.wolflink.minecraft.plugin.eclipticstructure.library.DynamicLibrary

class EclipticEngineering : JavaPlugin() {
    companion object {
        lateinit var instance: EclipticEngineering
    }
    override fun onLoad() {
        instance = this
    }
    override fun onEnable() {
        // 注册指令
        BuildCommand.register()
        AbilityCommand.register()
        // 注册可用的建筑结构
        StructureType.entries.forEach(StructureType::register)
        // 注册事件监听器
        BuildToolListener.register(this)
        WorkingToolListener.register(this)
        AuxiliaryBlockListener.register(this)
        HitMonsterListener.register(this)
        // 注册防御塔监听器
        TowerArrow.BukkitListener.register(this)
    }

    override fun onDisable() {
        // 取消注册可用的建筑结构
        StructureType.entries.forEach(StructureType::unregister)

        EEngineeringScope.cancel()
    }
}
