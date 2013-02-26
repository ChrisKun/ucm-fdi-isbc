<!doctype html>
<html>
    <head>
        <title>Sentwittment</title>        
		<meta charset="utf-8">
        <link rel="stylesheet" href="stylesheets/back_form.css">
    </head>
    <body>
	<!-- we're loading the JavaScript files at the bottom of the page so we don't delay page rendering -->
	<!-- try to load jQuery from CDN server and fallback to local source if not available -->
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="path/to/jquery-1.8.2.min.js"><\/script>')</script>
	<!-- load Zebra_Form's JavaScript file -->
	<script src="forms/public/javascript/zebra_form.js"></script>
    </body>
</html>

<?php
/**
	Sets the formulary and returns the values of the fields
*/
function form() {
	require_once '/forms/Zebra_Form.php';
    // instantiate a Zebra_Form object
    $form = new Zebra_Form('form');

    // "submit"
    $form->add('submit', 'btnBack', 'Volver a generar otra consulta');
    
    // if the form is valid
    if ($form->validate()) {
		header("Location: sentwittmentForm.php");
		exit;
	// otherwise
    } else{
		// generate output using a custom template
		$form->render('*horizontal');
	}
}
?>