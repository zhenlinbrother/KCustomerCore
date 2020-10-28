package com.linc.download.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import okhttp3.internal.and
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
/**
 * author       : linc
 * time         : 2020/10/27
 * desc         : 加密工具
 * version      : 1.0.0
 */
object EncryptionUtils {
    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private fun Hex(b: ByteArray?): String {
        val hs = StringBuilder()
        var stmp: String
        var n = 0
        while (b != null && n < b.size) {
            stmp = Integer.toHexString(b[n] and 0XFF)
            if (stmp.length == 1) {
                hs.append('0')
            }
            hs.append(stmp)
            n++
        }
        return hs.toString().toLowerCase().trim { it <= ' ' }
    }
    // -----------------------------------------SHA 加密 start---------------------------------------
    /**
     * SHA512_HMAC加密
     */
    @SuppressLint("NewApi")
    fun SHA512_HMAC(secret: String, message: String): ByteArray {
        return SHA_HMAC(secret, message, HMAC_SHA.HMAC_SHA_512)
    }

    /**
     * SHA256_HMAC加密
     */
    @SuppressLint("NewApi")
    fun SHA256_HMAC(secret: String, message: String): ByteArray {
        return SHA_HMAC(secret, message, HMAC_SHA.HMAC_SHA_256)
    }

    /**
     * SHA_HMAC加密
     *
     * @param secret  秘钥
     * @param message 消息
     * @return 加密后字符串
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun SHA_HMAC(
        secret: String,
        message: String,
        type: String
    ): ByteArray {
        val bytes: ByteArray
        bytes = try {
            val mac = Mac.getInstance(type)
            val secret_key =
                SecretKeySpec(secret.toByteArray(), type)
            mac.init(secret_key)
            mac.doFinal(message.toByteArray(StandardCharsets.UTF_8))
        } catch (e: Exception) {
            println("Error " + type + " ===========" + e.message)
            ByteArray(0)
        }
        return bytes
    }
    // -----------------------------------------SHA 加密 end----------------------------------------
    /**
     * base64 编码
     */
    private fun getBase64String(hash: ByteArray): String {
        return Base64.encodeToString(hash, Base64.DEFAULT)
            .trim { it <= ' ' }
    }

    /**
     * 使用标准URL Encode编码。注意和JDK默认的不同，空格被编码为%20而不是+。
     *
     * @param s String字符串
     * @return URL编码后的字符串
     */
    fun urlEncode(s: String?): String {
        return try {
            URLEncoder.encode(s, "UTF-8").replace("\\+".toRegex(), "%20")
        } catch (e: UnsupportedEncodingException) {
            throw IllegalArgumentException("UTF-8 encoding not supported!")
        }
    }

    // -----------------------------------------SHA 加密 start---------------------------------------
    fun sha1Encrypt(content: String): String? {
        return shaEncrypt(SHA.SHA_1, content)
    }

    fun sha256Encrypt(content: String): String? {
        return shaEncrypt(SHA.SHA_256, content)
    }

    fun sha512Encrypt(content: String): String? {
        return shaEncrypt(SHA.SHA_512, content)
    }

    fun sha384Encrypt(content: String): String? {
        return shaEncrypt(SHA.SHA_384, content)
    }

    /**
     * SHA加密
     *
     * @param type   加密类型 [EncryptionUtils.SHA]
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    fun shaEncrypt(type: String?, strSrc: String): String? {
        val md: MessageDigest
        val strDes: String
        val bt = strSrc.toByteArray()
        try {
            // 将此换成SHA-1、SHA-256、SHA-512、SHA-384等参数
            md = MessageDigest.getInstance(type)
            md.update(bt)
            strDes = Hex(md.digest()) // to HexString
        } catch (e: NoSuchAlgorithmException) {
            return null
        }
        return strDes
    }

    // -----------------------------------------SHA 加密 end-----------------------------------------
    // -----------------------------------------SHA 加密 start---------------------------------------
    fun md5(str: String): String? {
        return try {
            val md = MessageDigest.getInstance("MD5")
            md.update(str.toByteArray())
            BigInteger(1, md.digest()).toString(16)
        } catch (var2: Exception) {
            var2.printStackTrace()
            null
        }
    } // -----------------------------------------SHA 加密 end-----------------------------------------

    interface HMAC_SHA {
        companion object {
            const val HMAC_SHA_512 = "HMACSHA512"
            const val HMAC_SHA_256 = "HmacSHA256"
        }
    }

    /**
     * SHA 的类型
     */
    interface SHA {
        companion object {
            const val SHA_1 = "SHA-1"
            const val SHA_256 = "SHA-256"
            const val SHA_512 = "SHA-512"
            const val SHA_384 = "SHA-384"
        }
    }
}