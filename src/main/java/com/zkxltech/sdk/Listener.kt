package com.zkxltech.sdk

import com.zkxltech.sdk.entity.*
import java.lang.Exception

/**
 * Create by Panda on 2019-05-15
 */
interface Listener{

    fun exception(e: Exception)

    fun bandRep(bandRepEntity: BandRepEntity)

    fun handBandRep(deviceBandRepEntity: DeviceBandRepEntity)

    fun readCardRep(readCardRepEntity: ReadCardRepEntity)

    fun deviceInfoRep(deviceInfoRepEntity: DeviceInfoRepEntity)

    fun modifyWhiteRep(modifyWhiteRepEntity: ModifyWhiteRepEntity)

    fun sendOnlineAnswerRep(onlineAnswerRepEntity:OnlineAnswerRepEntity)

    fun answerRep(answerRepEntity:AnswerRepEntity)

    fun stopAnsweringRep(stopAnsweringRepEntity:StopAnsweringRepEntity)
}