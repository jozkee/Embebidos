
<?php

/*LOGIN*/

$user = $_POST['user'];

require_once 'funciones_bd.php';

$db = new funciones_BD();

//$array=$db->getNear($latitude,$longitude,$radius);
$array=$db->getCommented($user);

$string="[";

if ($row = mysql_fetch_array($array)){
   do {
        //echo "{\"ThreadID"."\"".":"."\"".$row[1]."\"".",";
        //echo "\"Desc"."\"".":"."\"".$row[0]."\""."}\n";
        $string=$string."{\"ThreadID"."\"".":"."\"".$row[1]."\"".", "."\"Desc"."\"".":"."\"".$row[0]."\""."},";
   } while ($row = mysql_fetch_array($array));
} else {
    $string=$string."{\"ThreadID\":\"0\",\"Desc\":\"Error, no se encontraron comentarios suyos.\"},";
}

$string=$string."{\"ThreadID\":\"0\",\"Desc\":\"0\"}]";

echo $string;

?>

