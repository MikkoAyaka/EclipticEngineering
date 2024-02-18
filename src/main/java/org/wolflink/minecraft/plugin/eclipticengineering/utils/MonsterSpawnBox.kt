package org.wolflink.minecraft.plugin.eclipticengineering.utils

import org.bukkit.Location
import org.bukkit.Material

/**
 * 怪物生成盒空间
 * 一共由垂直连续的4个坐标组成
 */
class MonsterSpawnBox(bottomLocation: Location) {
    private val box = arrayOfNulls<Location>(4)

    init {
        box[0] = bottomLocation.clone()
        box[1] = bottomLocation.clone().add(0.0, 1.0, 0.0)
        box[2] = bottomLocation.clone().add(0.0, 2.0, 0.0)
        box[3] = bottomLocation.clone().add(0.0, 3.0, 0.0)
    }

    /**
     * 向上移动一格
     */
    fun up() {
        for (i in 0..3) {
            box[i]!!.add(0.0, 1.0, 0.0)
        }
    }

    /**
     * 向下移动一格
     */
    fun down() {
        for (i in 0..3) {
            box[i]!!.add(0.0, -1.0, 0.0)
        }
    }

    val bottom: Location = box[0]!!
    val isAvailable: Boolean
        get() {
            val b0 = box[0]!!.block.type
            val b1 = box[1]!!.block.type
            val b2 = box[2]!!.block.type
            val b3 = box[3]!!.block.type
            return (box[0]!!.block.type.isSolid()
                    && availableTypes.contains(b1)
                    && availableTypes.contains(b2)
                    && availableTypes.contains(b3))
        }

    companion object {
        /**
         * 符合生成怪物的条件
         * 即：上三格不为方块，底部为方块
         */
        private val availableTypes: MutableSet<Material> = HashSet()

        init {
            availableTypes.add(Material.AIR)
            availableTypes.add(Material.CAVE_AIR)
            availableTypes.add(Material.SNOW)
            availableTypes.add(Material.SHORT_GRASS)
            availableTypes.add(Material.TALL_GRASS)
            availableTypes.add(Material.TALL_SEAGRASS)
            availableTypes.add(Material.SEAGRASS)
        }
    }
}
