
//LCD
#include <Arduino.h>
#include <SPI.h>
#include <Adafruit_GFX.h>
#include <Adafruit_PCD8544.h>

#include "test.c"

//ESP
#include <ESP8266WiFi.h>

#include <DNSServer.h>
#include <ESP8266WebServer.h>
#include "WiFiManager.h"

#include <FirebaseArduino.h>
#define FIREBASE_HOST "smartdoor-5a13c.firebaseio.com"
#define FIREBASE_AUTH "nNJDxaoyuRKmqI1J94AeEabXS0fZoMEsALVLKXDC"
#define SSID_NAME "teamVDK"
#define PASSWORD "12345678"

//SERVO
#include <Servo.h>

//LCD
const int8_t RST_PIN = D2;
const int8_t CE_PIN = D1;
const int8_t DC_PIN = D6;

Adafruit_PCD8544 display = Adafruit_PCD8544(DC_PIN, CE_PIN, RST_PIN);


//SERVO
Servo gServo;

//ESP
String id = "aRziBsWTF3d5mSExaYpcEwDvXFM2";
void configModeCallback (WiFiManager *myWiFiManager)
{
  digitalWrite(D3,HIGH);
  Serial.println("Entered config mode");
  Serial.println(WiFi.softAPIP());
  Serial.println(myWiFiManager->getConfigPortalSSID());
}


void setup() {
  Serial.begin(115200);
  lcd5110();
  esp8266();
  led();
  gServo.attach(D8);
  gServo.write(0);
}

void loop() {
    delay(500);
    String path = "Houses/" + id;
    FirebaseObject  housesObj = Firebase.get(path);
    if (Firebase.failed()) {
      Serial.print("Khong the lay du lieu!");
      Serial.println(Firebase.error());
      return; 
    }
    String current = housesObj.getString("current_check");  
    Serial.print(current);
    if( current == "true"){
      gServo.write(0); 
      digitalWrite(D3,LOW);
      digitalWrite(D4,HIGH);    
    }else{
      gServo.write(90); 
      digitalWrite(D3,HIGH);
      digitalWrite(D4,LOW); 
    }  
    delay(500);
  
}

void lcd5110(){

  display.begin();
  display.setContrast(60);
  delay(1000);
  display.clearDisplay();
  display.drawBitmap(20, 0, qrcode, 48, 48, 1);
  display.display();  

}

void esp8266(){

  WiFiManager wifiManager;
  wifiManager.setAPCallback(configModeCallback);
  if (!wifiManager.autoConnect(SSID_NAME, PASSWORD))
  {
    Serial.println("failed to connect and hit timeout");
    ESP.reset();
    delay(1000);
  }
  Serial.println("connected...yeey :)");
  firebase_ketnoi();
 }

void firebase_ketnoi(){
    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
    if (Firebase.failed()) {
        Serial.print("Khong the ket noi!");
        Serial.println(Firebase.error());
        return;
    }
}


void led(){

  pinMode(D3, OUTPUT); //Đèn đỏ
  pinMode(D4, OUTPUT); //Đèn xanh
  
}
 

