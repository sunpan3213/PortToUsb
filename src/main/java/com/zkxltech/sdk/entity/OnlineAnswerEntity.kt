package com.zkxltech.sdk.entity

import android.util.Log
import com.hoho.android.usbserial.util.HexDump
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Create by Panda on 2019-05-30
 */
class OnlineAnswerEntity : BaseEntity() {

    var question_tag: Int = 0//0x00or0x06普通作答(前者不可举手)   0x01or0x07抢答(前者不可举手)
    val list = arrayListOf<Question>()

    override fun write(): ArrayList<Byte> {
        inf.add(0x05)
        val format = SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:SSS")
        val time = format.format(Date())
        val split = time.split(":")
        Log.e("panda",time)
        for (str in split) {
            if (split.indexOf(str) == 0 || split.indexOf(str) == (split.size - 1)) {
                val bytes = HexDump.toByteArray(str.toShort())
                inf.add(bytes[0])
                inf.add(bytes[1])
            } else {
                inf.add(str.toInt().toByte())
            }
        }
        when (question_tag) {
            0 -> {
                inf.add(0x00)
            }
            1 -> {
                inf.add(0x06)
            }
            2 -> {
                inf.add(0x01)
            }
            3 -> {
                inf.add(0x07)
            }
        }
        for (item in list) {
            when (item.type) {
                "单选题" -> {//单选题
                    inf.add(0x00.toByte())
                    inf.add(item.id.toByte())
                    inf.add(0x0F)
                }
                "多选题" -> {//多选题
                    inf.add(0x01.toByte())
                    inf.add(item.id.toByte())
                    inf.add(0x3F)
                }
                "判断题" -> {//判断题
                    inf.add(0x02.toByte())
                    inf.add(item.id.toByte())
                    inf.add(0x03)
                }
                "数字题" -> {//数字题
                    val short = item.range.toShort()
                    if (short > 255) {
                        inf.add(0x43)
                        inf.add(item.id.toByte())
                        val bytes = HexDump.toByteArray(short)
                        inf.add(bytes[0])
                        inf.add(bytes[1])
                    } else {
                        inf.add(0x03.toByte())
                        inf.add(item.id.toByte())
                        inf.add(short.toByte())
                    }
                }
                "通用题" -> {//通用题
                    inf.add(0x04.toByte())
                    inf.add(item.id.toByte())
                    inf.add(0xFF.toByte())
                }
                "小数题" -> {//小数题
                    inf.add(0x85.toByte())
                    inf.add(item.id.toByte())
                    val strs = item.range.toString().split(".")
                    Log.e("panda","整数->"+strs[0])
                    Log.e("panda","小数->"+strs[1])
                    if (strs.size <= 1 || strs[0].toInt() > 9999 || strs[1].toInt() > 99) {
                        //小数不符合规定
                        return arrayListOf()
                    }
                    val bigSB = StringBuilder()
                    val smallSB = StringBuilder()
                    val big = HexDump.toByteArray(strs[0].toInt(), false)
                    val small = HexDump.toByteArray(strs[1].toInt(), false)
                    val bigStr = bigSB.append(HexDump.byteToBit(big[2])).append(HexDump.byteToBit(big[3])).toString()
                    val smallStr =
                        smallSB.append(HexDump.byteToBit(small[2])).append(HexDump.byteToBit(small[3])).toString()
                    val plus = bigStr.substring(2, bigStr.length).plus(smallStr.substring(6, smallStr.length))
                    Log.e("panda","总->"+plus)
                    for (i in 0 until plus.length / 8) {
                        Log.e("panda","byte->"+HexDump.toHexString(HexDump.bitToByte(plus.substring(i * 8, (i + 1) * 8))))
                        inf.add(HexDump.bitToByte(plus.substring(i * 8, (i + 1) * 8)))
                    }
                }
                "投票题" -> {//投票题
                    inf.add(0x06.toByte())
                    inf.add(item.id.toByte())
                    inf.add(0xAA.toByte())
                }
                "评分题" -> {//评分题
                    inf.add(0x07.toByte())
                    inf.add(item.id.toByte())
                    inf.add(item.range.toByte())
                }
            }
        }
        return super.write()
    }
}