package com.zkxltech.sdk.entity

/**
 * Create by Panda on 2019-05-28
 */
class DeviceInfoEntity :BaseEntity(){

    override fun write(): ArrayList<Byte> {
        inf.add(0x01)
        return super.write()
    }
}