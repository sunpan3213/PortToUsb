package com.zkxltech.sdk.entity

import com.hoho.android.usbserial.util.HexDump

/**
 * Create by Panda on 2019-05-28
 */
class DeviceBandRepEntity : BaseEntity() {

    var uid: Int = 0
    var replace: Byte = 0x00
    var repalceId: Int = 0

    fun read(data: ByteArray) {
        val bytes = ByteArray(4)
        System.arraycopy(data, 0, bytes, 0, 4)
        uid = HexDump.bytesToInt(bytes, true)
        if (data.size >= 5) {
            replace = data[4]
            System.arraycopy(data, 5, bytes, 0, 4)
            repalceId = HexDump.bytesToInt(bytes, true)
        }
    }
}