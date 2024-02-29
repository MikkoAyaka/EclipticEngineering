package org.wolflink.minecraft.plugin.eclipticengineering.dictionary

import org.wolflink.minecraft.plugin.eclipticengineering.resource.item.*

object ResourceMapper {
    fun mapToSpecialResource(name: String?): SpecialItem? {
        return when(name) {
            "AncientWood" -> AncientWood
            "BuildMenuItem" -> BuildMenuItem
            "DisguiserBook" -> DisguiserBook
            "EvergreenWood" -> EvergreenWood
            "MainMenuItem" -> MainMenuItem
            "PioneerBook" -> PioneerBook
            "SpecialDiamond" -> SpecialDiamond
            "SpecialGold" -> SpecialGold
            "SpecialIron" -> SpecialIron
            "TaskBook" -> TaskBook
            else -> null
        }
    }
}