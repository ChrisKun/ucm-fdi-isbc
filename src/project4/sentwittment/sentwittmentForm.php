<!doctype html>
<html>
    <head>
        <title>Sentwittment Form</title>        
		<meta charset="utf-8"> 
		<link rel="stylesheet" href="stylesheets/style_form.css">		
    </head>
    <body>
	<?php
		// We need session variables to export the variables between formularies
		session_start();
		main();
	?>
    </body>
</html>

<?php //Main Program
function main() {
	require_once 'formulary.php';
	$values = array();
	$values = form();	
}
?>