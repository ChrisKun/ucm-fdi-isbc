<!doctype html>
<html>
    <head>
        <title>Sentwittment Results</title>        
		<meta charset="utf-8">
        <link rel="stylesheet" type="stylesheet" href="stylesheets/style_results.css">
    </head>
    <body>
	<div id="chart_values"></div>
	<div id="chart_types"></div>
	<?php
	echo "<pre>";
		session_start();		
		if (!isset($_SESSION["values"])) {
			header("Location: sentwittmentForm.php");
			exit;
		} else 
			main();
	echo "</pre>";
	?>
    </body>
</html>

<?php //Main Program
function main() {	
	require_once 'back.php';
	form();
	//print_r($_SESSION["values"]);
	$values = $_SESSION["values"];
	session_destroy();
	// Stemming
	require_once './stemmer/Spanish.php';
	// Loads the lexicon
	require_once 'wordProcessing.php';	
	$lexicon = loadLexicon("lexicon.txt");
	$query = getQueryFromForm($values);
	$filename = "http://search.twitter.com/search.json?".$query;
	$json = file_get_contents($filename, true);
	$decode = json_decode($json, true);
    $tweets = $decode["results"];

    $numTweets = $values["returnpp"];
    $page = round($numTweets / 100);
    for ($i=0;$i<$page;$i++){
    
        $json = file_get_contents($filename . "$page=" . $page, true);
        $decode = json_decode($json, true);
        $tweets = array_merge($tweets, $decode["results"]);
    }
    //echo ($numTweets) . "<br>";
    
	$chartValues = NULL;
	for($i=0;$i<$numTweets;$i++){	
		$tweet = $tweets[$i]["text"];
		echo "<h2>" . "Tweet " . $i . "</h2>";
		echo $tweet . "<br>";
		$value = tweetValue($lexicon, $tweet);
		$chartValues[] = $value;
	}
	if ($chartValues != NULL) {
		drawChartTweetsValues($chartValues);
		drawChartTweetsTypes($chartValues);		
	} else {
		echo 'No se han encotrado tweets' . "<br>";
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
	// For a proper search we have to chances whitespaces for "+"
	$query = preg_replace("#\ #", "+",$values['query']);
	// Query	
	$query = "q=" . $query;
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
	include('./google_chart/Chart.php');
	
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