package org.wolflink.minecraft.plugin.eclipticengineering

import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorCrop
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorLog
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorOre
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.EnergySource
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.HotSpring
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.RespawnBeacon
import org.wolflink.minecraft.plugin.eclipticengineering.structure.totem.TotemExperience
import org.wolflink.minecraft.plugin.eclipticengineering.structure.totem.TotemSpeed
import org.wolflink.minecraft.plugin.eclipticengineering.structure.totem.TotemStrength
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.TowerArrow
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.TowerLightning
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.TowerLookout
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.TowerPotion
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureBlueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureBuilder
import org.wolflink.minecraft.plugin.eclipticstructure.structure.StructureRegistry

enum class StructureType(private val blueprint: StructureBlueprint, private val supplier: (StructureBuilder)->Structure) {

    GENERATOR_ORE(GeneratorOre.blueprint,{ GeneratorOre(it) }),// 采矿场
    GENERATOR_LOG(GeneratorLog.blueprint,{ GeneratorLog(it) }),// 伐木场
    GENERATOR_CROP(GeneratorCrop.blueprint,{ GeneratorCrop(it) }),// 耕种场

    RESPAWN_BEACON(RespawnBeacon.blueprint,{ RespawnBeacon(it) }),// 重生信标
    ENERGY_SOURCE(EnergySource.blueprint,{ EnergySource(it) }),// 能源中心
    HOT_SPRING(HotSpring.blueprint,{ HotSpring(it)}),// 温泉

    TOTEM_EXPERIENCE(TotemExperience.blueprint,{TotemExperience(it)}),// 经验图腾
    TOTEM_SPEED(TotemSpeed.blueprint,{TotemSpeed(it)}),// 速度图腾
    TOTEM_STRENGTH(TotemStrength.blueprint,{TotemStrength(it)}),// 力量图腾

    TOWER_ARROW(TowerArrow.blueprint,{TowerArrow(it)}),// 弓箭塔
    TOWER_LOOKOUT(TowerLookout.blueprint,{TowerLookout(it)}),// 瞭望塔
    TOWER_LIGHTNING(TowerLightning.blueprint,{TowerLightning(it)}),// 引雷塔
    TOWER_POTION(TowerPotion.blueprint,{TowerPotion(it)}),// 药水塔
    ;
    fun register() {
        StructureRegistry.register(EclipticEngineering.instance,name,blueprint,supplier)
    }
    fun unregister() {
        StructureRegistry.unregister(name)
    }
}