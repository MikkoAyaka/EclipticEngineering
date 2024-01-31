package org.wolflink.minecraft.plugin.eclipticengineering

import org.wolflink.minecraft.plugin.eclipticengineering.structure.GeneratorOre
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureBlueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureBuilder
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureRegistry

enum class StructureType(val blueprint: StructureBlueprint,val supplier: (StructureBuilder)->Structure) {

    GENERATOR_ORE(GeneratorOre.blueprint,{GeneratorOre(it)}),// 采矿场
//    GENERATOR_LOG,// 伐木场
//    GENERATOR_CROP,// 耕种场
//
//    RESPAWN_BEACON,// 重生信标
//    ENERGY_SOURCE,// 能源中心
//    HOT_SPRING,// 温泉
//
//    TOTEM_EXPERIENCE,// 经验图腾
//    TOTEM_SPEED,// 速度图腾
//    TOTEM_STRENGTH,// 力量图腾
//
//    TOWER_ARROW,// 弓箭塔
//    TOWER_LOOKOUT,// 瞭望塔
//    TOWER_LIGHTNING,// 引雷塔
//    TOWER_POTION,// 药水塔
    ;
    fun register() {
        StructureRegistry.register(EclipticEngineering.instance,name,blueprint,supplier)
    }
    fun unregister() {
        StructureRegistry.unregister(name)
    }
}