package com.example.languagebox

import android.Manifest
import android.annotation.SuppressLint
import android.app.ComponentCaller
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothStatusCodes
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.util.UUID

class MainActivity : FlutterActivity(){

    var channel = "com.example.languagebox/BLE"

    val bLUETOOTH_SCAN_PERMISSION_CODE = 101
    val REQUEST_BLE_ENABLE = 12

    private val SERVICE_UUID = "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
    private val CHARACTERISTIC_UUID = "beb5483e-36e1-4688-b7f5-ea07361b26a8"

    val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                result.let {
                    val devicesBLE = "${result.device.name},${result.device.address}"
                    runOnUiThread {
                        methodChannel.invokeMethod("getDevices",devicesBLE)
                    }
                }

                Log.d("BLE_SCAN", "Found device: ${result.device.name ?: "Unknown"} - ${result.device.address}")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)

            methodChannel.invokeMethod("error",errorCode)
        }
    }

    private lateinit var bluetoothManager : BluetoothManager
    private lateinit var bluetoothAdapter : BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private lateinit var methodChannel: MethodChannel

    var service : BluetoothGattService? = null
    private lateinit var characteristic : BluetoothGattCharacteristic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter= bluetoothManager.adapter
        bluePermission()

        if (bluetoothAdapter.isEnabled){
            bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
        else{
            enableBLE()
        }
    }

    @SuppressLint("MissingPermission")
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        methodChannel= MethodChannel(flutterEngine.dartExecutor,channel)

        methodChannel.setMethodCallHandler @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT) { call, result ->
            when(call.method){
                "start" ->{
                    scanBLE()
                }
                "stop"->{
                    stopBLE()
                }
                "connectGATT"->{
                    val address = call.argument<String>("address")
                    address?.let { connectGATT(address=it) }
                    stopBLE()
                }
                "disconnectGATT"->{
                    disconnectGATT()
                }
                "send"->{
                    val data = call.argument<String>("data")
                    data?.let { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        sendDATA(it)
                    }
                    };
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == bLUETOOTH_SCAN_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                scanBLE()
            }
        }
        else{
            bluePermission()
        }
    }

    fun bluePermission(){
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Manifest.permission.BLUETOOTH_SCAN
        } else {
            Manifest.permission.ACCESS_FINE_LOCATION
        }
        if(ActivityCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(permission, Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH),bLUETOOTH_SCAN_PERMISSION_CODE)
        }
        else{
            Log.d("","permission ")
        }
    }

    fun scanBLE(){
        if (!bluetoothAdapter.isEnabled){
            enableBLE()
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            bluetoothLeScanner.startScan(leScanCallback)
        }
        else{
            Log.d("asdsa","sadmaskdnonasd")
        }
    }

    fun stopBLE(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            runOnUiThread {
                bluetoothLeScanner.stopScan(leScanCallback)
            }
        }
    }

    fun enableBLE(){
        if(!bluetoothAdapter.isEnabled){
            val bleIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startActivityForResult(bleIntent,REQUEST_BLE_ENABLE)
            }

        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if (requestCode==REQUEST_BLE_ENABLE && resultCode==RESULT_OK){
            bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
    }

    val bluetoothGattCallback = object : BluetoothGattCallback(){
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState== BluetoothProfile.STATE_CONNECTED){
                gatt?.discoverServices()
                runOnUiThread {
                    methodChannel.invokeMethod("connectStatus" , true)
                    Toast.makeText(context,"${gatt?.device?.name} connected", Toast.LENGTH_SHORT).show()
                }

            }
            else{
                runOnUiThread {
                    methodChannel.invokeMethod("connectStatus", false)
                }
            }
        }


        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status== BluetoothGatt.GATT_SUCCESS){
                service = gatt?.getService(UUID.fromString(SERVICE_UUID))
                characteristic =
                    (service?.getCharacteristic(UUID.fromString(CHARACTERISTIC_UUID)) ?: if (characteristic!=null){
                        gatt?.setCharacteristicNotification(characteristic,true)
                        var descriptor = characteristic!!.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                        gatt?.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                    } else {
                        return
                    }) as BluetoothGattCharacteristic
            }
        }


        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
            runOnUiThread {
                methodChannel.invokeMethod("received",value)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            runOnUiThread {
                methodChannel.invokeMethod("received", String(value, Charsets.UTF_8))
            }
            Log.d("SENT","${String(value, Charsets.UTF_8)}")
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            if (status== BluetoothGatt.GATT_SUCCESS){
                Log.d("SENT","${characteristic?.value}")
            }
        }

    }

    var bluetoothGatt : BluetoothGatt? = null

    fun connectGATT(address:String){
        bluetoothAdapter.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    device.connectGatt(this,false,bluetoothGattCallback)
                }
            }
            catch (e: IllegalArgumentException){
                Toast.makeText(this,"${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingPermission", "NewApi")
    private fun sendDATA(data:String){

        val service = bluetoothGatt?.getService(UUID.fromString(SERVICE_UUID))
        val characteristic = service?.getCharacteristic(UUID.fromString(CHARACTERISTIC_UUID))
        val valueToSend = data.toByteArray(Charsets.UTF_8)

        characteristic?.let {
            val result = bluetoothGatt!!.writeCharacteristic(
                it,
                valueToSend,
                BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            )
            if (result != BluetoothStatusCodes.SUCCESS) {
                Log.e("BLE", "Write failed with status: $result")
            }
        }
    }


    fun disconnectGATT(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt=null
        runOnUiThread {
            methodChannel.invokeMethod("connectStatus", false)
        }
    }

}