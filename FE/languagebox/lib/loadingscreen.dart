import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:languagebox/login_page/google_auth.dart';
import 'package:languagebox/login_page/loginPage.dart';
import 'package:languagebox/main.dart';

class LoadingScreen extends StatelessWidget {
  const LoadingScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: FutureBuilder(
        future: Future.delayed(Duration(seconds: 3)),
        builder: (context, asyncSnapshot) {
          if(asyncSnapshot.connectionState==ConnectionState.waiting){
            return Center(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircleAvatar(
                    backgroundImage: AssetImage("assets/logo.png"),
                    radius: 80,
                  ),
                  CircularProgressIndicator(),
                  Text("Loading")
                ],
              ),
            );
          }
          else if(asyncSnapshot.hasData){

            return Container();
          }
          else{
            Future.microtask((){
            Navigator.
              pushReplacement(context, MaterialPageRoute(builder: (context)=>LoadingHomeScreen()));
            });
            return Container();
          }
        }
      ),
    );
  }
}

class LoadingHomeScreen extends StatefulWidget {
  const LoadingHomeScreen({super.key});

  @override
  State<LoadingHomeScreen> createState() => _LoadingHomeScreenState();
}

class _LoadingHomeScreenState extends State<LoadingHomeScreen> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: FutureBuilder(future: GoogleAuth().checkLogin(), builder: (BuildContext context, AsyncSnapshot<dynamic> snapshot) {
        if (snapshot.connectionState==ConnectionState.waiting){
          return LoadingScreen();
        }
        else if(snapshot.hasData){
          Future.delayed(Duration(seconds: 5));
          Map<dynamic,dynamic> data = snapshot.data;
          return MyHomePage(id: data["id"]);
          }
        else if(snapshot.hasError){
          Future.delayed(Duration(seconds: 5));
          return LoginPage();
        }
        else{
          return Text("data");
        }
      }),
    );
  }
}

