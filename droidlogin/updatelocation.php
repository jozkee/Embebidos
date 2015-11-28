<?php

$usuario = $_POST['usuario'];
$latitude = $_POST['latitud'];
$longitude = $_POST['longitud'];

require_once 'funciones_bd.php';
$db = new funciones_BD();

	if($db->ifLocationExists($usuario)){
        if($db->updateLocation($usuario,$latitude,$longitude)){
			echo("Se actualizo la ubicacion correctamente.");
        }
        else{
			echo("Error actualizando ubicacion.");
        }
	}else{
        if($db->addLocation($usuario,$latitude,$longitude)){
			echo("Se agrego la ubicacion correctamente.");
        }
        else{
			echo("Se produjo un error al añadir.");
        }
	}



?>



