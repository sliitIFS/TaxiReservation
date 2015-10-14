
<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');
class mapController extends CI_Controller {
 function __construct()
 {
 parent::__construct();
 }
 function index()
 {
 // Load the library
 $this->load->view("user_header"); 
 $this->load->library('googlemaps');
 // Load our model
 $this->load->model('map_model', '', TRUE);
 // Initialize the map, passing through any parameters
 $config['center'] = 'Colombo,Sri Lanka';
 $config['zoom'] = "auto";
 $this->googlemaps->initialize($config);
 // Get the co-ordinates from the database using our model
 $coords = $this->map_model->get_coordinates_avail();
  $coords2 = $this->map_model->get_coordinates_unavail();
 // Loop through the coordinates we obtained above and add them to the map
 foreach ($coords as $coordinate) {
 $marker = array();
 $marker['position'] = $coordinate->Latitude.','.$coordinate->Longitude;
 $marker['infowindow_content']='<b>Driver Name :</b>'.$coordinate->FName.' '.$coordinate->LName.'<br /><b>Driver NIC :</b>'.$coordinate->NIC.'<br /><b>Phone No :</b>'.$coordinate->Phone_No.'<br /><b>Vehicle Type :</b>'.$coordinate->Type.'<br/><b>A/C :</b>'.$coordinate->AC.'<br/><b>No of Seats :</b>'.$coordinate->Seats;
 //$marker['icon']=' http://maps.google.com/mapfiles/ms/icons/blue.png';
 $marker['icon']=' http://taxires.site90.com/taxi/taxigreen.png';
 $this->googlemaps->add_marker($marker);
 }
  foreach ($coords2 as $coordinate) {
 $marker = array();
 $marker['position'] = $coordinate->Latitude.','.$coordinate->Longitude;
 $marker['infowindow_content']='<b>Driver Name :</b>'.$coordinate->FName.' '.$coordinate->LName.'<br /><b>Driver NIC :</b>'.$coordinate->NIC.'<br /><b>Phone No :</b>'.$coordinate->Phone_No.'<br /><b>Vehicle Type :</b>'.$coordinate->Type.'<br/><b>A/C :</b>'.$coordinate->AC.'<br/><b>No of Seats :</b>'.$coordinate->Seats;
 //$marker['icon']=' http://maps.google.com/mapfiles/ms/icons/red.png';
  $marker['icon']=' http://taxires.site90.com/taxi/taxired.png';
 $this->googlemaps->add_marker($marker);
 }
 // Create the map
 $data = array();
 $data['map'] = $this->googlemaps->create_map();
 // Load our view, passing through the map data
//$this->load->view("header");
 $this->load->view('map', $data);


   
 }



}