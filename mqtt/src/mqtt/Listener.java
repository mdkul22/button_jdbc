package mqtt;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.*;


public class Listener implements MqttCallback{
MqttClient client;


public Listener() {	
}

public static void main(String[] args)
{
	new Listener().doDemo();
}

public void doDemo() {
	try{
		client = new MqttClient("tcp://173.39.91.82:1883", "Sending");
		client.connect();
		client.setCallback(this);
		client.subscribe("tqb/topic");
		MqttMessage message = new MqttMessage();
		message.setPayload("A single message".getBytes());
		client.publish("tqb/setup", message);
	}
	catch(MqttException e){
		e.printStackTrace();
	}
}
@Override
public void connectionLost(Throwable cause){
System.out.println("Connection lost!");	
}

@Override 
public void messageArrived(String topic, MqttMessage message) 
		throws Exception {
	System.out.println(message);
	byte obj[] = message.getPayload();
	String s = new String(obj);
	JSONObject jsonObj = new JSONObject(s);
	String mac = jsonObj.getString("mac");
	String building = jsonObj.getString("building");
	String floor = jsonObj.getString("floor");
	String posX = jsonObj.getString("posX");
	String posY = jsonObj.getString("posY");
	
	System.out.println("mac is " + mac);
	System.out.println("building is " + building);
	
}

@Override
public void deliveryComplete(IMqttDeliveryToken token)
{	
	System.out.println("message delivery done!");
}

}
