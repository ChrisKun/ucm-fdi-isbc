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
    </body>
</html>

<?php //Main Program

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
	}
	echo "</pre>";
}
?>

<?php // Functions

/**
    Loads the lexicon given by $file
*/
function loadLexicon($file){
  $file = fopen($file, "r") or exit("Unable to open file!");
  $lexicon = array();
  while(!feof($file)){
    addStringToLexicon($lexicon, fgets($file));
  }
  fclose($file);
  return $lexicon;
}

/**
    Adds info of $string in $lexicon
*/
function addStringToLexicon(&$lexicon,$string){
  $string = trim($string);
  $parts = explode("\t",$string);
  
  //echo $parts[0] . " -> " . $parts[1] . "<br>"; 
  $lexicon[$parts[0]] = array("id" => $parts[1], "neg" => 0, "pos" => 0);
  $count = count($parts);
  if (strcmp($parts[2], "neg") == 0) $lexicon[$parts[0]]["neg"] = $lexicon[$parts[0]]["neg"]+1;
  if (strcmp($parts[2], "pos") == 0) $lexicon[$parts[0]]["pos"] = $lexicon[$parts[0]]["pos"]+1;
  if ($count == 4){
    if (strcmp($parts[3], "pos") == 0){ 
      $lexicon[$parts[0]]["pos"] = $lexicon[$parts[0]]["pos"]+1;
    } else {
      $lexicon[$parts[0]]["neg"] = $lexicon[$parts[0]]["neg"]+1;
    }
  }
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
    $patterns[2] = '#_-(),;.:@#';
    //$patterns[2] = '#^[a-zA-Z]+#';

    $string = preg_replace($patterns, "", $string);
    //Removing unnecessary blanks
    $string = preg_replace("#\n\t(\ )+#", " ", $string);
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
  //Recover values from Lexicon
  //Return array of values
  
  return $value;
}

/**
    Consults a $tweet in $lexicon and returns total value of $tweet
*/
function tweetValue($lexicon, $tweet){
  $textCleaned = cleanString($tweet);
  $words = explode(" ",$textCleaned); print_r($words);
  applyStemming($words);
  applyLemming($words);
  $value = 0;
  $count = count($words);
	for($i=0;$i<$count;$i++){
        $value += consultLexicon($lexicon, $words[$i]);
	}
  return $value;
}

/**
    Apply Stemming to given array $words
*/
function applyStemming(&$words){

}

/**
    Aplly Lemming to given array $words
*/
function applyLemming(&$words){

}
?>