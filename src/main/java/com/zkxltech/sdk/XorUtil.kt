package com.zkxltech.sdk

import kotlin.experimental.xor

/**
 * Create by Panda on 2019-05-16
 * 异或校验工具
 */
object XorUtil {

    fun xor(data: ByteArray):Byte {
        var temp: Byte = 0x00
        for (byte in data) {
            temp = temp.xor(byte)
        }
        return temp
    }

}