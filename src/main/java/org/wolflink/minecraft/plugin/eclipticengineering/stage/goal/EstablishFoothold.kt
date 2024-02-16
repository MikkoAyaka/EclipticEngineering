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
        "在这个被幽匿感染侵蚀的世界里",
        "你们，无畏的开拓者",
        "被选中重建家园的重任",
        "带着希望和决心",
        "踏上了地面之旅",
        "地面充满了重重险境",
        "但你们的目标只有一个",
        "找到一片净土",
        "重建我们的家园",
    )
    override var leaveStory: Story? = Story(
        "随着你的决心和勇气",
        "新的旅程正式开始",
        "在这片荒凉之地上",
        "寻找生存和重建的希望",
        "就是你现在的使命",
    )
    override suspend fun finishCheck() {
        while (status == Status.IN_PROGRESS) {
            val result = StructureRepository.findBy { it is EnergySource }.any()
            if (result) {
                val energySource = StructureRepository.findBy { it is EnergySource }.first()
                // 更新据点坐标
                GoalHolder.footholdLocation = energySource.builder.buildLocation
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