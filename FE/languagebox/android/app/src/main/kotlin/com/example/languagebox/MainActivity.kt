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

    private val channel = "com.example.languagebox/BLE"

    private val bLUETOOTH_SCAN_PERMISSION_CODE = 101
    private val REQUEST_BLE_ENABLE = 12

    // Your UUIDs
    private val SERVICE_UUID = "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
    private val CHARACTERISTIC_UUID = "beb5483e-36e1-4688-b7f5-ea07361b26a8"
    private val CLIENT_CHARACTERISTIC_CONFIG_UUID = "00002902-0000-1000-8000-00805f9b34fb" // Standard descriptor for notifications

    // BLE Objects
    private lateinit var bluetoothManager : BluetoothManager
    private lateinit var bluetoothAdapter : BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    // Connection/Data Objects
    private var bluetoothGatt : BluetoothGatt? = null // *** Now correctly initialized to null
    private var service : BluetoothGattService? = null
    private var characteristic : BluetoothGattCharacteristic? = null // Changed to nullable

    private lateinit var methodChannel: MethodChannel

// -------------------------------------------------------------------------------------------------
// Lifecycle and Setup
// -------------------------------------------------------------------------------------------------

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
                "start" -> scanBLE()
                "stop" -> stopBLE()
                "connectGATT"->{
                    val address = call.argument<String>("address")
                    address?.let { connectGATT(address=it) }
                    stopBLE()
                }
                "disconnectGATT"-> disconnectGATT()
                "send"->{
                    val data = call.argument<String>("data")
                    // Note: API level check is only necessary if using Tiramisu-specific APIs,
                    // which sendDATA is not (but let's keep it for your original structure)
                    data?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            sendDATA(it)
                        } else {
                            sendDATA(it) // For non-Tiramisu devices
                        }
                    }
                }
            }
        }
    }

// -------------------------------------------------------------------------------------------------
// GATT Callback Implementation
// -------------------------------------------------------------------------------------------------

    private val bluetoothGattCallback = object : BluetoothGattCallback(){

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if (newState == BluetoothProfile.STATE_CONNECTED){
                bluetoothGatt = gatt // Ensure gatt object is assigned here as well (redundant with connectGATT fix but safe)
                gatt.discoverServices()
                runOnUiThread {
                    methodChannel.invokeMethod("connectStatus" , true)
                    Toast.makeText(context,"${gatt.device.name} connected", Toast.LENGTH_SHORT).show()
                }

            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // Ensure state cleanup on disconnect
                gatt.close()
                bluetoothGatt = null
                runOnUiThread {
                    methodChannel.invokeMethod("connectStatus", false)
                }
            }
        }

        // *** FIX: Simplified and corrected service/characteristic discovery logic ***
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS){
                service = gatt.getService(UUID.fromString(SERVICE_UUID))
                characteristic = service?.getCharacteristic(UUID.fromString(CHARACTERISTIC_UUID))

                if (characteristic != null) {
                    Log.d("BLE_GATT", "Service and Characteristic found.")
                    // Optional: Enable notifications if needed for this characteristic
                    /*
                    if (gatt.setCharacteristicNotification(characteristic, true)) {
                        val descriptor = characteristic!!.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID))
                        if (descriptor != null) {
                            gatt.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                        }
                    }
                    */
                } else {
                    Log.e("BLE_GATT", "Characteristic $CHARACTERISTIC_UUID not found in service $SERVICE_UUID.")
                }
            } else {
                Log.e("BLE_GATT", "Service discovery failed with status: $status")
            }
        }

        // *** FIX: Corrected and crucial callback for write confirmation ***
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            if (status == BluetoothGatt.GATT_SUCCESS){
                // This is the true success log!
                Log.d("SENT_CONFIRM", "Data successfully sent/acknowledged. Value: ${String(characteristic.value, Charsets.UTF_8)}")
            } else {
                Log.e("SENT_CONFIRM", "Write failed on callback with status: $status")
            }
        }

        // ... (onCharacteristicRead and onCharacteristicChanged are fine)

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            runOnUiThread {
                methodChannel.invokeMethod("received", String(value, Charsets.UTF_8))
            }
            Log.d("RECEIVED","${String(value, Charsets.UTF_8)}")
        }
    }

// -------------------------------------------------------------------------------------------------
// Connection and Data Methods
// -------------------------------------------------------------------------------------------------

    @SuppressLint("MissingPermission")
    fun connectGATT(address:String){
        bluetoothAdapter.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // *** CRITICAL FIX: Assign the returned BluetoothGatt object! ***
                    bluetoothGatt = device.connectGatt(this,false,bluetoothGattCallback)
                    Log.d("BLE_GATT", "Attempting connection to ${device.address}")
                }
            }
            catch (e: IllegalArgumentException){
                Toast.makeText(this,"Invalid address: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("BLE_GATT", "Invalid MAC Address or failed remote device retrieval.")
            }
        }
    }


    @SuppressLint("MissingPermission", "NewApi")
    private fun sendDATA(data:String){
        // 1. Get the characteristic (use the class-level characteristic if already discovered)
        val targetCharacteristic = characteristic ?: bluetoothGatt?.getService(UUID.fromString(SERVICE_UUID))?.getCharacteristic(UUID.fromString(CHARACTERISTIC_UUID))

        val valueToSend = data.toByteArray(Charsets.UTF_8)
        Log.d("SENT_REQUEST", "Attempting to send: $data (${valueToSend.size} bytes)")

        targetCharacteristic?.let {
            if (bluetoothGatt == null) {
                Log.e("BLE", "Cannot send data: bluetoothGatt is null (not connected).")
                return
            }

            // 2. Queue the write request
            val result = bluetoothGatt!!.writeCharacteristic(
                it,
                valueToSend,
                BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            )

            // 3. Check if the request was successfully queued
            if (result == BluetoothStatusCodes.SUCCESS) {
                Log.d("SENT_REQUEST", "Write request queued successfully. Waiting for onCharacteristicWrite callback.")
            } else {
                // This indicates a failure to queue, NOT a transmission failure.
                Log.e("BLE", "Write request failed to queue with status: $result")
            }
        } ?: Log.e("BLE", "Characteristic is null. Cannot send data.")
    }

    @SuppressLint("MissingPermission")
    fun disconnectGATT(){
        if (bluetoothGatt != null) {
            bluetoothGatt?.disconnect()
            // We rely on onConnectionStateChange (newState=STATE_DISCONNECTED) to call close() and cleanup
        } else {
            // Already disconnected or never connected
            runOnUiThread {
                methodChannel.invokeMethod("connectStatus", false)
            }
        }
    }

// -------------------------------------------------------------------------------------------------
// Scanning and Permission Methods (Untouched)
// -------------------------------------------------------------------------------------------------

    val leScanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
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

    @SuppressLint("MissingPermission")
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

    @SuppressLint("MissingPermission")
    fun stopBLE(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            runOnUiThread {
                bluetoothLeScanner.stopScan(leScanCallback)
            }
        }
    }

    @SuppressLint("MissingPermission")
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

    @Suppress("DEPRECATION")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_BLE_ENABLE && resultCode==RESULT_OK){
            bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
    }
}