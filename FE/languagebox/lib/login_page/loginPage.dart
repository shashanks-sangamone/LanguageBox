import 'dart:convert';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
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

  User? _user;

  @override
  void initState() {
    super.initState();
    checkLogin();
  }

  signIn()async{
    final usercredensial = await GoogleAuth().signInWithGoogle();
    SharedPreferences preferences = await SharedPreferences.getInstance();
    setState(() {
      _user=usercredensial?.user;
    });

    Map<dynamic,dynamic> map1 = {
      "name":_user!.displayName,
      "email":_user!.email,
      "profile":_user!.photoURL,
    };
    String jsonString = jsonEncode(map1);
    await preferences.setString("google", jsonString);
    // print(await usercredensial!.user);
    if(usercredensial!=null){
      Navigator.push(context, MaterialPageRoute(builder: (context)=>MyHomePage()));
    }
    return usercredensial;
  }

  checkLogin()async{
    GoogleSignIn auth = GoogleSignIn();
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();

    setState(() {
      print(sharedPreferences.getString("google"));
    });
    if(await auth.isSignedIn()){
      Navigator.push(context, MaterialPageRoute(builder: (context)=>MyHomePage()));
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
            InkWell(
                child: CircleAvatar(backgroundImage: AssetImage("assets/google.png")),
              onTap: ()async{
                var data = await signIn();

              },
            ),
            InkWell(
              child: CircleAvatar(backgroundImage: AssetImage("assets/google.png")),
              onTap: ()async{
                await GoogleAuth().signOut();
              },
            ),
            Text("${_user?.photoURL},${_user?.email},${_user?.displayName}")
          ],
        ),
      ),
    );
  }
}
