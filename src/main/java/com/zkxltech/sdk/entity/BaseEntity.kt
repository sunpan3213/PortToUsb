package com.zkxltech.sdk.entity

import com.hoho.android.usbserial.util.HexDump
import com.zkxltech.sdk.XorUtil

/**
 * Create by Panda on 2019-05-16
 */
open class BaseEntity {

    val phb: Byte = 0xBD.toByte()//固定
    val pcb1 = 0x05.toByte()//固定
    val pcb2 = 0x01.toByte()//接收器
    val pcb3 = 0x01.toByte()//1.3.3版本
    var ifs = arrayListOf<Byte>()//inf的长度->2byte
    var inf = arrayListOf<Byte>()//指令内容
    var chk = 0x00.toByte()//上面所有字节异或校验
    val peb = 0xBE.toByte()//固定

    open fun write(): ArrayList<Byte> {
        val bytes = arrayListOf<Byte>()
        bytes.add(phb)
        bytes.add(pcb1)
        bytes.add(pcb2)
        bytes.add(pcb3)
        ifs.addAll(HexDump.toByteArray(inf.size.toShort()).toList())
        bytes.addAll(ifs)
        bytes.addAll(inf)
        chk = XorUtil.xor(getBytes2Xor().toByteArray())
        bytes.add(chk)
        bytes.add(peb)
        return bytes
    }

    protected fun getBytes2Xor(): ArrayList<Byte> {
        val toXor = arrayListOf<Byte>()
        toXor.add(phb)
        toXor.add(pcb1)
        toXor.add(pcb2)
        toXor.add(pcb3)
        toXor.addAll(ifs)
        toXor.addAll(inf)
        return toXor
    }

}