<?php
include 'conn.php';

// check if the request method is POST
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    
    // get raw POST data and decode it
    $postData = json_decode(file_get_contents("php://input"), true);
    
    // check if OTP is provided in the request
    if (isset($postData['otp'])) {
        
		// connect to your database 
        $conn = new mysqli("localhost","root","","smd");
        
        // check connection
        if ($conn->connect_error) {
            // Unable to connect to the database
            $response = array("success" => false, "message" => "Database connection failed");
            echo json_encode($response);
            exit;
        }
        // Extract OTP from the decoded POST data
        $otp = $postData['otp'];
        
        // query the database to check if the provided OTP is valid
        $storedOtp = "123456"; // Example: OTP stored in the database
        if ($otp === $storedOtp) {
            // OTP verification successful
            $response = array("success" => true, "message" => "OTP verification successful");
            echo json_encode($response);
        } else {
            // OTP verification failed
            $response = array("success" => false, "message" => "Incorrect OTP");
            echo json_encode($response);
        }
        
    } else {
        // if required field is missing from the request
        $response = array("success" => false, "message" => "OTP is required");
        echo json_encode($response);
    }
} else {
    // Invalid request method
    $response = array("success" => false, "message" => "Invalid request method");
    echo json_encode($response);
}

?>
