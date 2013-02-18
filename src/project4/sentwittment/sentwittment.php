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
		$textCleaned = cleanString($decode["results"][$i]["text"]);
    $words = explode(" ",$textCleaned);
    print_r($words);
		//echo $i . ":\t" . $textCleaned . "<br>";
	}
	echo "</pre>";
}
?>

<?php // Functions

function loadLexicon($file){
  $file = fopen($file, "r") or exit("Unable to open file!");
  $lexicon = array();
  while(!feof($file)){
    addStringToLexicon($lexicon, fgets($file));
  }
  fclose($file);
  return $lexicon;
}

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

function cleanString($string){
	//Patterns of sequences we want to remove.
  echo $string . "<br>" . utf8_decode($string) . "<br>";
  $string = utf8_decode($string);
  $string = strtolower($string);
  $patterns = array();
  $patterns[0] = '#http://[a-zA-Z0-9.\/]*#';
  $patterns[1] = '#\d#';
  $patterns[2] = '#_-().#';
	
  $string = preg_replace($patterns, " ", $string);
  //Removing unnecessary blanks
  $string = preg_replace("#(\ )+#", " ", $string);
  $string = trim($string);
	
  return $string;
}
?>

<?php //Auxiliar Functions
function normalize ($cadena){
    $originales =  'ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûýýþÿRr';
    $modificadas = 'aaaaaaaceeeeiiiidnoooooouuuuybsaaaaaaaceeeeiiiidnoooooouuuyybyRr';
    $cadena = utf8_decode($cadena);
    $cadena = strtr($cadena, utf8_decode($originales), $modificadas);
    $cadena = strtolower($cadena);
    return utf8_encode($cadena);
}
?>