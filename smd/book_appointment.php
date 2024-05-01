<?php
// Include database configuration
include 'conn.php';

// Check if POST request is sent
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Get mentor ID and selected time from the request
    $mentorId = $_POST['mentorId'];
    $selectedTime = $_POST['selectedTime'];

    // Connect to your database (replace placeholders with actual database credentials)
    $conn = new mysqli("localhost","root","","smd");

    // Check the connection
    if ($conn->connect_error) {
        // Unable to connect to the database
        $response = array("success" => false, "message" => "Database connection failed");
        echo json_encode($response);
        exit;
    }

    // Update the bookedTime in the database with the selected time
    $updatedTime = "$selectedTime on 30-12-2025"; // Assuming a fixed date for simplicity
    $sql = "UPDATE mentors SET bookedTime = '$updatedTime' WHERE mid = '$mentorId'";
    if (mysqli_query($conn, $sql)) {
        echo json_encode(array("message" => "Appointment booked successfully"));
    } else {
        // Output the SQL error message
        echo json_encode(array("error" => "Failed to book appointment", "sql_error" => mysqli_error($conn)));
    }
}

// Close database connection
mysqli_close($conn);
?>