package com.zerone.zerone_pos.helpers

/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.*

class PrinterBluetooth {
    companion object {
        private val PRINTER_UUID = UUID.fromString(Config.printer_uuid)
        private val Innerprinter_Address = Config.printer_address
        fun getBTAdapter(): BluetoothAdapter {
            return BluetoothAdapter.getDefaultAdapter()
        }

        fun getDevice(bluetoothAdapter: BluetoothAdapter): BluetoothDevice? {
            var innerprinter_device: BluetoothDevice? = null
            val devices = bluetoothAdapter.bondedDevices
            for (device in devices) {
                if (device.address == Innerprinter_Address) {
                    innerprinter_device = device
                    break
                }
            }
            return innerprinter_device
        }

        @Throws(IOException::class)
        fun getSocket(device: BluetoothDevice): BluetoothSocket {
            val socket = device.createRfcommSocketToServiceRecord(PRINTER_UUID)
            socket.connect()
            return socket
        }

        @Throws(IOException::class)
        fun sendData(bytes: ByteArray, socket: BluetoothSocket) {
            val out = socket.outputStream
            out.write(bytes, 0, bytes.size)
            out.close()
        }
    }
}