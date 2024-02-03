package org.wolflink.minecraft.plugin.eclipticengineering.block

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.ability.Ability
import org.wolflink.minecraft.plugin.eclipticengineering.extension.abilityTree
import org.wolflink.minecraft.plugin.eclipticengineering.extension.simpleEquals
import org.wolflink.minecraft.plugin.eclipticengineering.extension.simpleSet
import org.wolflink.minecraft.plugin.eclipticstructure.coroutine.EStructureScope
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
    private val collectSeconds: Int,
    private val requiredAbilityType: Ability,
    private val requiredAbilityLevel: Int
) {
    companion object {
        private val random = Random()
    }
    init {
        resourceCycle.number = random.nextDouble()
    }
    private var collectProgress = 0.0 // 采集进度(0.0~1.0)
    private var lastBreakTime = Calendar.getInstance().timeInMillis
    // 上次采集该方块的时间(毫秒)
    private var lastAcquireTime = Calendar.getInstance().timeInMillis
    /**
     * 玩家是否满足条件采集该方块
     */
    private fun canBreak(player: Player) =
        // 建筑结构正常工作
        structure.available
                // 方块状态为最终态
                && location.block.simpleEquals(resourceCycle.finalBlockData)
                // 玩家当前使用的工具能够采集当前方块
                && location.block.isPreferredTool(player.inventory.itemInMainHand)
                // 拥有对应的能力
                && player.abilityTree.hasAbility(requiredAbilityType,requiredAbilityLevel)

    /**
     * 玩家尝试破坏资源方块(最快每秒触发一次)
     */
    fun breaking(player: Player) {
        val now = Calendar.getInstance().timeInMillis
        if(now - lastBreakTime < 1000) return
        lastBreakTime = now
        if(!canBreak(player)) return
        collectProgress += 1.0 / collectSeconds
        if(collectProgress >= 1.0) collect()
    }

    /**
     * 资源方块被成功采集
     */
    private fun collect() {
        // 重置进度
        collectProgress = 0.0
        // 刷新时间
        lastAcquireTime = Calendar.getInstance().timeInMillis
        // 生成资源
        location.world.dropItemNaturally(location,resourceCycle.droppedItem)
        // 重置周期
        resourceCycle.number = random.nextDouble()
        // 重置方块
        location.block.simpleSet(resourceCycle.initialBlockData)

        Bukkit.getScheduler().runTaskLater(EclipticEngineering.instance, Runnable {
            location.block.simpleSet(resourceCycle.finalBlockData)
        },20L * cooldownSeconds)

    }
}