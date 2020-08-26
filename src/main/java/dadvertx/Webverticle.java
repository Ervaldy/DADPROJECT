package dadvertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;


public class Webverticle extends AbstractVerticle{
	
	private MySQLPool mysqlpool;
	
	public void webIndex(RoutingContext routingcontext) {

		 HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "text/html");

	     response.sendFile("webpage\\index.html");
	     response.end();
	}
	
	public void webInfo(RoutingContext routingcontext) {

		 HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "text/html");

	     response.sendFile("webpage\\info.html");
	     response.end();
	}
	public void webLogin(RoutingContext routingcontext) {
		
		 HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "text/html");

	     response.sendFile("webpage\\login.html");
	     response.end();
	}
	
	public void webAdminCSS(RoutingContext routingcontext) {
		
		 HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "text/css");

	     response.sendFile("webpage\\css\\administracion.css");
	     response.end();
	}
	
	public void webMainCSS(RoutingContext routingcontext) {
		
		 HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "text/css");

	     response.sendFile("webpage\\css\\style.css");
	     response.end();
	}
	public void webLoginCSS(RoutingContext routingcontext) {
		
		 HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "text/css");

	     response.sendFile("webpage\\css\\login.css");
	     response.end();
	}
	public void webIndexJS(RoutingContext routingcontext) {
		
		 HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "text/javascript");

	     response.sendFile("webpage\\js\\index.js");
	     response.end();
	}
	public void webLoginJS(RoutingContext routingcontext) {
		
		 HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "text/javascript");

	     response.sendFile("webpage\\js\\login.js");
	     response.end();
	}
	
	public void webAdminJS(RoutingContext routingcontext) {
		
		 HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "text/javascript");

	     response.sendFile("webpage\\js\\administracion.js");
	     response.end();
	}
	
	public void webAdministracion(RoutingContext routingcontext) {
		
		String uname = routingcontext.request().getFormAttribute("uname");
		String psw = routingcontext.request().getFormAttribute("psw");

		
		mysqlpool.query("SELECT * FROM daddb.usuarios WHERE nombre = \"" + uname + "\" AND password = \"" + psw + "\"", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultset = res.result();
				System.out.println("El número de elementos obtenidos es:" + resultset.size());
				HttpServerResponse response = routingcontext.response();
				if(resultset.size()>0) {

					response.setStatusCode(200);
					response.putHeader("content-type", "text/html");

				     response.sendFile("webpage\\panelDeControl.html");
				     response.end();
				}else {
					
					response.setStatusCode(200);
					response.putHeader("content-type", "text/html");
				    response.sendFile("webpage\\loginError.html");
				    response.end();
				}
			}
	
			else {
				HttpServerResponse response = routingcontext.response();
				response.setStatusCode(200);
				response.putHeader("content-type", "text/html");
			    response.sendFile("webpage\\loginError.html");
			    response.end();
			}
		});
		
	}
	
	public void imgFondoVerde(RoutingContext routingcontext){
		HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "image/png");

	     response.sendFile("webpage\\img\\fondoverde.png");
	     response.end();
	}
	
	public void imgFondoAmarillo(RoutingContext routingcontext){
		HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "image/png");

	     response.sendFile("webpage\\img\\fondoamarillo.png");
	     response.end();
	}
	
	public void imgFondoRojo(RoutingContext routingcontext){
		HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "image/png");

	     response.sendFile("webpage\\img\\fondorojo.png");
	     response.end();
	}
	
	public void imgFavicon(RoutingContext routingcontext){
		HttpServerResponse response = routingcontext.response();
	     response.putHeader("content-type", "image/png");

	     response.sendFile("webpage\\img\\favicon.png");
	     response.end();
	}
	
	public void start(Promise<Void> startPromise) {
		
		System.out.println("Despliega web");
		
		MySQLConnectOptions mysqlconnectoptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("daddb").setUser("root").setPassword("PaRADAD7");
		PoolOptions pooloptions = new PoolOptions().setMaxSize(5);
		mysqlpool = MySQLPool.pool(vertx, mysqlconnectoptions, pooloptions);
		
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		vertx.createHttpServer().requestHandler(router::handle).listen(8080,result ->{
			if(result.succeeded()) {
				startPromise.complete();
			}else {
				startPromise.fail(result.cause());
			}
		});
		
		
		router.route("/web/").handler(this::webIndex);
		router.route("/web/info").handler(this::webInfo);
		router.route("/web/login").handler(this::webLogin);
		router.route("/web/panelAdmin").handler(this::webAdministracion);
		
		router.route("/web/css/administracion.css").handler(this::webAdminCSS);
		router.route("/web/css/style.css").handler(this::webMainCSS);
		router.route("/web/css/login.css").handler(this::webLoginCSS);
		
		router.route("/web/js/index.js").handler(this::webIndexJS);
		router.route("/web/js/administracion.js").handler(this::webAdminJS);
		
		router.route("/web/img/fondoVerde").handler(this::imgFondoVerde);
		router.route("/web/img/fondoAmarillo").handler(this::imgFondoAmarillo);
		router.route("/web/img/fondoRojo").handler(this::imgFondoRojo);
		router.route("/web/img/favicon").handler(this::imgFavicon);
	}
	

}
