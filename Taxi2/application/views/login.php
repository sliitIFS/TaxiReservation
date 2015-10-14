 <?php if(isset($error)){ 
 ?>
  <div style="width:100%;text-align: center;color: red;">
  <?php echo $error; ?>
  </div>
 <?php
 }?>
<div style=" margin-top: 100px;" class="container col-xs-12 col-sm-6 col-md-offset-3 col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-4 mainForm">
    <div class="content panel panel-default loginMain">
      <div class="row">
        
		<div class="login-form panel-heading">
          <h2>Login</h2>
           <?php echo form_open('login/chaeck_user');?>
            <fieldset >
                                  <div id="maindiv" class="panel-body">
                                      <div class="clearfix">
                       
                       <input type="email" class="form-control" id="email_id" name="email" placeholder="Username">
                      </div>
                      <div class="clearfix">
                        
                        <input type="password" class="form-control" id="passWrd_id" name="passWrd" placeholder="Password">
                      </div>
              
                  <input type="submit" id="login_Id" name="login" class="btn  btn-primary" value="Sign in">
<!--              <button class="btn  btn-primary" type="submit">Sign in</button>-->
              </div>
            </fieldset>
          </form>
		  <a href="<?php echo base_url();?>index.php/login/change_password">Change password</a>
        </div>
      </div>
    </div>
  </div>
   
<script type="text/javascript">
   
</script>
 </form>

 

 
