package com.zkxltech.sdk

import android.util.Log
import com.hoho.android.usbserial.util.HexDump
import com.zkxltech.sdk.CommunicateHelper.list
import com.zkxltech.sdk.entity.*

/**
 * Create by Panda on 2019-05-16
 */
object WriteDataUtil {

    //手动绑定
    fun handBand() {
        val entity = BandEntity()
        val bytes = entity.write()
        CommunicateHelper.writeBytes(bytes.toByteArray())
        Log.e("panda", "send->" + HexDump.dumpHexString(bytes.toByteArray()))
    }

    //开启or关闭刷卡
    fun startReadCard(b: Boolean) {
        val entity = ReadCardEntity()
        entity.isStart = b
        val bytes = entity.write()
        CommunicateHelper.writeBytes(bytes.toByteArray())
        Log.e("panda", "send->" + HexDump.dumpHexString(bytes.toByteArray()))
    }

    //自动绑定
    fun autoBand(list: ArrayList<Int>) {
        val entity = BandEntity()
        entity.isHand = false
        entity.list.addAll(list)
        val bytes = entity.write()
        CommunicateHelper.writeBytes(bytes.toByteArray())
        Log.e("panda", "send->" + HexDump.dumpHexString(bytes.toByteArray()))
    }

    //获取接收器信息
    fun getDeviceInfo() {
        val entity = DeviceInfoEntity()
        val bytes = entity.write()
        CommunicateHelper.writeBytes(bytes.toByteArray())
        Log.e("panda", "send->" + HexDump.dumpHexString(bytes.toByteArray()))
    }

    //修改白名单
    fun modifyWhite(list: ArrayList<Int>) {
        val entity = ModifyWhiteListEntity()
        entity.list.addAll(list)
        val bytes = entity.write()
        CommunicateHelper.writeBytes(bytes.toByteArray())
        Log.e("panda", "send->" + HexDump.dumpHexString(bytes.toByteArray()))
    }

    //在线回答(方便demo里面操作问题集合)
    fun onlineAnswer(tag: Int, list: LinkedHashMap<Int, Question>) {
        val entity = OnlineAnswerEntity()
        entity.question_tag = tag
        for (key in list.keys) {
            entity.list.add(list[key]!!)
        }
        val bytes = entity.write()
        CommunicateHelper.writeBytes(bytes.toByteArray())
        Log.e("panda", "send->" + HexDump.dumpHexString(bytes.toByteArray()))
    }

    //在线回答
    fun onlineAnswer(tag: Int, list: ArrayList<Question>) {
        val entity = OnlineAnswerEntity()
        entity.question_tag = tag
        entity.list.addAll(list)
        val bytes = entity.write()
        CommunicateHelper.writeBytes(bytes.toByteArray())
        Log.e("panda", "send->" + HexDump.dumpHexString(bytes.toByteArray()))
    }

    //停止作答
    fun stopAnswering(){
        val entity = StopAnsweringEntity()
        val bytes = entity.write()
        CommunicateHelper.writeBytes(bytes.toByteArray())
        Log.e("panda", "send->" + HexDump.dumpHexString(bytes.toByteArray()))
    }
}