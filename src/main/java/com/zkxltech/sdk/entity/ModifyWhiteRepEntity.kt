package com.zkxltech.sdk.entity

/**
 * Create by Panda on 2019-05-17
 */
class ModifyWhiteRepEntity : BaseEntity() {

    var code: Byte = 0x00

    fun read(data: ByteArray) {
        code = data[0]
    }

}