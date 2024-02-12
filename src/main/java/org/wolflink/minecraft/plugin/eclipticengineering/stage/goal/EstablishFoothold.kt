package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import kotlinx.coroutines.delay
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.wolflink.minecraft.plugin.eclipticengineering.extension.gamingPlayers
import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.EnergySource
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository

object EstablishFoothold : Goal(0) {
    override val nextGoal: Goal = UpgradeFoothold
    override var intoStory: Story? = Story(
        "<green>孩子们，这里是剧情",
        "<green>时间紧任务重，你们的目标是",
        "<green>寻找一处合适的地点作为据点，",
        "<green>尽快建造能源中心。",
    )
    override var leaveStory: Story? = null
    override suspend fun finishCheck() {
        while (status == Status.IN_PROGRESS) {
            val result = StructureRepository.findBy { it is EnergySource }.any()
            if (result) {
                finish()
                break
            }
            delay(3000)
        }
    }

    override suspend fun failedCheck() {
        // Nothing
    }

    /**
     * 根据完成状态发放奖励
     */
    override fun giveRewards() {
        gamingPlayers.forEach {
            // TODO 发放奖励
            it.giveExp(114)
            it.inventory.addItem(ItemStack(Material.DIRT, 51))
        }
    }
}