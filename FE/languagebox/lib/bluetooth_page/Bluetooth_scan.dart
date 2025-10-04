import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:languagebox/main.dart';

class BluetoothScan extends StatelessWidget {
  const BluetoothScan({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: BluetoothScanHome(),
    );
  }
}


class BluetoothScanHome extends StatefulWidget {
  const BluetoothScanHome({super.key});

  @override
  State<BluetoothScanHome> createState() => _BluetoothScanHomeState();
}

class _BluetoothScanHomeState extends State<BluetoothScanHome> {
  @override
  void initState() {
    super.initState();
    start();
    channel.setMethodCallHandler(handler);
    // connect();
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
    stop();
  }

  var channel = MethodChannel("com.example.languagebox/BLE");

  start()async{
    await channel.invokeMethod("start",{});
  }

  connect(String MAC)async{
    await channel.invokeMethod("connectGATT",{"MAC":"68:25:DD:32:98:2A"});
  }



  stop()async{
    await channel.invokeMethod("stop");
  }

  List<dynamic> list1 =[];

  Future<void> handler(MethodCall call) async {
    switch (call.method) {
      case 'getDevices':
        var data = call.arguments;
        print(data);
        setState(() {
          if (!list1.contains(data)){
            list1.add(data);
          }
        });
        break;

      case 'connectionState':
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("BluetoothDareess"),
      ),
      body: ListView.builder(itemCount: list1.length,itemBuilder: (context,index){
        return InkWell(
          child: Card(
            child: ListTile(
              title: Text("${list1[index]["name"]}"),
              subtitle: Text("${list1[index]["address"]}"),
            ),
          ),
          onTap: ()async{
            await connect(list1[index]["address"]);
            Navigator.push(context, MaterialPageRoute(builder: (context)=>MyHomePage()));
          },
        );
      }),
    );
  }
}
