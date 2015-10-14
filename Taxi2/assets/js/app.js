var PompeiiValidateInputy={
          Validate:function thatMethod(str){
            //  alert(str.length);
    if((str.length)==0){
       return false;
    }else{
        
         return true;
    }
 }
      }
$(document).ready(function() {
$("#homeOpction").dialog({
                autoOpen: false,
                modal: false,
                
               // title:"frmSwitch",
                width:($(window).width()/100)*30,
                height:($(window).height()/100)*30,
               // position: [350,100],
                resize:false,
                resizable: false,
                open: function(ev, ui){
                },
                close: function(event, ui){  $(this).remove();}
            }).dialog('open'); 
              parent.$("#homeOpction").dialog().parents(".ui-dialog").find(".ui-dialog-titlebar").remove();
              
      $('#navUser').click(function(){
          $( "#navEvent" ).removeClass( "active" );

      }) ;   
     
 
});