#include <WiFi.h>
#include <HTTPClient.h>

#define INTERNAL_LED 2

const char* SSID = "Benzekri_EXT";
const char* PASSWORD = "315262329";

const char* serverGet = "http://192.168.14.101:5000/led/checkState";

unsigned long lastReconnectAttempt = 0;
const unsigned long connectionTimeOut = 10000;

void connectToWiFi() {
  Serial.print("Connecting to WiFi");
  WiFi.begin(SSID, PASSWORD);
  unsigned long startAttemptTime = millis();

  while (WiFi.status() != WL_CONNECTED && millis() - startAttemptTime < connectionTimeOut) {
    Serial.print(".");
    delay(500);
  }

  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("\nConnected to WiFi!");
  } else {
    Serial.println("\nFailed to connect.");
  }
}


void setup(){
  pinMode(INTERNAL_LED, OUTPUT); 
  Serial.begin(115200);
  connectToWiFi();
}

void loop() {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println(" WiFi Disconnected");
    
    if (millis() - lastReconnectAttempt > connectionTimeOut) {
      lastReconnectAttempt = millis();
      Serial.println("Trying to reconnect...");
      connectToWiFi();
    }
    delay(1000);
    return;
  }

  HTTPClient client;
  client.begin(serverGet);
  int httpCode = client.GET();

  if (httpCode > 0) {
    String data = client.getString();
    // Serial.println("GET response: " + data);
    Serial.print("Raw data: [");
    Serial.print(data);
    Serial.println("]");


    int ledStatus = data.toInt();
    Serial.print("Parsed status: ");
    Serial.println(ledStatus);
    
    digitalWrite(INTERNAL_LED , ledStatus);

  } 
  else {
    Serial.println("GET request failed");
  }
  client.end();
  delay(400);
  

}
