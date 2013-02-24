<!doctype html>
<html>
    <head>
        <title>Sentwittment</title>        
		<meta charset="utf-8">
        <link rel="stylesheet" href="forms/public/css/zebra_form.css">
    </head>
    <body>
	<center>
	<?php
		main();
	?>
	</center>
	<!-- we're loading the JavaScript files at the bottom of the page so we don't delay page rendering -->

	<!-- try to load jQuery from CDN server and fallback to local source if not available -->
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script>window.jQuery || document.write('<script src="path/to/jquery-1.8.2.min.js"><\/script>')</script>

	<!-- load Zebra_Form's JavaScript file -->
	<script src="forms/public/javascript/zebra_form.js"></script>
    </body>
</html>

<?php //Main Program
function main() {
	require_once 'formulary.php';
	$values = array();
	$values = form();
	// Stemming
	require_once '/stemmer/Spanish.php';
	// Loads the lexicon
	$lexicon = loadLexicon("lexicon.txt");
	if (!empty($values["query"]))
	{
		//$query = getQueryFromForm();
		$filename = "http://search.twitter.com/search.json?".$query;
		$json = file_get_contents($filename, true);
		$decode = json_decode($json, true);

		echo "<pre>";
		$count = count($decode["results"]);
		for($i=0;$i<$count;$i++){
			$tweet = $decode["results"][$i]["text"];
			$value = tweetValue($lexicon, $tweet);    
		}
		echo "</pre>";	
	}
}
// Functions

/**
    Loads the lexicon given by $file
*/
function loadLexicon($file){
	static $lexicon = NULL;	
	if (!is_null($lexicon)) return $lexicon;
	$file = fopen($file, "r") or exit("Unable to open file!");
	$lexicon = array();
	$pos = 0;
	while(!feof($file)){
		addStringToLexicon($lexicon, fgets($file));
		$pos++;
	}
	fclose($file);
	return $lexicon;
}

/**
    Adds info of $string in $lexicon
	All the strings in the lexicon have the same two beginning parts: word & number in wordnet
	We don't use the wordnet number
	The resultant array is like: array['lexicon'] = ['stem' => X, 'neg' => Y, 'pos' => Z]
*/
function addStringToLexicon(&$lexicon,$string){
	$string = trim($string);
	$parts = explode("\t",$string);
	$stem = spanish::stemm($parts[0]);
	$lexicon[$parts[0]] = array("stem" => $stem, "neg" => 0, "pos" => 0);
	for ($i=2;$i<count($parts);$i++) {
		if (strcmp($parts[$i], "neg") == 0)
			$lexicon[$parts[0]]["neg"] = $lexicon[$parts[0]]["neg"]+1;
		else if (strcmp($parts[$i], "pos") == 0)
			$lexicon[$parts[0]]["pos"] = $lexicon[$parts[0]]["pos"]+1;
	}
	echo $parts[0] . " [" . $stem . " , " . $lexicon[$parts[0]]["neg"] . " , " . $lexicon[$parts[0]]["pos"] . "] " . "<br>"; 
}

/**
Get the neccessary info from forms and return a string with the configured query
*/
function getQueryFromForm(){
	// Query
	$query = "q=" . $_POST["query"];
	// Return Per Page
	if (!empty($_POST["rpp"])){
		$query .= "&rpp=".$_POST["rpp"];
	} else {
		$query .= "&rpp=25";
	}
	// Result Type
	switch ($_POST["result_type"]) {
			case 1:
				$query .= "&result_type=recent"; break;
			case 2:
				$query .= "&result_type=popular"; break;
	}
	echo $query ."<br>";
	return $query;
	
	/***
	Opcionales:
		He visto que tenemos la opcion de acceder al geocode. Podriamos agrupar por zonas si no es mucha movida
	*/
}

/**
    Removes from string all characters aren't representing words
*/
function cleanString($string){
	//Patterns of sequences we want to remove.
    echo utf8_decode($string) . "<br>";
    $string = utf8_decode($string);
    $string = strtolower($string);
    $patterns = array();
    $patterns[0] = '#http://[a-zA-Z0-9.\/]*#';
    $patterns[1] = '#\d#';
    $patterns[2] = '#_|-|\+|\(|\)|\'|\"|,|;|\.|:|@|=|<|>|!|\?|\|ª|´"#';

    $string = preg_replace($patterns, "", $string);
	//Removing unnecessary blanks
    $string = preg_replace("#(\n|\t|\ )+#", " ", $string);
    //$string = preg_replace("#\s#", " ", $string);
    $string = trim($string);

    return $string;
}

/**
    Consults a $string in $lexicon and returns a value
*/
function consultLexicon($lexicon, $string){
	$value = 0;
	//Check if string is in Lexicon
	if (array_key_exists($string,$lexicon)) {
		$value += $lexicon[$string]["pos"];
		$value -= $lexicon[$string]["neg"];
		echo 'si' . " " . $string . "<br>";
	// If it isn't, then apply stemming
	} else if (strlen($string) > 2){
		$stem = spanish::stemm($string);
		$value = checkStem($stem, $lexicon);		
	} 
	return $value;
}

/**
    Consults a $tweet in $lexicon and returns total value of $tweet
	NOTA: seria la leche poder eliminar los acentos en la cadena global, pero no se porque me los cambia a cosas del estilo: A?a
*/
function tweetValue($lexicon, $tweet){		
	$textCleaned = cleanString($tweet);
	$words = explode(" ",$textCleaned); print_r($words);
	$value = 0;
	$count = count($words);
	for($i=0;$i<$count;$i++){
		$words[$i] = stripAccents($words[$i]);
		$value += consultLexicon($lexicon, $words[$i]);
	}
	if($value > 0) $value = 1;
	else if ($value < 0) $value = -1;
	//echo $value . "<br>";	
	return $value;
}

/************************ AUXILIARY FUNCTIONS ************************/ 

/**
	Replaces the accents and the Ò (in the lexicon the words doesn't have Ò, instead there are words with n)
	Expample: daÒino -> danino
*/
function stripAccents($string){
	$tofind = "¿¡¬√ƒ≈‡·‚„‰Â“”‘’÷ÿÚÛÙıˆ¯»… ÀËÈÍÎ«ÁÃÕŒœÏÌÓÔŸ⁄€‹˘˙˚¸ˇ—Ò";
	$replac = "AAAAAAaaaaaaOOOOOOooooooEEEEeeeeCcIIIIiiiiUUUUuuuuyNn";
	return(strtr($string,$tofind,$replac));
}

/**
	Checks if $stem exists in $lexicon and operates the values of all the words in the lexicon with that stem
*/
function checkStem($stem, $lexicon){
	$value = 0;
	foreach ($lexicon as &$content) {
		if (strcmp($stem, $content["stem"]) == 0) {
			echo $stem . " raiz " . "si" . "<br>";
			$value += $content["pos"];
			$value -= $content["neg"];
		}
	}
	return $value;
}
?>