package org.wolflink.minecraft.plugin.eclipticengineering.extension

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Ageable

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
    this.blockData = blockData
}