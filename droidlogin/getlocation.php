
<?php

/*LOGIN*/

$usuario = $_POST['usuario'];

require_once 'funciones_bd.php';
$db = new funciones_BD();
    //int userID;
    try{
        $resultado[]=array("latitud"=>(String)$db->getUserLocation($usuario)[0],
                            "longitud"=>(String)$db->getUserLocation($usuario)[1]
                            );
	} catch(Exception $e){
        $resultado[]=array("latitud"=>"0",
                            "longitud"=>"0"
                            );
	}
echo json_encode($resultado);
?>
