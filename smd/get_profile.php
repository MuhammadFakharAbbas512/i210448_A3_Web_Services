<?php
include 'conn.php';

// Check if user ID is provided in the request
if (isset($_GET['userId'])) {
    $userId = $_GET['userId'];
	// Connect to your database (replace placeholders with actual database credentials)
    $conn = new mysqli("localhost","root","","smd");

    // Check the connection
    if ($conn->connect_error) {
        // Unable to connect to the database
        $response = array("success" => false, "message" => "Database connection failed");
        echo json_encode($response);
        exit;
    }
    // Query to retrieve user profile data from your database based on the provided user ID
    // Replace this query with your actual database query
    $query = "SELECT name, city FROM users WHERE userId = '$userId'";
    
// Prepare and execute the SQL statement to retrieve users from the database
$stmt = $conn->prepare("SELECT name, city FROM users WHERE userId = '$userId'");
$stmt->execute();

// Get the result set from the prepared statement
$result = $stmt->get_result();

// Initialize an empty array to store users
$users = array();

// Fetch mentors from the result set
while ($row = $result->fetch_assoc()) {
    // Add each mentor to the array
    $users[] = $row;
}

// Close the database connection
$stmt->close();

// Return the JSON response containing the list of mentors
echo json_encode($users);}
?>
