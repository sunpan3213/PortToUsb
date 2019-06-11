package com.zkxltech.sdk.entity

import com.hoho.android.usbserial.util.HexDump

/**
 * Create by Panda on 2019-05-29
 */
class ModifyWhiteListEntity : BaseEntity() {

    var list = arrayListOf<Int>()

    override fun write(): ArrayList<Byte> {
        inf.add(0x04)
        if (list.size > 0) {
            for (id in list) {
                inf.addAll(HexDump.toByteArray(id, true).asList())
            }
        }
        return super.write()
    }
}