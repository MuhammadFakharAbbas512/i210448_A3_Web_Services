<?php
// Include database connection
include 'conn.php';

// Get raw POST data from the request and decode it
$postData = json_decode(file_get_contents('php://input'), true);


// Check if all required fields are present in the decoded data
if(isset($postData['userId'], $postData['mentorId'], $postData['rating'], $postData['review'])) {
    // Assign values from the decoded data
    $userId = $postData['userId'];
    $mentorId = $postData['mentorId'];
    $rating = $postData['rating'];
    $review = $postData['review'];

	// Connect to your database (replace placeholders with actual database credentials)
    $conn = new mysqli("localhost","root","","smd");

    // Check the connection
    if ($conn->connect_error) {
        // Unable to connect to the database
        $response = array("success" => false, "message" => "Database connection failed");
        echo json_encode($response);
        exit;
    }
    // Define review table name
    $tableName = 'reviews'; // Adjust according to your database structure

    

    // Insert review into the database
    $query = "INSERT INTO $tableName (userId, mentorId, rating, review) VALUES ('$userId', '$mentorId', '$rating', '$review')";
    if (mysqli_query($conn, $query)) {
        // Review saved successfully
        echo json_encode(array('message' => 'Review saved successfully'));
    } else {
        // Error saving review
        echo json_encode(array('error' => 'Failed to save review'));
    }
} else {
    // Required fields are missing
    echo json_encode(array('error' => 'Missing required fields'));
}

// Close database connection
mysqli_close($conn);

?>