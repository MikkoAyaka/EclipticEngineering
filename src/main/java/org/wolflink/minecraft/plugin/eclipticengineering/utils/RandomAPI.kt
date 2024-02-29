package org.wolflink.minecraft.plugin.eclipticengineering.utils

import java.util.Random

val RandomAPI = Random()

/**
 * 给定类型和出现次数，生成随机分布列表
 */
fun <T> Map<T,Int>.toRandomDistribution() = this
    .flatMap { (key, value) -> List(value) { key } }
    .shuffled()