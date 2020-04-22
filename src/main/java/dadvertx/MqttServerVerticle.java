package dadvertx;

import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.ClientAuth;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.mqtt.MqttTopicSubscription;
import io.vertx.mqtt.messages.MqttPublishMessage;
import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;
import io.netty.handler.codec.mqtt.MqttQoS;

public class MqttServerVerticle extends AbstractVerticle {

		public static final String TOPIC_PETICIONES= "peticiones";
		public static final String TOPIC_MEDICIONES= "mediciones";
		
		private static final SetMultimap<String, MqttEndpoint> clients = LinkedHashMultimap.create();
	
		public void start (Promise<Void> promise) {
			MqttServerOptions options = new MqttServerOptions();
			options.setPort(1885);
			options.setClientAuth(ClientAuth.REQUIRED);
			MqttServer mqttServer = MqttServer.create(vertx, options);
			init(mqttServer);
			
			
		}
		public void init(MqttServer mqttServer) {
			
			mqttServer.endpointHandler(endpoint ->{
				System.out.println("Mqtt client [" + endpoint.clientIdentifier() + "] request to connect, clean session = "
						+ endpoint.isCleanSession());
				if(endpoint.auth().getUsername().contentEquals("mqttbroker") &&
							endpoint.auth().getPassword().contentEquals("mqttbrokerpass")){
					endpoint.accept();
					handleSubscription(endpoint);
					handleUnsubscription(endpoint);
					publishHandler(endpoint);
					handleClientDisconnect(endpoint);
					
				}else{
					endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD);
				}
			}).listen(ar-> {
				if(ar.succeeded()) {
					System.out.println("Error on starting the server");
					ar.cause().printStackTrace();
				}
			});
			
		}
		
		private static void handleSubscription(MqttEndpoint endpoint) {
	        endpoint.subscribeHandler(subscribe -> {
	            List<MqttQoS> grantedQosLevels = new ArrayList<>();
	            for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
	                System.out.println("Subscription for " + s.topicName() + " with QoS " + s.qualityOfService());
	                grantedQosLevels.add(s.qualityOfService());
	                clients.put(s.topicName(), endpoint);
	            }
	            endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);
	        });
	    }
		private static void handleUnsubscription(MqttEndpoint endpoint) {
	        endpoint.unsubscribeHandler(unsubscribe -> {
	            for (String t: unsubscribe.topics()) {
	                System.out.println("Unsubscription for " + t);
	                clients.remove(t, endpoint);
	            }
	            endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
	        });
	    }
		private static void publishHandler(MqttEndpoint endpoint) {
	        endpoint.publishHandler(message -> {
	            handleQoS(message, endpoint);
	        }).publishReleaseHandler(messageId -> {
	            endpoint.publishComplete(messageId);
	        });
	    }
		private static void handleQoS(MqttPublishMessage message, MqttEndpoint endpoint) {
	        if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
	            String topicName = message.topicName();
	            switch (topicName) {
	            case TOPIC_PETICIONES:
	                System.out.println("Peticiones published");
	                break;
	            case TOPIC_MEDICIONES:
	                System.out.println("Mediciones published");
	                break;
	            }
	            for (MqttEndpoint subscribed: clients.get(message.topicName())) {
	                subscribed.publish(message.topicName(), message.payload(), message.qosLevel(), message.isDup(), message.isRetain());
	            }
	            endpoint.publishAcknowledge(message.messageId());
	        } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
	            endpoint.publishRelease(message.messageId());
	        }
	    }
		private static void handleClientDisconnect(MqttEndpoint endpoint) {
	        endpoint.disconnectHandler(h -> {
	            System.out.println("The remote client has closed the connection.");
	        });
	    }
}
