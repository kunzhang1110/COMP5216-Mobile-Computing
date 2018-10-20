<?php
    $con = mysqli_connect("localhost", "id7213647_jpmo_77", "qwerty", "id7213647_weightlossapp");
    
    $name = $_POST["name"];
    $age = $_POST["age"];
    $userName = $_POST["userName"];
    $weight = $_POST["weight"];
		$height = $_POST["height"];
		$password = $_POST["password"];
		$gender = $_POST["gender"];

    $statement = mysqli_prepare($con, "INSERT INTO User (name, userName, age, height, weight, password, gender) VALUES (?, ?, ?, ?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "ssiddss", $name, $userName, $age, $height, $weight, $password,$gender);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>
