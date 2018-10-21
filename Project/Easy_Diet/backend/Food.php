<?php
    $con = mysqli_connect("localhost", "id7213647_jpmo_77", "qwerty", "id7213647_weightlossapp");
    
    
    $statement = mysqli_prepare($con, "SELECT * FROM `TABLE 3`");

    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $item_name, $item_id, $Cal, $Fat, $Carbohydrates, $Sugars, $Protein);
    
    $response = array();
    $response1 = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["item_name"] = $item_name;  
        $response["item_id"] = $item_id;
        $response["Cal"] = $Cal;
	    $response["Fat"] = $Fat;
	    $response["Carbohydrates"] = $Carbohydrates;
	    $response["Sugars"] = $Sugars;
        $response["Protein"] = $Protein;
        $response1[$item_id] = $response;
        
    }
    echo json_encode($response1);
    
    
    
?>
