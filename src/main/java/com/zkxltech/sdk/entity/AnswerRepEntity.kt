package com.zkxltech.sdk.entity

import com.hoho.android.usbserial.util.HexDump

/**
 * Create by Panda on 2019-05-31
 */
class AnswerRepEntity : BaseEntity() {

    var power = 0
    var time = ""
    var uid = 0
    var list = arrayListOf<Answer>()

    fun read(data: ByteArray) {
        power = data[0].toInt()
        time = "".plus(HexDump.toHexString(data[1]).toInt(16) * 100 + HexDump.toHexString(data[2]).toInt(16)).plus("年")
            .plus(HexDump.toHexString(data[3]).toInt(16)).plus("月")
            .plus(HexDump.toHexString(data[4]).toInt(16)).plus("日")
            .plus(HexDump.toHexString(data[5]).toInt(16)).plus("时")
            .plus(HexDump.toHexString(data[6]).toInt(16)).plus("分")
            .plus(HexDump.toHexString(data[7]).toInt(16)).plus("秒")
            .plus(HexDump.toHexString(data[8]).toShort(16) * 100 + HexDump.toHexString(data[9]).toShort(16)).plus("毫秒")
        val bytes = ByteArray(4)
        System.arraycopy(data, 10, bytes, 0, 4)
        uid = HexDump.bytesToInt(bytes, true)
        var remain = data.size - 14
        var i = 13
        while (remain >= 3) {
            val answer = Answer("", 0, "")
            i++
            when (data[i]) {
                0x00.toByte() -> {//单选题
                    answer.type = "单选题"
                    i++
                    answer.id = data[i].toInt()
                    i++
                    when (data[i]) {
                        0x01.toByte() -> answer.value = "A"
                        0x02.toByte() -> answer.value = "B"
                        0x04.toByte() -> answer.value = "C"
                        0x08.toByte() -> answer.value = "D"
                        0x80.toByte() -> answer.value = "质疑"
                    }
                    remain -= 3
                }
                0x01.toByte() -> {//多选题
                    answer.type = "多选题"
                    i++
                    answer.id = data[i].toInt()
                    i++
                    when (data[i]) {
                        0x01.toByte() -> answer.value = "A"
                        0x02.toByte() -> answer.value = "B"
                        0x04.toByte() -> answer.value = "C"
                        0x08.toByte() -> answer.value = "D"
                        0x10.toByte() -> answer.value = "E"
                        0x20.toByte() -> answer.value = "F"
                        0x03.toByte() -> answer.value = "AB"
                        0x05.toByte() -> answer.value = "AC"
                        0x06.toByte() -> answer.value = "BC"
                        0x07.toByte() -> answer.value = "ABC"
                        0x09.toByte() -> answer.value = "AD"
                        0x0A.toByte() -> answer.value = "BD"
                        0x0B.toByte() -> answer.value = "ABD"
                        0x0C.toByte() -> answer.value = "CD"
                        0x0D.toByte() -> answer.value = "ACD"
                        0x0E.toByte() -> answer.value = "BCD"
                        0x0F.toByte() -> answer.value = "ABCD"
                        0x11.toByte() -> answer.value = "AE"
                        0x12.toByte() -> answer.value = "BE"
                        0x13.toByte() -> answer.value = "ABE"
                        0x14.toByte() -> answer.value = "CE"
                        0x15.toByte() -> answer.value = "ACE"
                        0x16.toByte() -> answer.value = "BCE"
                        0x17.toByte() -> answer.value = "ABCE"
                        0x18.toByte() -> answer.value = "DE"
                        0x19.toByte() -> answer.value = "ADE"
                        0x1A.toByte() -> answer.value = "BDE"
                        0x1B.toByte() -> answer.value = "ABDE"
                        0x1C.toByte() -> answer.value = "CDE"
                        0x1D.toByte() -> answer.value = "ACDE"
                        0x1E.toByte() -> answer.value = "BCDE"
                        0x1F.toByte() -> answer.value = "ABCDE"
                        0x21.toByte() -> answer.value = "AF"
                        0x22.toByte() -> answer.value = "BF"
                        0x23.toByte() -> answer.value = "ABF"
                        0x24.toByte() -> answer.value = "CF"
                        0x25.toByte() -> answer.value = "ACF"
                        0x26.toByte() -> answer.value = "BCF"
                        0x27.toByte() -> answer.value = "ABCF"
                        0x28.toByte() -> answer.value = "DF"
                        0x29.toByte() -> answer.value = "ADF"
                        0x2A.toByte() -> answer.value = "BDF"
                        0x2B.toByte() -> answer.value = "ABDF"
                        0x2C.toByte() -> answer.value = "CDF"
                        0x2D.toByte() -> answer.value = "ACDF"
                        0x2E.toByte() -> answer.value = "BCDF"
                        0x2F.toByte() -> answer.value = "ABCDF"
                        0x30.toByte() -> answer.value = "EF"
                        0x31.toByte() -> answer.value = "AEF"
                        0x32.toByte() -> answer.value = "BEF"
                        0x33.toByte() -> answer.value = "ABEF"
                        0x34.toByte() -> answer.value = "CEF"
                        0x35.toByte() -> answer.value = "ACEF"
                        0x36.toByte() -> answer.value = "BCEF"
                        0x37.toByte() -> answer.value = "ABCEF"
                        0x38.toByte() -> answer.value = "DEF"
                        0x39.toByte() -> answer.value = "ADEF"
                        0x3A.toByte() -> answer.value = "BDEF"
                        0x3B.toByte() -> answer.value = "ABDEF"
                        0x3C.toByte() -> answer.value = "CDEF"
                        0x3D.toByte() -> answer.value = "ACDEF"
                        0x3E.toByte() -> answer.value = "BCDEF"
                        0x3F.toByte() -> answer.value = "ABCDEF"
                        0x80.toByte() -> answer.value = "质疑"
                    }
                    remain -= 3
                }
                0x02.toByte() -> {//判断题
                    answer.type = "判断题"
                    i++
                    answer.id = data[i].toInt()
                    i++
                    when (data[i]) {
                        0x01.toByte() -> answer.value = "对"
                        0x02.toByte() -> answer.value = "错"
                    }
                    remain -= 3
                }
                0x03.toByte() -> {//数字题 range = 1byte
                    answer.type = "数字题"
                    i++
                    answer.id = data[i].toInt()
                    i++
                    if (data[i].toInt() != -1) {
                        answer.value = data[i].toInt().toString()
                    }
                    remain -= 3
                }
                0x43.toByte() -> {//数字题 range = 2byte
                    answer.type = "数字题"
                    i++
                    answer.id = data[i].toInt()
                    if (data[i + 2] != 0xFF.toByte() || data[i + 1] != 0xFF.toByte()) {
                        val array = byteArrayOf(data[i + 2], data[i + 1])
                        answer.value = HexDump.toHexString(array).toShort(16).toString()
                    }
                    i++
                    i++
                    remain -= 4
                }
                0x04.toByte() -> {//通用题 回答的什么就会变成相应的题型
                    answer.type = "通用题"
                    i++
                    answer.id = data[i].toInt()
                    i++
                    //todo
                    answer.value = data[i].toString()
                    remain -= 3
                }
                0x85.toByte() -> {//小数题
                    answer.type = "小数题"
                    i++
                    answer.id = data[i].toInt()
                    //todo
                    i++
                    i++
                    i++
                    answer.value
                    remain -= 5
                }
                0x06.toByte() -> {//投票题
                    answer.type = "投票题"
                    i++
                    answer.id = data[i].toInt()
                    i++
                    when (data[i]) {
                        0x01.toByte() -> {
                            answer.value = "赞成"
                        }
                        0x02.toByte() -> {
                            answer.value = "反对"
                        }
                        0x03.toByte() -> {
                            answer.value = "弃权"
                        }
                    }
                    remain -= 3
                }
                0x07.toByte() -> {//评分题
                    answer.type = "评分题"
                    i++
                    answer.id = data[i].toInt()
                    i++
                    if (data[i].toInt() != -1) {
                        answer.value = data[i].toInt().toString()
                    }
                    remain -= 3
                }
            }
            list.add(answer)
        }

    }
}