package com.zkxltech.sdk.entity

/**
 * Create by Panda on 2019-05-28
 */

class ReadCardEntity :BaseEntity(){

    var isStart:Boolean = true

    override fun write(): ArrayList<Byte> {
        if (isStart){
            inf.add(0x0B)
        }else{
            inf.add(0x0C)
        }
        return super.write()
    }
}