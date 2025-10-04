import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:languagebox/api/request_handler.dart';
import 'package:languagebox/loadingscreen.dart';
import 'package:languagebox/login_page/loginPage.dart';
import 'package:firebase_core/firebase_core.dart';
import 'bluetooth_page/Bluetooth_scan.dart';
import 'firebase_options.dart';


void main() async{
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );
  runApp(const LoadingHomeScreen());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: const LoginHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});


  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    controller.dispose();
    disconnect();
    super.dispose();
  }



  var data1 = 0.0;

  var name ="";

  var channel = MethodChannel("com.example.languagebox/BLE");

  connector(String a)async{
    await channel.invokeMethod("sendData",{"data":a});
  }

  send(String a)async{
    await channel.invokeMethod("sendData",{"data":a});
  }

  disconnect()async{
    await channel.invokeMethod("disconnect");
  }


  TextEditingController controller = TextEditingController(text: "ABC");

  RequestHandler requestHandler = RequestHandler();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.black,
        title: Center(child: Text("Language Box",style: TextStyle(color: Colors.white),textAlign: TextAlign.center,)),
      ),
      body: Column(
        children: [
          SizedBox(
            height: 200,
            child: FutureBuilder(future: requestHandler.getRequest(requestHandler.wordsGetAll), builder: (context,AsyncSnapshot<List<dynamic>> snapshot){
              if(snapshot.connectionState==ConnectionState.waiting){
                return CircularProgressIndicator();
              }
              else if(snapshot.hasError){
                return Text("${snapshot.error}");
              }
              else if(snapshot.hasData){
                var data = snapshot.data;
                return ListView.builder(itemCount: data?.length,itemBuilder: (context,index){
                  return Card(
                    child: InkWell(
                      child: ListTile(
                        title: Text("${data?[index]}"),
                      ),
                      onTap: ()async{
                        await send("${data?[index]}");
                      },
                    ),
                  );
                });
              }
              else{
                return Text("data");
              }
            }),
          ),
          Expanded(
            child: Container(
              child: Column(
                children: [
                  TextField(
                    controller: controller,
                  ),
                  ElevatedButton(onPressed: ()async{
                      await send(controller.text);
                  }, child: Text("send"))
                ],
              ),
            ),
          )
        ],
      ),
      floatingActionButton: FloatingActionButton(onPressed: (){
        Navigator.push(context, MaterialPageRoute(builder: (context)=>BluetoothScan()));
      },child: Icon(Icons.refresh),),
    );
  }
}
