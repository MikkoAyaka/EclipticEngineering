package org.wolflink.minecraft.plugin.eclipticengineering.ability

import org.bukkit.Color
import java.util.UUID

enum class Ability(val displayName: String,val color: Color) {
    SMELTING("煅烧",Color.fromRGB(255,112,71)),
    MINING("挖掘",Color.fromRGB(255,215,0)),
    FARMING("耕种",Color.fromRGB(205,133,63)),
    LOGGING("砍伐",Color.fromRGB(34,139,34)),
    BREEDING("养殖",Color.fromRGB(238,210,238)),
    BUILDING("建造",Color.fromRGB(0,191,255)),
    ENGINEERING("器械",Color.fromRGB(110,123,139)),
    COMBAT("战斗",Color.fromRGB(255,48,48)),
    ARCANE("奥术",Color.fromRGB(191,62,255))
}
