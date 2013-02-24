<html>
    <head>
        <title>Sentwittment</title>
        <link rel="stylesheet" href="style.css">		
    </head>
    <body>
        <form action = "sentwittment.php" method="post">
            Query: <input type="text" name="query"><br>
            ReturnPerPage: <input type="int" name="rpp"><br>
            Result Type: <select name="result_type">
                <option value="0" selected>Mixed</option>
                <option value="1">Recent</option>
                <option value="2">Popular</option>
            </select><br>
            <input type="submit">
        </form>
		<div id="chart_values"></div>
		<div id="chart_types"></div>
    </body>
</html>

<?php //Main Program
require_once '/stemmer/Spanish.php';
$lexicon = loadLexicon("lexicon.txt");

if (!empty($_POST["query"]))
{
	$query = getQueryFromForm();
	$filename = "http://search.twitter.com/search.json?".$query;
	$json = file_get_contents($filename, true);
	$decode = json_decode($json, true);

	echo "<pre>";
	$count = count($decode["results"]);
	for($i=0;$i<$count;$i++){
		$tweet = $decode["results"][$i]["text"];
        $value = tweetValue($lexicon, $tweet);   
		$values[] = $value;
	}
	
	drawChartTweetsValues($values);
	drawChartTweetsTypes($values);
}
?>

<?php // Functions

/**
    Loads the lexicon given by $file
*/
function loadLexicon($file){
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
	//echo $parts[0] . " [" . $stem . " , " . $lexicon[$parts[0]]["neg"] . " , " . $lexicon[$parts[0]]["pos"] . "] " . "<br>"; 
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
	echo $value . "<br>";	
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

function drawChartTweetsValues($values){
	include('/google_chart/Chart.php');
	
	//number of tweets
	$num = count($values);
	
	// chart type
	$chart = new Chart('LineChart');

	$data = array(); // new array
	
	// titles
	$data[0] = array('tweet','vote');	
	
	// function format
	for($i=0;$i<$num;$i++){
		$data[$i+1] = array($i,$values[$i]);
	}

	$chart->load($data, 'array');

	$options = array('title' => 'Tweets Values', 'is3D' => true, 'width' => 400, 'height' => 300);
	echo $chart->draw('chart_values', $options);
	

}

function drawChartTweetsTypes($values){
	//include('/google_chart/Chart.php');
	
	//number of tweets
	$num = count($values);
	
	// chart type
	$chart = new Chart('PieChart');

	// classify types
	
	$neu = 0;
	$pos = 0;
	$neg = 0;
	
	for($i=0;$i<$num;$i++){
		if ($values[$i] > 0){
			$pos++;
		}
		else if ($values[$i] < 0){
			$neg++;
		}
		else{
			$neu++;
		}
	}
	
	$data = array(
	array('tweet type','number'),
	array('negative feedback', $neg),
	array('neutral feedback', $neu),
	array('positive feedback', $pos)
	);

	$chart->load($data, 'array');

	$options = array('title' => 'Feedback', 'is3D' => true, 'width' => 400, 'height' => 300);
	echo $chart->draw('chart_types', $options);
	

}
?>

