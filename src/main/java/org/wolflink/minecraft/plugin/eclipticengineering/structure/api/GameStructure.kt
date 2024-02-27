package org.wolflink.minecraft.plugin.eclipticengineering.structure.api

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GameStructureBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.requirement.Requirement
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder

abstract class GameStructure(val type: StructureType,val gsBlueprint: GameStructureBlueprint, builder: Builder, val maxAmount: Int = 100): Structure(gsBlueprint, builder) {

    /**
     * 该建筑是否拥有能源(被其它能源建筑充能)
     */
    fun hasEnergySource(): Boolean {
        return GameStructureCounter.energyStructures.any { it.available && builder.buildLocation.distance(it.builder.buildLocation) <= 40 }
    }

    /**
     * 修理当前建筑 20% 最大耐久
     * @param player    尝试进行修理的玩家
     * @return          修理的结果
     */
    fun repair(player: Player): Boolean {
        // 条件不通过
        if(gsBlueprint.repairConditions.map { it.isSatisfy(player) }.any { !it }) {
            player.playSound(player, Sound.ENTITY_VILLAGER_NO,1f,1f)
            player.sendMessage("$MESSAGE_PREFIX <yellow>不满足建筑修复条件，无法进行修复。".toComponent())
            return false
        }
        // 需求收取
        gsBlueprint.repairConditions.filterIsInstance<Requirement>().forEach { it.delivery(player) }
        player.playSound(player,Sound.ENTITY_VILLAGER_YES,1f,1f)
        player.sendMessage("$MESSAGE_PREFIX <green>建筑修复成功。".toComponent())
        doRepair(blueprint.maxDurability * 0.2)
        return true
    }

    /**
     * 摧毁当前建筑
     * @param player    尝试进行摧毁的玩家
     * @return          摧毁的结果
     */
    fun destroy(player: Player): Boolean {
        Bukkit.broadcast("$MESSAGE_PREFIX ${player.name} 摧毁了一个 ${blueprint.structureName} 建筑。".toComponent())
        super.destroy()
        return true
    }
}