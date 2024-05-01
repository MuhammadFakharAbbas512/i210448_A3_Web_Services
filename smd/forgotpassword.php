<?php
include 'conn.php';

// Decode raw POST data if present
$data = json_decode(file_get_contents("php://input"), true);
// Check if the email parameter is set in the decoded data
if(isset($data['email'])) {
    // Sanitize the email input
    $email = filter_var($data['email'], FILTER_SANITIZE_EMAIL);
	// Connect to your database (replace placeholders with actual database credentials)
    $conn = new mysqli("localhost","root","","smd");
        
        // Check the connection
    if ($conn->connect_error) 
		{
            // Unable to connect to the database
            $response = array("success" => false, "message" => "Database connection failed");
            echo json_encode($response);
            exit;
        }
        

    // Prepare and execute the SQL statement to check if the email exists in the database
    $stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    // Check if the email exists
    if($result->num_rows > 0) {
        // Email exists in the database
        $response['success'] = true;
        $response['message'] = "Email exists. Reset your password.";
    } else {
        // Email does not exist in the database
        $response['success'] = false;
        $response['message'] = "Email does not exist.";
    }

    // Close the database connection
    $stmt->close();
} else {
    // Email parameter is not set
    $response['success'] = false;
    $response['message'] = "Email parameter is missing.";
}

// Return the JSON response
echo json_encode($response);
?>
