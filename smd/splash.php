<?php

include 'conn.php';

// check if user is already logged in
$postData = json_decode(file_get_contents("php://input"), true);

// check if userId is present in the decoded POST data
if (isset($postData['userId'])) {
    
    // Extract userId from the decoded POST data
    $userId = $postData['userId'];
    
    // Connect to your database (replace placeholders with actual database credentials)
    $conn = new mysqli("localhost", "root", "", "smd");
    
    // Check the connection
    if ($conn->connect_error) {
        // Unable to connect to the database
        $response = array("success" => false, "message" => "Database connection failed");
        echo json_encode($response);
        exit;
    }
    
    // Prepare SQL statement to retrieve user data from the database
    $sql = "SELECT * FROM users WHERE userId = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $userId);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows > 0) {
        // User found in the database
        $row = $result->fetch_assoc();
        // Return user data as JSON response
        $response = array("success" => true, "userData" => $row);
        echo json_encode($response);
    } else {
        // User not found in the database
        $response = array("success" => false, "message" => "User not found");
        echo json_encode($response);
    }
    
    // Close the database connection
    $stmt->close();
    $conn->close();
    
} else {
    // userId parameter is missing from the request
    $response = array("success" => false, "message" => "userId parameter is missing");
    echo json_encode($response);
}

?>
