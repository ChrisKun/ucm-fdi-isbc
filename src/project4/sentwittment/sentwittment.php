<!doctype html>
<html>
    <head>
        <title>Sentwittment Results</title>        
		<meta charset="utf-8">
        <link rel="stylesheet" type="stylesheet" href="stylesheets/style_results.css">
    </head>
    <body>
	<?php
		session_start();
		main();
	?>
	<div id="chart_values"></div>
	<div id="chart_types"></div>
    </body>
</html>

<?php //Main Program
function main() {	
	// print_r($_SESSION["values"]);
	$values = $_SESSION["values"];
	require_once 'back.php';
	require_once 'wordProcessing.php';	
	form();
	// Stemming
	require_once '/stemmer/Spanish.php';
	// Loads the lexicon
	$lexicon = loadLexicon("lexicon.txt");
	if (!empty($values["query"]))
	{
		$query = getQueryFromForm($values);
		$filename = "http://search.twitter.com/search.json?".$query;
		$json = file_get_contents($filename, true);
		$decode = json_decode($json, true);

		echo "<pre>";
		$count = count($decode["results"]);
		for($i=0;$i<$count;$i++){	
			$tweet = $decode["results"][$i]["text"];
			echo "Tweet" . $i . " -> ";
			echo $tweet . "<br>";
			$value = tweetValue($lexicon, $tweet);
			$chartValues[] = $value;
		}
		echo "</pre>";
		drawChartTweetsValues($chartValues);
		drawChartTweetsTypes($chartValues);				
	}
}
/**
    Consults a $tweet in $lexicon and returns total value of $tweet
*/
function tweetValue($lexicon, $tweet){
	$words = explode(" ",$tweet); 
	//print_r($words);
	$value = 0;
	$count = count($words);
	for($i=0;$i<$count;$i++){
		$words[$i]=cleanString($words[$i]);
		$value += consultLexicon($lexicon, $words[$i]);
	}
	//echo $value . "<br>";	
	return $value;
}

/**
Get the neccessary info from forms and return a string with the configured query
*/
function getQueryFromForm($values){
	// Query
	$query = "q=" . $values["query"];
	// Return Per Page
	$query .= "&rpp=".$values["returnpp"];
	// Result Type
	switch ($values["type"]) {
			case 'recent':
				$query .= "&result_type=recent"; break;
			case 'popular':
				$query .= "&result_type=popular"; break;
			default:
				$query .= "&result_type=mixed"; break;
	}
	//echo $query ."<br>";
	return $query;
}

/********** CHARTS ***********/
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