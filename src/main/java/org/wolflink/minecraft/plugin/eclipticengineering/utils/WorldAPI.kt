package org.wolflink.minecraft.plugin.eclipticengineering.utils

import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.wolflink.minecraft.plugin.eclipticengineering.eeLogger
import java.io.File

object WorldAPI {
    fun delete(worldName: String) {
        Bukkit.unloadWorld(worldName,false)
        File(Bukkit.getPluginsFolder().parent,worldName).apply {
            if(!exists()) eeLogger.info("$name 删除失败：未找到相关世界文件夹 $path")
            else deleteWorldFiles(this)
        }
    }
    private fun deleteWorldFiles(path: File) {
        if (path.exists()) {
            val files: Array<out File>? = path.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isDirectory()) {
                        deleteWorldFiles(file)
                    } else {
                        file.delete()
                    }
                }
            }
            path.delete()
        }

    }
    fun create(worldName: String) {
        Bukkit.createWorld(WorldCreator(worldName))
        eeLogger.info("$worldName 创建完成")
    }
    fun regen(worldName: String) {
        delete(worldName)
        create(worldName)
        eeLogger.info("$worldName 已完成刷新")
    }
}