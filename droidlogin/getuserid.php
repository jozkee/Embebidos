
<?php

/*LOGIN*/

$usuario = $_POST['usuario'];

require_once 'funciones_bd.php';
$db = new funciones_BD();
    //int userID;
    try{
        //userID=$db->getUSerID($usuario);
        $resultado[]=array("userid"=>(String)$db->getUserID($usuario));
	} catch(Exception $e){
        $resultado[]=array("userid"=>"0");
	}
echo json_encode($resultado);
?>
