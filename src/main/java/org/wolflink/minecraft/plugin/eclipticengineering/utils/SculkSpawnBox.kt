package org.wolflink.minecraft.plugin.eclipticengineering.utils

import org.bukkit.Location
import org.bukkit.Material

/**
 * 幽匿结构生成盒模型
 * 中心是催发体，边上4个是幽匿块，Y轴等高
 */
class SculkSpawnBox(center: Location) {
    /**
     * 0 中
     * 1 Z+1
     * 2 Z-1
     * 3 X-1
     * 4 X+1
     */
    private var locations = arrayOfNulls<Location>(5)

    init {
        locations[0] = center.clone()
        locations[1] = center.clone().add(0.0, 0.0, 1.0)
        locations[2] = center.clone().add(0.0, 0.0, -1.0)
        locations[3] = center.clone().add(-1.0, 0.0, 0.0)
        locations[4] = center.clone().add(1.0, 0.0, 0.0)
    }

    fun spawn() {
        locations[0]!!.block.type = Material.SCULK_CATALYST
        locations[1]!!.block.type = Material.SCULK
        locations[2]!!.block.type = Material.SCULK
        locations[3]!!.block.type = Material.SCULK
        locations[4]!!.block.type = Material.SCULK
    }

    /**
     * 向下移动一格
     */
    fun down() {
        for (i in 0..4) locations[i]!!.add(0.0, -1.0, 0.0)
    }

    val isAvailable: Boolean
        /**
         * 只生成在固体中
         */
        get() {
            for (i in 0..5) {
                for (location in locations) {
                    if (!location!!.block.type.isSolid()) {
                        return if (i < 5) {
                            down()
                            break
                        } else false
                    }
                }
            }
            return true
        }
}
