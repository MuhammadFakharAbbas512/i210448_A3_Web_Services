<?php
include 'conn.php';

try {
    // Check if the mentor ID is present in the GET request
    if(isset($_GET['mid'])) {
        // Connect to your database (replace placeholders with actual database credentials)
        $conn = new mysqli("localhost","root","","smd");

        // Check the connection
        if ($conn->connect_error)
        {
            throw new Exception("Database connection failed");
        }
        // Sanitize the mentor ID
        $mid = mysqli_real_escape_string($conn, $_GET['mid']);

        // Search for mentors by ID using prepared statements
        $stmt = $conn->prepare("SELECT * FROM mentors WHERE mid = ?");
        $stmt->bind_param("s", $mid);
        $stmt->execute();

        $result = $stmt->get_result();
        $mentors = array();
        while ($row = $result->fetch_assoc()) {
            $mentors[] = $row;
        }

        // Return search results as JSON
        echo json_encode($mentors);
    } else {
        throw new Exception("Mentor ID is missing.");
    }
} catch (Exception $e) {
    // Return error response as JSON
    $response['success'] = false;
    $response['message'] = $e->getMessage();
    echo json_encode($response);
}

// Close database connection
mysqli_close($conn);
?>