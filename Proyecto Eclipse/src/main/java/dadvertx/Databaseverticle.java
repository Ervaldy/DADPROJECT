package dadvertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import types.Actuacion;
import types.Aula;
import types.Medicion;
import types.Sensor;
import types.Usuario;

public class Databaseverticle extends AbstractVerticle{
	
	private MySQLPool mysqlpool;
	private void getMedicion(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.mediciones WHERE id_medicion =" + routingcontext.request()
		.getParam("id_medicion"), res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				
				JsonArray result = new JsonArray();
				
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Medicion(row.getInteger("id_medicion"),
							row.getInteger("id_sensor"),
							row.getInteger("id_aula"),
							row.getDouble("ruido"),
							row.getDouble("CO2"),
							row.getDouble("humedad"),
							row.getDouble("temperatura"),
							row.getLong("fecha"),
							row.getDouble("heatindex"))));
				}
				
				JsonObject jsonFinal = result.getJsonObject(0);
				
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
				.end(jsonFinal.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
	}
		});
		
	}
	private void getMedicionActual(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.mediciones WHERE id_aula =" + routingcontext.request()
		.getParam("id_aula") + " ORDER BY fecha DESC LIMIT 1", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Medicion(row.getInteger("id_medicion"),
							row.getInteger("id_sensor"),
							row.getInteger("id_aula"),
							row.getDouble("ruido"),
							row.getDouble("CO2"),
							row.getDouble("humedad"),
							row.getDouble("temperatura"),
							row.getLong("fecha"),
							row.getDouble("heatindex"))));
				}
				
				JsonObject jsonFinal = result.getJsonObject(0);
				
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
				.end(jsonFinal.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
	}
		});
		
	}
	private void getAula(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.aulas WHERE id_aula =" + routingcontext.request()
		.getParam("id_aula"), res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Aula(row.getInteger("id_aula"),
							row.getString("nombre"),
							row.getBoolean("peticion")
							)));
				}
				
				JsonObject jsonFinal = result.getJsonObject(0);
				
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
				.end(jsonFinal.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
	}
		});
		
	}
	private void getAulaTodas(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.aulas", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Aula(row.getInteger("id_aula"),
							row.getString("nombre"),
							row.getBoolean("peticion")
							)));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
				.end(result.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
		
	}
	
	private void getUsuarioTodos(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.usuarios", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Usuario(row.getInteger("id_usuario"),
							row.getString("nombre"),
							row.getString("password")
							)));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
				.end(result.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
		
		
	}
	
	private void getActuacion(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.actuaciones WHERE id_actuacion =" + routingcontext.request()
		.getParam("id_actuacion"), res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Actuacion(row.getInteger("id_actuacion"),
							row.getInteger("id_medicion")
							)));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.end(result.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
	}
		});
		
	}
	private void getSensor(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.sensores WHERE id_sensor =" + routingcontext.request()
		.getParam("id_sensor"), res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Sensor(row.getInteger("id_sensor"),
							row.getString("nombre")
							)));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.end(result.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
		
	}
	
	private void getSensores(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.sensores", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Sensor(row.getInteger("id_sensor"),
							row.getString("nombre")
							)));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
				.end(result.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
		
	}
	
	private void getUsuario(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.usuarios WHERE id_usuario =" + routingcontext.request()
		.getParam("id_usuario"), res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Usuario(row.getInteger("id_usuario"),
							row.getString("nombre"),
							row.getString("password")
							)));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.end(result.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
	}
		});
		
	}
	private void getMedicionTodas(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.mediciones WHERE id_aula =" + routingcontext.request()
		.getParam("id_aula") + " ORDER BY fecha DESC", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Medicion(row.getInteger("id_medicion"),
							row.getInteger("id_sensor"),
							row.getInteger("id_aula"),
							row.getDouble("ruido"),
							row.getDouble("CO2"),
							row.getDouble("humedad"),
							row.getDouble("temperatura"),
							row.getLong("fecha"),
							row.getDouble("heatindex"))));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
				.end(result.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
		
	}
	private void getMedicionSensor(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.mediciones WHERE id_sensor =" + routingcontext.request()
		.getParam("id_sensor") + " ORDER BY fecha DESC", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Medicion(row.getInteger("id_medicion"),
							row.getInteger("id_sensor"),
							row.getInteger("id_aula"),
							row.getDouble("ruido"),
							row.getDouble("CO2"),
							row.getDouble("humedad"),
							row.getDouble("temperatura"),
							row.getLong("fecha"),
							row.getDouble("heatindex"))));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.end(result.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
	}
		});
		
	}
	private void putMedicion(RoutingContext routingcontext) {
		System.out.println(routingcontext.getBodyAsString());
		Medicion medicion = Json.decodeValue(routingcontext.getBodyAsString(), Medicion.class);
		medicion.setFecha((System.currentTimeMillis() / 1000L));
		
		System.out.println(medicion.toString());
		mysqlpool.preparedQuery("INSERT INTO daddb.mediciones( id_sensor, id_aula, ruido,"
				+ " CO2, humedad, temperatura, fecha, heatindex) VALUES(?,?,?,?,?,?,?,?)", Tuple.of(
						medicion.getId_sensor(),medicion.getId_aula(),medicion.getRuido(),medicion.getCO2(),
						medicion.getHumedad(),medicion.getTemperatura(),medicion.getFecha(), medicion. getHeatindex()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						medicion.setId_medicion((int) id);
						
						Actuacion act = new Actuacion((int) id);
						
						if((medicion.getTemperatura() > 25) || (medicion.getTemperatura() < 18) 
								|| medicion.getHeatindex() > 25 || medicion.getRuido() != 0) {
							//mysqlpool.preparedQuery("INSERT INTO daddb.actuaciones(id_medicion) VALUES (?)", Tuple.of(id)), handler ->{});
							mysqlpool.preparedQuery("INSERT INTO daddb.actuaciones(id_medicion) VALUES(?)", Tuple.of(
									act.getId_medicion()),
							handlerAct ->{
								if(handlerAct.succeeded()) {
									System.out.println(handler.result().rowCount());
									long id_actuacion = handler.result().property(MySQLClient.LAST_INSERTED_ID);
									act.setId_actuacion((int) id_actuacion);
								}else {
									System.out.println(handlerAct.cause().toString());
								}
							});
						}
						
						
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						//.end(JsonObject.mapFrom(medicion).encodePrettily());
						.end(Json.encodePrettily(medicion));
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
						
					}
				});
	}
	private void putAula(RoutingContext routingcontext) {
		Aula aula = Json.decodeValue(routingcontext.getBodyAsString(), Aula.class);
		mysqlpool.preparedQuery("INSERT INTO daddb.aulas( nombre, peticion) VALUES(?, ?)", Tuple.of(
						aula.getNombre(),
						aula.getPeticion()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						aula.setId_aula((int) id);
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(aula).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
					}
				});
	}
	
	private void postAulaPeticion(RoutingContext routingcontext) {
		System.out.println(routingcontext.getBodyAsString());
		Aula aula = Json.decodeValue(routingcontext.getBodyAsString(), Aula.class);
		System.out.println(aula);
		System.out.println(aula.toString());
		mysqlpool.preparedQuery("UPDATE daddb.aulas SET peticion = ? WHERE id_aula = ?",
				Tuple.of(
				aula.getPeticion(),
				aula.getId_aula()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(aula).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
					}
				});
	}
	
	private void postAulaEdicion(RoutingContext routingcontext) {
		Aula aula = Json.decodeValue(routingcontext.getBodyAsString(), Aula.class);
		mysqlpool.preparedQuery("UPDATE daddb.aulas SET peticion = ?, nombre = ? WHERE id_aula = ?",
				Tuple.of(
				aula.getPeticion(),
				aula.getNombre(),
				aula.getId_aula()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(aula).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
					}
				});
	}
	
	private void postSensorEdicion(RoutingContext routingcontext) {

		Sensor sensor = Json.decodeValue(routingcontext.getBodyAsString(), Sensor.class);
		mysqlpool.preparedQuery("UPDATE daddb.sensores SET nombre = ? WHERE id_sensor = ?",
				Tuple.of(
				sensor.getNombre(),
				sensor.getId_sensor()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(sensor).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
					}
				});
	}
	
	private void postUsuarioEdicion (RoutingContext routingcontext) {

		Usuario usuario = Json.decodeValue(routingcontext.getBodyAsString(), Usuario.class);
		mysqlpool.preparedQuery("UPDATE daddb.usuarios SET nombre = ?, password = ? WHERE id_usuario = ?",
				Tuple.of(
				usuario.getNombre(),
				usuario.getPassword(),
				usuario.getId_usuario()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(usuario).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
					}
				});
	}
	
	private void putSensor(RoutingContext routingcontext) {
		Sensor sensor = Json.decodeValue(routingcontext.getBodyAsString(), Sensor.class);
		mysqlpool.preparedQuery("INSERT INTO daddb.sensores(  nombre) VALUES(?)", Tuple.of(
						sensor.getNombre()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						sensor.setId_sensor((int) id);
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(sensor).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
					}
				});
	}
	
	private void getActuacionTodas(RoutingContext routingcontext) {
		mysqlpool.query("SELECT * FROM daddb.ACTUACIONES", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				JsonArray result = new JsonArray();
				for (Row row : resultset) {
					result.add(JsonObject.mapFrom(new Actuacion(row.getInteger("id_actuacion"),
							row.getInteger("id_medicion")
							)));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
				.putHeader("Access-Control-Allow-Methods","GET, POST, OPTIONS")
				.putHeader("Access-Control-Allow-Credentials", "true")
				.end(result.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
		
	}
	
	private void putActuacion(RoutingContext routingcontext) {
		Actuacion actuacion = Json.decodeValue(routingcontext.getBodyAsString(), Actuacion.class);
		mysqlpool.preparedQuery("INSERT INTO daddb.actuaciones( id_medicion) VALUES(?)", Tuple.of(
						actuacion.getId_medicion()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						actuacion.setId_actuacion((int) id);
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(actuacion).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
					}
				});
	}
	private void putUsuario(RoutingContext routingcontext) {
		Usuario usuario = Json.decodeValue(routingcontext.getBodyAsString(), Usuario.class);
		mysqlpool.preparedQuery("INSERT INTO daddb.usuarios( nombre, password) VALUES(?,?)", Tuple.of(
						usuario.getNombre(),usuario.getPassword()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						usuario.setId_usuario((int) id);
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(usuario).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
					}
				});
	}
	private void deleteMedicion(RoutingContext routingcontext) {
		System.out.println("Entra en delete medicion");
		mysqlpool.preparedQuery("DELETE FROM daddb.mediciones WHERE id_medicion = " + routingcontext
				.request().getParam("id_medicion"), res ->{
					if(res.succeeded()) {
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end("{\"respuesta\": \"Eliminada una medicion\"}");
						
					}else {
						System.out.println(res.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(res.cause()).encodePrettily());
					}
				});
		
	}
	private void deleteMedicionActual(RoutingContext routingcontext) {
		mysqlpool.preparedQuery(" DELETE FROM daddb.mediciones WHERE id_aula =" + routingcontext.request() 
					.getParam("id_aula") + " ORDER BY fecha DESC LIMIT 1", res ->{
					if(res.succeeded()) {
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end("{\"respuesta\": \"Eliminada una medicion\"}");
						
					}else {
						System.out.println(res.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(res.cause()).encodePrettily());
					}
				});
		
	}
	private void deleteAula(RoutingContext routingcontext) {
		mysqlpool.preparedQuery("DELETE FROM daddb.aulas WHERE id_aula = " + routingcontext
				.request().getParam("id_aula"), res ->{
					if(res.succeeded()) {
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end("{\"respuesta\": \"Eliminada un aula\"}");
						
					}else {
						System.out.println(res.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(res.cause()).encodePrettily());
					}
				});
		
	}
	private void deleteSensor(RoutingContext routingcontext) {
		mysqlpool.preparedQuery("DELETE FROM daddb.sensores WHERE id_sensor = " + routingcontext
				.request().getParam("id_sensor"), res ->{
					if(res.succeeded()) {
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end("{\"respuesta\": \"Eliminado un sensor\"}");
						
					}else {
						System.out.println(res.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(res.cause()).encodePrettily());
					}
				});
		
	}
	private void deleteActuacion(RoutingContext routingcontext) {
		mysqlpool.preparedQuery("DELETE FROM daddb.actuaciones WHERE id_actuacion = " + routingcontext
				.request().getParam("id_actuacion"), res ->{
					if(res.succeeded()) {
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end("{\"respuesta\": \"Eliminada una actuacion\"}");
						
					}else {
						System.out.println(res.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(res.cause()).encodePrettily());
					}
				});
		
	}
	private void deleteUsuario(RoutingContext routingcontext) {
		mysqlpool.preparedQuery("DELETE FROM daddb.usuarios WHERE id_usuario = " + routingcontext
				.request().getParam("id_usuario"), res ->{
					if(res.succeeded()) {
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end("{\"respuesta\": \"Eliminado un usuario\"}");
						
					}else {
						System.out.println(res.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.putHeader("Access-Control-Allow-Origin", "http://localhost:8080")
						.putHeader("Access-Control-Allow-Methods","GET, POST, PATCH, PUT, DELETE, OPTIONS")
						.putHeader("Access-Control-Allow-Credentials", "true")
						.end(JsonObject.mapFrom(res.cause()).encodePrettily());
					}
				});
		
	}
	
	
	
	public void start(Promise<Void> startPromise) {
		
		System.out.println("Despliega db");
		
		MySQLConnectOptions mysqlconnectoptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("daddb").setUser("root").setPassword("PaRADAD7");
		PoolOptions pooloptions = new PoolOptions().setMaxSize(5);
		mysqlpool = MySQLPool.pool(vertx, mysqlconnectoptions, pooloptions);
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		vertx.createHttpServer().requestHandler(router::handle).listen(8090,result ->{
			if(result.succeeded()) {
				startPromise.complete();
			}else {
				startPromise.fail(result.cause());
			}
		});
		
		router.get("/usuario/:id_usuario").handler(this::getUsuario);
		router.post("/usuario/nuevo").handler(this::putUsuario);
		router.get("/usuario/todos/").handler(this::getUsuarioTodos);
		router.post("/usuario/edita-admin/:id_usuario").handler(this::postUsuarioEdicion);
		router.post("/usuario/borra/:id_usuario").handler(this::deleteUsuario);
		
		router.get("/aulas").handler(this::getAulaTodas);
		router.get("/aula/:id_aula").handler(this::getAula);
		router.post("/aula/nueva").handler(this::putAula);
		router.post("/aula/borra/:id_aula").handler(this::deleteAula);
		router.post("/aula/edita/:id_aula").handler(this::postAulaPeticion);
		router.post("/aula/edita-admin/:id_aula").handler(this::postAulaEdicion);
		
		router.get("/sensor/:id_sensor").handler(this::getSensor);
		router.get("/sensor/todos/").handler(this::getSensores);
		router.post("/sensor/nuevo/").handler(this::putSensor);
		router.post("/sensor/edita-admin/:id_sensor").handler(this::postSensorEdicion);
		router.post("/sensor/borra/:id_sensor").handler(this::deleteSensor);
		
		router.get("/medicion/:id_medicion").handler(this::getMedicion);
		router.get("/medicion/todas/:id_aula").handler(this::getMedicionTodas);
		router.put("/medicion").handler(this::putMedicion);
		router.get("/medicion/actual/:id_aula").handler(this::getMedicionActual);
		router.post("/medicion/borra/:id_medicion").handler(this::deleteMedicion);
		router.get("/medicion/sensor/:id_sensor").handler(this::getMedicionSensor);
		router.delete("/medicion/actual/:id_aula").handler(this::deleteMedicionActual);
		
		
		router.get("/actuacion/todas").handler(this::getActuacionTodas);
		router.get("/actuacion/:id_actuacion").handler(this::getActuacion);	
		router.put("/actuacion").handler(this::putActuacion);
		router.post("/actuacion/borra/:id_actuacion").handler(this::deleteActuacion);
		
		
	}

}
