package org.example;

import java.io.IOException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main {
  public static void main(String[] args) {
    //String mqttBroker = "tcp://test.mosquitto.org:1883";
    String mqttBroker = "tcp://localhost:1883";
    String mqttTopic = "/test/topic";
    String username = "JavaClient";
    String password = "JavaPass";
    String testMsg = "Hi from the Java Application";
    int qos = 1;

    try {
      MqttClient mqttClient = new MqttClient(mqttBroker, mqttTopic);
      MqttConnectOptions connOpts = new MqttConnectOptions();
      //connOpts.setUserName(username);
     // connOpts.setPassword(password.toCharArray());
      mqttClient.connect(connOpts);

      if (mqttClient.isConnected()) {
        setupCallback(mqttClient);
        mqttClient.subscribe(mqttTopic, qos);
        mqttClient.publish(mqttTopic, new MqttMessage(testMsg.getBytes()));
      }

      System.out.println("MQTT client connected. Press any key to stop.");
      System.in.read();
      mqttClient.disconnect();
      mqttClient.close();

    } catch (MqttException e) {
      e.printStackTrace();
    } catch (IOException e) {
      throw new RuntimeException();
    }

  }

  private static void setupCallback(final MqttClient mqttClient) {
    mqttClient.setCallback(new MqttCallback() {
      @Override
      public void connectionLost(final Throwable throwable) {
        System.out.println("Connection Lost");
      }

      @Override
      public void messageArrived(final String s, final MqttMessage mqttMessage) throws Exception {
        System.out.println("Message Arrived: " + s + ": " + new String(mqttMessage.getPayload()));
      }

      @Override
      public void deliveryComplete(final IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Delivery Complete");
      }
    });
  }

}
