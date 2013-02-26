<!doctype html>
<html>
    <head>
        <title>Sentwittment</title>        
		<meta charset="utf-8">
        <link rel="stylesheet" href="stylesheets/sentwittment_form.css">
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

    // the label for the "query" element
    $form->add('label', 'label_query', 'query', 'Query');
    // add the "query" element
    $obj = & $form->add('text', 'query', '', array('autocomplete' => 'off'));
    // set rules
    $obj->set_rule(array(
        // error messages will be sent to a variable called "error", usable in custom templates
        'required'  =>  array('error', 'Se necesita una query'),
    ));

    // "Return Per page"
    $form->add('label', 'label_returnpp', 'returnpp', 'Numero de Tweets');
    $obj = & $form->add('text', 'returnpp', '', array('autocomplete' => 'off'));
    $obj->set_rule(array(
		'number'    =>  array('', 'error', 'El valor ha de ser un entero'),
		'length' 	=> 	array(0,3, 'error', 'El numero es demasiado grande'),
    ));

	// "Result Type"
	$form->add('label', 'label_type', 'type', 'Tipo de resultado:');
    $obj = & $form->add('select', 'type', 'mixed');
    $obj->add_options(array(
        'mixed'		=> 'Mixto',
		'recent' 	=> 'Reciente',
		'popular' 	=> 'Popular',
    ),true);
	
    // "submit"
    $form->add('submit', 'btnsubmit', 'Submit');
    
    // if the form is valid
    if ($form->validate()) {
		$values = array();
		foreach ($_POST as $key => $value) {
			if (strpos($key, 'name_') !== 0 && strpos($key, 'timer_') !== 0 && strpos($key, 'response_') !== 0) {
				$values[$key] = $value;
				//echo $key . " " . $values[$key] . "<br>"; 				
			}
		}
		$_SESSION["values"] = $values;
		header("Location: sentwittment.php");		
	// otherwise
    } else
		// generate output using a custom template
		$form->render('*horizontal');
}
?>