import 'dart:io';

import 'package:flutter/material.dart';

import '../api/request_handler.dart';

class CreateList extends StatelessWidget {
  const CreateList({super.key,required this.id});

  final int id;
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: CreateListHome(id: id,),
    );
  }
}


class CreateListHome extends StatefulWidget {
  const CreateListHome({super.key,required this.id,this.type});

  final int id;
  final String? type;
  @override
  State<CreateListHome> createState() => _CreateListHomeState();
}

class _CreateListHomeState extends State<CreateListHome> {
  static List<dynamic> listAlphabets = List<String>.generate(26, (index) =>String.fromCharCode('a'.codeUnitAt(0)+index),);
  static List<dynamic> listNumbers = List<int>.generate(20,(index)=>index+2);
  String drp1 = listAlphabets.first;
  int drp2 = listNumbers.first;
  RequestHandler requestHandler = RequestHandler();
  List<dynamic> data1 = [];
  TextEditingController search = TextEditingController();
  TextEditingController listName = TextEditingController();
  late Map<dynamic,dynamic> data;

  @override
  void initState() {
    super.initState();
    if(widget.type!=null){
      Future.microtask(()async{
        data = await requestHandler.getUserListById(widget.id);
        print(data);
        List<dynamic> data2=await requestHandler.getUserListByFilename(data["userId"]["id"], data["filename"]);
        setState(() {
          print(data2[0].split(","));
          data1=data2[0].split(",");
          listName.text=data["listName"];
        });
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          // title: widget.type==null?Text("Add Lists"):Text("Update Lists"),
          title: Padding(
            padding: const EdgeInsets.all(18.0),
            child: TextField(
              controller: listName,
              decoration: InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: "Enter name of the list"
              ),
            ),
          ),
        ),
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Container(
              alignment: Alignment.center,
              height: MediaQuery.of(context).size.height/8,
              child: SingleChildScrollView(
                controller: ScrollController(
                ),
                child: Column(
                  children: [
                    Wrap(alignment: WrapAlignment.center,children: data1.map((e) =>
                        Card(clipBehavior: Clip.antiAlias,elevation: 20,margin: EdgeInsets.all(4),child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Text(e),
                              InkWell(child: Icon(Icons.cancel_outlined),onTap: (){
                                setState(() {
                                  data1.remove(e);
                                });
                              },)
                            ],
                          ),
                        )),).toList(),),
                  ],
                ),
              ),
            ),
            // Row(
            //   crossAxisAlignment: CrossAxisAlignment.center,
            //   mainAxisAlignment: MainAxisAlignment.spaceAround,
            //   children: [
            //     DropdownButton(
            //         value: drp1,
            //         items: listAlphabets.map<DropdownMenuItem<dynamic>>((dynamic value){
            //           return DropdownMenuItem(
            //             value: value,
            //             child: Text("$value"),
            //           );
            //         }).toList(),
            //         onChanged: (dynamic value){
            //           setState(() {
            //             drp1=value;
            //           });
            //         }),
            //     DropdownButton(
            //         value: drp2,
            //         items: listNumbers.map<DropdownMenuItem<int>>((dynamic value){
            //           return DropdownMenuItem(
            //             value: value,
            //             child: Text("$value"),
            //           );
            //         }).toList(),
            //         onChanged: (int? value){
            //           setState(() {
            //             drp2=value!;
            //           });
            //         }),
            //   ],
            // ),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: SearchBar(
                controller: search,
                leading: Icon(Icons.search),
                hintText: "Add words to this list",
                trailing: [Padding(
                  padding: const EdgeInsets.only(left: 8),
                  child: ElevatedButton(style: ElevatedButton.styleFrom(backgroundColor: Colors.blue),child: Text("Add",style: TextStyle(color: Colors.white),),onPressed: (){
                    setState(() {
                      if(!data1.contains(search.text)){
                        data1.add(search.text);
                        search.clear();
                      }
                    });
                  },),
                ),Container()],
                onChanged: (value){
                  setState(() {
                    search.text;
                  });
                },
              ),
            ),
            Expanded(
              child: Container(
                alignment: Alignment.topCenter,
                child: FutureBuilder(future: requestHandler.getWordAll(), builder: (context,AsyncSnapshot<dynamic> snapshot){
                  if(snapshot.connectionState==ConnectionState.waiting){
                    return Center(child: CircularProgressIndicator());
                  }
                  else if(snapshot.hasError){
                    return Center(child: Text("${snapshot.error}"),);
                  }
                  else if (snapshot.hasData){
                    List<dynamic> data2 = snapshot.data;
                    var data = data2.where((value)=> value.toString().contains(search.text)).toList();
                    return Flexible(
                      child: ListView.builder(shrinkWrap: true,itemCount: data.length,itemBuilder: (context,index){
                        return InkWell(
                          child: Text("${data[index]}"),
                          onTap: ()async{
                            setState(() {
                              if(!data1.contains(data[index])){
                                data1.add(data[index]);
                              }
                            });
                          },
                        );
                      }),
                    );
                  }
                  else{
                    return Text("No data");
                  }
                }),
              ),
            ),
            ElevatedButton(onPressed: ()async{
              if(listName.text.isNotEmpty){
                File file = await requestHandler.fileCreate(listName.text, data1.toString().replaceAll("[", "").replaceAll("]", ""));
                // print(file.readAsString());
                var data2 = widget.type==null?await requestHandler.addUserList(widget.id,listName.text,listName.text,file):await requestHandler.updateUserList(widget.id,file);
                // var data3 = await requestHandler.updateListNameById(widget.id,listName.text);
                // print(data3);
                // print(data);
                setState(() {

                });
                ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text("Successful"))
                );
                Navigator.pop(context);
              }
              else{
                ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text("Please Enter the List Name above"))
                );
              }
            }, child: widget.type==null?Text("Create List",style: TextStyle(color: Colors.white),):Text("Update List",style:  TextStyle(color: Colors.white)),style: ElevatedButton.styleFrom(
                elevation: 12,
                backgroundColor: Colors.black,
                shape: ContinuousRectangleBorder(borderRadius: BorderRadius.circular(12))
            ),),
          ],
        )
    );
  }
}
