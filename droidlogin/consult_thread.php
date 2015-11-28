
<?php

/*LOGIN*/

$ubicacion = $_POST['direc'];



require_once 'funciones_bd.php';
$db = new funciones_BD();

	if($db->search_threads($ubicacion)){

	$resultado[]=array("logstatus"=>"0");
	}else{
	$resultado[]=array("logstatus"=>"1");
	}

echo json_encode($resultado);




?>
