package com.linc.download.utils

import android.graphics.Bitmap
import com.linc.download.utils.CloseUtils.close
import java.io.*
import java.util.*
/**
 * author       : linc
 * time         : 2020/10/27
 * desc         : 文件操作
 * version      : 1.0.0
 */
object FileUtils {
    private const val BUFFER_LENGTH = 2048
    private val RANDOM = Random()
    private const val BOUND = 10000
    private const val SEPARATION_CHAR = '-'
    private const val DOT_CHAR = '.'

    /**
     * 文件是否存在
     *
     * @param file 文件
     * @return true：存在；false：不存在
     */
    fun isFileExist(file: File): Boolean {
        return file.exists()
    }
    // ----------------------------------- 文件夹创建 start------------------------------------------
    /**
     * 创建文件夹
     *
     * @param parentFolder 父类文件夹
     * @param folderName   子文件夹
     * @return 创建的文件夹，如果失败则返回null
     */
    fun createFolder(parentFolder: File?, folderName: String?): File? {
        val folder = File(parentFolder, folderName)
        val isSuccess = createFolder(folder)
        return if (isSuccess) {
            folder
        } else {
            null
        }
    }

    /**
     * 创建文件夹
     *
     * @param folderPath 文件夹路径
     * @return 创建的文件夹，如果失败则返回null
     */
    fun createFolder(folderPath: String?): File? {
        val folder = File(folderPath)
        val isSuccess = createFolder(folder)
        return if (isSuccess) {
            folder
        } else {
            null
        }
    }

    /**
     * 创建文件夹
     *
     * @param folder 文件夹路径
     * @return true：创建成功；false：创建失败
     */
    fun createFolder(folder: File): Boolean {
        return if (!folder.exists()) {
            folder.mkdirs()
        } else true
    }
    // ----------------------------------- 文件夹创建 end--------------------------------------------
    // ------------------------------------ 文件创建 start-------------------------------------------
    /**
     * 在指定文件夹下创建一个文件
     *
     * @param folder 文件夹
     * @param prefix 文件前缀，可为null，则不增加前缀
     * @param suffix 文件后缀，可为null，则不增加后缀
     * @return 成功创建则返回file，否则返回null
     */
    fun createFileViaAuto(
        folder: File?,
        prefix: String?,
        suffix: String?
    ): File? {
        val random = RANDOM.nextInt(BOUND)
        val currentTimeMillis = System.currentTimeMillis()

        // 添加前缀
        val fileName = StringBuilder()
        if (prefix != null && prefix.length > 0) {
            fileName.append(prefix)
                .append("-")
        }

        // [0-9999]-[13时间戳]
        fileName.append(random)
            .append(SEPARATION_CHAR)
            .append(currentTimeMillis)

        // 添加后缀
        if (suffix != null && suffix.length > 0) {
            fileName.append(DOT_CHAR)
                .append(suffix)
        }
        val file = File(folder, fileName.toString())
        val isSuc = createFile(file)
        return if (isSuc) file else null
    }

    /**
     * 创建文件
     *
     * @param filePath 文件路径
     * @return 创建的文件，失败则返回null
     */
    fun createFile(filePath: String?): File? {
        val file = File(filePath)
        val isSuc = createFile(file)
        return if (isSuc) file else null
    }

    /**
     * 创建文件
     *
     * @param file 文件
     * @return 创建的文件，失败则返回false
     */
    fun createFile(file: File): Boolean {
        val parentFile = file.parentFile
        if (!parentFile.exists()) {
            val isSuccess = createFolder(parentFile)
            // 创建失败
            if (!isSuccess) {
                return false
            }
        }
        if (!file.exists()) {
            var isMakeSuccess = false
            try {
                isMakeSuccess = file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return isMakeSuccess
        }
        return true
    }

    /**
     * 创建文件
     *
     * @param folder   文件夹
     * @param fileName 文件名
     * @return 创建的文件，失败则返回null
     */
    fun createFile(folder: File, fileName: String?): File? {
        if (!folder.exists()) {
            val isSuccess = createFolder(folder)
            // 创建失败
            if (!isSuccess) {
                return null
            }
        }
        val file = File(folder, fileName)
        if (!file.exists()) {
            var isMakeSuccess = false
            try {
                isMakeSuccess = file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (!isMakeSuccess) {
                return null
            }
        }
        return file
    }
    // ------------------------------------ 文件创建 start-------------------------------------------
    /**
     * 将 流 保存至 文件
     *
     * @param file        保存的文件
     * @param inputStream 流
     * @return 保存成功返回true，否则返回false
     */
    fun saveStreamToFile(file: File?, inputStream: InputStream): Boolean {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            val myByte = ByteArray(BUFFER_LENGTH)
            while (true) {
                val len = inputStream.read(myByte)
                if (len == -1) {
                    fileOutputStream.flush()
                    return true
                }
                fileOutputStream.write(myByte, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            close(inputStream)
            close(fileOutputStream)
        }
    }

    /**
     * 将 byte 保存至文件
     *
     * @param file  存储的文件
     * @param bytes 需要存储的byte数据
     * @return 保存成功则返回true，否则返回false
     */
    fun saveByteToFile(file: File?, bytes: ByteArray?): Boolean {
        var fileOutputStream: FileOutputStream? = null
        return try {
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(bytes)
            fileOutputStream.flush()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            close(fileOutputStream)
        }
    }

    /**
     * 删除文件
     *
     * @param file 需要删除的文件
     * @return 删除成功返回true，否则返回false
     */
    fun deleteFile(file: File?): Boolean {
        return if (file == null) {
            true
        } else {
            if (file.exists()) file.delete() else true
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath 需要删除的文件夹路径
     * @return 删除成功返回true，否则false
     */
    fun deleteFolder(folderPath: String?): Boolean {
        val dir = File(folderPath)
        return deleteFolder(dir)
    }

    /**
     * 删除文件夹
     *
     * @param folder 需要删除的文件夹
     * @return 删除成功返回true，否则false
     */
    fun deleteFolder(folder: File?): Boolean {
        var delete = true

        // 不为空 且 存在 且为文件夹
        if (folder != null && folder.exists() && folder.isDirectory) {
            val listFiles = folder.listFiles()
            val fileLength = listFiles.size

            // 循环删除文件
            for (fileIndex in 0 until fileLength) {
                val file = listFiles[fileIndex]
                if (file.isFile) {
                    if (!file.delete()) {
                        delete = false
                    }
                } else if (file.isDirectory) {
                    deleteFolder(file)
                }
            }
            if (!folder.delete()) {
                delete = false
            }
            return delete
        }
        return true
    }

    fun renameFileAtTheSameFolder(oldFile: File, newName: String?): Boolean {
        return oldFile.renameTo(File(oldFile.parent, newName))
    }

    fun renameFileAtTheSameFolder(
        filePath: String?,
        newName: String?
    ): Boolean {
        val oldFile = File(filePath)
        return oldFile.renameTo(File(oldFile.parent, newName))
    }

    fun renameFile(oldFile: File, newPath: String?): Boolean {
        return oldFile.renameTo(File(newPath))
    }

    fun renameFile(oldPath: String?, newPath: String?): Boolean {
        val oldFile = File(oldPath)
        return oldFile.renameTo(File(newPath))
    }

    /**
     * 将 String 写入文件
     *
     * @param file    存储文件
     * @param content 内容
     * @return 写入成功返回true，否则false
     */
    fun saveStringToFile(file: File?, content: String): Boolean {
        return saveByteToFile(file, content.toByteArray())
    }

    /**
     * 读取文件内容
     *
     * @param file 文件
     * @return 成功则返回内容，否则返回 ""
     */
    fun readFromFile(file: File?): String {
        var bufferedReader: BufferedReader? = null
        try {
            bufferedReader =
                BufferedReader(InputStreamReader(FileInputStream(file)))
            val stringBuilder = StringBuilder()
            var tempLine: String?
            while (bufferedReader.readLine().also { tempLine = it } != null) {
                stringBuilder.append(tempLine)
            }
            return stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close(bufferedReader)
        }
        return ""
    }

    /**
     * 转接流，将 输入流 ==转给==> 输出流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @param isCloseAuto  是否关闭流，会将 "输入流" 和 "输出流" 都关闭
     * @return 转送成功则返回true，否则返回false
     */
    fun transmitStream(
        inputStream: InputStream,
        outputStream: OutputStream,
        isCloseAuto: Boolean
    ): Boolean {
        try {
            val tempByte = ByteArray(2048)
            while (true) {
                val len = inputStream.read(tempByte)
                if (len == -1) {
                    outputStream.flush()
                    return true
                }
                outputStream.write(tempByte, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            if (isCloseAuto) {
                close(inputStream)
                close(outputStream)
            }
        }
    }

    /**
     * 转接流，将 输入流 ==转给==> 输出流
     *
     * @param inputStream   输入流
     * @param outputStream1 输出流
     * @param outputStream2 输出流
     * @param isCloseAuto   是否关闭流，会将 "输入流" 和 "输出流" 都关闭
     * @return 转送成功则返回true，否则返回false
     */
    fun transmitStream(
        inputStream: InputStream,
        outputStream1: OutputStream,
        outputStream2: OutputStream,
        isCloseAuto: Boolean
    ): Boolean {
        try {
            val tempByte = ByteArray(2048)
            while (true) {
                val len = inputStream.read(tempByte)
                if (len == -1) {
                    outputStream1.flush()
                    outputStream2.flush()
                    return true
                }
                outputStream1.write(tempByte, 0, len)
                outputStream2.write(tempByte, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            if (isCloseAuto) {
                close(inputStream)
                close(outputStream1)
                close(outputStream2)
            }
        }
    }

    /**
     * 获取扩展名
     *
     * @param fileName 文件名
     * @return 会返回最后 一个"." 之后的内容，否则返回 ""
     */
    fun getExtensionName(fileName: String?): String {
        if (fileName != null && fileName.length > 0) {

            // 获取 "." 的下标
            val dot = fileName.lastIndexOf(46.toChar())

            // 如果 "." 的下标在合法范围，则裁剪
            if (dot > -1 && dot < fileName.length - 1) {
                return fileName.substring(dot + 1)
            }
        }
        return ""
    }

    /**
     * 获取不带扩展名的 文件名
     *
     * @param fileName 文件名
     * @return 返回最后 一个"." 之前的内容，否则全部返回
     */
    fun getFileWithoutExtName(fileName: String?): String {
        // 文件名为 null 或者 为""
        return if (fileName == null || fileName == "") {
            ""
        } else {
            val dot = fileName.lastIndexOf(".")
            if (dot < 0) fileName else fileName.substring(0, dot)
        }
    }

    /**
     * 保存图片
     *
     * @param picFile 存储图片的文件
     * @param bitmap  需要保存的数据
     * @param quality 图像质量
     * @return 成功返回true，失败则返回false
     */
    fun saveBitmap(picFile: File?, bitmap: Bitmap, quality: Int): Boolean {
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(picFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos)
            fos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            close(fos)
        }
        return true
    }
}

