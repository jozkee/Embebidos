<?php
$txt = $_POST['text'];
$threadID = $_POST['thread'];
$user = $_POST['user'];

require_once 'funciones_bd.php';
$db = new funciones_BD();

if($db->postComment($threadID,$txt,$user)){
	echo(" Se escribio con exito.");
}else{
    echo(" ha ocurrido un error.");
}
?>



