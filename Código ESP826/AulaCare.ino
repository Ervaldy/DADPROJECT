#include <LiquidCrystal_I2C.h>

#include "DHT.h"

#define DHTPIN 2
#define DHTTYPE DHT11
#include <math.h>

#include <Arduino.h>
#include "ArduinoJson.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <SoftwareSerial.h>

DHT dht(DHTPIN, DHTTYPE);

// Inicializa la pantalla
LiquidCrystal_I2C lcd(0x27, 16, 2);


int id_sensor = 1;     // Variable global para el identificador del sensor
int id_aula = 2;       // Variable global para el identificador del aula

int MICA = A0;         // Pin de lectura del valor análogico del KY-038
int MIC = 14;          // Pin de lectura del valor digital del KY-038
int VALOR;             // Variable para almacenar valor leído por A0
int VALORA;            // Variable para almacenar valor de D0
int cont;

char responseBuffer[300];
WiFiClient client;

String SSID = "DAD";
String PASS = "ProyectoDAD";

String SERVER_IP = "192.168.43.203";
int SERVER_PORT = 8090;

void setup() {
  Serial.begin(9600);
  Serial.println(("¡Proyecto DAD!"));

  pinMode(MIC, INPUT);      // pin A0 como entrada
  pinMode(MICA, INPUT);     // pin D0 como entrada

  //Inicializa el sensor DHT-22
  dht.begin();
  
  //Inicializa el lcd
  lcd.begin();
  lcd.backlight();

  lcd.setCursor(0, 0);
  lcd.print("Iniciando el");
  lcd.setCursor(0, 1);      
  lcd.print("sistema...");

  //Conexión con el WiFi
  Serial.print("Conectando...");
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.print("Conectado, dirección IP: ");
  Serial.print(WiFi.localIP());

  // Lectura inicial
  lectura();

  // Contador de tiempos de espera
  cont = 0;
}

void loop() {

  //Cada cinco minutos hace una lectura
  if(cont >= 10){
    lectura();
    cont = 0;
  }
  //Cada treinta segundos comprueba peticiones de lecturas adicionales
  else{
    if(checkPetition()){
        lectura();
        sendLeido();
    }
    cont++;
  }
  Serial.println(cont);
  delay(30000);
}


// Función que cambia el cambio petición de las Aulas a false tras haber hecho una medición extra.
void sendLeido(){
  
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/aula/edita/2", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(10) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);
    doc["id_aula"] = 2;
    doc["peticion"] = false;

    String output;
    serializeJson(doc, output);
    int httpCode = http.POST(output);
    String payload = http.getString();
  
  }
  
}

// Comprueba si hay mediciones extras a realizar.
bool checkPetition(){
  
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/aula/" + (String) id_aula, true);
  
    int httpCode = http.GET();
    String payload = http.getString();
    
    const size_t capacity = JSON_OBJECT_SIZE(10) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc, payload);

    if(error){
      Serial.print("deserializeJson() falló:");
      Serial.print(error.c_str());
    }
    
    return doc["peticion"].as<bool>();
    
  }
}

// Envía una medición al servidor
void sendPostRequest(double ruido, double humedad, double temperatura, double hic){

  // Envío de medición
  if (WiFi.status() == WL_CONNECTED){
    
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/medicion", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(10) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);
    doc["id_sensor"] = id_sensor;
    doc["id_aula"] = id_aula;
    doc["ruido"] = ruido;
    doc["co2"] = 0.0;
    doc["humedad"] = humedad;
    doc["temperatura"] = temperatura;
    doc["fecha"] = 0;
    doc["heatindex"] = hic;
    
    String output;
    serializeJson(doc, output);

    int httpCode = http.PUT(output);
  
  }
  
}

//  Envía una medición e imprime por el LCD mensaje de advertencia o los valores correctos.
void lectura(){
  
    //Lectura de la humedad
    float h = dht.readHumidity();
    //Lectura de la temperatura
    float t = dht.readTemperature();
  
    // Comprueba si ha fallado la lectura.
    if (isnan(h) || isnan(t)) {
      Serial.println(F("¡Lectura fallida del DHT-22!"));
      return;
    }

    // Calcula el índice de bochorno
    float hic = dht.computeHeatIndex(t, h, false);
  
    Serial.print(F("Humidity: "));
    Serial.print(h);
    Serial.print(F("%  Temperature: "));
    Serial.print(t);
    Serial.print(F("°C "));
    Serial.print(F("°F  Heat index: "));
    Serial.print(hic);
    Serial.print(F("°C "));
    
    VALORA = analogRead(MICA);   // obtiene valor de A0
    VALOR = digitalRead(MIC);   // obtiene valor de D0

    //Leemos la calidad del ruido
    String calidadRuido = "Buena";
    int valorRuido = 0;


    // Se hacen varias lecturas del ruido para evitar falsos positivos de valores correctos
    int mruido = 0;
    while(mruido < 50){
      VALOR = digitalRead(MIC);
      if(VALOR == 1){
          calidadRuido = "Mala";
          valorRuido = 1;
          delay(100);
      }
      mruido++;
      delay(100);
    }
    
    
    Serial.println(VALORA);
    
    Serial.println(calidadRuido);


    //Limpiamos la pantalla e imprimimos mensajes de advertencia o los valores leídos.
    lcd.clear();
    if((t < 18) || (t  > 25)){
      lcd.setCursor(0, 0);
      lcd.print("Temperatura no");
      lcd.setCursor(0, 1);
      lcd.print("adecuada.");
    }else if(hic > 25){
      lcd.setCursor(0, 0);
      lcd.print("Valor de HI");
      lcd.setCursor(0, 1);
      lcd.print("no adecuado.");
    }else if(valorRuido == 1){
      lcd.setCursor(0, 0);
      lcd.print("Ruido");
      lcd.setCursor(0, 1);
      lcd.print("elevado.");
    }
    else{
      String stringtemperatura = "T:" + (String)(int)round(t) + (char) 223 + "C H:" + (String)(int)round(h) + "%";
      String stringhiruido = "HI:" + (String)(int)round(hic) + (char) 223 + "C R:" + calidadRuido;

      lcd.setCursor(0, 0);
      lcd.print(stringtemperatura);
      lcd.setCursor(0, 1);
      lcd.print(stringhiruido);
      
    }

  //Se envía al servidor el valor leído.
  sendPostRequest(valorRuido, h, t, hic);
}
