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
							row.getLong("fecha"))));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.end(result.encodePrettily());
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
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
							row.getLong("fecha"))));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
				.end(result.encodePrettily());
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
							row.getLong("fecha"))));
				}
				routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
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
							row.getLong("fecha"))));
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
		Medicion medicion = Json.decodeValue(routingcontext.getBodyAsString(), Medicion.class);
		mysqlpool.preparedQuery("INSERT INTO daddb.mediciones( id_sensor, id_aula, ruido,"
				+ " CO2, humedad, temperatura, fecha) VALUES(?,?,?,?,?,?,?)", Tuple.of(
						medicion.getId_sensor(),medicion.getId_aula(),medicion.getRuido(),medicion.getCO2(),
						medicion.getHumedad(),medicion.getTemperatura(),medicion.getFecha()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						medicion.setId_medicion((int) id);
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(medicion).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
					}
				});
	}
	private void putAula(RoutingContext routingcontext) {
		Aula aula = Json.decodeValue(routingcontext.getBodyAsString(), Aula.class);
		mysqlpool.preparedQuery("INSERT INTO daddb.aulas( nombre) VALUES(?)", Tuple.of(
						aula.getNombre()),
				handler ->{
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
						long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
						aula.setId_aula((int) id);
						routingcontext.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(aula).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
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
						.end(JsonObject.mapFrom(sensor).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
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
						.end(JsonObject.mapFrom(usuario).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end(JsonObject.mapFrom(handler.cause()).encodePrettily());
					}
				});
	}
	
	public void start(Promise<Void> startPromise) {
		MySQLConnectOptions mysqlconnectoptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("daddb").setUser("root").setPassword("PARADAD");
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
		
		router.get("/medicion/:id_medicion").handler(this::getMedicion);
		router.get("/medicion/actual/:id_aula").handler(this::getMedicionActual);
		router.get("/aula/:id_aula").handler(this::getAula);
		router.get("/actuacion/:id_actuacion").handler(this::getActuacion);
		router.get("/sensor/:id_sensor").handler(this::getSensor);
		router.get("/usuario/:id_usuario").handler(this::getUsuario);
		router.get("/medicion/todas/:id_aula").handler(this::getMedicionTodas);
		router.get("/medicion/sensor/:id_sensor").handler(this::getMedicionSensor);
		router.put("/medicion").handler(this::putMedicion);
		router.put("/aula").handler(this::putAula);
		router.put("/sensor").handler(this::putSensor);
		router.put("/actuacion").handler(this::putActuacion);
		router.put("/usuario").handler(this::putUsuario);
	}

}
