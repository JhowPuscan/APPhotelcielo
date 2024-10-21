<?php
$conn =mysqli_connect('localhost:3307','root','','hotel_cielo');
if(!$conn){
    echo "Error en conexión: " . mysqli_connect_error();
}

