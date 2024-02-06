package org.wolflink.minecraft.plugin.eclipticengineering.resource

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTable
import org.wolflink.minecraft.plugin.eclipticengineering.extension.simpleEquals
import org.wolflink.minecraft.plugin.eclipticengineering.extension.simpleSet
import org.wolflink.minecraft.plugin.eclipticengineering.extension.toRoma
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toHex
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import java.util.Calendar
import java.util.Random

/**
 * 资源型方块
 * 允许符合条件的玩家进行采集
 * 采集后变更状态直至冷却时间结束
 */
abstract class ResourceBlock(
    private val structure: Structure,
    val location: Location,
    @get:Synchronized private val resourceCycle: ResourceCycle,
    private val cooldownSeconds: Int,
    private val breakAmount: Int,
    private val harvestSound: Sound,
    private val requiredAbilityType: Ability,
    private val requiredAbilityLevel: Int
) {
    companion object {
        private val random = Random()
    }
    private var collectProgress = 0.0 // 采集进度(0.0~1.0)
    private var lastBreakTime = Calendar.getInstance().timeInMillis
    // 上次采集该方块的时间(毫秒)
    private var lastAcquireTime = Calendar.getInstance().timeInMillis
    /**
     * 玩家是否满足条件采集该方块
     */
    private fun breakCheck(player: Player):Boolean {
        // 建筑结构正常工作
        val structureAvailable = structure.available
        if(!structureAvailable) {
            player.playSound(player,Sound.ENTITY_VILLAGER_NO,1f,1f)
            player.sendActionBar("$MESSAGE_PREFIX 建筑结构当前无法正常工作。".toComponent())
            return false
        }
        // 方块状态为最终态
        val blockIsFinal = location.block.simpleEquals(resourceCycle.finalBlockData)
        if(!blockIsFinal) {
            player.playSound(player,Sound.ENTITY_VILLAGER_NO,1f,1f)
            player.sendActionBar("$MESSAGE_PREFIX 资源产出中，请耐心等待。".toComponent())
            return false
        }
        // 玩家当前使用的工具能够采集当前方块
        val hasTool = location.block.isPreferredTool(player.inventory.itemInMainHand)
        if(!hasTool) {
            player.playSound(player,Sound.ENTITY_VILLAGER_NO,1f,1f)
            player.sendActionBar("$MESSAGE_PREFIX 需要使用正确的工具采集资源。".toComponent())
            return false
        }
        // 拥有对应的能力
        if(!player.abilityTable.checkAbilityWithNotice(requiredAbilityType,requiredAbilityLevel)) {
            return false
        }
        return true
    }
    /**
     * 玩家尝试破坏资源方块(最快每秒触发两次)
     */
    fun breaking(player: Player) {
        val now = Calendar.getInstance().timeInMillis
        if(now - lastBreakTime < 500) return
        lastBreakTime = now
        if(!breakCheck(player)) return
        collectProgress += 1.0 / breakAmount
        player.playSound(player, harvestSound,1f,0.5f)
        if(collectProgress >= 1.0) collect()
    }
    suspend fun reset() {
        // 重置进度
        collectProgress = 0.0
        // 刷新时间
        lastAcquireTime = Calendar.getInstance().timeInMillis
        // 重置周期
        resourceCycle.number = random.nextDouble()
        // 重置方块
        location.block.simpleSet(resourceCycle.initialBlockData)
        // 冷却时间结束后方块变更为最终状态
        var count = 0
        while (count < cooldownSeconds) {
            delay(1000)
            if(structure.available) count++
        }
        location.block.simpleSet(resourceCycle.finalBlockData)
    }
    /**
     * 资源方块被成功采集
     */
    private fun collect() {
        // 生成资源
        location.world.dropItemNaturally(location,resourceCycle.droppedItem)
        EEngineeringScope.launch { reset() }
    }
}