import 'dart:convert';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:shared_preferences/shared_preferences.dart';

class GoogleAuth{
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final GoogleSignIn _googleSignIn = GoogleSignIn();


  Future<UserCredential?> signInWithGoogle() async {
    try {
      final GoogleSignInAccount? googleUser = await _googleSignIn.signIn();
      if (googleUser == null) return null;

      final GoogleSignInAuthentication googleAuth = await googleUser.authentication;

      final AuthCredential credential = GoogleAuthProvider.credential(
        accessToken: googleAuth.accessToken,
        idToken: googleAuth.idToken,
      );


      final UserCredential userCredential = await _auth.signInWithCredential(credential);
      return userCredential;

    } catch (e) {
      return null;
    }
  }

  Future<bool> signOut() async {
    SharedPreferences preferences = await SharedPreferences.getInstance();
    try {
      await _googleSignIn.signOut();
      await _auth.signOut();
      await preferences.remove("google");
      await preferences.remove("user");
      return true;
    } catch (e) {
      return false;
    }


  }
  signIn()async{
    final usercredensial = await GoogleAuth().signInWithGoogle();
    SharedPreferences preferences = await SharedPreferences.getInstance();
    User? _user = usercredensial?.user;
    Map<dynamic,dynamic> map1 = {
      "name":_user!.displayName,
      "email":_user.email,
      "profile":_user.photoURL,
    };
    String jsonString = jsonEncode(map1);
    await preferences.setString("google", jsonString);
    return usercredensial;
  }

  checkLogin() async {
    GoogleSignIn auth = GoogleSignIn();
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    if(await auth.isSignedIn()){
      var data = jsonDecode(sharedPreferences.getString("user").toString());
      return data;
    }
    else{
      return Future.error("Loged out");
    }
  }

  getLogin()async{
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance();
    return sharedPreferences.getString("google");
  }
}