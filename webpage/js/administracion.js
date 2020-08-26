$( document ).ready(function() {
        console.log( "document loaded" );
        
        recuperaMediciones();
        recuperaActuaciones();
        recuperaAulas();
        recuperaUsuarios();
        recuperaSensores();
        
        $("#carga-nueva-aula").click(function() {
        	
        	var json = "{ \"nombre\": \""+  $("#nombre-nueva-aula").val()  +" \", \"peticion\": false}";
        		$.post( "http://localhost:8090/aula/nueva", json, 
    				function( data ) {
        			$(".info-admin-table-aulas tbody").empty();
        			$("#nombre-nueva-aula").val("");
        	 		recuperaAulas();
    			}, "json");
        	  
        	}
        );
        
        $("#carga-nuevo-sensor").click(function() {
        	
        	var json = "{ \"nombre\": \""+  $("#nombre-nuevo-sensor").val()  +" \"}";
        		$.post( "http://localhost:8090/sensor/nuevo/", json, 
    				function( data ) {
        			$(".info-admin-table-sensores tbody").empty();
        			$("#nombre-nuevo-sensor").val("");
        	 		recuperaSensores();
    			}, "json");
        	  
        	}
        );
        
        $("#carga-nuevo-usuario").click(function() {
        	
        	var json = "{ \"nombre\": \""+  $("#nombre-nuevo-usuario").val()  +" \", \"password\": \""+$("#password-nuevo-usuario").val()+"\"}";
        	$.post( "http://localhost:8090/usuario/nuevo", json, 
    				function( data ) {
        			$(".info-admin-table-usuarios tbody").empty();
        			$("#nombre-nuevo-usuario").val("")
        			$("#password-nuevo-usuario").val("")
        	 		recuperaUsuarios();
    			}, "json");
        });
});

function editaUsuario(id_usuario){

	var json = "{\"id_usuario\" : "+ id_usuario +", \"nombre\": \""+ $("#"+id_usuario+"-nombreUsuario").val() +"\", \"password\": \""+ $("#"+id_usuario+"-passwordUsuario").val()+"\"}";
	
	$.post( "http://localhost:8090/usuario/edita-admin/" + id_usuario, json, 
			function( data ) {
			$(".info-admin-table-usuarios tbody").empty();
	 		recuperaUsuarios();
		}, "json");

}


function recuperaUsuarios(){
	
$.getJSON('http://localhost:8090/usuario/todos/', function(data) {
		
		$.each(data, function(index, element) {
			 $(".info-admin-table-usuarios tbody").append("<tr>" +
			 		"<td><input type='text' id='"+element["id_usuario"]+"-nombreUsuario' value='"+element["nombre"]+"' class='form-control'></input></td>" + 
			 		"<td><input type='text' id='"+element["id_usuario"]+"-passwordUsuario' value='"+element["password"]+"' class='form-control'></input></td>" +
			 		"<td><button type='button' class='btn btn-primary' onclick='editaUsuario(" + element["id_usuario"] +")'>Editar</button></td>" +
			 		"<td><button type='button' class='btn btn-danger' onclick='borrarUsuario(" + element["id_usuario"] +")'>Borrar</button></td></tr>");
		});
		
	});
	
}

function recuperaSensores(){
	
	$.getJSON('http://localhost:8090/sensor/todos/', function(data) {
		
		$.each(data, function(index, element) {
			
			 $(".info-admin-table-sensores tbody").append("<tr>" +
    			 		"<td><input type='text' value ="+ element["id_sensor"] +" class='form-control' disabled></input></td>" +
    			 		"<td><input type='text' id='"+element["id_sensor"]+"-nombreSensor' value='"+ element["nombre"]+"' class='form-control'></td>" +
    			 		"<td><button type='button' class='btn btn-primary' onclick='editaSensor(" + element["id_sensor"] +")'>Editar</button></td>" +
    			 		"<td><button type='button' class='btn btn-danger' onclick='borrarSensor(" + element["id_sensor"] +")'>Borrar</button></td>");
			
		});
		
	});
	
}

function recuperaActuaciones(){
	$.getJSON('http://localhost:8090/actuacion/todas/', function(data) {
		
		$.each(data, function(index, element) {
			
			$.getJSON("http://localhost:8090/medicion/" + element["id_medicion"], function(medicion) {
     			 
      			 $.getJSON('http://localhost:8090/aula/'+ medicion["id_aula"], function(data) {
      				
      				var date = new Date(medicion["fecha"]*1000);
       			 
      				var day = (date.getDate() < 10 ? '0' : '') + date.getDate(); 
      				var month = (date.getMonth() < 9 ? '0' : '') + (date.getMonth() + 1); 
      				var year = date.getFullYear(); 

	       			 var hours = ((date.getHours() % 12 || 12) < 10 ? '0' : '') + (date.getHours() % 12 || 12); 
	       			 var minutes = (date.getMinutes() < 10 ? '0' : '') + date.getMinutes(); 
	       			 var meridiem = (date.getHours() >= 12) ? 'pm' : 'am';
	
	       			 var formattedDate = hours +":" + minutes+ " " +meridiem+ " del día " + day + "/"+ month + "/" + year;
	       			 
	       			var ruido = "";
	                	if(medicion["ruido"] == 0){
	                		ruido = "Bueno";
	                	}else{
	                		ruido = "Malo";
	                	}
	       			 $(".info-admin-table-actuaciones tbody").append("<tr>" +
	       			 		"<td>"+ data["nombre"] +"</td>" +
	       			 		"<td>"+formattedDate+"</td>" +
	       			 		"<td>"+medicion["temperatura"].toFixed(1) +"ºC</td>" +
	       			 		"<td>"+medicion["humedad"].toFixed(1) +"%</td>" +
	       			 		"<td>"+medicion["heatindex"].toFixed(1) +"ºC</td>" +
	       			 		"<td>"+ ruido +"</td>" +
	       			 		"<td><button type='button' class='btn btn-danger' onclick='borrarActuacion(" + element["id_actuacion"] +")'>Borrar</button></td>" +
	       			 		"</tr>");
      				
      			 });
				
				 
				
			});
			
		});
		
		
	});
}

function recuperaMediciones(){
	
	 $.getJSON('http://localhost:8090/aulas', function(data) {
         $.each(data, function(index, element) {
        	 
         	$.getJSON("http://localhost:8090/medicion/todas/" + element["id_aula"], function(data) {
         		
         		 $.each(data, function(index, medicion) {
         			 
         			 var date = new Date(medicion["fecha"]*1000);
         			 
         			 var day = (date.getDate() < 10 ? '0' : '') + date.getDate(); 
         			 var month = (date.getMonth() < 9 ? '0' : '') + (date.getMonth() + 1); 
         			 var year = date.getFullYear(); 

         			 var hours = ((date.getHours() % 12 || 12) < 10 ? '0' : '') + (date.getHours() % 12 || 12); 
         			 var minutes = (date.getMinutes() < 10 ? '0' : '') + date.getMinutes(); 
         			 var meridiem = (date.getHours() >= 12) ? 'pm' : 'am';

         			 var formattedDate = hours +":" + minutes+ " " +meridiem+ " del día " + day + "/"+ month + "/" + year;
         			 
         			var ruido = "";
                  	if(medicion["ruido"] == 0){
                  		ruido = "Bueno";
                  	}else{
                  		ruido = "Malo";
                  	}
         			 $(".info-admin-table-mediciones tbody").append("<tr>" +
         			 		"<td>"+element["nombre"] +"</td>" +
         			 		"<td>"+formattedDate+"</td>" +
         			 		"<td>"+medicion["temperatura"].toFixed(1) +"ºC</td>" +
         			 		"<td>"+medicion["humedad"].toFixed(1) +"%</td>" +
         			 		"<td>"+medicion["heatindex"].toFixed(1) +"ºC</td>" +
         			 		"<td>"+ ruido +"</td>" +
         			 		"<td><button type='button' class='btn btn-danger' onclick='borrarMedicion(" + medicion["id_medicion"] +")'>Borrar</button></td>" +
         			 		"</tr>");
         		 });	
         		
         	});
         	
         	
         });
     
     });
	
	
}

function recuperaAulas(){
	
	$.getJSON('http://localhost:8090/aulas', function(data) {
		
		$.each(data, function(index, element) {
			
			var select = "<select name='"+element["id_aula"]+"-peticion' id='"+element["id_aula"]+"-peticion' class='form-control'>"
			if(element["peticion"] == false){
				select += " <option value='false' selected='selected'>No</option> <option value ='true'>Sí</option>"
			}else{
				select += " <option value='false'>No</option> <option value ='true' selected='selected'>Sí</option>"
			}
			
			select += "</select>";
			

			
			 $(".info-admin-table-aulas tbody").append("<tr>" +
					"<td><input type='text' value ="+ element["id_aula"] +" class='form-control' disabled></input></td>" +
			 		"<td><input type='text' id='"+element["id_aula"]+"-nombre' value='"+element["nombre"]+"' class='form-control'></input></td>" + 
			 		"<td>" + select + "</td>" +
			 		"<td><button type='button' class='btn btn-primary' onclick='editaAula(" + element["id_aula"] +")'>Editar</button></td>" +
			 		"<td><button type='button' class='btn btn-danger' onclick='borrarAula(" + element["id_aula"] +")'>Borrar</button></td></tr>");
		});
		
	});
	
}

function borrarMedicion(id_medicion) {
	
	$.post( "http://localhost:8090/medicion/borra/" + id_medicion, 
			function( data ) {
		 	$(".info-admin-table-mediciones tbody").empty();
		 	recuperaMediciones();
		  	
		}, "json");
}

function borrarActuacion(id_actuacion) {
	
	$.post( "http://localhost:8090/actuacion/borra/" + id_actuacion, 
			function( data ) {
		 	$(".info-admin-table-actuaciones tbody").empty();
		 	recuperaActuaciones();
		  	
		}, "json");
}

function borrarAula(id_aula){
	$.post( "http://localhost:8090/aula/borra/" + id_aula, 
			function( data ) {
		 	$(".info-admin-table-aulas tbody").empty();
		 	recuperaAulas();
		  	
		}, "json");
}

function borrarSensor(id_sensor){
	$.post( "http://localhost:8090/sensor/borra/" + id_sensor, 
			function( data ) {
		 	$(".info-admin-table-sensores tbody").empty();
		 	recuperaSensores();
		  	
		}, "json");
}

function borrarUsuario(id_usuario){
	$.post( "http://localhost:8090/usuario/borra/" + id_usuario, 
			function( data ) {
		 	$(".info-admin-table-usuarios tbody").empty();
		 	recuperaUsuarios();
		  	
		}, "json");
}


function editaAula(id_aula){
	
	var json = "{\"id_aula\" : "+ id_aula +", \"nombre\": \""+ $("#"+id_aula+"-nombre").val() +"\", \"peticion\": "+ $("#"+id_aula+"-peticion").val()+"}"
	
	$.post( "http://localhost:8090/aula/edita-admin/" + id_aula, json, 
			function( data ) {
			$(".info-admin-table-aulas tbody").empty();
	 		recuperaAulas();
		}, "json");
	
}

function editaSensor(id_sensor){
	
	var json = "{\"id_sensor\" : "+ id_sensor +", \"nombre\": \""+ $("#"+id_sensor+"-nombreSensor").val() +"\"}"
	
	$.post( "http://localhost:8090/sensor/edita-admin/" + id_sensor, json, 
			function( data ) {
			$(".info-admin-table-sensores tbody").empty();
	 		recuperaSensores();
		}, "json");
	
}