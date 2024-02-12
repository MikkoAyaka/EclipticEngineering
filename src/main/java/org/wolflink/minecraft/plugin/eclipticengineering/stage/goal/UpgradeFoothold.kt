package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import kotlinx.coroutines.delay
import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorCrop
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorLog
import org.wolflink.minecraft.plugin.eclipticengineering.structure.generator.GeneratorOre
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository

object UpgradeFoothold : Goal(0) {
    override val nextGoal = LocationSurvey
    override var intoStory: Story? = Story(
        "<green>孩子们，这里还是剧情",
        "<green>请尽快扩大你们的据点，",
        "<green>建造伐木场、采矿场、农场。",
    )
    override var leaveStory: Story? = null
    override suspend fun finishCheck() {
        while (status == Status.IN_PROGRESS) {
            val result1 = StructureRepository.findBy { it is GeneratorLog }.any()
            val result2 = StructureRepository.findBy { it is GeneratorCrop }.any()
            val result3 = StructureRepository.findBy { it is GeneratorOre }.any()
            if (result1 && result2 && result3) {
                finish()
                break
            }
            delay(5000)
        }
    }

    override suspend fun failedCheck() {

    }

    /**
     * 根据完成状态发放奖励
     */
    override fun giveRewards() {
        // TODO 苏哒哟
    }
}