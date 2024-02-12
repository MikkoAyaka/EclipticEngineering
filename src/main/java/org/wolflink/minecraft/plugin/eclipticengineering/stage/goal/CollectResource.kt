package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import kotlinx.coroutines.delay
import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.MiningStation
import org.wolflink.minecraft.plugin.eclipticengineering.structure.special.PipelineInterface
import org.wolflink.minecraft.plugin.eclipticstructure.repository.StructureRepository

object CollectResource : Goal(60) {
    override val nextGoal: Goal = WaveDefense
    override var intoStory: Story? = null
    override var leaveStory: Story? = null

    override suspend fun finishCheck() {
        whileTag@ while (status == Status.IN_PROGRESS) {
            // 开采站是否建立
            val result1 = StructureRepository.findBy { it is MiningStation && it.builder.buildLocation.distance(GoalHolder.specialLocation!!) < 50 }.any()
            // 开采站端口列表
            val miningStationPipelines = StructureRepository.findBy { it is PipelineInterface && it.builder.buildLocation.distance(GoalHolder.specialLocation!!) < 50}.map { it as PipelineInterface }
            // 开采站端口是否建立
            val result2 = miningStationPipelines.isNotEmpty()
            // 基地端口列表
            val footholdPipelines = StructureRepository.findBy { it is PipelineInterface && it.builder.buildLocation.distance(GoalHolder.footholdLocation!!) < 50}.map { it as PipelineInterface }
            // 基地端口是否建立
            val result3 = footholdPipelines.isNotEmpty()
            // TODO 缺少虚拟物品检查
            if (result1 && result2 && result3) {
                for (miningStationPipeline in miningStationPipelines) {
                    for (footholdPipeline in footholdPipelines) {
                        // 存在至少一个基地的端口与开采站端口有连接
                        if(miningStationPipeline.hasConnection(footholdPipeline)){
                            finish()
                            break@whileTag
                        }
                    }
                }
            }
            delay(3000)
        }
    }

    override suspend fun failedCheck() {
    }
    override fun giveRewards() {
    }
}