import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'AulaCare',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'AulaCare'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();

}

class _MyHomePageState extends State<MyHomePage> {

  Future<List<Aula>> _getAulas() async{

    var aulas = await http.get("http://192.168.1.42:8090/aulas/");
    var jsonAula = json.decode(aulas.body);

    List<Aula> listaAulas = [];

    for(var a in jsonAula){
    Aula aula = Aula(a["id_aula"], a["nombre"], a["peticion"]);

    var medicion = await http.get("http://192.168.1.42:8090/medicion/actual/${aula.id_aula.toString()}");
    var jsonMedicion = json.decode(medicion.body);

        aula.temperatura = jsonMedicion["temperatura"];
        aula.humedad = jsonMedicion["humedad"];
        aula.heatindex = jsonMedicion["heatindex"];
        aula.ruido = jsonMedicion["ruido"];
        aula.fecha = jsonMedicion["fecha"];

        listaAulas.add(aula);
    }

    return listaAulas;

  }

  void enviaPeticion(int id_aula){

    var jsonBody = jsonEncode({"id_aula" : id_aula, "peticion": true});
    
    print(jsonBody);

    http.post("http://192.168.1.42:8090/aula/edita/${id_aula}", headers: {"Content-Type": "application/json"}, body: jsonBody);

  }

  String stringSubtitle(Aula a){

    var res = "";

    var ruido = "Bueno";
    if(a.ruido != 0.0){
      ruido = "Malo";
    }

    var date = new DateTime.fromMillisecondsSinceEpoch(a.fecha * 1000);
    var fechaLocal = date.toLocal();

    res = "T: ${a.temperatura.toStringAsFixed(2)}ºC H: ${a.humedad}% \nHI: ${a.heatindex.toStringAsFixed(2)}ºC R: ${ruido} \nFecha: ${fechaLocal.hour}:${fechaLocal.minute} del día ${fechaLocal.day}/${fechaLocal.month}/${fechaLocal.year}";

    return res;
  }

  String colorMedicion(Aula a){
    String res = "Verde";

    if(((a.temperatura < 18) && (a.temperatura > 14)) || ((a.heatindex < 18) && (a.heatindex > 14)) ){
      res = "Amarillo";
    }else if(((a.temperatura > 25) && (a.temperatura < 27)) || ((a.heatindex > 25) && (a.heatindex < 27))){
      res = "Amarillo";
    }else if(a.temperatura <= 14 || a.heatindex <= 14 ){
      res = "Rojo";
    }else if(a.temperatura >= 27 || a.heatindex >= 27){
      res = "Rojo";
    }

    if(a.ruido != 0.0){
      res = "Rojo";
    }

    return res;

  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
    home: DefaultTabController(
      length: 2,
      child: Scaffold(
        appBar: AppBar(
          bottom: TabBar(
            tabs: [
              Tab(text: "Estado de aulas"),
              Tab(text: "Información"),
            ],
          ),
          title: Text('AulaCare'),
        ),
        body: TabBarView(
          children: [
            FutureBuilder(

            future: _getAulas(),
            builder: (BuildContext context, AsyncSnapshot snapshot){
              if(snapshot.data == null){
                  return Container(
                    child: Center(
                      child: Text("Cargando...")
                    )
                  );
              }else{
                  return ListView.builder(
                      itemCount: snapshot.data.length,
                      itemBuilder: (BuildContext context, int index){
                        if (colorMedicion(snapshot.data[index]) == "Verde"){
                          return ListTile(
                            leading: const SizedBox(
                              width: 42.0,
                              height: 42.0,
                              child: const DecoratedBox(
                                decoration: const BoxDecoration(
                                    color: Colors.green
                                ),
                              ),
                            ),
                            title: Text(snapshot.data[index].nombre),
                            subtitle: Text(stringSubtitle(snapshot.data[index])),
                            trailing: IconButton(
                              icon:Icon(Icons.send),
                              onPressed: () => {
                                enviaPeticion(snapshot.data[index].id_aula),

                                showDialog(
                                  context: context,
                                  builder: (BuildContext context) {
                                    return AlertDialog(
                                      title: Text("Petición enviada"),
                                      content: Text("Petición enviada para el aula ${snapshot.data[index].nombre}"),
                                      actions: [
                                        FlatButton(
                                          child: Text("OK"),
                                          onPressed: () {
                                            Navigator.of(context, rootNavigator: true).pop('dialog');
                                          },
                                        ),
                                      ],
                                    );;
                                  },
                                )
                              },
                            ),
                          );
                        } else  if (colorMedicion(snapshot.data[index]) == "Amarillo"){
                          return ListTile(
                            leading: const SizedBox(
                              width: 42.0,
                              height: 42.0,
                              child: const DecoratedBox(
                                decoration: const BoxDecoration(
                                    color: Colors.yellow
                                ),
                              ),
                            ),
                            title: Text(snapshot.data[index].nombre),
                            subtitle: Text(stringSubtitle(snapshot.data[index])),
                            trailing: IconButton(
                              icon:Icon(Icons.send),
                              onPressed: () => {
                                enviaPeticion(snapshot.data[index].id_aula),

                                showDialog(
                                  context: context,
                                  builder: (BuildContext context) {
                                    return AlertDialog(
                                      title: Text("Petición enviada"),
                                      content: Text("Petición enviada para el aula ${snapshot.data[index].nombre}"),
                                      actions: [
                                        FlatButton(
                                          child: Text("OK"),
                                          onPressed: () {
                                            Navigator.of(context, rootNavigator: true).pop('dialog');
                                          },
                                        ),
                                      ],
                                    );;
                                  },
                                )
                              },
                            ),
                          );
                        } else{
                          return ListTile(
                            leading: const SizedBox(
                              width: 42.0,
                              height: 42.0,
                              child: const DecoratedBox(
                                decoration: const BoxDecoration(
                                    color: Colors.red
                                ),
                              ),
                            ),
                            title: Text(snapshot.data[index].nombre),
                            subtitle: Text(stringSubtitle(snapshot.data[index])),
                            trailing: IconButton(
                              icon:Icon(Icons.send),
                              onPressed: () => {
                                enviaPeticion(snapshot.data[index].id_aula),

                                  showDialog(
                                    context: context,
                                    builder: (BuildContext context) {
                                    return AlertDialog(
                                      title: Text("Petición enviada"),
                                      content: Text("Petición enviada para el aula ${snapshot.data[index].nombre}"),
                                      actions: [
                                    FlatButton(
                                    child: Text("OK"),
                                      onPressed: () {
                                        Navigator.of(context, rootNavigator: true).pop('dialog');
                                      },
                                      ),
                                      ],
                                    );;
                                  },
                                )
                              },
                            ),
                          );
                        }
                      }
                  );
              }
            }

            ),
            Container(
                padding: EdgeInsets.all(25.0),
                child: Column(
                  children: [

                    Text("¿Qué es AulaCare?", style: Theme.of(context)
                        .textTheme
                        .headline5
                        .copyWith(color: Colors.blueAccent)),
                    Padding(
                        padding: EdgeInsets.all(8.0)
                    ),
                    Text("AulaCare es un servicio de monitorización de aulas con el objetivo de informar al alumnado y al profesado si las condiciones de un aula son buenas para ejercer la docencia o estudiar."),
                    Padding(
                        padding: EdgeInsets.all(8.0)
                    ),
                    Text("¿Qué medidas se tienen en cuenta?", style: Theme.of(context)
                        .textTheme
                        .headline5
                        .copyWith(color: Colors.blueAccent)),
                    Padding(
                        padding: EdgeInsets.all(8.0)
                    ),
                    Text("Para decidir cómo de buena o malas son las conficiones se hacen las siguientes mediciones: temperatura, humedad, índice de bochorno, ruido. El índice de bochorno es una relación entre la temperatura y la humedad ambiente."),
                    Padding(
                        padding: EdgeInsets.all(8.0)
                    ),
                    Text("¿Cómo puedo saber si las condiciones son buenas?", style: Theme.of(context)
                        .textTheme
                        .headline5
                        .copyWith(color: Colors.blueAccent)),
                    Padding(
                        padding: EdgeInsets.all(8.0)
                    ),
                    Text("En la página principal aparecen todas las mediciones de las aulas, acompañadas de un código de color: si es verde las condiciones son óptimas, si es amarillo sobrepasan los límites recomendados pero por poco y si es rojo es que los valores están lejos de los ideales."),
                  ]
                ),




            )],
        ),
      ),
    ),
    );
  }
}

class Aula{
  int id_aula;
  String nombre;
  bool peticion;
  double temperatura;
  double humedad;
  int fecha;
  double heatindex;
  double ruido;


  Aula(this.id_aula, this.nombre, this.peticion);

  String toString(){
    return "[${this.id_aula.toString()}], [${this.nombre}], [${this.peticion.toString()}], [${this.temperatura.toString()}]"
        ", [${this.humedad.toString()}], [${this.heatindex.toString()}], [${this.ruido.toString()}]"
        ", [${this.fecha.toString()}]]";
  }

}
