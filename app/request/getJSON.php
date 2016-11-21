<?php
require "../init.php";
try{
    if(isset($_GET["type"])) {
        $query = $db->select($_GET["type"])
            ->run();
        $json = json_encode($query, JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP);
        print_r($json);
    }
} catch (PDOException $e){
    print $e->getMessage();
}


