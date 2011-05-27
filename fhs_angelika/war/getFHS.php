<?php  
	header('Content-Type: text/javascript');
  	header('Cache-Control: no-cache');
  	header('Pragma: no-cache');

  	require_once 'config.php';

	$con = mysql_connect($host,$user,$pwd);
	if (!$con)
	{
		die('Could not connect: ' . mysql_error());
	}

  	$table = $_GET['table'];
	$cols = $_GET['cols'];
	
	$where = $_GET['where'];
	echo 'WHERE' . $where;
	$group = $_GET['group'];
	$limit = $_GET['limit'];
	
	$col_arr = explode(",",$cols);
	mysql_select_db($db,$con);
	
	$query = 'SELECT ' . $cols . ' FROM ' . $table;

	$result = mysql_query($query);

	$i=0;
	while($row = mysql_fetch_array($result))
	{
		for ($j=0;$j<count($col_arr);$j++)
		{
			$value{$i}{$col_arr[$j]}=$row[$col_arr[$j]];
		}				
		$i++;
	}

	echo json_encode($value);
	mysql_close($con);
  	
?>