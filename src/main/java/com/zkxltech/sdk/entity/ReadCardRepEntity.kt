package com.zkxltech.sdk.entity

import com.hoho.android.usbserial.util.HexDump

/**
 * Create by Panda on 2019-05-28
 */
class ReadCardRepEntity :BaseEntity(){

    var uid = 0

    fun read(data: ByteArray) {
        uid = HexDump.bytesToInt(data, true)
    }

}