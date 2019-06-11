package com.zkxltech.sdk.entity

/**
 * Create by Panda on 2019-05-30
 */
class OnlineAnswerRepEntity : BaseEntity() {

    var code: Byte = 0x00

    fun read(data: ByteArray) {
        code = data[0]
    }
}