<?php
    $con = mysqli_connect("localhost", "id7213647_jpmo_77", "qwerty", "id7213647_weightlossapp");
    
    $userName = $_POST["userName"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM User WHERE userName = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $userName, $password);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userId, $name, $userName, $age, $height, $weight, $password, $gender, $pal, $dietChoice, $targetWeight, $targetDuration, $dailyCarboTargetHi,$dailyCarboTargetLo,$dailyProteinTargetHi, $dailyProteinTargetLo, $dailyFatTargetHi, $dailyFatTargetLo, $dailyCalorieTarget);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
			  $response["userId"] = $userId;			
        $response["name"] = $name;
        $response["userName"] = $userName;
				
				$response["age"] = $age;
				$response["weight"] = $weight;
				$response["height"] = $height;
        $response["gender"] = $gender;
        $response["pal"] = $pal;				

        $response["dietChoice"] = $dietChoice;
        $response["targetWeight"] = $targetWeight;			
        $response["targetDuration"] = $targetDuration;			

        $response["dailyCarboTargetHi"] = $dailyCarboTargetHi;						
        $response["dailyCarboTargetLo"] = $dailyCarboTargetLo;		
        $response["dailyProteinTargetHi"] = $dailyProteinTargetHi;						
        $response["dailyProteinTargetLo"] = $dailyProteinTargetLo;		
        $response["dailyFatTargetHi"] = $dailyFatTargetHi;
        $response["dailyFatTargetLo"] = $dailyFatTargetLo;		
        $response["dailyCalorieTarget"] = $dailyCalorieTarget;		

				
    }
    
    echo json_encode($response);
?>
