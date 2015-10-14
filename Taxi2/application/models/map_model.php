<?php
class Map_model extends CI_Model {
 function __construct()
 {
 parent::__construct();
 }
 function get_coordinates_avail()
 {
 $return = array();
// $get_data_query=$this->db->query("SELECT MAX(a.Availability_Id),d.Certification ,d.FName,d.LName,d.NIC,d.Phone_No,a.Latitude,a.Longitude,v.Type,v.AC,v.Seats FROM driver d,availability a,vehicle v where a.Availability='AVAILABLE' and d.NIC=a.Driver_NIC ");
 //$get_data=$get_data_query->result_array();
 $query = $this->db->query("SELECT MAX(a.Availability_Id),d.Certification ,d.FName,d.LName,d.NIC,d.Phone_No,a.Latitude,a.Longitude,v.Type,v.AC,v.Seats FROM driver d,availability a,vehicle v where a.Availability='AVAILABLE' and d.NIC=a.Driver_NIC  and a.Driver_NIC=v.Driver_NIC ");

if ($query->num_rows() > 0)
{
   foreach ($query->result() as $row)
   {
   	array_push($return, $row);
   }
}
return $return;

 }
  function get_coordinates_unavail()
 {
 $return = array();
// $get_data_query=$this->db->query("SELECT MAX(a.Availability_Id),d.Certification ,d.FName,d.LName,d.NIC,d.Phone_No,a.Latitude,a.Longitude,v.Type,v.AC,v.Seats FROM driver d,availability a,vehicle v where a.Availability='AVAILABLE' and d.NIC=a.Driver_NIC ");
 //$get_data=$get_data_query->result_array();
 $query = $this->db->query("SELECT Max(a.Availability_Id),d.Certification ,d.FName,d.LName,d.NIC,d.Phone_No,a.Latitude,a.Longitude,v.Type,v.AC,v.Seats FROM driver d,availability a,vehicle v where a.Availability='UNAVAILABLE' and d.NIC=a.Driver_NIC  and a.Driver_NIC=v.Driver_NIC and a.Driver_NIC not in(SELECT a.Driver_NIC FROM availability a where a.Availability='AVAILABLE'
having MAX(a.Availability_Id))
group by a.Driver_NIC
");

if ($query->num_rows() > 0)
{
   foreach ($query->result() as $row)
   {
   	array_push($return, $row);
   }
}
return $return;

 }
}