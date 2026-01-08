import 'package:flutter/services.dart';

class BluetoothControll{
  bool connectStatus = false;

  MethodChannel methodChannel = MethodChannel("com.example.languagebox/BLE");

  scanBle()async{
    await methodChannel.invokeListMethod("start");
  }

  stopBle()async{
    await methodChannel.invokeListMethod("stop");
  }

  connectGATT(String address)async{
    await methodChannel.invokeListMethod("connectGATT",{"address":address});
  }

  sendDATA(String a)async{
    print(a);
    await methodChannel.invokeListMethod("send",{"data":a});
  }

  disconnectGATT()async{
    await methodChannel.invokeMethod("disconnectGATT");
  }

  Set<dynamic> list1 = {};

  bleLisener()async{
    methodChannel.setMethodCallHandler((call)async{
      if(call.method=="connectStatus"){
        connectStatus=call.arguments;
        print(connectStatus);
      }
      else if (call.method=="getDevices"){
        var data = call.arguments;
        if (!data.contains("null")){
          list1.add(call.arguments);
        }
      }
    });
  }


  retrrnList()async{
    print(list1);
    return list1;
  }

}