package com.zkxltech.sdk

import com.hoho.android.usbserial.util.HexDump
import com.zkxltech.sdk.entity.*

/**
 * Create by Panda on 2019-06-10
 */
object DecodeUtil {

    fun decode(data: ByteArray) {
        val phb = data[0]
        val pcb1 = data[1]
        val pcb2 = data[2]
        val pcb3 = data[3]
        val lengthBytes = ByteArray(2)
        System.arraycopy(data, 4, lengthBytes, 0, 2)
        val length: Short = HexDump.toHexString(lengthBytes).toShort(16)//ifs
        val cmd = data[6]
        val bytes = ByteArray(length - 1)
        System.arraycopy(data, 7, bytes, 0, length - 1)
        val chk = data[6 + length]
        val peb = data[7 + length]
        when (cmd) {
            //绑定结果
            0x0D.toByte() -> {
                val entity = BandRepEntity()
                entity.read(bytes)
                CommunicateHelper.mListener?.bandRep(entity)
            }
            //反馈手动绑定
            0x36.toByte() -> {
                val entity = DeviceBandRepEntity()
                entity.read(bytes)
                CommunicateHelper.mListener?.handBandRep(entity)
            }
            //uid号
            0x35.toByte() -> {
                val entity = ReadCardRepEntity()
                entity.read(bytes)
                CommunicateHelper.mListener?.readCardRep(entity)
            }
            //获取设备信息
            0x01.toByte() -> {
                val entity = DeviceInfoRepEntity()
                entity.read(bytes)
                CommunicateHelper.mListener?.deviceInfoRep(entity)
            }
            //清空白名单
            0x04.toByte() -> {
                val entity = ModifyWhiteRepEntity()
                entity.read(bytes)
                CommunicateHelper.mListener?.modifyWhiteRep(entity)
            }
            //发送在线回答
            0x05.toByte() -> {
                val entity = OnlineAnswerRepEntity()
                entity.read(bytes)
                CommunicateHelper.mListener?.sendOnlineAnswerRep(entity)
            }
            //收到答案
            0x31.toByte() -> {
                val entity = AnswerRepEntity()
                entity.read(bytes)
                CommunicateHelper.mListener?.answerRep(entity)
            }
            //收到停止作答
            0x06.toByte() -> {
                val entity = StopAnsweringRepEntity()
                entity.read(bytes)
                CommunicateHelper.mListener?.stopAnsweringRep(entity)
            }
        }
    }
}