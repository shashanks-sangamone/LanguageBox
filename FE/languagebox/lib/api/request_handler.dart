import 'dart:convert';

import 'package:http/http.dart' as http;

class RequestHandler{

  String  baseUrl = "http://45.79.126.196:5000/";

  String  companiesAdd = "companies/add";
  String  companiesDeleteById = "companies/delete/{id}";
  String  companiesGetById = "companies/get/{id}";
  String  companiesGetAll = "companies/getAll";
  String  companiesUpdateCompanyName = "companies/updateCompanyName/{companyName}/{id}";
  String  companiesUpdateCountryId = "companies/updateCountryId/{countryId}/{id}";

  String  devicesAdd = "devices/add";
  String  devicesGetById = "devices/get/{id}";
  String  devicesGetAll = "devices/getAll";
  String  devicesUpdate = "devices/update/{deviceId}";

  String  plansAdd = "plans/add";
  String  plansGetById = "plans/get/{id}";
  String  plansGetAll = "plans/getAll";

// Service: subscription
  String  subscriptionAdd = "subscription/add";
  String  subscriptionGetById = "subscription/get/{id}";
  String  subscriptionGetAll = "subscription/getAll";

  String  userlistsAdd = "userlists/add";
  String  userlistsGetById = "userlists/get/id";
  String  userlistsGetAll = "userlists/getAll";
  String  userlistsReadByFilename = "userlists/read/filename";

  String  usersAdd = "users/add";
  String  usersGetAll = "users/getAll";
  String  usersGetByEmail = "users/getByEmail/{email}";
  String  usersGetById = "users/getById/{id}";

  String  wordsGetByLenAndAlpha = "words/get/{len}/{alpha}";
  String  wordsGetAll = "words/getAll";
  String  wordsGetByAlpha = "words/getByAlpha/{alpha}";
  String  wordsGetByLen = "words/getByLen/{len}";


  getRequest(String endpoint) async {
    String finalEndpoint = endpoint;


    final uri = Uri.parse('$baseUrl$finalEndpoint');
    final response = await http.get(
      uri,
      headers: {'Accept': 'application/json'},
    );

    if (response.statusCode == 200) {
      return response.body;
    } else {
      throw Exception(
          'Failed to load data from $finalEndpoint: ${response.body}');
    }
  }

  postRequest(String endpoint, Map<String, dynamic> body) async {
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

}