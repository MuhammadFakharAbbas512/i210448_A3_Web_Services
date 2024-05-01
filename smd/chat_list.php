<?php
include 'conn.php';

// Connect to your database
$conn = new mysqli("localhost", "root", "", "smd");

// Check the connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Fetch users from the database
$sql = "SELECT userId, name, profileImage FROM users";
$result = $conn->query($sql);

// Check if there are any users
if ($result->num_rows > 0) {
    $users = array();
    while ($row = $result->fetch_assoc()) {
        $users[] = $row;
    }
    // Output the users as JSON
    echo json_encode($users);
} else {
    echo "0 results";
}

// Close the database connection
$conn->close();
?>
