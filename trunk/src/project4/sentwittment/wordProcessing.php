<?php
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
	//echo $parts[0] . " [" . $stem . " , " . $lexicon[$parts[0]]["neg"] . " , " . $lexicon[$parts[0]]["pos"] . "] " . "<br>"; 
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
		//echo 'si' . " " . $string . "<br>";
	// If it isn't, then apply stemming
	} else if (strlen($string) > 2){
		$stem = spanish::stemm($string);
		$value = checkStem($stem, $lexicon);		
	} 
	return $value;
}

/************************ AUXILIARY FUNCTIONS ************************/ 

/**
    Removes from string all characters aren't representing words
*/
function cleanString($string){
	$string = utf8_decode($string);	
	// Replace the accents
	$string = stripAccents($string);
    $string = strtolower($string);
	// Patterns of url or numbers
	$patterns = array();
    $patterns[0] = '#http://[a-zA-Z0-9.\/]*#';
    $patterns[1] = '#\d#';
    $string = preg_replace($patterns, " ", $string);
	// Pattern of symbols we don't want to process
	$string = preg_replace('([^A-Za-z])', ' ', $string);	
	//Removing unnecessary blanks
    $string = preg_replace("#(\n|\t|\ )+#", " ", $string);
    $string = trim($string);
	//echo $string . "<br>";
    return $string;
}

/**
	Replaces the accents and the ñ (in the lexicon the words doesn't have ñ, instead there are words with n)
	Expample: dañino -> danino
*/
function stripAccents($string){
	$tofind = "ÀÁÂÃÄÅàáâãäåÒÓÔÕÖØòóôõöøÈÉÊËèéêëÇçÌÍÎÏìíîïÙÚÛÜùúûüÿÑñ";
	$replac = "AAAAAAaaaaaaOOOOOOooooooEEEEeeeeCcIIIIiiiiUUUUuuuuyNn";
	$string=strtr($string,$tofind,$replac);     					
	return $string;
}

/**
	Checks if $stem exists in $lexicon and operates the values of all the words in the lexicon with that stem
*/
function checkStem($stem, $lexicon){
	$value = 0;
	foreach ($lexicon as &$content) {
		if (strcmp($stem, $content["stem"]) == 0) {
			//echo $stem . " raiz " . "si" . "<br>";
			$value += $content["pos"];
			$value -= $content["neg"];
		}
	}
	return $value;
}
?>