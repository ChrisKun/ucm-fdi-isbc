<!doctype html>
<html>
    <head>
        <title>Sentwittment</title>        
		<meta charset="utf-8">
        <link rel="stylesheet" type="stylesheet" href="stylesheets/zebra_form.css">
    </head>
    <body>
	<?php
		main();
	?>
	<div id="chart_values"></div>
	<div id="chart_types"></div>
    </body>
</html>

<?php //Main Program
session_start();
function main() {
	require_once 'formulary.php';
	$values = array();
	$values = form();	
}
?>