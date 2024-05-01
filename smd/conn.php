<?php
$conn=mysqli_connect("localhost","root","","smd");//"localhost", "username", "password", and "database_name"
if(!$conn){
 die("Connection failed: ".mysqli_connect_error());
}
?>