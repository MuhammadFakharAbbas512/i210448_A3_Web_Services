<?php
include 'conn.php';

// Get the raw POST data and decode it
$post_data = json_decode(file_get_contents("php://input"), true);

// Check if the request method is POST and if the decoded POST data is not empty
if ($_SERVER['REQUEST_METHOD'] === 'POST' && !empty($post_data)) {
    
    // Check if email and password are provided in the decoded POST data
    if (isset($post_data['email']) && isset($post_data['password'])) {
        
        // Extract email and password from the decoded POST data
        $email = $post_data['email'];
        $password = $post_data['password'];
        
        // Connect to your database (replace placeholders with actual database credentials)
        $conn = new mysqli("localhost","root","","smd");
        
        // Check the connection
        if ($conn->connect_error) {
            // Unable to connect to the database
            $response = array("success" => false, "message" => "Database connection failed");
            echo json_encode($response);
            exit;
        }
        
        // Prepare SQL statement to retrieve user data from the database based on email
        $sql = "SELECT * FROM users WHERE email = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $result = $stmt->get_result();
        
        if ($result->num_rows > 0) {
            // User found in the database
            $row = $result->fetch_assoc();
            // Verify password
            if (password_verify($password, $row['password'])) {
                // Password is correct
                $response = array("success" => true, "message" => "Login successful", "user" => $row);
                echo json_encode($response);
            } else {
                // Password is incorrect
                $response = array("success" => false, "message" => "Incorrect password");
                echo json_encode($response);
            }
        } else {
            // User not found in the database
            $response = array("success" => false, "message" => "User not found");
            echo json_encode($response);
        }
        
        // Close the database connection
        $stmt->close();
        $conn->close();
        
    } else {
        // Required fields are missing from the decoded POST data
        $response = array("success" => false, "message" => "Email and password are required");
        echo json_encode($response);
    }
} else {
    // Invalid request method or empty decoded POST data
    $response = array("success" => false, "message" => "Invalid request method or empty POST data");
    echo json_encode($response);
}

?>
