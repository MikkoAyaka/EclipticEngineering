package org.wolflink.minecraft.plugin.eclipticengineering.config

object Config {
    val buildMenuCmd = "dm open 建造菜单 %player_name%"
    val mainMenuCmd = "dm open 据点看板 %player_name%"
    val lobbyWorldName = "spawn"
    val lobbyWalls = listOf(
        "33,100,-9 33,95,-14",
        "24,95,15 19,103,15",
        "-5,99,15 -13,95,15",
        "-27,103,29 -32,95,29",
        "-39,95,30 -47,103,30",
        "-52,95,29 -55,103,29"
    )
}