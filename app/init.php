<?php
require 'classes/class.basicdb.php';
require 'system/config.php';
try{
    $db = new BasicDB($config["db"]["host"], $config["db"]["name"], $config["db"]["user"], $config["db"]["password"]);
}catch (PDOException $e){
    print $e->getMessage();
}
