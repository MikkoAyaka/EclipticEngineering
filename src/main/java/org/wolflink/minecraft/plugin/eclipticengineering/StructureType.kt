package org.wolflink.minecraft.plugin.eclipticengineering

import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorCrop
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorLog
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorOre
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.EnergySource
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.RespawnBeacon
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.TowerArrow
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder
import org.wolflink.minecraft.plugin.eclipticstructure.structure.registry.StructureRegistry

enum class StructureType(
    private val blueprints: List<Blueprint>,
    private val supplier: (structureLevel: Int, builder: Builder) -> Structure
) {

    GENERATOR_ORE(GeneratorOre.blueprints, GeneratorOre::create),// 采矿场
    GENERATOR_LOG(GeneratorLog.blueprints, GeneratorLog::create),// 伐木场
    GENERATOR_CROP(GeneratorCrop.blueprints, GeneratorCrop::create),// 耕种场

    RESPAWN_BEACON(RespawnBeacon.blueprints,RespawnBeacon::create),// 重生信标
    ENERGY_SOURCE(EnergySource.blueprints,EnergySource::create),// 能源中心
//    HOT_SPRING(HotSpring.blueprint,{ HotSpring(it)}),// 温泉
//
//    TOTEM_EXPERIENCE(TotemExperience.blueprint,{TotemExperience(it)}),// 经验图腾
//    TOTEM_SPEED(TotemSpeed.blueprint,{TotemSpeed(it)}),// 速度图腾
//    TOTEM_STRENGTH(TotemStrength.blueprint,{TotemStrength(it)}),// 力量图腾
//
    TOWER_ARROW(TowerArrow.blueprints,TowerArrow::create),// 弓箭塔
//    TOWER_LOOKOUT(TowerLookout.blueprint,{TowerLookout(it)}),// 瞭望塔
//    TOWER_LIGHTNING(TowerLightning.blueprint,{TowerLightning(it)}),// 引雷塔
//    TOWER_POTION(TowerPotion.blueprint,{TowerPotion(it)}),// 药水塔
    ;

    fun register() {
        StructureRegistry.register(EclipticEngineering.instance, name, blueprints, supplier)
    }

    fun unregister() {
        StructureRegistry.unregister(name)
    }
}