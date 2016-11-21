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
        }
        else
            echo "ERROR!";
    }
    else
        echo "ERROR!";
} catch (Exception $e){
    $e->getMessage();
}