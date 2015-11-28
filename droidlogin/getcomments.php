
<?php

/*LOGIN*/

$tID = $_POST['thread'];;

require_once 'funciones_bd.php';

$db = new funciones_BD();

$array=$db->getComments($tID);
//$array=$db->getNear(0.0,0.0,237.0);

$string="[";

if ($row = mysql_fetch_array($array)){
   do {
        //echo "{\"ThreadID"."\"".":"."\"".$row[1]."\"".",";
        //echo "\"Desc"."\"".":"."\"".$row[0]."\""."}\n";
        $string=$string."{\"ThreadID"."\"".":"."\"".$row[1]."\"".", "."\"Desc"."\"".":"."\"".$row[0]."\""."},";
   } while ($row = mysql_fetch_array($array));
} else {
    $string=$string."{\"ThreadID\":\"0\",\"Desc\":\"No se encontraron comentarios\"},";
}

$string=$string."{\"ThreadID\":\"0\",\"Desc\":\"0\"}]";

echo $string;

?>

