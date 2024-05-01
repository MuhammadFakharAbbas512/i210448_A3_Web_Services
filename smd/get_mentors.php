<?php
include 'conn.php';

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

// Prepare and execute the SQL statement to retrieve mentors from the database
$stmt = $conn->prepare("SELECT * FROM mentors");
$stmt->execute();

// Get the result set from the prepared statement
$result = $stmt->get_result();

// Initialize an empty array to store mentors
$mentors = array();

// Fetch mentors from the result set
while ($row = $result->fetch_assoc()) {
    // Add each mentor to the array
    $mentors[] = $row;
}

// Close the database connection
$stmt->close();

// Return the JSON response containing the list of mentors
echo json_encode($mentors);
?>