<html>
<head><?php echo $map['js']; ?></head>
<body>
<form action="<?=site_url('map/direction')?>" method="post">
<label for="place1">Start Place</label><input type="text" name="place1" id="myPlaceTextBox"/><br />
<label for="place2">End Place</label><input type="text" name="place2"/><br />
<input type="submit" name="submit" value="Diection"/>
</form>
	<?php echo $map['html']; ?>
	<div id="directionsDiv"></div>
</body>
</html>