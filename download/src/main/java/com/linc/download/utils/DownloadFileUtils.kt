package com.linc.download.utils

import android.os.Environment
import com.linc.download.config.DownloadConfig
import java.io.File

/**
 * author       : linc
 * time         : 2020/10/27
 * desc         : 下载工具
 * version      : 1.0.0
 */
object DownloadFileUtils {

    /**
     * 获取文件夹
     * @return File
     */
    fun getFolder() : File {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            return createSaveFolder(Environment.getExternalStorageDirectory().absolutePath)
        } else {
            return createSaveFolder(Environment.getDataDirectory().absolutePath)
        }
    }

    private fun createSaveFolder(url: String) : File {
        return FileUtils.createFolder(
            File(url),
            DownloadConfig.instance?.downloadFile
        )!!
    }

    /**
     * 获取是否存在
     * @param fileName String
     * @return Boolean
     */
    fun isExist(fileName: String) : Boolean {
        val file = getFile(fileName)
        return FileUtils.isFileExist(file!!)
    }

    /**
     * 获取是否存在
     * @param file File
     * @return Boolean
     */
    fun isExist(file: File) = FileUtils.isFileExist(file)

    /**
     * 创建文件
     * @param fileName String
     * @return File?
     */
    fun createFile(fileName: String) : File? {
        val file = getFile(fileName)
        return if (FileUtils.createFile(file!!)){
            file
        } else {
            null
        }
    }

    /**
     * 删除文件
     * @param fileName String
     * @return Boolean
     */
    fun deleteFile(fileName: String) : Boolean {
        val file = getFile(fileName)
        return FileUtils.deleteFile(file)
    }

    /**
     * 删除文件
     * @param file File
     * @return Boolean
     */
    fun deleteFile(file: File) = FileUtils.deleteFile(file)

    /**
     * 获取文件
     * @param fileName String
     * @return File?
     */
    fun getFile(fileName: String) : File? {
        return File(getFolder(), fileName)
    }

    /**
     * 计算进度
     * @param length Long
     * @param totalSize Long
     * @return Int
     */
    fun calculatePercent(length: Long, totalSize: Long)
            = if (totalSize <= 0){ 0 } else { (length * 100 / totalSize).toInt() }
}