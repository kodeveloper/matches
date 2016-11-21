<?php
require ("../init.php");
try{
    if($_SERVER['REQUEST_METHOD'] == 'POST') {
        $data = json_decode(file_get_contents("php://input"));
        $user_id = $data->user_id;
        $product_id = $data->product_id;
        $status = $data->status;
        $coin = $data->coin;
        if ($user_id != "" && $product_id != "" && $status != "" && $coin != "") {
            $query = $db->insert("product_viewed")
                ->set(array(
                    "user_id" => $user_id,
                    "product_id" => $product_id,
                    "status" => $status,
                    "coin" => $coin
                ));
            print_r($query);
            if($query)
                echo "Last Row:" . $db->lastId();
            $query_user_coin = $db->select("users")
                ->from("coin")
                ->where("user_id", $user_id)
                ->run();
            $user_earn_coin = $query_user_coin[0]["coin"];
            $query_user_last_coin = $db->update("users")
                ->where("user_id", $user_id)
                ->set(array(
                    "coin" => ($coin + $user_earn_coin)
                ));
            /* if ($results->rowCount()){
                $arr = array(
                    "user_id" => $query[0]["user_id"],
                    "first_name" => $query[0]["first_name"],
                    "last_name" => $query[0]["last_name"],
                    "coin" => $query[0]["coin"]);
                print_r(json_encode($arr));
            }
            else
                echo "ERROR!";*/
        }
        else
            echo "ERROR!";
    }
    else
        echo "ERROR!";
} catch (Exception $e){
    $e->getMessage();
}