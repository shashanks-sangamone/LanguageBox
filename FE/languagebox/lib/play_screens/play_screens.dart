import 'dart:math';

import 'package:flutter/material.dart';

import '../api/bluetooth_cont.dart';
import '../api/request_handler.dart';

class PlayScreens extends StatelessWidget {
  const PlayScreens({super.key,required this.id,required this.fileName});

  final int id;
  final String fileName;
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: PlayScreensHome(id: id,fileName: fileName,),
    );
  }
}

class PlayScreensHome extends StatefulWidget {
  const PlayScreensHome({super.key,required this.id,required this.fileName});

  final int id;
  final String fileName;
  @override
  State<PlayScreensHome> createState() => _PlayScreensHomeState();
}

class _PlayScreensHomeState extends State<PlayScreensHome> {

  static List<dynamic> listType = ["Normal","Random","Sorted"];
  static List<dynamic> listMode = ["Manual","Automatic"];
  static List<dynamic> automaticTime = ["Seconds","Minutes","Hours","Day"];
  static List<dynamic> listNumbers = List<int>.generate(59,(index)=>index+1);
  static List<dynamic> listSeconds = List<int>.generate(29,(index)=>index+30);
  static List<dynamic> listNumbers1 = List<int>.generate(23,(index)=>index+1);
  String drp1 = listType.first;
  String drp2 = listMode.first;
  String drp3 = automaticTime.first;
  int drp4 = listNumbers.first;
  int drp5 = listNumbers1.first;
  int drp6 = listSeconds.first;
  RequestHandler requestHandler = RequestHandler();
  List<dynamic> data1=[];
  List<dynamic> history =[];
  int hisPTR = -1;
  int autoM = 1;
  TextEditingController search = TextEditingController();
  TextEditingController listName = TextEditingController();
  late var selList = data1.first;
  BluetoothControll bluetoothControll = BluetoothControll();

  @override
  void initState() {
    Future.microtask(()async{
      List<dynamic> data=await requestHandler.getUserListByFilename(widget.id, widget.fileName);
      setState(() {
        print(data[0].split(","));
        data1=data[0].split(",");
        historyAdd();
      });
    });
    super.initState();
  }


  historyAdd(){
    setState(() {
      history.add(selList);
      hisPTR+=1;
      print(history);
    });
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Play List - ${widget.fileName.replaceAll(".txt", "")}"),
      ),
      body: data1.isEmpty?Container():Column(

        children: [
          Padding(
            padding: const EdgeInsets.all(18.0),
            child: Container(
              height: MediaQuery.of(context).size.height/3,
              alignment: Alignment.center,
              child: SingleChildScrollView(
                controller: ScrollController(
                  ),
                child: Column(
                  children: [
                    Wrap(alignment: WrapAlignment.center,children: data1.map((e) =>
                    e==selList? Card(
                      color: Colors.black,
                        clipBehavior: Clip.antiAlias,elevation: 20,margin: EdgeInsets.all(4),child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Text(e,style: TextStyle(fontWeight: FontWeight.bold,fontSize: 20,color: Colors.white),),
                        )):
                    InkWell(
                      child: Card(clipBehavior: Clip.antiAlias,elevation: 20,margin: EdgeInsets.all(4),child: Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text(e),
                      )),
                      onTap: (){
                        setState(() {
                          selList=e;
                          historyAdd();
                        });
                      },
                    ),
                    ).toList(),),
                  ],
                ),
              ),
            ),
          ),
          Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            mainAxisSize: MainAxisSize.max,
            children: [
              hisPTR==0?Container():ElevatedButton(onPressed: ()async{
                setState(() {
                  hisPTR-=1;
                  selList=history[hisPTR];
                });
                await bluetoothControll.sendDATA("${selList}");
              }, child: Text("Previous",style: TextStyle(fontSize: 30),)),
              hisPTR+1==history.length?Container():ElevatedButton(onPressed: ()async{
                setState(() {
                  print(hisPTR);
                  print(history.length);
                  hisPTR+=1;
                  selList=history[hisPTR];
                });
                await bluetoothControll.sendDATA("${selList}");
              }, child: Text("Next",style: TextStyle(color: Colors.white,fontSize: 30)),style: ElevatedButton.styleFrom(backgroundColor: Colors.blue),)
            ],
          ),
          Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            mainAxisSize: MainAxisSize.max,
            children: [
              DropdownButton(value: drp1,
                  items: listType.map<DropdownMenuItem<String>>((value){
                return DropdownMenuItem(child: Text("${value}"),
                value: value,);
              }).toList(), onChanged: (value){
                setState(() {
                  drp1=value!;
                  if (value=="Sorted"){
                    data1.sort();
                    selList=data1.first;
                  }
                  else if(value=="Random"){
                    data1.shuffle(Random());
                    selList=data1.first;
                  }
                });
              }),
              DropdownButton(value: drp2,
                  items: listMode.map<DropdownMenuItem<String>>((value){
                    return DropdownMenuItem(child: Text("${value}"),
                      value: value,);
                  }).toList(), onChanged: (value){
                    setState(() {
                      drp2=value!;
                    });
                  }),
            ],
          ),
          drp2!="Manual"?Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            mainAxisSize: MainAxisSize.max,
            children: [
              drp3=="Seconds"?DropdownButton(value: drp6,
                  items: listSeconds.map<DropdownMenuItem<int>>((value){
                    return DropdownMenuItem(child: Text("${value}"),
                      value: value,);
                  }).toList(), onChanged: (value){
                    setState(() {
                      drp6=value!;
                      autoM=value;
                    });
                  })
                  :
              drp3=="Minutes"?DropdownButton(value: drp4,
                  items: listNumbers.map<DropdownMenuItem<int>>((value){
                    return DropdownMenuItem(child: Text("${value}"),
                      value: value,);
                  }).toList(), onChanged: (value){
                    setState(() {
                      drp4=value!;
                      autoM=value;
                    });
                  })
              :drp3=="Hours"?DropdownButton(value: drp5,
                  items: listNumbers1.map<DropdownMenuItem<int>>((value){
                    return DropdownMenuItem(child: Text("${value}"),
                      value: value,);
                  }).toList(), onChanged: (value){
                    setState(() {
                      drp5=value!;
                      autoM=value;
                    });
                  }):Container(),
              DropdownButton(value: drp3,
                  items: automaticTime.map<DropdownMenuItem<String>>((value){
                    return DropdownMenuItem(child: Text("${value}"),
                      value: value,);
                  }).toList(), onChanged: (value){
                    setState(() {
                      drp3=value!;
                    });
                  }),
            ],
          ):Container(),
          drp2!="Manual"?ElevatedButton(onPressed: ()async{
            await bluetoothControll.sendDATA(data1.join(",")+"|${drp3[0]}-${autoM}");
          }, child: Text("Submit")):Container()
        ],
      ),
    );
  }
}
