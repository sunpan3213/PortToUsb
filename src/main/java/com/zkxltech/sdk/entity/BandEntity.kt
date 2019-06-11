package com.zkxltech.sdk.entity

import com.hoho.android.usbserial.util.HexDump

/**
 * Create by Panda on 2019-05-17
 */
class BandEntity : BaseEntity() {

    var isHand = true
    var list = arrayListOf<Int>()

    override fun write(): ArrayList<Byte> {
        inf.add(0x0D)
        if (isHand) {//手动
            inf.add(0x01)
        } else {//自动
            inf.add(0x02)
            for (item in list){
                inf.addAll(HexDump.toByteArray(item,true).asList())
            }
        }
        return super.write()
    }

}