<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
class user extends CI_Controller{
	
	public function dashboard(){
		//session_start();
		$this->load->view("user_header");
		$this->load->view("employee");
		$this->load->view("logfooter");
	}

  public function myMain(){
    //session_start();
    $this->load->view("user_header");    
    $this->load->view("logfooter");
  }



	public function index(){
       $this->login_page();
      


    }

    public function show_map(){
    $this->load->view("header");
    $this->load->view("map");
  //  $this->load->controller("map");
    $this->load->view("logfooter");
     }  
	
	public function add_new(){
        $this->load->view("header");
		$this->load->view("add_new");
		$this->load->view("logfooter");
     }	
  
	public function insert_member(){ 
		$form_data = $this->input->post();
		$user_array['role_code']=$form_data['role'];
		$user_array['name']=$form_data['name'];
    $user_array['lname']=$form_data['lname'];
    $user_array['nic']=$form_data['nic'];
		$user_array['contact']=$form_data['contact'];
		$user_array['email']=$form_data['email'];
		$user_array['address']=$form_data['address'];
		$user_array['password']=$form_data['password'];

    if (filter_var($user_array['email'], FILTER_VALIDATE_EMAIL)) {

    $this->load->model('user_model');
        $insert=$this->user_model->insert_user($user_array);    
    if($insert){
      redirect('/user/employee_page/','refresh');
    }else{
      redirect('/user/add_new/','refresh');
    }
  
}
  else{
      print '<script type="text/javascript">'; 
            print 'alert("The email address '.$user_array['email'].' is incorrect")'; 
            print '</script>';  

    }

    
    
  }

	
	public function employee_page(){
		$this->load->view("user_header");
		$this->load->view("employee");
		$this->load->view("logfooter");
	}
	
	public function employee_table(){
		$this->load->model('user_model');
        $all_employee =$this->user_model->get_all_data_employee_infor_data_table();
        $new_data = array();   

        foreach ($all_employee as $c) {
            
            $new_data[] = array(
                '<div class="gridId">'.$c['Name'].'</div>' ,
                '<div class="gridId">'.$c['lname'].'</div>' ,
                '<div class="gridId">'.$c['nic'].'</div>' ,
                '<div class="gridId">'.$c['email'].'</div>' ,
                '<div class="gridId">'.$c['contact'].'</div>' ,
                '<div class="gridId">'.$c['address'].'</div>',
                '<div class="gridId" ><a href="edit_user/'.$c['id'].'" id="close_btn" title="Edit" class="btn btn-success pull-right gridButton "><span class="glyphicon glyphicon-edit"></span></a></div>',
                '<div class="gridId delete_Admin_grid" id="'.$c['id'].'" ><a href="delete/'.$c['id'].'" title="Delete"  id="close_btn" class="btn btn-danger pull-right gridButton "><span class="glyphicon glyphicon-trash"></span></a></div>',
                
            );
        }  




        

        $json = array(
          
            'aaData' => $new_data,
        );

        echo json_encode($json);
        exit;
	}

  

    public function delete($id)
    {
      
        $this->load->model('user_model');
        $this-> user_model->delele_user($id);
        redirect('/user/employee_page/','refresh');
      
       
        }
      
     
    

    public function edit_user($id){
    $this->load->model('user_model');
        $data['infor'] =$this->user_model->empEdit($id);
    
        $this->load->view("header");
    $this->load->view("edit",$data);
    $this->load->view("logfooter");
     }
   
   public function edit_member(){ 
    $form_data = $this->input->post();
    $user_array['role_code']=$form_data['role'];
    $user_array['name']=$form_data['name'];
    $user_array['lname']=$form_data['lname'];
    $user_array['nic']=$form_data['nic'];
    $user_array['contact']=$form_data['contact'];
    //$user_array['email']=$form_data['email'];
    $user_array['address']=$form_data['address'];
    $user_array['password']=md5($form_data['password']);
    $id=$form_data['id'];//print_r($user_array);exit;
    $this->load->model('user_model');
        $update=$this->user_model->edit_user($user_array,$id);    
    if($update){
      redirect('/user/employee_page/','refresh');
    }else{
      redirect('/user/add_new/','refresh');
    }
  }
	
	public function check_new_user(){
		$email = $this->input->post("email");
		$this->load->model('user_model');
        $result=$this->user_model->check_new_user($email);
		if($result[0]['countUser']==1){
			echo 1;exit;
		}else{
			echo 0;exit;
		}
	}
  ////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////
   public function dashboard_passenger(){
    //session_start();
    $this->load->view("user_header");
    $this->load->view("passenger");
    $this->load->view("logfooter");
  }

  public function add_new_passenger(){
        $this->load->view("header");
    $this->load->view("add_passenger");
    $this->load->view("logfooter");
     }  
  
  public function insert_member_pass(){ 
    $form_data = $this->input->post();
    
    $user_array['fname']=$form_data['fname'];
    $user_array['lname']=$form_data['lname'];    
    $user_array['phone']=$form_data['phone'];
    $user_array['email']=$form_data['email'];    
    $user_array['pass']=$form_data['pass'];

    if (filter_var($user_array['email'], FILTER_VALIDATE_EMAIL)) {

    $this->load->model('user_model');
        $insert=$this->user_model->save_passenger($user_array);    
    if($insert){
      redirect('/user/employee_page_pass/','refresh');
    }else{
      redirect('/user/add_new_passenger/','refresh');
    }
  
}
  else{
      print '<script type="text/javascript">'; 
            print 'alert("The email address '.$user_array['email'].' is incorrect")'; 
            print '</script>';  

    }

    
    
  }

  
  public function employee_page_pass(){
    $this->load->view("user_header");
    $this->load->view("passenger");
    $this->load->view("logfooter");
  }
  
  public function passenger_table(){
    $this->load->model('user_model');
        $all_employee =$this->user_model->show_passenger();
        $new_data = array();   

        foreach ($all_employee as $c) {
            
            $new_data[] = array(
                '<div class="gridId">'.$c['fname'].'</div>' ,
                '<div class="gridId">'.$c['lname'].'</div>' ,   
                '<div class="gridId">'.$c['phone'].'</div>' ,             
                '<div class="gridId">'.$c['email'].'</div>' ,        
               
                '<div class="gridId" ><a href="edit_user_pass/'.$c['id'].'" id="close_btn" title="Edit" class="btn btn-success pull-right gridButton "><span class="glyphicon glyphicon-edit"></span></a></div>',
                '<div class="gridId delete_Admin_grid" id="'.$c['id'].'" ><a href="delete_pass/'.$c['id'].'" title="Delete"  id="close_btn" class="btn btn-danger pull-right gridButton "><span class="glyphicon glyphicon-trash"></span></a></div>',
                
            );
        }  




        

        $json = array(
          
            'aaData' => $new_data,
        );

        echo json_encode($json);
        exit;
  }

 // public function delete_record_pass($id){
     //print_r($id);exit;
   //     $this->load->model('user_model');
    //    $result=$this->user_model->delele_user($id);
    //    if($result){
     //        redirect('/user/employee_page_pass/', 'refresh');
      //  }
 //   }

    public function delete_pass($id)
    {
       
        $this->load->model('user_model');
        $this-> user_model->passengerdelete($id);
        redirect('/user/employee_page_pass/','refresh');
      
        
        }
  
    public function edit_user_pass($id){
    $this->load->model('user_model');
    $data['infor'] =$this->user_model->passengerEdit($id);
    
    $this->load->view("header");
    $this->load->view("edit_passenger",$data);
    $this->load->view("logfooter");
     }
   
   public function edit_member_pass(){ 
    $form_data = $this->input->post();   
    $user_array['fname']=$form_data['fname'];
    $user_array['lname']=$form_data['lname'];    
    $user_array['phone']=$form_data['phone'];
    //$user_array['email']=$form_data['email'];
   
    $user_array['pass']=md5($form_data['pass']);
    $id=$form_data['id'];//print_r($user_array);exit;
    $this->load->model('user_model');
        $update=$this->user_model->edit_passenger($user_array,$id);    
    if($update){
      redirect('/user/employee_page_pass/','refresh');
    }else{
      redirect('/user/add_new_passenger/','refresh');
    }
  }


}    

	
	


?>
