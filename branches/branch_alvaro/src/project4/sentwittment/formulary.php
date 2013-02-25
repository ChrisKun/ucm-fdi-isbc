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
    $form->add('label', 'label_returnpp', 'returnpp', 'Tweets Per Page');
    $obj = & $form->add('text', 'returnpp', '', array('autocomplete' => 'off'));
    $obj->set_rule(array(
		'number'    =>  array('', 'error', 'El valor ha de ser un entero'),
		'length' 	=> 	array(0,3, 'error', 'El numero es demasiado grande'),
    ));

	// "Result Type"
	$form->add('label', 'label_type', 'type', 'Result Type:');
    $obj = & $form->add('select', 'type', 'mixed');
    $obj->add_options(array(
        'mixed'		=> 'Mixed',
		'recent' 	=> 'Recent',
		'popular' 	=> 'Popular',
    ),true);
	
    // "submit"
    $form->add('submit', 'btnsubmit', 'Submit');
    
    // if the form is valid
    if ($form->validate()) {
		$values = array();
		foreach ($_POST as $key => $value) {
			if (strpos($key, 'name_') !== 0 && strpos($key, 'timer_') !== 0 && strpos($key, 'response_') !== 0) {
				$values[$key] = array("value" => $value);
				echo $key . " " . $values[$key]["value"] . "<br>"; 				
			}
		}
		form();
		return $values;
	// otherwise
    } else
		// generate output using a custom template
		$form->render('*horizontal');
}
?>