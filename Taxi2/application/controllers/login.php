<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
class login extends CI_Controller{
  public function index(){
       $this->login_page();
       
           
     

    }
    public function login_page(){
        $this->load->view("header");
        $this->load->view("login");
        $this->load->view("logfooter");
    }  
	
    public function chaeck_user(){ 
		$email = $this->input->post("email");
		$pass = $this->input->post("passWrd");
		$this->load->model('login_model');
        $check=$this->login_model->check_login($email,$pass);
		if($check){
			$data_all=$this->login_model->get_all_login_data($email);
			session_start();
			$_SESSION['id']=$data_all[0]['id'];
			$_SESSION['email']=$data_all[0]['email'];
			$_SESSION['role_id']=$data_all[0]['role_code'];
			$_SESSION['name']=$data_all[0]['Name'];
			
			if($check[0]['flag']==1){
				redirect('/login/change_password_page/','refresh');
			}else if($check[0]['flag']==0){
				redirect('mapController','refresh');
			}
		}else{
			$data['error']="<div class='error'> Please Check Username and Password  </div>";
			$this->load->view("header");
			$this->load->view("login", $data);
			$this->load->view("logfooter");
		}
    }
	
    public function change_password_page(){
    	//$data['error']="<div class='error'> Please Check Username, Current Password and New Password </div>";
		$this->load->view("header");
		$this->load->view("change_pw");//,$data
		$this->load->view("logfooter");
     }
	
	public function change_password(){
		$form_data = $this->input->post();
        $this->load->model('login_model');
        $check=$this->login_model->change_password($form_data);
		if($check){
			redirect('/user/dashboard/','refresh');
		}else{
			redirect('/login/change_password_page/','refresh');
		}
	}

	/*function addtask()
    {
       

                    $this->form_validation->set_rules('name', 'name', 'required|max_length[120]|is_unique[task.name]');            
                    $this->form_validation->set_rules('category', 'category', 'required|max_length[200]');
                    $this->form_validation->set_rules('description', 'description', 'required|max_length[200]');      
                
                    $this->form_validation->set_rules('start_date', 'start_date', 'required|max_length[100]');
                    $this->form_validation->set_rules('end_date', 'end_date', 'required|max_length[550]'); 
                    $this->form_validation->set_rules('emp_id', 'emp_id', 'required|max_length[550]');             
                            
                    $this->form_validation->set_error_delimiters('<br /><span class="error">', '</span>');
                
                    if ($this->form_validation->run() == FALSE) // validation hasn't been passed
                    {
                        $this->load->view('header');
                        $this->load->view('task/add_task');
                      
                    } 
                    else
                    {
                      if($this->task_db->save_task()){
                       
                        redirect('task', 'refresh');
                      }
                      else{
                        echo "<script>alert('Recode is not inserted');</script>";
                      }
    
                    } 
    //    }
      //  else
   //     {
            //If no session, redirect to login page
     //       redirect('login', 'refresh');
      //  }
    }*/


    
////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////  

    

  


////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////

     

//////////////////////////////////////////////////////////////

	}
?>
