<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of login
 *
 * @author Administrator
 */
class login_model extends CI_Model {
   
    public function check_login($email,$pass){
		$hash_password = md5($pass);
        $data= $this->db->query('SELECT COUNT(*) AS log FROM tbl_user WHERE email="'.$email.'" AND password="'.$hash_password.'"');
		$count= $data->result_array();
			if($count[0]['log']==1){
				$chekUser= $this->db->query('SELECT flag  FROM tbl_user WHERE email="'.$email.'" AND password="'.$hash_password.'"');
				return $chekUser->result_array();
			}else{
				return false ;
			}
    }
    public function get_all_login_data($email){
        $data= $this->db->query('SELECT * FROM tbl_user WHERE email="'.$email.'"');
       return $data->result_array();
    }
	 public function change_password($data){
		 $hash = md5($data['c_passWrd']);
		 $check_sql= $this->db->query('SELECT COUNT(*) AS log FROM tbl_user WHERE email="'.$data['email'].'" AND password="'.$hash.'"');
		 $result=$check_sql->result_array();
		 if($result[0]['log']==1){
			 $this->db->where('email', $data['email']);
			$result=$this->db->update('tbl_user', array('password'=>md5($data['n_passWrd']),'flag'=>0));
			return $result;
		 }else {
			 return false ;
		 }
       //print_r($data->result_array());exit;
	   
	 }
	
	
	
	
}

?>
