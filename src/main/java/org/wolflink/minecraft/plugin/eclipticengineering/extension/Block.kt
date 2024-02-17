package org.wolflink.minecraft.plugin.eclipticengineering.extension

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Ageable
import org.bukkit.block.data.BlockData
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering

/**
 * 简单判断两种方块是否为同一类型
 */
fun Block.simpleEquals(blockData: BlockData): Boolean {
    if(this.type == blockData.material) {
        val blockData1 = this.blockData
        val blockData2 = blockData
        // 作物方块
        if(blockData1 is Ageable && blockData2 is Ageable) {
            // 生长阶段是否一致
            return blockData1.age == blockData2.age
        }
        // 非作物
        if(blockData1 !is Ageable && blockData2 !is Ageable) return true
    }
    return false
}
fun Block.simpleSet(blockData: BlockData) {
    Bukkit.getScheduler().runTask(EclipticEngineering.instance, Runnable {
        this.blockData = blockData
    })
}

/**
 * 两个方块之间是否存在连接(被指定材质的方块连接)
 */
fun Block.hasConnection(another: Block,connectionMaterial: Material): Boolean {
    // 缓存方块数据(避免重复查找)
    val cacheBlocks = mutableSetOf<Block>()
    // 寻路最末端的方块
    var endBlocks = setOf(this)
    while (endBlocks.isNotEmpty()) {
        if(another in endBlocks) break
        val newEndBlocks = mutableSetOf<Block>()
        endBlocks.forEach { endBlock ->
            listOf(
                endBlock.getRelative(1,0,0),
                endBlock.getRelative(0,1,0),
                endBlock.getRelative(0,0,1),
                endBlock.getRelative(-1,0,0),
                endBlock.getRelative(0,-1,0),
                endBlock.getRelative(0,0,-1)
            ).forEach {
                if(it !in cacheBlocks && it.type == connectionMaterial) newEndBlocks.add(it)
            }
        }
        cacheBlocks.addAll(endBlocks)
        endBlocks = newEndBlocks
    }
    return endBlocks.isNotEmpty()
}