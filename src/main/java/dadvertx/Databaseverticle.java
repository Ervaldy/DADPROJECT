package dadvertx;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import types.Medicion;

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
							row.getDouble("co2"),
							row.getDouble("humedad"),
							row.getDouble("temperatura"),
							row.getLong("fecha"))));
				}
			}else {
				routingcontext.response().setStatusCode(401).putHeader("content-type", "application/json")
			.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
	}
		});
		
	}
	
	public void starts(Promise<Void> startPromise) {
		MySQLConnectOptions mysqlconnectoptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("daddb").setUser("root").setPassword("PARADAD");
		PoolOptions pooloptions = new PoolOptions().setMaxSize(5);
		mysqlpool = MySQLPool.pool(vertx, mysqlconnectoptions, pooloptions);
		Router router = Router.router(vertx);
		vertx.createHttpServer().requestHandler(router::handle).listen(8090,result ->{
			if(result.succeeded()) {
				startPromise.complete();
			}else {
				startPromise.fail(result.cause());
			}
		});
		router.get("/medicion/:id_medicion").handler(this::getMedicion);
	}

}
