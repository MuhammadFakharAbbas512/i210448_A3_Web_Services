<?php
include 'conn.php';

// Get the raw POST data and decode it
$post_data = json_decode(file_get_contents("php://input"), true);

// Check if the request method is POST and if the decoded POST data is not empty
if ($_SERVER['REQUEST_METHOD'] === 'POST' && !empty($post_data)) {
    
    // Check if all required fields are present in the decoded POST data
    if (isset($post_data['userId'], $post_data['name'], $post_data['email'], $post_data['phone'], $post_data['country'], $post_data['city'])) {
     
        // Extract user data from the decoded POST data
        $userId = $post_data['userId'];
        $name = $post_data['name'];
        $email = $post_data['email'];
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
        
        // Connect to your database (replace placeholders with actual database credentials)
        $conn = new mysqli("localhost","root","","smd");
        
        // Check the connection
        if ($conn->connect_error) {
            // Unable to connect to the database
            $response = array("success" => false, "message" => "Database connection failed");
            echo json_encode($response);
            exit;
        }
        
        // Prepare SQL statement to update user profile in the database
        $sql = "UPDATE users SET name = ?, email = ?, phone = ?, country = ?, city = ? WHERE userId = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ssssss", $name, $email, $phone, $country, $city, $userId);
        
        // Execute the SQL statement
        if ($stmt->execute()) {
            // User profile updated successfully
            $response = array("success" => true, "message" => "User profile updated successfully");
            echo json_encode($response);
        } else {
            // Failed to update user profile
            $response = array("success" => false, "message" => "Failed to update user profile");
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
// Function to upload image
function uploadFile($base64Image, $folder)
{
    $target_dir = "uploads/$folder/";
    $target_file = $target_dir . uniqid() . '.jpg';
    $fileData = base64_decode($base64Image);
    if (file_put_contents($target_file, $fileData)) {
        return $target_file;
    } else {
        return false;
    }
}
?>
