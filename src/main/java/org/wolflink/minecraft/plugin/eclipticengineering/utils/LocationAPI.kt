package org.wolflink.minecraft.plugin.eclipticengineering.utils

import org.bukkit.Location
import org.wolflink.common.ioc.Singleton
import kotlin.math.cos
import kotlin.math.sin

object LocationAPI {
    /**
     * 获取以给定坐标为中心，水平角度为 yaw，距离为 distance 的目标坐标
     */
    fun getLocationByAngle(center: Location, yaw: Double, distance: Double): Location {
        var angle = (yaw + 90) % 360
        if (angle < 0) {
            angle += 360.0
        }
        // 将角度转换为弧度
        val radian = Math.toRadians(angle)
        // 计算新坐标的X和Z值
        val deltaX = cos(radian) * distance
        val deltaZ = sin(radian) * distance

        // 获取中心坐标的世界、X、Y、Z值
        val world = center.getWorld()
        val centerX = center.x
        val centerY = center.y
        val centerZ = center.z

        // 计算新坐标的X、Y、Z值
        val newX = centerX + deltaX
        val newZ = centerZ + deltaZ

        // 创建新的Location对象
        return Location(world, newX, centerY, newZ)
    }

    /**
     * 在一定的Y轴误差内(建议至少>=3)，
     * 获取离给定坐标最近的坐标地面(至少3格高度的空间)，不一定是地表(X和Z不改变)
     * 如果没能找到则返回null
     */
    fun getNearestSurface(location: Location, deltaY: Int): Location? {
        val upBox = MonsterSpawnBox(location.clone().add(0.0, -1.0, 0.0))
        val downBox = MonsterSpawnBox(location.clone().add(0.0, -3.0, 0.0))
        for (i in 0 until deltaY) {
            if (upBox.isAvailable) return upBox.bottom.add(0.0, 1.0, 0.0)
            if (downBox.isAvailable) return downBox.bottom.add(0.0, 1.0, 0.0)
            upBox.up()
            downBox.down()
        }
        return null
    }

    /**
     * 在一定的Y轴误差内获取给定坐标最近的固体方块坐标(不包含自身)
     */
    fun getNearestSolid(location: Location, deltaY: Int): Location? {
        val center = location.block
        for (i in 1..deltaY) {
            if (center.getRelative(0, i, 0).type.isSolid()) return location.clone().add(0.0, i.toDouble(), 0.0)
            if (center.getRelative(0, -i, 0).type.isSolid()) return location.clone().add(0.0, -i.toDouble(), 0.0)
        }
        return null
    }
}
