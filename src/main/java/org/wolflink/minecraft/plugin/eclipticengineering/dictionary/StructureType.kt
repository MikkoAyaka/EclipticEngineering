package org.wolflink.minecraft.plugin.eclipticengineering.dictionary

import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.structure.api.GameStructure
import org.wolflink.minecraft.plugin.eclipticengineering.structure.decoration.SpinTieMeme
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.Lighthouse
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorCrop
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorLog
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorOre
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.EnergySource
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.MiningStation
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.RespawnBeacon
import org.wolflink.minecraft.plugin.eclipticengineering.structure.survival.EnchantRoom
import org.wolflink.minecraft.plugin.eclipticengineering.structure.survival.ForgeRoom
import org.wolflink.minecraft.plugin.eclipticengineering.structure.survival.LargeCampfire
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.BlazeTurret
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.SnowGolemTurret
import org.wolflink.minecraft.plugin.eclipticengineering.structure.tower.TowerArrow
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.blueprint.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder
import org.wolflink.minecraft.plugin.eclipticstructure.structure.registry.StructureRegistry

enum class StructureType(
    val displayName: String,
    val clazz: Class<out GameStructure>,
    private val blueprints: List<Blueprint>,
    private val supplier: (structureLevel: Int, builder: Builder) -> Structure
) {

    GENERATOR_ORE("精炼矿场",GeneratorOre::class.java,GeneratorOre.blueprints, GeneratorOre::create),
    GENERATOR_LOG("精培木场",GeneratorLog::class.java,GeneratorLog.blueprints, GeneratorLog::create),
    GENERATOR_CROP("精培农场",GeneratorCrop::class.java,GeneratorCrop.blueprints, GeneratorCrop::create),

    LIGHT_HOUSE("幽光灯塔", Lighthouse::class.java, Lighthouse.blueprints, Lighthouse::create),// 灯塔
    RESPAWN_BEACON("幽光重生信标",RespawnBeacon::class.java,RespawnBeacon.blueprints,RespawnBeacon::create),// 重生信标
    ENERGY_SOURCE("幽光能量发生场",EnergySource::class.java,EnergySource.blueprints,EnergySource::create),// 能源中心
    MINING_STATION("勘探站台",MiningStation::class.java,MiningStation.blueprints,MiningStation::create),
//    HOT_SPRING(HotSpring.blueprint,{ HotSpring(it)}),// 温泉
//
//    TOTEM_EXPERIENCE(TotemExperience.blueprint,{TotemExperience(it)}),// 经验图腾
//    TOTEM_SPEED(TotemSpeed.blueprint,{TotemSpeed(it)}),// 速度图腾
//    TOTEM_STRENGTH(TotemStrength.blueprint,{TotemStrength(it)}),// 力量图腾
//
    TOWER_ARROW("弓箭高塔",TowerArrow::class.java,TowerArrow.blueprints,TowerArrow::create),// 弓箭塔
//    TOWER_LOOKOUT(TowerLookout.blueprint,{TowerLookout(it)}),// 瞭望塔
//    TOWER_LIGHTNING(TowerLightning.blueprint,{TowerLightning(it)}),// 引雷塔
//    TOWER_POTION(TowerPotion.blueprint,{TowerPotion(it)}),// 药水塔

    TURRET_BLAZE("烈焰防御塔台",BlazeTurret::class.java,BlazeTurret.blueprints,BlazeTurret::create),// 烈焰防御塔台
    TURRET_SNOW_GOLEM("雪人防御塔台",SnowGolemTurret::class.java,SnowGolemTurret.blueprints,SnowGolemTurret::create),// 雪人防御塔台

    ENCHANT_ROOM("附魔站台",EnchantRoom::class.java,EnchantRoom.blueprints,EnchantRoom::create),
    FORGE_ROOM("锻造站台",ForgeRoom::class.java,ForgeRoom.blueprints,ForgeRoom::create),
    LARGE_CAMPFIRE("大型营火",LargeCampfire::class.java,LargeCampfire.blueprints,LargeCampfire::create),

    SPIN_TIE_MEME("非常好建筑，使我的领带旋转",SpinTieMeme::class.java,SpinTieMeme.blueprints,SpinTieMeme::create)
    ;

    fun register() {
        StructureRegistry.register(EclipticEngineering.instance, name, blueprints, supplier)
    }

    fun unregister() {
        StructureRegistry.unregister(name)
    }
}