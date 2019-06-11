package com.zkxltech.sdk.entity

import com.hoho.android.usbserial.util.HexDump

/**
 * Create by Panda on 2019-05-29
 */
class DeviceInfoRepEntity : BaseEntity() {

    var code: Byte = 0x00
    var deviceId: Int = 0
    var softVersion: String = ""//3bytes
    var hardVersion: String = ""
    var companyName: String = ""
    var sendChannel: Byte = 0x00
    var receiveChannel: Byte = 0x00
    var power: Byte = 0x00
    var protocol: Byte = 0x00
    val whiteList = arrayListOf<Int>()

    fun read(data: ByteArray) {
//        00 7B 14 B8 B6 02 00 05 0B 5A 4C 2D 52 50 35 35
//        32 43 2D 46 08 7A 6B 78 6C 74 65 63 68 03 35 03
//        00
        code = data[0]//00
        val bytes = ByteArray(4)
        System.arraycopy(data, 1, bytes, 0, 4)
        deviceId = HexDump.bytesToInt(bytes, false)//7B 14 B8 B6
        softVersion = HexDump.toHexString(data[5]).plus(".").plus(HexDump.toHexString(data[6])).plus(".")
            .plus(HexDump.toHexString(data[7]))//02 00 05
        val hardArray = ByteArray(data[8].toInt())//0B
        System.arraycopy(data, 8 + 1, hardArray, 0, hardArray.size)
        hardVersion = getNameString(hardArray)//5A 4C 2D 52 50 35 35 32 43 2D 46
        val companyArray = ByteArray(data[9 + hardArray.size].toInt())//08
        System.arraycopy(data, 9 + 1 + hardArray.size, companyArray, 0, companyArray.size)
        companyName = getNameString(companyArray)//7A 6B 78 6C 74 65 63 68
        sendChannel = data[8 + 1 + hardArray.size + 1 + companyArray.size]//03
        receiveChannel = data[9 + 1 + hardArray.size + 1 + companyArray.size]//35
        power = data[10 + 1 + hardArray.size + 1 + companyArray.size]//03
        protocol = data[11 + 1 + hardArray.size + 1 + companyArray.size]//00
        val pre = 11 + 1 + hardArray.size + 1 + companyArray.size + 1
        if (protocol == 0x00.toByte()) {//0X00
            if (((data.size - pre) >= 0) && (data.size - pre) % 5 == 0) {
                val num = (data.size - pre) / 5
                val idBytes = ByteArray(4)
                for (i in 0 until num) {
                    System.arraycopy(data, pre + 1 + i * 5, idBytes, 0, 4)
                    whiteList.add(HexDump.bytesToInt(idBytes, true))
                }
            } else {
                throw Exception("whiteList's bytes num is wrong")
            }
        } else if (protocol == 0x80.toByte()) {//0X80
            if (((data.size - pre) >= 0) && (data.size - pre) % 6 == 0) {
                val num = (data.size - pre) / 6
                val idBytes = ByteArray(4)
                for (i in 0 until num) {
                    System.arraycopy(data, pre + 2 + i * 6, idBytes, 0, 4)
                    whiteList.add(HexDump.bytesToInt(idBytes, true))
                }
            } else {
                throw Exception("whiteList's bytes num is wrong")
            }
        } else {//0x40

        }
    }

    private fun getNameString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in 0 until bytes.size) {
            if (bytes[i] > ' '.toByte() && bytes[i] < '~'.toByte()) {
                sb.append(String(bytes, i, 1))
            } else {
                sb.append(" ")
            }
        }
        return sb.toString()
    }

}