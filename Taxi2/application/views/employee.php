
<!--start: Content Container-->
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 ">
                        
                        <!--start: Left Nivagation-->
<!--                         
                        <!--end: Left Nivagation-->

                        <!--start: Dynamic Content-->
                            <div class="col-xs-12 col-sm-12 col-md-9 col-lg-9">
                                <!--start: Dyanamic-->
                                <div class="panel panel-default">
                                  <div class="panel-body">
                                    
                                      <p class="lead">DRIVER</p>
<input type="button" value="Register New Driver" id="btnNewEmp"  class="btn btn-primary btnContect newEvntButton"/>
                                      <div style="width:100%" class="gridDiv table-responsive">
                                          
<!--                                          <a href="#" id="btnNewevent" class="list-group-item">NEW EVENT</a>-->
<table     class="datatable table table-hover">
	<thead>
		<tr>
<!--			<th >Id</th>-->
      <th width="15%">firstname</th>
      <th width="15%">lastname</th>
      <th width="15%">nic</th>
			<th width="15%">email</th>
      <th width="15%">contact</th>
			<th width="15%">address</th>
			<th width="5%">action</th>
                       <th width="5%"></th>
					 
					  
                       
                        
                        
                        
		</tr>
	</thead>
</table>
     
</div>
                                  </div>
                                </div>
                                <!--end: Dynamic-->
                            </div>
                        <!--end: Dynamic Content-->

                    </div>

<script type="text/javascript">
$("li").removeClass("active");
  $("#navEmp").addClass("active");

  dt = $('.datatable').dataTable( {
        "bAutoWidth": true,
        "aLengthMenu": [[10, 20, 50, -1], [10, 20, 50, "All"]],            
        'iDisplayLength':10,
		"aoColumnDefs": [ { "bSortable": false, "aTargets": [ 1,2,3] } ],
        "sAjaxSource": "<?php echo base_url();?>index.php/user/employee_table",
		"bSortClasses": true
    });
$(function() {
    $( document ).tooltip();
  });	

$("#btnNewEmp").click(function(){
window.location.href = "<?php echo base_url();?>index.php/user/add_new";
});





</script>
