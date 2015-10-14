<html>
<head>
	<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		function showCoords(newLat, newLng)
		{
		$('.ll_preview').html("lat:"+newLat+", lang:"+newLng);
		}
	</script>
	<?php echo $map['js']; ?>
</head>
<body><?php echo $map['html']; ?>
<div class="ll_preview"></div></body>
</html>