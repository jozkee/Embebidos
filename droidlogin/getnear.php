
<?php

/*LOGIN*/

$latitude = $_POST['latitud'];
$longitude = $_POST['longitud'];
$radius = $_POST['radio'];

require_once 'funciones_bd.php';

$db = new funciones_BD();

$array=$db->getNear($latitude,$longitude,$radius);
//$array=$db->getNear(0.0,0.0,237.0);

$string="[";

if ($row = mysql_fetch_array($array)){
   do {
        //echo "{\"ThreadID"."\"".":"."\"".$row[1]."\"".",";
        //echo "\"Desc"."\"".":"."\"".$row[0]."\""."}\n";
        $string=$string."{\"ThreadID"."\"".":"."\"".$row[1]."\"".", "."\"Desc"."\"".":"."\"".$row[0]."\""."},";
   } while ($row = mysql_fetch_array($array));
} else {
    $string=$string."{\"ThreadID\":\"0\",\"Desc\":\"Error, no se encontraron temas\"},";
}

$string=$string."{\"ThreadID\":\"0\",\"Desc\":\"0\"}]";

echo $string;

?>

