<!--<form class="form-horizontal">-->
    <?php $attributes = array('class' => 'form-horizontal','id'=>"addEmpForm");echo form_open('user/insert_member', $attributes);?>
    <div style="width:30%; border-style: solid;border-width: 1px;  margin: 0 auto;padding: 34px;">
<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
               
                <!--start: Content Container-->
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                       
                        <!--start: Dynamic Content-->
                            <div>
                                <!--start: Dyanamic-->
                                <div class="panel panel-default">
                                  <div class="panel-body">
                                    
                                      <h2>New Driver  </h2>

                                      

                                      <!--start: Form area-->
                                      <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
                                         
                                                <hr>
                                                  <div class="form-group clearfix">
                                                    <label ><small> Position</small></label>
                                                    <div class="col-xs-12">
                                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                            <select id="role" name="role">
                                                                    <option value="0"></option>
                                                                    <option value="1">CEO</option>
                                                                    <option value="2">administrator</option>
                                                                    <option value="3">employee</option>
                                                                    <option value="4">receptionist</option>
                                                                   
                                                            </select>
                                                    </div>
                                                </div>
                                                </div>
                                                 <div class="form-group clearfix">
                                                    <label ><small> First Name</small></label>
                                                    <div class="col-xs-12">
                                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                            <input type="text" id="name"  name="name" class="form-control validateInputy" placeholder="Name">
                                                        </div>
                                                    </div>
                                                </div>

                                                 <div class="form-group clearfix">
                                                    <label ><small> Last Name</small></label>
                                                    <div class="col-xs-12">
                                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                            <input type="text" id="lname"  name="lname" class="form-control validateInputy" placeholder="lname">
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group clearfix">
                                                    <label ><small> NIC</small></label>
                                                    <div class="col-xs-12">
                                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                            <input type="text" id="nic"  name="nic" class="form-control validateInputy" placeholder="nic">
                                                        </div>
                                                    </div>
                                                </div>
                                               
                                                <div class="form-group clearfix">
                                                    <label ><small>Contact Number</small></label>
                                                    <div class="col-xs-12">
                                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                             <input type="text" id="contact"  name="contact" class="form-control validateInputy" placeholder="contact number">
                                                        </div>
                                                    </div>
                                                </div>
                                                  <div class="form-group clearfix">
                                                    <label ><small>Email</small></label>
                                                    <div class="col-xs-12">
                                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                            <input type="Email" name="email" id="email" class="form-control " placeholder="Email">
                                                        </div>
                                                    </div>
                                                </div>
                                                 <div class="form-group clearfix">
                                                    <label ><small>Main Address</small></label>
                                                    <div class="col-xs-12">
                                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                            <textarea name="address" id="address" class="form-control " ></textarea>
                                                        </div>
                                                    </div>
                                                </div>
                                                 <div class="form-group clearfix">
                                                    <label ><small>Password</small></label>
                                                    <div class="col-xs-12">
                                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                                           <input type="text" name="password"  id="password" class="form-control " placeholder="Password">
                                                        </div>
                                                    </div>
                                                </div>
                                            
                                            

  
                                      </div> 
                                      

                                  </div>
                                </div>
                                <!--end: Dynamic-->
                            </div>
                        <!--end: Dynamic Content-->

                    </div>
                <!--end: Content Container-->
            </div>
            <div>
            <input type="button" id="insert" value="Register" style="float: right;margin-right: 15px;  margin: 5px;"  name="event_family" class="btn btn-primary btnContect"/>
<button type="button"  onclick="window.history.back();" style=" margin: 5px;"class="btn btn-danger pull-right ">Back</button>
            </div>
            
            </div>
            <script>
            /*
            
     $('#insert').click(function(){
      $.ajax({
                url  : "<?php echo base_url();?>index.php/user/check_new_user",
                type : "post",
                cache: false ,
                data : { 'email':$('#email').val() },
                 success : function(data){
                         if(data==1){
                            $( "#addEmpForm" ).submit();
                         }  else{
                             alert('Your Email Alrady already exists');
                             return false  ; 
                         }   
                 }
            });
 });        
        */

     $('#insert').click(function(){
         if($('#role').val()!='0'){
            if($('#name').val().length!=0){
              if($('#lname').val().length!=0){
                if($('#nic').val().length!=0){
                if($('#contact').val().length==10){
                    if($('#email').val().length!=0){
                        if($('#address').val().length!=0){
                            if($('#password').val().length!=0){
                                $( "#addEmpForm" ).submit();
                            }else{
                                alert("please fill all fields");
                            }
                        }else{
                            alert("please fill all fields");
                        }
                    }else{
                        alert("please fill all fields");
                    }
                    
                }else{
                    alert("contact should be 10 numbers");
                }
            }else{
                alert("please fill all fields");
            }
          }else{
                alert("please fill all fields");
            }
            }else{
                alert("please fill all fields");
            }
         }else{
             alert("please fill all fields");
         }
 });
        
            </script>
            
            