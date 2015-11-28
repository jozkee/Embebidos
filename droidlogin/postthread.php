<?php
$username = $_POST['user'];
$msg = $_POST['thread'];

require_once 'funciones_bd.php';
$db = new funciones_BD();

if($db->postThread($username,$msg)){
	echo(" El usuario fue agregado a la Base de Datos correctamente.");
}else{
    echo(" ha ocurrido un error.");
}
?>



