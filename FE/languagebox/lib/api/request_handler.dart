import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart' as http;
import 'package:path_provider/path_provider.dart';

class RequestHandler{

  String baseUrl = "http://45.79.126.196:5000/";

  String companiesAdd = "companies/add";
  String companiesDeleteById = "companies/delete/{id}";
  String companiesGetById = "companies/get/{id}";
  String companiesGetAll = "companies/getAll";
  String companiesUpdateCompanyName = "companies/updateCompanyName/{companyName}/{id}";
  String companiesUpdateCountryId = "companies/updateCountryId/{countryId}/{id}";

  String devicesAdd = "devices/add";
  String devicesGetById = "devices/get/{id}";
  String devicesGetAll = "devices/getAll";
  String devicesUpdate = "devices/update/{deviceId}";

  String plansAdd = "plans/add";
  String plansGetById = "plans/get/{id}";
  String plansGetAll = "plans/getAll";

  String subscriptionAdd = "subscription/add";
  String subscriptionGetById = "subscription/get/{id}";
  String subscriptionGetAll = "subscription/getAll";

  String userlistsAdd = "userlists/add";
  String userlistsGetById = "userlists/get/";
  String userlistsGetByUserId = "userlists/getByUser/";
  String userlistsGetAll = "userlists/getAll";
  String userlistsReadByFilename = "userlists/read/";
  String userlistsUpdateByUserListById = "userlists/update/byUserList/1";
  String userListsUpdateListNameById = "userlists/update/listNameBy/";
  String userListsDeleteById = "userlists/delete/";

  String usersAdd = "users/add";
  String usersGetAll = "users/getAll";
  String usersGetByEmail = "users/getByEmail/";
  String usersGetById = "users/getById/";

  String wordsGetByLenAndAlpha = "words/get/";
  String wordsGetAll = "words/getAll";
  String wordsGetByAlpha = "words/getByAlpha/{alpha}";
  String wordsGetByLen = "words/getByLen/";


  getRequest(String endpoint) async {
    String finalEndpoint = endpoint;


    final uri = Uri.parse('$baseUrl$finalEndpoint');
    final response = await http.get(
      uri,
      headers: {'Accept': 'application/json'},
    );

    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else {
      throw Exception(
          'Failed to load data from $finalEndpoint: ${response.body}');
    }
  }

  postRequest(String endpoint, Map<String, dynamic>? body) async {
    final uri = Uri.parse('$baseUrl$endpoint');
    final response = await http.post(
      uri,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(body),
    );


    if (response.statusCode == 200 || response.statusCode == 201) {
      return response.body;
    } else {
      throw Exception('Failed to post data to $endpoint: ${response.body}');
    }
  }

  putRequest(String endpoint, Map<String, dynamic> body) async {
    final uri = Uri.parse('$baseUrl$endpoint');
    final response = await http.put(
      uri,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(body),
    );

    if (response.statusCode == 200) {
      return response.body;
    } else {
      throw Exception('Failed to put data to $endpoint: ${response.body}');
    }
  }

  deleteRequest(String endpoint, int id) async {
    final uri = Uri.parse('$baseUrl$endpoint');
    final response = await http.delete(
      uri,
      headers: {'Content-Type': 'application/json'}
    );

    if (response.statusCode == 200) {
      return response.body;
    } else {
      throw Exception('Failed to put data to $endpoint: ${response.body}');
    }
  }

  // wordsGetByLenAndAlpha = "words/get/{len}/{alpha}";
  // wordsGetAll = "words/getAll";
  // wordsGetByAlpha = "words/getByAlpha/{alpha}";
  // wordsGetByLen = "words/getByLen/{len}";

  getWordByLenAndAlpha(len,alpha)async{
    return await getRequest(wordsGetByLenAndAlpha+"$len/$alpha");
  }

  getWordAll()async{
    return await getRequest(wordsGetAll);
  }

  getWordByLen(len)async{
    return await getRequest(wordsGetByLen+"$len");
  }

  getWordByAlpha(alpha)async{
    return await getRequest(wordsGetByAlpha+"$alpha");
  }

  // userlistsAdd = "userlists/add";
  // userlistsGetById = "userlists/get/id";
  // userlistsGetAll = "userlists/getAll";
  // userlistsReadByFilename = "userlists/read/filename";

  getUserListAll()async{
    return await getRequest(userlistsGetAll);
  }

  getUserListById(id)async{
    return await getRequest(userlistsGetById+"$id");
  }

  getUserListByUserId(userId)async{
    return await getRequest(userlistsGetByUserId+"$userId");
  }

  addUserList(int userId, String listName, String filename, File file) async {

    final uri = Uri.parse("$baseUrl$userlistsAdd");
    var request = http.MultipartRequest('POST', uri);
    request.fields['userId'] = userId.toString();
    request.fields['listName'] = listName;
    request.fields['filename'] = filename;

    var multipartFile = await http.MultipartFile.fromPath(
      'file',
      file.path
    );
    request.files.add(multipartFile);

    try {
      var streamedResponse = await request.send();
      var response = await http.Response.fromStream(streamedResponse);

      if (response.statusCode == 200) {
        print("Upload successful: ${response.body}");
      } else {
        print("Upload failed with status: ${response.statusCode}");
        print("Reason: ${response.body}");
      }
    } catch (e) {
      print("Error uploading file: $e");
    }
  }

  updateUserList(int id, File file) async {

    final uri = Uri.parse("$baseUrl$userlistsUpdateByUserListById");
    var request = http.MultipartRequest('POST', uri);
    request.fields['id'] = id.toString();

    var multipartFile = await http.MultipartFile.fromPath(
        'file',
        file.path
    );
    request.files.add(multipartFile);

    try {
      var streamedResponse = await request.send();
      var response = await http.Response.fromStream(streamedResponse);

      if (response.statusCode == 200) {
        print("Upload successful: ${response.body}");
      } else {
        print("Upload failed with status: ${response.statusCode}");
        print("Reason: ${response.body}");
      }
    } catch (e) {
      print("Error uploading file: $e");
    }
  }

  updateListNameById(id,listName)async{
    return await postRequest(userListsUpdateListNameById+"$id"+"/$listName",null);
  }

  getUserListByFilename(id,filename)async{
    return await getRequest(userlistsReadByFilename+"$id"+"/$filename");
  }

  // companiesAdd = "companies/add";
  // companiesDeleteById = "companies/delete/{id}";
  // companiesGetById = "companies/get/{id}";
  // companiesGetAll = "companies/getAll";
  // companiesUpdateCompanyName = "companies/updateCompanyName/{companyName}/{id}";
  // companiesUpdateCountryId = "companies/updateCountryId/{countryId}/{id}";

  getCompaniesAll()async{
    return await getRequest(companiesGetAll);
  }

  getCompaniesById(id)async{
    return await getRequest(companiesGetById+"$id");
  }

  addCompanies(body)async{
    return await postRequest(companiesAdd, body);
  }

  // usersAdd = "users/add";
  // usersGetAll = "users/getAll";
  // usersGetByEmail = "users/getByEmail/{email}";
  // usersGetById = "users/getById/{id}";

  getUsersAll()async{
    return await getRequest(usersGetAll);
  }

  getUsersById(id)async{
    return await getRequest(usersGetById+"$id");
  }

  addUsers(body)async{
    return await postRequest(usersAdd, body);
  }

  getUsersByEmail(email)async{
    return await getRequest(usersGetByEmail+"$email");
  }

  Future<File> fileCreate(fileName,content)async{
    var filePath = await getApplicationDocumentsDirectory();

    File file = File("${filePath.path}/$fileName.txt");

    return await file.writeAsString(content);
  }

  deleteUserListById(id)async{
    return await deleteRequest("$userListsDeleteById$id",id);
  }

}