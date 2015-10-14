<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" >
        <LINK REL="SHORTCUT ICON" HREF="<?php echo base_url();?>assets/img/favicon1.ico">
        <title>Quickab Reservation System</title>
      
        
        <script type="text/javascript" src="<?php echo base_url();?>assets/js/jquery.js"></script>
        <script type="text/javascript" src="<?php echo base_url();?>assets/js/bootstrap.min.js"></script>

<script type="text/javascript" charset="utf-8" src="<?php echo base_url();?>assets/js/DataTables/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<?php echo base_url();?>assets/js/jquery-ui-1.10.3/ui/jquery-ui.js"></script>
<script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyDY0kkJiTPVd2U7aTOAwhc9ySH6oHxOIYM&sensor=false"></script>
<!--<script type="text/javascript" src="<?php echo base_url();?>assets/js/jquery.mobile-1.4.2.min.js"></script>-->
<script type="text/javascript" src="<?php echo base_url();?>assets/js/chosen.jquery.min.js"></script>
<script type="text/javascript" src="<?php echo base_url();?>assets/js/app.js"></script>
<script type="text/javascript" src="<?php echo base_url()?>assets/js/bootstrap-timepicker-gh-pages/js/bootstrap-timepicker.min.js"></script>

<!--<script src="<?php base_url()?>assets/js/lightbox.min.js"></script>-->
<link rel="stylesheet" href="<?php echo base_url();?>assets/js/jquery-ui-1.10.3/themes/base/jquery-ui.css">
<link rel="stylesheet" href="<?php echo base_url();?>assets/js/DataTables/media/css/demo_table.css">
<link rel="stylesheet" href="<?php echo base_url();?>assets/css/bootstrap.css">
<link rel="stylesheet" href="<?php echo base_url();?>assets/css/animate.css">
<link rel="stylesheet" href="<?php echo base_url();?>assets/css/chosen.min.css">
<!--<link rel="stylesheet" href="<?php echo base_url();?>assets/css/jquery.mobile-1.4.2.min.css">-->
<link rel="stylesheet" href="<?php echo base_url();?>assets/css/style.css">
<link href="<?php echo base_url();?>assets/css/lightbox.css" rel="stylesheet" />
    </head>
    <body >
   <?php session_start();?>
        <div class="navbar navbar-inverse">
  <div class="navbar-header">
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
      <p class="navbar-brand" style="font-style:italic;font-weight: 900;">Quickab Reservation System</p>
  </div>
  <div class="navbar-collapse collapse navbar-responsive-collapse">
    <ul class="nav navbar-nav">

     
	  <?php if($_SESSION['role_id']==1){?>
      <li  id="navEmp"><a href="<?php echo base_url();?>index.php/user/employee_page">DRIVER</a></li>
	  <?php }?>

   

    <?php if($_SESSION['role_id']==1){?>
      <li  id="navPass"><a href="<?php echo base_url();?>index.php/mapController/">VIEW MAP</a></li>
    <?php }?>
    
   
    </ul>

    <ul class="nav navbar-nav navbar-right">
        <li class="dropdown">
                              <a href="#" class="dropdown-toggle" data-toggle="dropdown">Welcome <?php  echo $_SESSION['name'].'&nbsp;&nbsp;&nbsp;|'; ?> <b class="caret"></b></a>
                              <ul class="dropdown-menu">
                                <li> <a href="<?php echo base_url();?>index.php/login">LOG OUT</a></li>
                                
                              </ul>
                            </li>
      
    </ul>
  </div>
</div>