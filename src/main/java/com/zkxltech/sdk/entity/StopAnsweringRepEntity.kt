package com.zkxltech.sdk.entity

import com.hoho.android.usbserial.util.HexDump

/**
 * Create by Panda on 2019-05-17
 */
class StopAnsweringRepEntity : BaseEntity() {

    var code: Byte = 0x00

    fun read(data: ByteArray) {
        code = data[0]
    }

}