package com.zkxltech.sdk

import android.util.Log
import com.hoho.android.usbserial.util.HexDump
import java.lang.Exception
import java.nio.ByteBuffer
import java.util.*

/**
 * Create by Panda on 2019-06-10
 */
class DataPackageThread : Thread() {

    private val mTaskQueue = LinkedList<ByteArray>() // 待解析任务队列
    private val ticket = Object()

    override fun run() {
        while (!isInterrupted) {
            val bytes = poll() ?: continue
            try {
                DecodeUtil.decode(bytes)
            }catch (e:Exception){

            }
        }
    }

    fun decodeLater(byteArray: ByteArray) {
        Log.e("panda","解析数据->"+HexDump.toHexString(byteArray))
        synchronized(mTaskQueue) {
            mTaskQueue.add(byteArray)
        }
        synchronized(ticket) {
            ticket.notify()
        }
    }

    private fun poll(): ByteArray? {
        synchronized(mTaskQueue) {
            if (mTaskQueue.size > 0) {
                val bytes = mTaskQueue.removeAt(0)

                return bytes
            }
        }
        synchronized(ticket) {
            try {
                ticket.wait()
            } catch (ex: InterruptedException) {
            }

        }
        return null
    }
}