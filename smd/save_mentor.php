<?php

include 'conn.php';
// Check if POST request is sent
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Get mentor data from the request
    $mentorName = $_POST['name'];
    $mentorDesc = $_POST['description'];
    $mentorStatus = $_POST['status'];
    $mentorFee = $_POST['fee'];
	$dp = $_POST['dpUrl'];
	
    // Connect to your database (replace placeholders with actual database credentials)
    $conn = new mysqli("localhost","root","","smd");

    // Check the connection
    if ($conn->connect_error) {
        // Unable to connect to the database
        $response = array("success" => false, "message" => "Database connection failed");
        echo json_encode($response);
        exit;
    }
    // Upload image and video files
    $dpUrl = uploadFile($_FILES['dpUrl'], 'dp');
    $videoUrl = uploadFile($_FILES['videoFile'], 'video');

    // Insert mentor data into the database
    $sql = "INSERT INTO mentors (name, description, status, fee, dpUrl) VALUES ('$mentorName', '$mentorDesc', '$mentorStatus', '$mentorFee', '$dp')";
    if (mysqli_query($conn, $sql)) {
        echo json_encode(array("message" => "Mentor added successfully"));
    } else {
        echo json_encode(array("error" => "Failed to add mentor"));
    }
} else {
    echo json_encode(array("error" => "Invalid request method"));
}

// Function to upload files
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

// Close database connection
mysqli_close($conn);
?>