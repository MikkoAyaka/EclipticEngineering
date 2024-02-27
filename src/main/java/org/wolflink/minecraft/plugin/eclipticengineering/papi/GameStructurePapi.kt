package org.wolflink.minecraft.plugin.eclipticengineering.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.ConditionBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.blueprint.GameStructureBlueprint
import org.wolflink.minecraft.plugin.eclipticengineering.dictionary.StructureType
import org.wolflink.minecraft.plugin.eclipticengineering.utils.ReflectionAPI

/**
 * %eegs_xxxxx_x_buildConditions% 类型名称为 xxxxx 等级为 x 的建筑的建造条件(如果有下划线，用连字符代替-)
 * %eegs_xxxxx_tags% 建筑类型为 xxxxx 的标签
 * %eegs_xxxxx_x_tags% 建筑类型为 xxxxx 的标签
 * %eegs_xxxxx_x_blueprint_xxx% 获取 xxxxx 建筑 x 等级的蓝图中的 xxx 属性
 */
object GameStructurePapi: PlaceholderExpansion()  {
    override fun getIdentifier() = "eegs"

    override fun getAuthor() = "MikkoAyaka"

    override fun getVersion() = "1.0.0"

    private val regexBuildConditions = "[A-Za-z-]+_\\d+_buildConditions".toRegex()
    private val regexRepairConditions = "[A-Za-z-]+_\\d+_repairConditions".toRegex()
    private val regexTags1 = "[A-Za-z-]+_tags".toRegex()
    private val regexTags2 = "[A-Za-z-]+_\\d+_tags".toRegex()
    private val regexBlueprint = "[A-Za-z-]+_\\d+_blueprint_[A-Za-z]+".toRegex()
    override fun onRequest(player: OfflinePlayer?, params: String): String {
        if(player == null || player.player == null) return "未找到玩家"
        val args = params.split('_')
        try {
            when {
                params.matches(regexBuildConditions) -> {
                    val structureType = StructureType.valueOf(args[0].uppercase().replace('-','_'))
                    val level = args[1].toInt()
                    val blueprint = structureType.blueprints[level-1] as ConditionBlueprint
                    if(blueprint.buildConditions.isEmpty()) return "    &#E8E8E8无"
                    return blueprint.buildConditions.joinToString(separator = "\n") {
                        if(it.isSatisfy(player.player)) "    &#33FF33☑"+it.description
                        else "    &#FF3333☒"+it.description
                    }
                }
                params.matches(regexRepairConditions) -> {
                    val structureType = StructureType.valueOf(args[0].uppercase().replace('-','_'))
                    val level = args[1].toInt()
                    val blueprint = structureType.blueprints[level-1] as ConditionBlueprint
                    if(blueprint.repairConditions.isEmpty()) return "    &#E8E8E8无"
                    return blueprint.repairConditions.joinToString(separator = "\n") {
                        if(it.isSatisfy(player.player)) "    &#33FF33☑"+it.description
                        else "    &#FF3333☒"+it.description
                    }
                }
                params.matches(regexTags1) || params.matches(regexTags2) -> {
                    val structureType = StructureType.valueOf(args[0].uppercase().replace('-','_'))
                    val tags = structureType.blueprints
                        .filterIsInstance<GameStructureBlueprint>()
                        .firstOrNull()?.tags ?: return "未找到 $structureType 相关蓝图数据"
                    if(tags.isEmpty()) return "    &#E8E8E8无"
                    return tags.joinToString(separator = "\n") { "    &#E8E8E8"+it.displayName }
                }
                params.matches(regexBlueprint) -> {
                    val structureType = StructureType.valueOf(args[0].uppercase().replace('-','_'))
                    val level = args[1].toInt()
                    val blueprintAttribute = args[3]
                    val blueprint = structureType.blueprints[level-1]
                    return ReflectionAPI.getFieldValue(blueprint,blueprintAttribute).toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "转换变量异常"
        }
        return "未知变量"
    }
}