<?php
require '../init.php';
try{
    $owner      = $_GET["owner"];
    $image_url  = $_GET["image_url"];
    $desc       = $_GET["description"];
    $coin       = $_GET["coin"];
    if(isset($owner) && isset($image_url) && isset($desc) && isset($coin)){
        $query = $db -> insert("products")
            -> set(array(
               "owner" => $owner,
                "image_url" => $image_url,
                "description" => $desc,
                "coin" => $coin
            ));
    }
} catch (PDOException $e){
    $e->getMessage();
}