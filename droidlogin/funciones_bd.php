<?php

class funciones_BD {
    private $db;

    // constructor

    function __construct() {
        require_once 'connectbd.php';
        // connecting to database

        $this->db = new DB_Connect();
        $this->db->connect();

    }

    // destructor
    function __destruct() {

    }

    /**
     * agregar nuevo usuario
     */
    public function adduser($username, $password) {
        $result = mysql_query("INSERT INTO user(username,passw) VALUES('$username', '$password')");
        $stranger = mysql_query("INSERT INTO strangers(Strange_Mail,Strange_Display) VALUES('$username','')");
        $location = mysql_query("INSERT INTO ubicaciones(Ubicacion_latitud,Ubicacion_Longitud) VALUES('0','0')");
        // check for successful store
        if ($result) {
            return true;
        } else {
            return false;
        }
    }


     /**
     * Verificar si el usuario ya existe por el username
     */

    public function isuserexist($username) {
        $result = mysql_query("SELECT username from user WHERE username = '$username'");
        $num_rows = mysql_num_rows($result); //numero de filas retornadas

        if ($num_rows > 0) {
            // el usuario existe
            return true;
        } else {
            // no existe
            return false;
        }
    }


	public function login($user,$passw){
        $result=mysql_query("SELECT COUNT(*) FROM user WHERE username='$user' AND passw='$passw' ");
        $count = mysql_fetch_row($result);
	/*como el usuario debe ser unico cuenta el numero de ocurrencias con esos datos*/
		if ($count[0]==0){
            return true;
		}else{
            return false;
		}
	}

	/*consulta de los thread cercanos*/
	public function getUserID($user){
        $result=mysql_query("SELECT Strange_ID FROM strangers WHERE Strange_Mail='$user'");
        if (!$result) {
            echo 'No se pudo ejecutar la consulta: ' . mysql_error();
            exit;
        }
        $id = mysql_fetch_row($result);
        return $id[0];
    }

    public function ifLocationExists($usuario){
        $id=intval($usuario);
        $result = mysql_query("SELECT Stranger_ID from ubicaciones WHERE Stranger_ID = '$id'");
        $num_rows = mysql_num_rows($result); //numero de filas retornadas

        if ($num_rows > 0) {
            return true;
        } else {
            return false;
        }
    }
    public function updateLocation($usuario,$latitude,$longitude){
        $id=intval($usuario);
        $lat=doubleval($latitude);
        $lon=doubleval($longitude);

        $result=mysql_query("UPDATE ubicaciones SET Ubicacion_Latitud='$lat',Ubicacion_Longitud='$lon' WHERE Stranger_ID='$id'");
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    public function addLocation($username, $latitude, $longitude) {
        $id=intval($username);
        $lat=doubleval($latitude);
        $lon=doubleval($longitude);

        $result = mysql_query("INSERT INTO ubicaciones(Stranger_ID,Ubicacion_Latitud,Ubicacion_Longitud) VALUES('$id', '$lat', '$lon')");
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    public function getUserLocation($user){
        $result=mysql_query("SELECT Ubicacion_Latitud, Ubicacion_Longitud FROM ubicaciones WHERE Stranger_ID='$user'");
        if (!$result) {
            echo 'No se pudo ejecutar la consulta: ' . mysql_error();
            exit;
        }
        $id = mysql_fetch_row($result);
        return $id;
    }

    public function getNear($latitude, $longitude, $radius){
        $lat=doubleval($latitude);
        $lon=doubleval($longitude);
        $rad=doubleval($radius);

        /*$result=mysql_query("SELECT threads.Thread_Titulo,
            threads.Thread_ID,
			strangers.Strange_ID,
            strangers.Strange_Rango
            FROM threads, strangers
            WHERE threads.Strange_ID  = strangers.Strange_ID AND strangers.Strange_Rango < '$rad'");*/

        $result=mysql_query("SELECT threads.Thread_Titulo,
            threads.Thread_ID
            FROM threads, ubicaciones
            WHERE threads.Strange_ID  = ubicaciones.Stranger_ID
                    AND ubicaciones.Ubicacion_Latitud < '$lat'+'$rad'
                    AND ubicaciones.Ubicacion_Latitud > '$lat'-'$rad'
                    AND ubicaciones.Ubicacion_Longitud < '$lon'+'$rad'
                    AND ubicaciones.Ubicacion_Longitud > '$lon'-'$rad'");

        return $result;
    }


    public function getMine($user){
        $u=intval($user);

        $result=mysql_query("SELECT threads.Thread_Titulo,
            threads.Thread_ID
            FROM threads
            WHERE threads.Strange_ID  = '$u'");

        return $result;
    }

    public function getCommented($user){
        $u=intval($user);

        $result=mysql_query("SELECT DISTINCT threads.Thread_Titulo, threads.Thread_ID
            FROM comentarios, threads
            WHERE threads.Thread_ID = comentarios.Thread_ID
                AND comentarios.Stranger_ID  = '$u'");
        return $result;
    }

     public function getThreads($id_autor){
        $id_aut=intval($id_autor);

        $result=mysql_query("SELECT Thread_ID, Thread_Titulo FROM threads WHERE Strange_ID='$id_aut'");
        $id=mysql_fetch_row($result);

        return $result;
    }

    public function postThread($username, $msg) {
        $user=intval($username);
        $result = mysql_query("INSERT INTO threads(Thread_Titulo,Strange_ID) VALUES('$msg', '$user')");
        // check for successful store
        if ($result) {
            return true;
        } else {
            return false;
        }
    }
    public function getComments($tID){
        $t=intval($tID);

        $result=mysql_query("SELECT Comentario_Texto, Stranger_ID
            FROM comentarios
            WHERE Thread_ID = '$t'");
        return $result;
    }

    public function postComment($threadID, $txt, $user) {
        $tID=intval($threadID);
        $u=intval($user);
        $result = mysql_query("INSERT INTO comentarios(Thread_ID,Stranger_ID,Comentario_Texto) VALUES('$tID', '$u', '$txt')");
        // check for successful store
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

}
?>
