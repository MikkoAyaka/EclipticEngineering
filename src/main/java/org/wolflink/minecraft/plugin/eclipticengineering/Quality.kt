package org.wolflink.minecraft.plugin.eclipticengineering

import org.bukkit.Color

enum class Quality(val displayName: String,val color: Color) {
    RARE("稀有",Color.fromRGB(84,255,159)),
    EXQUISITE("卓越",Color.fromRGB(0,191,255)),
    EPIC("史诗",Color.fromRGB(132,112,255)),
    LEGENDARY("传说",Color.fromRGB(255,127,80)),
    RELIC("远古",Color.fromRGB(255,48,48)),
    UNIQUE("唯一",Color.fromRGB(255,245,0))
}