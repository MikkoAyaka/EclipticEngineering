package org.wolflink.minecraft.plugin.eclipticengineering.stage

import kotlinx.coroutines.launch
import net.citizensnpcs.api.event.NPCLeftClickEvent
import net.citizensnpcs.api.event.NPCRightClickEvent
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Container
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.EclipticEngineering
import org.wolflink.minecraft.plugin.eclipticengineering.config.Config
import org.wolflink.minecraft.plugin.eclipticengineering.config.MESSAGE_PREFIX
import org.wolflink.minecraft.plugin.eclipticengineering.extension.onlinePlayers
import org.wolflink.minecraft.plugin.eclipticengineering.interaction.Counter
import org.wolflink.minecraft.plugin.eclipticengineering.utils.RandomAPI
import org.wolflink.minecraft.plugin.eclipticstructure.extension.register
import org.wolflink.minecraft.plugin.eclipticstructure.extension.toComponent
import org.wolflink.minecraft.plugin.eclipticstructure.extension.unregister
import org.wolflink.minecraft.wolfird.framework.gamestage.stage.Stage

class PreGameStage(stageHolder: StageHolder) : Stage("搜集阶段", stageHolder) {
    private fun removeWalls() {
        Config.lobbyWalls.forEach {
            it.forEach { world, x, y, z ->
                world.setBlockData(x,y,z,Material.AIR.createBlockData())
            }
        }
    }
    private fun placeWalls() {
        Config.lobbyWalls.forEach {
            it.forEach { world, x, y, z ->
                world.setBlockData(x,y,z,Material.DEEPSLATE_BRICK_WALL.createBlockData())
            }
        }
    }
    override fun onEnter() {
        removeWalls()
        PreGameListener.register(EclipticEngineering.instance)
        onlinePlayers.forEach {
            it.teleport(Config.lobbyLocation)
            it.sendActionBar("$MESSAGE_PREFIX 在倒计时结束前，尽量多搜集些物资吧，接下来就要正式出发了。".toComponent())
            it.playSound(it, Sound.ENTITY_VILLAGER_AMBIENT, 1f, 1f)
            it.addPotionEffect(PotionEffect(PotionEffectType.SPEED,20 * 60,1,false,false))
        }
        EEngineeringScope.launch {
            if(Config.debugMode) Counter.count(5)
            else Counter.count(60)
            EclipticEngineering.runTask { stageHolder.next() }
        }
    }

    override fun onLeave() {
        placeWalls()
        PreGameListener.clearChest()
        PreGameListener.unregister()
    }
}
// 玩家平均开箱数量，用于计算概率
private const val averChestAmount = 20
// 随机概率表 数量(在每次游戏中期望出现的次数) 物品(堆叠)
private val chestItemRandomTable: Set<Pair<Double,ItemStack>> = setOf(
    10.0 to ItemStack(Material.COBWEB),
    10.0 to ItemStack(Material.ROTTEN_FLESH),
    10.0 to ItemStack(Material.SPIDER_EYE),
    3.0 to ItemStack(Material.DRIED_KELP,2),
    3.0 to ItemStack(Material.SWEET_BERRIES,2),
    3.0 to ItemStack(Material.APPLE,2),
    0.05 to ItemStack(Material.DIAMOND_SWORD, 1),
    0.1 to ItemStack(Material.IRON_SWORD, 1),
    0.35 to ItemStack(Material.STONE_SWORD, 1),
    0.1 to ItemStack(Material.WOODEN_SWORD,1),

    0.6 to ItemStack(Material.LEATHER_HELMET, 1),
    0.6 to ItemStack(Material.LEATHER_CHESTPLATE, 1),
    0.6 to ItemStack(Material.LEATHER_LEGGINGS, 1),
    0.6 to ItemStack(Material.LEATHER_BOOTS, 1),

    0.4 to ItemStack(Material.CHAINMAIL_HELMET, 1),
    0.4 to ItemStack(Material.CHAINMAIL_CHESTPLATE, 1),
    0.4 to ItemStack(Material.CHAINMAIL_LEGGINGS, 1),
    0.4 to ItemStack(Material.CHAINMAIL_BOOTS, 1),

    0.2 to ItemStack(Material.IRON_HELMET, 1),
    0.2 to ItemStack(Material.IRON_CHESTPLATE, 1),
    0.2 to ItemStack(Material.IRON_LEGGINGS, 1),
    0.2 to ItemStack(Material.IRON_BOOTS, 1),

    0.03 to ItemStack(Material.DIAMOND_HELMET, 1),
    0.03 to ItemStack(Material.DIAMOND_CHESTPLATE, 1),
    0.03 to ItemStack(Material.DIAMOND_LEGGINGS, 1),
    0.03 to ItemStack(Material.DIAMOND_BOOTS, 1),

    3.0 to ItemStack(Material.BREAD, 3),
    0.5 to ItemStack(Material.COOKED_PORKCHOP, 2),
    0.5 to ItemStack(Material.COOKED_BEEF, 2),
    0.5 to ItemStack(Material.POTATO, 4),
    0.5 to ItemStack(Material.CARROT, 4),
    0.5 to ItemStack(Material.GOLDEN_CARROT, 1),
    0.1 to ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1),
    10.0 to ItemStack(Material.OAK_LOG, 5),
    10.0 to ItemStack(Material.COBBLESTONE, 5),
    6.0 to ItemStack(Material.IRON_INGOT, 2),
    0.8 to ItemStack(Material.GOLD_INGOT, 2),
    0.3 to ItemStack(Material.DIAMOND, 1),
    2.0 to ItemStack(Material.TORCH, 16),
    0.5 to ItemStack(Material.WATER_BUCKET, 1),
    0.4 to ItemStack(Material.LAVA_BUCKET, 1),
    0.1 to ItemStack(Material.WOODEN_PICKAXE, 1),
    0.4 to ItemStack(Material.STONE_PICKAXE, 1),
    0.2 to ItemStack(Material.IRON_PICKAXE, 1),
    0.4 to ItemStack(Material.GOLDEN_PICKAXE, 1),
    0.05 to ItemStack(Material.DIAMOND_PICKAXE, 1),
    0.1 to ItemStack(Material.WOODEN_AXE, 1),
    0.4 to ItemStack(Material.STONE_AXE, 1),
    0.2 to ItemStack(Material.IRON_AXE, 1),
    0.4 to ItemStack(Material.GOLDEN_AXE, 1),
    0.05 to ItemStack(Material.DIAMOND_AXE, 1),
    0.1 to ItemStack(Material.WOODEN_SHOVEL, 1),
    0.4 to ItemStack(Material.STONE_SHOVEL, 1),
    0.2 to ItemStack(Material.IRON_SHOVEL, 1),
    0.4 to ItemStack(Material.GOLDEN_SHOVEL, 1),
    0.05 to ItemStack(Material.DIAMOND_SHOVEL, 1)
)
private object PreGameListener : Listener {
    private val cacheChest = mutableSetOf<Container>()
    @EventHandler
    fun on(e: PlayerInteractEvent) {
        if (e.hasBlock() && e.clickedBlock?.state is Container) {
            val chestBlock = e.clickedBlock!!.state as Container
            if(chestBlock in cacheChest) return
            chestBlock.inventory.clear()
            cacheChest.add(chestBlock)
            EEngineeringScope.launch {
                try {
                    chestBlock.inventory.contents = chestBlock.inventory
                        .apply {
                            chestItemRandomTable
                                .filter { RandomAPI.nextDouble() <= it.first / averChestAmount }
                                .forEach { this.addItem(it.second) }
                        }
                        .contents.apply { this.shuffle() }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    fun clearChest() {
        cacheChest.forEach {
            it.inventory.clear()
        }
        cacheChest.clear()
    }
    @EventHandler
    fun on(e: NPCLeftClickEvent) {
        e.isCancelled = true
    }
    @EventHandler
    fun on(e: NPCRightClickEvent) {
        e.isCancelled = true
    }
}