package org.wolflink.minecraft.plugin.eclipticengineering.structure.api

import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import java.util.concurrent.atomic.AtomicInteger

/**
 * 游戏建筑结构统计者
 */
object GameStructureCounter {
    // 计数器，统计每种建筑结构的数量
    private val counter: Map<Class<out GameStructure>,AtomicInteger> = StructureType.entries.map { it.clazz }.associateWith { AtomicInteger(0) }
    // 能源建筑结构
    val energyStructures = mutableSetOf<GameStructure>()
    fun getAtomicCount(clazz: Class<out GameStructure>) = counter[clazz] ?: throw IllegalArgumentException("未注册到计数器的建筑结构类：${clazz::class.java.name}")
}