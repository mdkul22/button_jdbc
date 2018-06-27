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
			client = new MqttClient("tcp://iot.eclipse.org:1883", "Sending");
			client.connect();
			client.setCallback(this);
			client.subscribe("tqb/setup", 0);
			client.subscribe("tqb/topic", 0);
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
		if(topic.equals("tqb/topic"))
		{
			insertQuery inserter = new insertQuery(s);
			String mac = inserter.insertAlertRow();
			if(mac.equals("-1"))
				return;
			if(mac.equals("D"))
			{
				System.out.println("Deleted row as alertVal = 0");
				return; 
			}
			String pubmsg = inserter.tableLinker(mac);
			MqttMessage msg = new MqttMessage();
			msg.setPayload(pubmsg.getBytes());
			client.publish("tqb/mobile", msg);
		}
		if(topic.equals("tqb/setup")){
			System.out.println("Entered setup1");
			insertQuery inserter = new insertQuery(s);
			inserter.insertSetupRow();
		}
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token)
	{	
		System.out.println("message delivery done!");
	}


}
