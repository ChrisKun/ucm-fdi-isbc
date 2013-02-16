<html>
<body>

<form action = "sentwittment.php" method="post">
Query: <input type="text" name="query"><br>
ReturnPerPage: <input type="int" name="rpp"><br>
Result Type: <select name="result_type">
<option value="0" selected>Populares</option>
<option value="1">Recientes</option>
<option value="2">Mixtos</option>
</select><br>
<input type="submit">
</form>

</body>
</html>

<?php //Main Program

$lexicon = loadLexicon();
if (isset($_POST["query"]))
{
/*
  $q = "q=" . $_POST["query"] ; // Query
  $rpp = "rpp=" . $_POST["rpp"]; // Return Per Page
  switch ($_POST["result_type"]) {
	case 0:
		$result_type = "result_type=popular";
		break;
	case 1:
		$result_type = "result_type=recent";
		break;
	case 2:
		$result_type = "result_type=mixed";
		break;
  }
  $include_entities = "include_entities=false"; // Metadata about Tweets
*/
  $query = getQueryFromForm();
  /* 
  La búsqueda se realiza por consulta web con un 'search.json?' al que hay que
  indicarle obligatoriamente un concepto con 'q= ' que representa el concepto
  de la query
  La llamada original al buscador sería con las siguientes opciones
    'q= ' -> Concepto de la Query
    'rpp= ' -> Numero de tweets por pagina
    'include_entities= ' -> Para incluir metadata
    'result_type= ' -> Tweets populares, recientes o mixtos
  
  La llamada Original
    http://search.twitter.com/search.json?q=blue%20angels&rpp=5&include_entities=true&result_type=mixed
  */
  //$format = "json?".$q."&".$rpp."&".$include_entities."&".$result_type;
  $filename = "http://search.twitter.com/search.json?".$query;

  $json = file_get_contents($filename, true);
  $decode = json_decode($json, true);

  echo "<pre>";
  $count = count($decode["results"]);
  for($i=0;$i<$count;$i++){
  //print_r($decode["results"][$i]["created_at"]."<br>");
  echo $i . ":\t" . $decode["results"][$i]["text"]."<br>" ;
  }
  echo "</pre>";
  }
?>

<?php // Functions

function loadLexicon(){
  $file = fopen("lexicon.txt", "r") or exit("Unable to open file!");
  $lexicon = array();
  while(!feof($file)){
    addStringToLexicon($lexicon, fgets($file));
  }
  fclose($file);
  return $lexicon;
}

function addStringToLexicon(&$lexicon,$string){
  $parts = explode("\t",$string);
  $lexicon[$parts[0]] = array("id" => $parts[1], "neg" => 0, "pos" => 0);
  $count = count($parts);
  if (strcmp($parts[2], "neg") == 0) $lexicon[$parts[0]]["neg"] = $lexicon[$parts[0]]["neg"]+1;
  if (strcmp($parts[2], "pos") == 0) $lexicon[$parts[0]]["pos"] = $lexicon[$parts[0]]["pos"]+1;
  if ($count == 4){
    if (strcmp($parts[3], "pos") > 0){ 
      $lexicon[$parts[0]]["pos"] = $lexicon[$parts[0]]["pos"]+1;
    } else {
      $lexicon[$parts[0]]["neg"] = $lexicon[$parts[0]]["neg"]+1;
    }
  }
}

/**
Get the neccessary info from the forms and return a string with the configured query
*/
function getQueryFromForm(){
	// Query
	$query = "q=" . $_POST["query"];
	// Return Per Page
	if (isset($_POST["rpp"])){
		$query .= "&rpp=".$_POST["rpp"];
	} else {
		echo "<br>Rpp de 50 <br>";
		$query .= "&rpp=50";
	}
	echo "<br>".$_POST["rpp"]."<br>";
	// Result Type
	if (isset($_POST["result_type"])){
		
		switch ($_POST["result_type"]) {
			case 0:
				$result_type = "result_type=popular"; break;
			case 1:
				$result_type = "result_type=recent"; break;
			case 2:
				$result_type = "result_type=mixed"; break;
		}
	
	return $query;	
	}
}
?>