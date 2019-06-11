package com.zkxltech.sdk

import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.util.HexDump
import com.hoho.android.usbserial.util.SerialInManager
import com.hoho.android.usbserial.util.SerialOutManager
import com.zkxltech.sdk.CommunicateHelper.mSerialListener
import com.zkxltech.sdk.CommunicateHelper.serialInManager
import com.zkxltech.sdk.CommunicateHelper.serialOutManager
import com.zkxltech.sdk.entity.*
import java.lang.Exception

/**
 * Create by Panda on 2019-05-16
 */
object CommunicateHelper {

    val TAG = CommunicateHelper.javaClass.simpleName
    val list = arrayListOf<ByteArray>()
    val indexList = arrayListOf<Int>()
    var mListener: Listener? = null
    var serialInManager: SerialInManager? = null
    var serialOutManager: SerialOutManager? = null
    var mSerialListener: SerialInManager.Listener? = null
    var decodeThread: DataPackageThread? = null

    fun start(port: UsbSerialPort, listener: Listener?) {
        mListener = listener
        mSerialListener = object : SerialInManager.Listener {
            override fun onNewData(data: ByteArray) {

                Log.e("panda", "receive->" + HexDump.dumpHexString(data))

                if (data[0] == 0xBD.toByte() && data[data.lastIndex] == 0xBE.toByte()) {//完整
                    Log.e(TAG, "完整数据")
                    list.clear()//清除未组包的无效数据
                    indexList.clear()
                    synchronized(this) {
                        data.filterIndexed { index, byte ->
                            if (byte == 0XBD.toByte()) {
                                indexList.add(index)
                            } else {
                                false
                            }
                        }
                        for (i in indexList.indices) {
                            Log.e("panda", "index->" + indexList[i])
                            if (data[indexList[i] + 1] == 0x85.toByte() && data[indexList[i] + 2] == 0x01.toByte() && data[indexList[i] + 3] == 0x01.toByte()) {//完整包头BD 85 01 01
                                val lengthBytes = ByteArray(2)
                                System.arraycopy(data, indexList[i] + 4, lengthBytes, 0, 2)
                                val length = HexDump.toHexString(lengthBytes).toShort(16) + 6 + 2//包总长度
                                val bytes = ByteArray(length)
                                System.arraycopy(data, indexList[i], bytes, 0, bytes.size)
                                decodeThread?.decodeLater(bytes)
                            }
                        }
                    }
                }
                if (data[0] == 0xBD.toByte() && data[data.lastIndex] != 0xBE.toByte()) {//包尾没有
                    Log.e(TAG, "没包尾数据")
                    list.clear()//清除未组包的无效数据
                    indexList.clear()
                    list.add(data)
                }
                if (data[0] != 0xBD.toByte() && data[data.lastIndex] == 0xBE.toByte()) {//包头没有 包尾有
                    Log.e(TAG, "没包头数据")
                    list.add(data)
                    val array = ByteArray(5 * 1024)
                    var offset = 0
                    while (list.size > 0) {
                        val bytes = list.removeAt(0)
                        System.arraycopy(bytes, 0, array, offset, bytes.size)
                        offset += bytes.size
                    }
                    val realData = ByteArray(offset)
                    System.arraycopy(array, 0, realData, 0, realData.size)
                    Log.e(CommunicateHelper.javaClass.simpleName, "组包数据->" + HexDump.toHexString(realData))
                    Log.e(CommunicateHelper.javaClass.simpleName, "组包数据长度->" + realData.size)
                    indexList.clear()
                    synchronized(this) {
                        realData.filterIndexed { index, byte ->
                            if (byte == 0XBD.toByte()) {
                                indexList.add(index)
                            } else {
                                false
                            }
                        }
                        for (i in indexList.indices) {
                            Log.e("panda", "index->" + indexList[i])
                            if (realData[indexList[i] + 1] == 0x85.toByte() && realData[indexList[i] + 2] == 0x01.toByte() && realData[indexList[i] + 3] == 0x01.toByte()) {//完整包头BD 85 01 01
                                val lengthBytes = ByteArray(2)
                                System.arraycopy(realData, indexList[i] + 4, lengthBytes, 0, 2)
                                val length = HexDump.toHexString(lengthBytes).toShort(16) + 6 + 2//包总长度
                                val bytes = ByteArray(length)
                                System.arraycopy(realData, indexList[i], bytes, 0, bytes.size)
                                decodeThread?.decodeLater(bytes)
                            }
                        }
                    }
                }
                if (data[0] != 0xBD.toByte() && data[data.lastIndex] != 0xBE.toByte()) {//没包头包尾的中间数据
                    Log.e(TAG, "没包头包尾的中间数据")
                    list.add(data)
                }

            }

            override fun onRunError(e: Exception) {
                mListener?.exception(e)
            }
        }
        decodeThread = DataPackageThread()
        decodeThread?.start()
        serialInManager = SerialInManager(port, mSerialListener)
        serialOutManager = SerialOutManager(port)
        Thread(serialOutManager).start()
        Thread(serialInManager).start()
    }


    fun stop() {
        decodeThread?.interrupt()
        serialOutManager?.stop()
        serialInManager?.stop()
        decodeThread = null
        serialOutManager = null
        serialInManager = null
        mSerialListener = null
        mListener = null
    }

    fun writeBytes(bytes: ByteArray) {
        serialOutManager?.writeAsync(bytes)
    }
}