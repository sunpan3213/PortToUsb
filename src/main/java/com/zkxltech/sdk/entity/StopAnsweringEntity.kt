package com.zkxltech.sdk.entity

import com.hoho.android.usbserial.util.HexDump

/**
 * Create by Panda on 2019-05-17
 */
class StopAnsweringEntity : BaseEntity() {


    override fun write(): ArrayList<Byte> {
        inf.add(0x06)
        return super.write()
    }

}