import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:languagebox/main.dart';

import '../api/bluetooth_cont.dart';

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


  BluetoothControll bluetoothControll = BluetoothControll();


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Bluetooth Devices"),
      ),
      body: FutureBuilder(future: bluetoothControll.retrrnList(), builder: (context,AsyncSnapshot<dynamic> snapshot){
        print(snapshot.data);
        if(snapshot.connectionState==ConnectionState.waiting){
          return CircularProgressIndicator();
        }
        else if(snapshot.hasError){
          return Center(child: Text("${snapshot.error}"),);
        }
        else if (snapshot.hasData){
          Set<dynamic> data = snapshot.data;
          List<dynamic> data1 = data.toList();
          return ListView.builder(shrinkWrap: true,itemCount: data1.length,itemBuilder: (context,index){
            List<dynamic> bleDev = data1[index].toString().split(",");
            return InkWell(
              child: Card(
                child: ListTile(
                  title: Text("${bleDev[0]}"),
                  subtitle: Text("${bleDev[1]}"),
                ),
              ),
              onTap: ()async{
                String address = data1[index].toString().split(",")[1];
                await bluetoothControll.connectGATT(address);
                await bluetoothControll.stopBle();
                if(bluetoothControll.connectStatus){
                  bluetoothControll.stopBle();
                  Navigator.pop(context);
                }
              },
            );
          });
        }
        else{
          return Text("No data");
        }
      }),
      floatingActionButton: FloatingActionButton(onPressed: (){
        setState(() {
        });
      }),
    );
  }
}
