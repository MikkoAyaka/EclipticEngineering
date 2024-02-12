package org.wolflink.minecraft.plugin.eclipticengineering.stage.goal

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.wolflink.minecraft.plugin.eclipticengineering.EEngineeringScope
import org.wolflink.minecraft.plugin.eclipticengineering.stage.story.Story

abstract class Goal(private val prepareTimeSeconds: Int) {
    // 下一个目标
    abstract val nextGoal: Goal
    protected abstract var intoStory: Story?
    protected abstract var leaveStory: Story?

    // 任务完成状态
    protected var status = Status.NOT_STARTED
        private set(value) {
            if (value == field) return
            field = value
            if (field == Status.FINISHED || field == Status.FAILED) {
                giveRewards()
                EEngineeringScope.launch { next() }
            }
        }

    enum class Status {
        NOT_STARTED,
        PREPARING,
        IN_PROGRESS,
        FINISHED,
        FAILED
    }

    /**
     * 进入当前任务
     */
    fun into() {
        status = Status.PREPARING
        EEngineeringScope.launch {
            delay(prepareTimeSeconds * 1000L)
            status = Status.IN_PROGRESS
            intoStory?.broadcast()
            startCheck()
        }
        afterInto()
    }
    open fun afterInto(){}

    private suspend fun startCheck() {
        EEngineeringScope.launch { finishCheck() }
        EEngineeringScope.launch { failedCheck() }
        EEngineeringScope.launch { timerTask() }
    }

    /**
     * 进入下一个任务
     */
    private suspend fun next() {
        leaveStory?.broadcast()
        GoalHolder.nowGoal = nextGoal
        nextGoal.into()
    }

    fun finish() {
        status = Status.FINISHED
    }

    fun failed() {
        status = Status.FAILED
    }

    open suspend fun timerTask(){}
    abstract suspend fun finishCheck()
    abstract suspend fun failedCheck()

    /**
     * 根据完成状态发放奖励
     */
    abstract fun giveRewards()
}