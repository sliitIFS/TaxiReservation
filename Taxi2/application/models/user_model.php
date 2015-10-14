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
class user_model extends CI_Model {
   
    public function insert_user($user_array){
		$user_array['flag']=1;
		$user_array['password']= $hash = md5($user_array['password']);
		
		$check_user_query= $this->db->query('SELECT COUNT(*) AS countUser FROM tbl_user WHERE email="'.$user_array['email'].'" ');//print_r('SELECT COUNT(*) AS countUser FROM tbl_user WHERE email="'.$user_array['email'].'" ');exit;
		 $countOfUsers=$check_user_query->result_array();
		 if($countOfUsers[0]['countUser']==0){
			  $result= $this->db->insert('tbl_user', $user_array);
			return true;
		 }else{
			 return false;
		 }
      
    }

	public function get_all_data_employee_infor_data_table(){
		$get_data_query=$this->db->query('SELECT * FROM tbl_user');
		$get_data=$get_data_query->result_array();
		return $get_data;
	}
	
	public function check_new_user($email){
		$check_user_query= $this->db->query('SELECT COUNT(*) AS countUser FROM tbl_user WHERE email="'.$email.'" ');//print_r('SELECT COUNT(*) AS countUser FROM tbl_user WHERE email="'.$user_array['email'].'" ');exit;
		return $check_user_query->result_array();
	}

    public function delele_user($id){
        //$data=array('status'=>0);
        //$this->db->where('id', $id);
        $result=$this->db->delete('tbl_user', array('id' => $id)); 
        return $result;
    }
    
    public function empEdit($id){
            $get_data_query=$this->db->query('SELECT * FROM tbl_user WHERE id='.$id);
        $get_data=$get_data_query->result_array();
        return $get_data;
    }
    
    public function edit_user ($data,$id){
        return $this->db->update('tbl_user', $data, "id = ".$id);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public function save_passenger( $insert_task)
    {

        $insert_task = array(

            'fname' => $this->input->post('fname'),
            'lname' => $this->input->post('lname'),
            'phone' => $this->input->post('phone'),
            'email' => $this->input->post('email'),
            'pass' => $this->input->post('pass')
            
            
        );

        

        
        $result=$this->db->insert('passenger', $insert_task);
        if($result){
             return true;

        }
        else{
            return false;
        }

       

    }

	public function show_passenger(){
		$get_data_query=$this->db->query('SELECT * FROM passenger');
		$get_data=$get_data_query->result_array();
		return $get_data;
	}


    public function passengerdelete($id)

    {

    $this->db->where('id',$id);

    return $this->db->delete('passenger');

    }


    public function passengerEdit($id){
            $get_data_query=$this->db->query('SELECT * FROM passenger WHERE id='.$id);
        $get_data=$get_data_query->result_array();
        return $get_data;
    }
    
    public function edit_passenger ($data,$id){
        return $this->db->update('passenger', $data, "id = ".$id);
    }

    ////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////

	

    public function save_category()
    {

        $insert_category = array(

            'name' => $this->input->post('name'),
            'assign_person' => $this->input->post('assign_person'),
            'date' => $this->input->post('date'),
            'description' => $this->input->post('description')
          
            
        );

        $this->db->insert('category', $insert_category);

        return true;


    }

    

     

    public function showCategory(){
        $get_data_query=$this->db->query('SELECT * FROM category');
        $get_data=$get_data_query->result_array();
        return $get_data;
    }


     public function categorydelete($id)

    {

    $this->db->where('ID',$id);

    return $this->db->delete('category');

    }

    ///////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////

    public function sendMail($insert_mail){
                 
             $insert_mail = array(

            'name' => $this->input->post('name'),
            'mail' => $this->input->post('mail'),
            'emp_id' => $this->input->post('emp_id'),
            'description' => $this->input->post('description'),
            'start_date' => $this->input->post('start_date'),
            'end_date' => $this->input->post('end_date')
            
            
        );

       

           $result=$this->db->insert('mail', $insert_mail);
           if($result){
             return true;

        }
        else{
            return false;
        }

              
    }



    


	
    
 }

?>
