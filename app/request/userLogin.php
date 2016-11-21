<?php
require ("../init.php");
try{
    if($_POST) {
        $email = $_POST["email"];
        $password = $_POST["password"];
        if (isset($email) && isset($password) && filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $pass_md5 = md5($password);
            $query = $db->select("users")
                ->where("email", $email)
                ->where("password", $pass_md5)
                ->run();
            $sql = $db->getSqlString();
            $results = $db->query($sql);
            if ($results->rowCount()){
                $arr = array(
                    "user_id" => $query[0]["user_id"],
                    "first_name" => $query[0]["first_name"],
                    "last_name" => $query[0]["last_name"],
                    "coin" => $query[0]["coin"]);
                print_r(json_encode($arr));
            }
            else
                echo "ERROR!";
        }
        else
            echo "ERROR!";
    }
    else
        echo "ERROR!";
} catch (Exception $e){
    $e->getMessage();
}