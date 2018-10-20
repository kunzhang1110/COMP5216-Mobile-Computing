<?php
    $con = mysqli_connect("localhost", "id7213647_jpmo_77", "qwerty", "id7213647_weightlossapp");
    

		$userId = $_POST["userId"];
		$pal = $_POST["pal"];

		$dietChoice = $_POST["dietChoice"];		
		$targetWeight = $_POST["targetWeight"];
		$targetDuration = $_POST["targetDuration"];
		
		$dailyCarboTargetHi = $_POST["dailyCarboTargetHi"];
		$dailyCarboTargetLo = $_POST["dailyCarboTargetLo"];
		$dailyProteinTargetHi = $_POST["dailyProteinTargetHi"];
		$dailyProteinTargetLo = $_POST["dailyProteinTargetLo"];
		$dailyFatTargetHi = $_POST["dailyFatTargetHi"];
		$dailyFatTargetLo = $_POST["dailyFatTargetLo"];
		$dailyCalorieTarget = $_POST["dailyCalorieTarget"];		

    $statement = mysqli_prepare($con, "UPDATE User SET pal=?, dietChoice=?,targetWeight=?,targetDuration=?,dailyCarboTargetHi=?,dailyCarboTargetLo=?, dailyProteinTargetHi=?, dailyProteinTargetLo=?, dailyFatTargetHi=?, dailyFatTargetLo=?, dailyCalorieTarget=? WHERE userId=?");
					
    mysqli_stmt_bind_param($statement, "iiiiddddddds", $pal, $dietChoice, $targetWeight, $targetDuration,$dailyCarboTargetHi, $dailyCarboTargetLo, $dailyProteinTargetHi, $dailyProteinTargetLo,$dailyFatTargetHi, $dailyFatTargetLo, $dailyCalorieTarget, $userId);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>
