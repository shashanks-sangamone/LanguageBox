import 'package:flutter/material.dart';
import 'package:languagebox/main.dart';

class LoadingScreen extends StatelessWidget {
  const LoadingScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            CircleAvatar(
              backgroundImage: AssetImage("assets/google.png"),
            ),
            CircularProgressIndicator(),
            Text("Loading")
          ],
        ),
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
      home: FutureBuilder(future: Future.delayed(Duration(seconds: 4)), builder: (BuildContext context, AsyncSnapshot<dynamic> snapshot) {
        if (snapshot.connectionState!=ConnectionState.waiting){
          return MyApp();
        }
        else{
          return LoadingScreen();
        }
      }),
    );
  }
}

