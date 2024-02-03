package org.wolflink.minecraft.plugin.eclipticengineering.structure.generator

import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.resource.OreResourceBlock
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureAvailableEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureCompletedEvent
import org.wolflink.minecraft.plugin.eclipticstructure.event.structure.StructureDurabilityDamageEvent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.deepEquals
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Blueprint
import org.wolflink.minecraft.plugin.eclipticstructure.structure.IStructureListener
import org.wolflink.minecraft.plugin.eclipticstructure.structure.Structure
import org.wolflink.minecraft.plugin.eclipticstructure.structure.builder.Builder
import java.util.*

class GeneratorOre(builder: Builder) : Structure(blueprint,builder),IStructureListener {
    companion object {
        val random = Random()
        val blueprint = Blueprint(
            "§a采矿场",
            "${this::class.java.enclosingClass.simpleName}.schem",
            5,
            1000,
            ItemStack(Material.COBBLESTONE,128),
            ItemStack(Material.IRON_INGOT,16),
            ItemStack(Material.GOLD_INGOT,8)
            )
    }
    override val customListener = this
    // 矿物资源方块
    private val oreResources = mutableSetOf<OreResourceBlock>()
    init {
        addOreResource(-1.0,1.0,0.0)
        addOreResource(1.0,0.0,-1.0)
        addOreResource(-2.0,2.0,0.0)
        addOreResource(-1.0,3.0,-1.0)
    }
    private fun addOreResource(relativeX: Double,relativeY: Double,relativeZ: Double) {
        val loc = builder.buildLocation.clone().add(relativeX,relativeY,relativeZ)
        oreResources.add(OreResourceBlock(this,loc))
    }
    override fun onAvailable(e: StructureAvailableEvent) {
    }
    private fun getOreResourceBlock(player: Player):OreResourceBlock? {
        val targetBlock = player.getTargetBlockExact(4) ?: return null
        val targetLocationTriple = Triple(targetBlock.location.blockX,targetBlock.location.blockY,targetBlock.location.blockZ)
        return oreResources.firstOrNull {
            Triple(it.location.blockX,it.location.blockY,it.location.blockZ).deepEquals(targetLocationTriple)
        }
    }
    override fun onDurabilityDamage(e: StructureDurabilityDamageEvent) {
        if(e.damageSourceType == DamageSource.PLAYER_BREAK) {
            val player = e.damageSource as? Player ?: return
            val resource = getOreResourceBlock(player) ?: return
            // 取消事件，防止玩家不小心损坏结构
            e.isCancelled = true
            // 采集资源
            resource.breaking(player)
        }
    }

    override fun completed(e: StructureCompletedEvent) {
        oreResources.forEach {
            EEngineeringScope.launch { it.reset()  }
        }
    }
}