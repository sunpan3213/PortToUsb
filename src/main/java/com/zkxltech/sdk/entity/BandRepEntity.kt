package com.zkxltech.sdk.entity

import com.hoho.android.usbserial.util.HexDump

/**
 * Create by Panda on 2019-05-17
 */
class BandRepEntity : BaseEntity() {

    var code: Byte = 0x00
    var pin: String? = null

    fun read(data: ByteArray) {
        code = data[0]
        pin = HexDump.toHexString(data, 1, data.size - 1)
    }

}