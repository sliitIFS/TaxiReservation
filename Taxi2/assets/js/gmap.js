function initialize() {  
      
  infowindow = new google.maps.InfoWindow(
    { 
      size: new google.maps.Size(150,50)
    });

    var myOptions = {
      zoom: 16,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(document.getElementById("map"), myOptions);

    address = 'Sri Lanka'
    geocoder = new google.maps.Geocoder();
    geocoder.geocode( { 'address': address}, function(results, status) {
     map.fitBounds(results[0].geometry.viewport);

    }); 

//*******************************************************
setTimeout ( "setMapContent()", 2000 );// you can change this value according to your requirement.

function setMapContent(){
$.ajax({url:"http://unibook.byethost15.com/CabCI452/application/views/pages/getlocation.php",success:function(result){

// load map part with new values using result without refreshing the page
  }});

}

//*******************************************************
 
  } 
  function UpdateMap( )
	{
		var geocoder = new google.maps.Geocoder();    // instantiate a geocoder object
		
		// Get the user's inputted address
		var address = document.getElementById( "address" ).value + ", Sri Lanka";
	
		// Make asynchronous call to Google geocoding API
		geocoder.geocode( { 'address': address }, function(results, status) {
			var addr_type = results[0].types[0];	// type of address inputted that was geocoded
			if ( status == google.maps.GeocoderStatus.OK ) 
				ShowLocation( results[0].geometry.location, address, addr_type );
			else     
				alert("Geocode was not successful for the following reason: " + status);        
		});
	}
	
  
  function ShowLocation( latlng, address, addr_type )
	{
		// Center the map at the specified location
		map.setCenter( latlng );
		
		// Set the zoom level according to the address level of detail the user specified
		var zoom = 14;
		switch ( addr_type )
		{
		case "administrative_area_level_1"	: zoom = 9; break;		// user specified a state
		case "locality"						: zoom = 12; break;		// user specified a city/town
		case "street_address"				: zoom = 17; break;		// user specified a street address
		}
		map.setZoom( zoom );
	

	}