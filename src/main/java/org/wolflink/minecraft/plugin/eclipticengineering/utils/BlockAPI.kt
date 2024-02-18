package org.wolflink.minecraft.plugin.eclipticengineering.utils

import org.bukkit.Location
import org.bukkit.Material
import org.wolflink.minecraft.plugin.eclipticengineering.eeLogger

object BlockAPI {
    private const val OCEAN_RADIUS = 5
    private const val MAX_WATER_LEVEL_Y = 96

    /**
     * 处在水中的方块材质
     */
    private val waterMaterials = mutableSetOf(
        Material.WATER,
        Material.SEAGRASS,
        Material.TALL_SEAGRASS,
        Material.KELP,
        Material.BRAIN_CORAL_BLOCK,
        Material.BUBBLE_CORAL_BLOCK,
        Material.FIRE_CORAL_BLOCK,
        Material.TUBE_CORAL_BLOCK,
        Material.HORN_CORAL_BLOCK
    )
    /**
     * 半径为radius的方形区域内搜索方块
     */
    fun searchBlock(material: Material, center: Location, radius: Int): List<Location> {
        val world = center.getWorld()
        val result: MutableList<Location> = ArrayList()
        if (world == null) return result
        val centerX = center.blockX
        val centerY = center.blockY
        val centerZ = center.blockZ
        for (y in -radius..radius) {
            for (x in -radius..radius) {
                for (z in -radius..radius) {
                    if (world.getBlockAt(centerX + x, centerY + y, centerZ + z).type == material) {
                        result.add(center.clone().add(x.toDouble(), y.toDouble(), z.toDouble()))
                    }
                }
            }
        }
        return result
    }

    /**
     * 检查给定坐标是否处于海洋中
     */
    fun checkIsOcean(center: Location): Boolean {
        if (center.block.type != Material.WATER) return false
        val world = center.getWorld()
        val centerX = center.blockX
        val centerY = getWaterLevelY(center)
        if (centerY == -1) {
            eeLogger.warning("未找到坐标所处的水平面：" + center.blockX + "|" + center.blockY + "|" + center.blockZ)
            return false
        }
        val centerZ = center.blockZ
        for (x in -OCEAN_RADIUS..OCEAN_RADIUS) {
            for (z in -OCEAN_RADIUS..OCEAN_RADIUS) {
                val tempLoc = Location(world, (centerX + x).toDouble(), centerY.toDouble(), (centerZ + z).toDouble())
                if (tempLoc.block.type != Material.WATER) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 获取给定坐标当前所处水域的水平面
     * -1 则为没有找到
     */
    fun getWaterLevelY(waterLocation: Location): Int {
        var block = waterLocation.block
        while (block.y < MAX_WATER_LEVEL_Y) {
            val upBlock = block.getRelative(0, 1, 0)
            block = if (!waterMaterials.contains(upBlock.type)) return block.y else upBlock
        }
        return -1
    }
}
