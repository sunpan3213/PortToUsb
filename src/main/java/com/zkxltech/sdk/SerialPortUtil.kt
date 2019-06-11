package com.zkxltech.sdk

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Parcelable
import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import java.lang.Exception

/**
 * Create by Panda on 2019-05-15
 */

object SerialPortUtil {

    val TAG = SerialPortUtil.javaClass.simpleName
    val USB_PERMISSION = "com.zkxltech.port"
    var usbManager: UsbManager? = null
    var sPort: UsbSerialPort? = null
    var mListener: Listener? = null
    var mReceiver: UsbReceiver? = null

    fun open(context: Context, listener: Listener) {
        mListener = listener
        //注册usb监听
        val filter = IntentFilter()
        filter.addAction(USB_PERMISSION)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        mReceiver = UsbReceiver()
        context.registerReceiver(mReceiver, filter)

        //遍历连接
        usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        connect(context)
    }

    private fun connect(context: Context) {
        usbManager?.let {
            val drivers = UsbSerialProber.getDefaultProber().findAllDrivers(it)
            if (drivers.isEmpty()) {
                Log.e(TAG, "drivers isEmpty")
                return
            }
            for (driver in drivers) {
                val device = driver.device
                if (device.vendorId == 1027 && device.productId == 24577) {
                    if (usbManager!!.hasPermission(device)) {
                        val usbDeviceConnection = it.openDevice(device)
                        if (usbDeviceConnection == null) {
                            Log.e(TAG, "usbDeviceConnection is null")
                            return
                        }
                        try {
                            sPort = driver.ports[0]
                            sPort?.open(usbDeviceConnection)
                            sPort?.setParameters(1152000, 8, 1, UsbSerialPort.PARITY_NONE)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            sPort?.close()
                            sPort = null
                        }
                        comunicate()
                    } else {
                        getUsbPermission(context, device)
                    }

                }
            }
        }
    }

    private fun getUsbPermission(context: Context, device: UsbDevice) {
        val pendingIntent = PendingIntent.getBroadcast(context, 0, Intent(USB_PERMISSION), 0)
        usbManager?.requestPermission(device, pendingIntent)
    }

    private fun comunicate() {
        CommunicateHelper.stop()
        sPort?.let { CommunicateHelper.start(it, mListener) }
    }

    private fun closeComunicate(){
        CommunicateHelper.stop()
        sPort?.close()
        sPort = null
    }

    fun close(context: Context) {
        CommunicateHelper.stop()
        context.unregisterReceiver(mReceiver)
        sPort?.close()
        mReceiver = null
        sPort = null
        usbManager = null
        mListener = null
    }

    class UsbReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            when (intent?.action) {
                USB_PERMISSION -> {
                    val usbPremission = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                    if (usbPremission) {
                        connect(context)
                    } else {
                        android.os.Process.killProcess(android.os.Process.myPid())
                    }
                }
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                    val usbDevice = intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice
                    if (usbManager!!.hasPermission(usbDevice)) {
                        connect(context)
                    } else {
                        getUsbPermission(context, usbDevice)
                    }

                }
                UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                    closeComunicate()
                }
            }
        }

    }

}