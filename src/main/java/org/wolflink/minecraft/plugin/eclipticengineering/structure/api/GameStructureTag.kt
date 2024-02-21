package org.wolflink.minecraft.plugin.eclipticengineering.structure.api

/**
 * 星辉工程游戏建筑结构标识
 * 每个建筑结构可能有多个标识
 */
enum class GameStructureTag(val displayName: String) {
    ENERGY_REQUIRED("需要幽光能量"),
    ENERGY_SUPPLY("可提供能源"),

    COMPLICATED("构造复杂"), // TODO

    COMMON_RESOURCE_GENERATOR("可产出普通资源"),
    SPECIAL_RESOURCE_GENERATOR("可产出特殊资源"),

    AMOUNT_LIMITED("有限建造数量"),
}