package org.wolflink.minecraft.plugin.eclipticengineering.dictionary

import org.bukkit.Color

enum class EEDifficulty(val displayName: String, val color: Color) {
    EASY("轻松",Color.fromRGB(178,255,102)),
    NORMAL("普通",Color.fromRGB(102,255,255)),
    HARD("困难",Color.fromRGB(153,51,255)),
    HADES("地狱",Color.fromRGB(255,51,51)),
}