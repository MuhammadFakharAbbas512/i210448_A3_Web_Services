<?php
include 'conn.php';

// Get the raw POST data and decode it
$post_data = json_decode(file_get_contents("php://input"), true);

// Check if the request method is POST and if the decoded POST data is not empty
if ($_SERVER['REQUEST_METHOD'] === 'POST' && !empty($post_data)) {
    
    // Check if all required fields are present in the decoded POST data
    if (isset($post_data['name'], $post_data['email'], $post_data['password'], $post_data['phone'], $post_data['country'], $post_data['city'])) {
     
        // Extract user data from the decoded POST data
        $name = $post_data['name'];
        $email = $post_data['email'];
        $password = $post_data['password'];
        $phone = $post_data['phone'];
        $country = $post_data['country'];
        $city = $post_data['city'];
        
        // Validate email format
        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            // Email is not valid
            $response = array("success" => false, "message" => "Invalid email format");
            echo json_encode($response);
            exit;
        }
        
        // Hash the password for security
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
        
        // Connect to your database (replace placeholders with actual database credentials)
        $conn = new mysqli("localhost","root","","smd");
        
        // Check the connection
        if ($conn->connect_error) {
            // Unable to connect to the database
            $response = array("success" => false, "message" => "Database connection failed");
            echo json_encode($response);
            exit;
        }
        
        // Prepare SQL statement to insert user into the database
        $sql = "INSERT INTO users (name, email, password, phone, country, city) VALUES (?, ?, ?, ?, ?, ?)";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ssssss", $name, $email, $hashedPassword, $phone, $country, $city);
        
        // Execute the SQL statement
        if ($stmt->execute()) {
            // User signup successful
            $response = array("success" => true, "message" => "User signed up successfully");
            echo json_encode($response);
        } else {
            // Failed to signup user
            $response = array("success" => false, "message" => "Failed to signup user");
            echo json_encode($response);
        }
        
        // Close the database connection
        $stmt->close();
        $conn->close();
        
    } else {
        // Required fields are missing from the request
        $response = array("success" => false, "message" => "Required fields are missing");
        echo json_encode($response);
		
    }
} else {
    // Invalid request method or empty POST data
    $response = array("success" => false, "message" => "Invalid request method or empty POST data");
    echo json_encode($response);
}

?>
