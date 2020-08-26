$( document ).ready(function() {
        console.log( "document loaded" );

        function calculaColor(t, hi, r) { 
        	
        	var res = "Verde";
        	
        	if(((t < 18) && (t > 14)) || ((hi < 18) && (hi > 14)) ){
        		res = "Amarillo";
        	}else if(((t > 25) && (t < 27)) || ((hi > 25) && (hi < 27))){
        		res = "Amarillo";
        	}else if(t <= 14 || hi <= 14 ){
        		res = "Rojo";
        	}else if(t >= 27 || hi >= 27){
        		res = "Rojo";
        	}
        	
        	if(r != 0){
        		res = "Rojo";
        	}
        	
        	return res;
        	
        }
        
        $.getJSON('http://localhost:8090/aulas', function(data) {
            $.each(data, function(index, element) {
            	var media = "<div class=\"media\">" 
            					+ "<img class=\"media-object img-"+ element["id_aula"] +"\" src=\"http://localhost:8080/web/img/fondoRojo\">"
            					+ "<div class=\"media-body\"><h4 class=\"media-heading heading-aula\">" + element["nombre"] + "</h4>" 
            						+ "<button onclick=\"nuevaMedicion("+ element["id_aula"] +")\" class=\"boton-peticion boton-peticion-"+ element["id_aula"] +" \">Pedir nueva medición</button>"
            					+ "<p id=\"aula-"+ element["id_aula"] +"\"></p></div></div>";
                
            	$('#aulas-estado').append(media);
                
                $.getJSON("http://localhost:8090/medicion/actual/" + element["id_aula"], function(data) {
                	
                	var medidas = "<b>Temperatura</b>: "+ data["temperatura"].toFixed(1) + "ºC. <b>Humedad</b>: "+  data["humedad"]
                					+"%. <b>Índice de bochorno</b>: " + data["heatindex"].toFixed(1) + "º.";
           
                	var ruido = "";
                	if(data["ruido"] == 0){
                		ruido = " <b>Ruido</b>: Bueno.";
                	}else{
                		ruido = " <b>Ruido</b>: Malo.";
                	}
                	
                	var date = new Date(data["fecha"] * 1000);
                	var fechaMedicion = " <b>Fecha</b>: " + date.getHours() + ":" + date.getMinutes() + " del día " 
                			+ date.getDate() + "/" + (date.getMonth()+1) + "/" + date.getFullYear();
                	
                	var res = medidas.concat(ruido).concat(fechaMedicion);
                	$("#aula-" + data["id_aula"]).append(res);
                	
                	
                	var color = calculaColor(data["temperatura"], data["heatindex"], data["ruido"]);
                	$(".img-"+ data["id_aula"] +"").attr("src","http://localhost:8080/web/img/fondo"+color);
                	
                });   
            });          
        });
});


function nuevaMedicion(id_aula) {
	
	if($(".boton-peticion-"+ id_aula).text() == "Pedir nueva medición"){
		var json = "{\"id_aula\" : "+ id_aula +", \"peticion\": true}"
		$.post( "http://localhost:8090/aula/edita/" + id_aula, json, 
				function( data ) {
			  	$(".boton-peticion-"+ id_aula).text("Petición enviada")	;
			}, "json");
	}
	
	
}
