import 'dart:convert';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:languagebox/api/request_handler.dart';
import 'package:languagebox/login_page/google_auth.dart';
import 'package:languagebox/main.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LoginPage extends StatelessWidget {
  const LoginPage({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: LoginHomePage(),
    );
  }
}

class LoginHomePage extends StatefulWidget {
  const LoginHomePage({super.key});

  @override
  State<LoginHomePage> createState() => _LoginHomePageState();
}

class _LoginHomePageState extends State<LoginHomePage> {

  @override
  void initState() {
    super.initState();
    checkForLogin();
  }

  UserCredential? userCredential;
  RequestHandler requestHandler = RequestHandler();

  checkForLogin()async{
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    if (sharedPreferences.containsKey("google") && sharedPreferences.containsKey("user")){
      var google = jsonDecode(sharedPreferences.getString("google").toString());
      var user = jsonDecode(sharedPreferences.getString("user").toString());
      var data = await requestHandler.getUsersByEmail(google["email"].toString());
      await sharedPreferences.setString("user", jsonEncode(data));
      Navigator.pushReplacement(context, MaterialPageRoute(builder: (context)=>MyApp()));
      }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text("Login Using google"),
            userCredential==null?InkWell(
                child: CircleAvatar(backgroundImage: AssetImage("assets/google.png"),radius: 50,),
              onTap: ()async{
                SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
                userCredential = await GoogleAuth().signIn();
                await Future.delayed(Duration(seconds: 2));
                setState((){
                  print(userCredential);
                });
                var value = jsonDecode(sharedPreferences.getString("google").toString());
                var data = await requestHandler.getUsersByEmail(value["email"].toString());
                await sharedPreferences.setString("user", jsonEncode(data));
                Navigator.pushReplacement(context, MaterialPageRoute(builder: (context)=>MyApp()));
              },
            ):Container(),
            userCredential==null?Container():Column(
              children: [
                CircleAvatar(
                  backgroundImage: NetworkImage(userCredential!.user!.photoURL.toString(),scale: 1,),
                  radius: 50,
                ),
                Text("${userCredential?.user?.email}\n${userCredential?.user?.displayName}")
              ],
            ),
            InkWell(
              child: Text("Log Out"),
              onTap: ()async{
                setState(() {
                  userCredential==null;
                });
                await GoogleAuth().signOut();
              },
            ),
          ],
        ),
      ),
    );
  }
}
