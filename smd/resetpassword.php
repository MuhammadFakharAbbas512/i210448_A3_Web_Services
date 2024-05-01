<?php
include 'conn.php';

// Get the raw POST data and decode it
$postData = json_decode(file_get_contents('php://input'), true);

// Check if the email and new password parameters are set
if(isset($postData['email']) && isset($postData['password'])) {
    // Sanitize the email input
    $email = filter_var($postData['email'], FILTER_SANITIZE_EMAIL);
    // Hash the new password
    $newPassword = password_hash($postData['password'], PASSWORD_DEFAULT);

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
    // Prepare and execute the SQL statement to update the password in the database
    $stmt = $conn->prepare("UPDATE users SET password = ? WHERE email = ?");
    $stmt->bind_param("ss", $newPassword, $email);
    $stmt->execute();

    // Check if the password was updated successfully
    if($stmt->affected_rows > 0) {
        // Password reset successful
        $response['success'] = true;
        $response['message'] = "Password reset successful.";
    } else {
        // Email does not exist in the database
        $response['success'] = false;
        $response['message'] = "Password reset failed. Email not found.";
    }

    // Close the database connection
    $stmt->close();
} else {
    // Email or new password parameter is missing
    $response['success'] = false;
    $response['message'] = "Email or new password parameter is missing.";
}

// Return the JSON response
echo json_encode($response);
?>
