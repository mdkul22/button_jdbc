package mqtt;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class databaseUpdater implements MqttCallback{
	MqttClient client;


	public databaseUpdater() {	
	}

	public static void main(String[] args)
	{
		new databaseUpdater().beginSubscribe();
	}

	public void beginSubscribe() {
		try{
			client = new MqttClient("tcp://173.39.91.82:1883", "Sending");
			client.connect();
			client.setCallback(this);
			client.subscribe("tqb/topic");
			client.subscribe("tqb/alert");
			MqttMessage message = new MqttMessage();
			message.setPayload("Lets start this stuff!".getBytes());
			client.publish("tqb/begin", message);
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
		insertQuery update = new insertQuery(s);
		if(topic.equals("tqb/topic"))
		{
			insertQuery inserter = new insertQuery(update);
			insert.insertAlertRow();
		}
		if(topic.equals("tqb/setup")){
			insertQuery inserter = new insertQuery(update);
			inserter.insertSetupRow();
		}

		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token)
	{	
		System.out.println("message delivery done!");
	}


}
