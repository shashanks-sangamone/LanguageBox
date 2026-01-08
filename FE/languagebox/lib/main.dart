import 'dart:ffi';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:languagebox/api/bluetooth_cont.dart';
import 'package:languagebox/api/request_handler.dart';
import 'package:languagebox/loadingscreen.dart';
import 'package:languagebox/login_page/loginPage.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:languagebox/play_screens/create_list.dart';
import 'package:languagebox/play_screens/play_screens.dart';
import 'bluetooth_page/Bluetooth_scan.dart';
import 'firebase_options.dart';
import 'login_page/google_auth.dart';


void main() async{
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: LoadingScreen(),
      // home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({super.key,required this.id});

  int id;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  void initState() {
    setState(() {

    });
    super.initState();
  }

  @override
  void dispose() {
    controller.dispose();
    BluetoothControll().disconnectGATT();
    super.dispose();
  }

  var data1 = 0.0;

  var name ="";

  bool bleStatus=false;

  BluetoothControll bluetoothControll = BluetoothControll();

  TextEditingController controller = TextEditingController(text: "ABC");

  RequestHandler requestHandler = RequestHandler();

  int currentSlide=3;

  static List<dynamic> listAlphabets = List<String>.generate(26, (index) =>String.fromCharCode('a'.codeUnitAt(0)+index),);
  static List<dynamic> listNumbers = List<int>.generate(20,(index)=>index+2);
  String drp1 = listAlphabets.first;
  int drp2 = listNumbers.first;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.black,
        title: Center(child: Text("Language Box",style: TextStyle(color: Colors.white),textAlign: TextAlign.center,)),
        actions: [
          InkWell(child: Icon(Icons.account_balance),
          onTap: ()async{
            await GoogleAuth().signOut();
            Navigator.pushReplacement(context, MaterialPageRoute(builder: (context)=>MyApp()));
          },)
        ],
      ),
      body: currentSlide==0?
      FutureBuilder(future: requestHandler.getWordAll(), builder: (context,AsyncSnapshot<dynamic> snapshot){
        if(snapshot.connectionState==ConnectionState.waiting){
          return Center(child: CircularProgressIndicator());
        }
        else if(snapshot.hasError){
          return Center(child: Text("${snapshot.error}"));
        }
        else if(snapshot.hasData){
          List<dynamic> data = snapshot.data;
          return ListView.builder(itemBuilder: (context,index){
            return InkWell(
              child: ListTile(
                title: Text("${data[index]}"),
              ),
              onTap: ()async{
                await bluetoothControll.sendDATA("${data[index]}");
              },
            );
          });
        }
        else{
          return Center(child: Text("No data"));
        }
      }):currentSlide==1?Column(
        children: [
          Flexible(
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                DropdownButton(
                  value: drp1,
                    items: listAlphabets.map<DropdownMenuItem<dynamic>>((dynamic value){
                      return DropdownMenuItem(
                          value: value,
                          child: Text("$value"),
                      );
                    }).toList(),
                    onChanged: (dynamic value){
                      setState(() {
                        drp1=value;
                      });
                    }),
                DropdownButton(
                    value: drp2,
                    items: listNumbers.map<DropdownMenuItem<int>>((dynamic value){
                      return DropdownMenuItem(
                        value: value,
                        child: Text("$value"),
                      );
                    }).toList(),
                    onChanged: (int? value){
                      setState(() {
                        drp2=value!;
                      });
                    }),
              ],
            ),
          ),
          FutureBuilder(future: requestHandler.getWordByLenAndAlpha(drp2, drp1), builder: (context,AsyncSnapshot<dynamic> snapshot){
            if(snapshot.connectionState==ConnectionState.waiting){
              return Center(child: CircularProgressIndicator());
            }
            else if(snapshot.hasError){
              return Center(child: Text("${snapshot.error}"));
            }
            else if(snapshot.hasData){
              List<dynamic> data = snapshot.data;
              return SingleChildScrollView(
                controller: ScrollController(
                ),
                child: Wrap(alignment: WrapAlignment.center,children: data.map((e) =>
                    Card(clipBehavior: Clip.antiAlias,elevation: 20,margin: EdgeInsets.all(4),child: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text(e),
                    )),).toList(),),
              );
            }
            else{
              return Center(child: Text("No data"));
            }
          }),
        ],
      ):currentSlide==2?Column(
        children: [
          TextField(
            controller: controller,
          ),
          InkWell(
            child: Text("Send Ble Data"),
            onTap: ()async{
              await bluetoothControll.sendDATA(controller.text);
            },
          )
        ],
      ):FutureBuilder(future: requestHandler.getUserListByUserId(widget.id), builder: (context,AsyncSnapshot<dynamic> snapshot){
        if(snapshot.connectionState==ConnectionState.waiting){
          return Center(child: CircularProgressIndicator());
        }
        else if(snapshot.hasError){
          return Center(child: Text("${snapshot.error}"));
        }
        else if(snapshot.hasData){

          List<dynamic> data = snapshot.data;
          return ListView.builder(shrinkWrap: true,itemCount: data.length,itemBuilder: (context,index){
            return InkWell(
              child: Card(
                child: ListTile(
                  title: Text("${data[index]["listName"]}"),
                  subtitle: FutureBuilder(future: requestHandler.getUserListByFilename(widget.id, "${data[index]["filename"]}"), builder: (context,snapshot){
                    if(snapshot.connectionState==ConnectionState.waiting){
                      return Center(child: CircularProgressIndicator());
                    }
                    else if(snapshot.hasError){
                      return Center(child: Text("${snapshot.error}"));
                    }
                    else if(snapshot.hasData){
                      return Text("${snapshot.data}");
                    }
                    else return Text("data");
                  }),
                  trailing: InkWell(child: Icon(Icons.delete,size: 20,),
                    onTap: ()async{
                      await requestHandler.deleteUserListById(data[index]["id"]);
                      setState(() {
                      });
                    },),
                ),
              ),
              onTap: (){
                Navigator.push(context, MaterialPageRoute(builder: (context)=>PlayScreensHome(id:widget.id,fileName: data[index]["filename"],)));
              },
              onLongPress: ()async{
                await Navigator.push(context, MaterialPageRoute(builder: (context)=>CreateListHome(id: data[index]["id"],type:"update")));
              },
            );
          });
        }
        else{
          return Center(child: Text("No data"));
        }
      }),
      bottomNavigationBar: BottomNavigationBar(
        landscapeLayout: BottomNavigationBarLandscapeLayout.centered,
        backgroundColor: Colors.black87,
          elevation: 18,
          selectedItemColor: Colors.white,
          iconSize: 30,
          type: BottomNavigationBarType.fixed,
          unselectedItemColor: Colors.white30,
          selectedLabelStyle: TextStyle(color: Colors.white,fontSize: 16),
          items: [
            BottomNavigationBarItem(icon: Icon(Icons.home_filled),label: "Home",activeIcon: CircleAvatar(backgroundColor: Colors.white,child: Icon(Icons.home,color: Colors.black54,),)),
            BottomNavigationBarItem(icon: Icon(Icons.list_alt),label: "Sorted",activeIcon: CircleAvatar(backgroundColor: Colors.white,child: Icon(Icons.list_alt,color: Colors.black54),)),
            BottomNavigationBarItem(icon: Icon(Icons.view_list_rounded),label: "Custom",activeIcon: CircleAvatar(backgroundColor: Colors.white,child: Icon(Icons.view_list_rounded,color: Colors.black54),)),
            BottomNavigationBarItem(icon: Icon(Icons.add),label: "Add List",activeIcon: CircleAvatar(backgroundColor: Colors.white,child: Icon(Icons.add,color: Colors.black54),)),
          ],
        currentIndex: currentSlide,
        onTap: (value){
            setState(() {
              currentSlide=value;
            });
        },
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      floatingActionButton: currentSlide==3?
          FloatingActionButton(onPressed: (){
            Navigator.push(context, MaterialPageRoute(builder: (context)=>CreateListHome(id: widget.id,)));
          },child: Icon(Icons.add),)
          :FloatingActionButton.large(splashColor: Colors.black12,onPressed: ()async{
        bluetoothControll.scanBle();
        bluetoothControll.bleLisener();
        setState(() {
          bleStatus=bluetoothControll.connectStatus;
        });
        await showDialog(context: context, builder: (context){
          return Dialog(
            child: Column(
              mainAxisSize: MainAxisSize.max,
              children: [
                Expanded(
                  child: FutureBuilder(future: bluetoothControll.retrrnList(), builder: (context,AsyncSnapshot<dynamic> snapshot){
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
                ),
                ElevatedButton(onPressed: (){

                }, child: Text("Refresh")),
              ],
            ),
          );
        });
        // Navigator.push(context, MaterialPageRoute(builder: (context)=>BluetoothScan()));
      },child: bluetoothControll.connectStatus?Icon(Icons.bluetooth_disabled,color: Colors.black54):Icon(Icons.bluetooth_outlined,color: Colors.black54),elevation: 30,shape: CircleBorder(),autofocus: true,clipBehavior: Clip.antiAliasWithSaveLayer,backgroundColor: Colors.white,),
    );
  }
}
