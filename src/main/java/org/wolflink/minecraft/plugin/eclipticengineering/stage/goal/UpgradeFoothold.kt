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
        "尽管我们已经初步建立了据点",
        "但仍需要扩大并巩固这里",
        "怪物们正在黑暗之中伺机而动",
        "抓紧时间吧！",
    )
    override var leaveStory: Story? = Story(
        "这个据点看上去已经能够正常运作了",
        "稍作整顿，接下来将有困难的任务等待着你们",
    )
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