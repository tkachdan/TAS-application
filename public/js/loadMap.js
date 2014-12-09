/**
 * Created by tkachdan on 13-Nov-14.
 */
/*function init ( ) {
    var mapDiv = document.getElementById ( "mymap" ) ;
    var mapOptions = {
        center : new google.maps.LatLng ( 50.0833, 14.4167 ),
        zoom : 13,
        mapTypeId: google.maps.MapTypeId.HYBRID
    } ;
    var map = new google.maps.Map ( mapDiv, mapOptions ) ;
}
window.onload = init ;*/

var rendererOptions = {
    draggable: true
};
var directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
var directionsService = new google.maps.DirectionsService();
var map;

var czechRepublic = new google.maps.LatLng(49.7500, 15.7500);

function initialize() {

    var mapOptions = {
        zoom: 7,
        center: czechRepublic
    };
    map = new google.maps.Map(document.getElementById('mymap'), mapOptions);
    directionsDisplay.setMap(map);
    directionsDisplay.setPanel(document.getElementById('directionsPanel'));

    google.maps.event.addListener(directionsDisplay, 'directions_changed', function() {
        computeTotalDistance(directionsDisplay.getDirections());
    });

    calcRoute();
}

function calcRoute() {

    var request = {
        origin: 'Prague, Czech Republic',
        destination: 'Brno, Czech Republic',
        waypoints:[{location: 'Paris, France'}, {location: 'Berlin, Germany'}],
        travelMode: google.maps.TravelMode.DRIVING
    };
    directionsService.route(request, function(response, status) {
        if (status == google.maps.DirectionsStatus.OK) {
            directionsDisplay.setDirections(response);
        }
    });
}

function computeTotalDistance(result) {
    var total = 0;
    var myroute = result.routes[0];
    for (var i = 0; i < myroute.legs.length; i++) {
        total += myroute.legs[i].distance.value;
    }
    total = total / 1000.0;
    document.getElementById('total').innerHTML = total + ' km';
}

google.maps.event.addDomListener(window, 'load', initialize);
