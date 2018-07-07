import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:geolocation/geolocation.dart';
import 'package:http/http.dart' as http;
import 'package:xml2json/xml2json.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Weather Lab',
      theme: new ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or press Run > Flutter Hot Reload in IntelliJ). Notice that the
        // counter didn't reset back to zero; the application is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: new MyHomePage(title: 'Flutter Weather App'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => new _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String _status = "Push button to request weather for current location";
  String _imageUri = "";

  _requestWeather(Location location) async {
    String latitude = location.latitude.toStringAsFixed(2);
    String longitude = location.longitude.toStringAsFixed(2);
    String dataURL = "https://api.met.no/weatherapi/locationforecast/1.9?lat=$latitude&lon=$longitude";
    http.Response response = await http.get(dataURL);
    final Xml2Json transformer = new Xml2Json();
    transformer.parse(response.body);
    String jsn = transformer.toGData();
    Map data = json.decode(jsn);
    List times = data['weatherdata']['product']['time'];
    // Find first temperature and symbol.
    Map temperature;
    Map symbol;
    for (Map time in times) {
      Map mapLocation = time['location'];
      if (temperature == null && mapLocation.containsKey("temperature")) {
        temperature = mapLocation['temperature'];
      }
      if (symbol == null && mapLocation.containsKey('symbol')) {
        symbol = mapLocation['symbol'];
      }
      if (symbol != null && temperature != null) {
        break;
      }
    }

    setState(() {
      _status = "${temperature['value']} ${temperature['unit']}";
      if (symbol != null) {
        _imageUri =
        "https://api.met.no/weatherapi/weathericon/1.1/?symbol=${symbol['number']}&content_type=image/png";
      }
    });
  }

  void _requestLocationAndWeather() {
    setState(() {
      _status = "Requesting location...";
    });
    Geolocation.currentLocation(accuracy: LocationAccuracy.best).listen((
        result) {
      if (result.isSuccessful) {
        setState(() {
          _status = "Requesting weather...";
        });
        _requestWeather(result.location);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return new Scaffold(
      appBar: new AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: new Text(widget.title),
      ),
      body: new Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: new Column(
          // Column is also layout widget. It takes a list of children and
          // arranges them vertically. By default, it sizes itself to fit its
          // children horizontally, and tries to be as tall as its parent.
          //
          // Invoke "debug paint" (press "p" in the console where you ran
          // "flutter run", or select "Toggle Debug Paint" from the Flutter tool
          // window in IntelliJ) to see the wireframe for each widget.
          //
          // Column has various properties to control how it sizes itself and
          // how it positions its children. Here we use mainAxisAlignment to
          // center the children vertically; the main axis here is the vertical
          // axis because Columns are vertical (the cross axis would be
          // horizontal).
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Text(
              '$_status',
            ),
            Image.network(_imageUri)
          ],
        ),
      ),
      floatingActionButton: new FloatingActionButton(
        onPressed: _requestLocationAndWeather,
        tooltip: 'Request weather',
        child: new Icon(Icons.wb_sunny),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
