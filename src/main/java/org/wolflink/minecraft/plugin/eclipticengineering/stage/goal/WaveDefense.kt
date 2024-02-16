package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story

object WaveDefense : Goal(480) {
    override val nextGoal: Goal = LocationSurvey
    override var intoStory: Story? = Story(
        "怪物的脚步越来越近",
        "我们必须准备好防御",
        "它们要来了",
    )
    override var leaveStory: Story? = Story(
        "我们成功抵御了怪物的进攻",
        "它们暂时退去了"
    )

    override suspend fun finishCheck() {
    }

    override suspend fun failedCheck() {
    }

    override fun giveRewards() {
    }
}