<?php
header("Content-Type: application/json; charset=UTF-8");
include 'conexion.php';

// Obtener datos del POST
$nombre = isset($_POST['nombre']) ? trim($_POST['nombre']) : '';
$email = isset($_POST['email']) ? trim($_POST['email']) : '';

// Validar datos
if (empty($nombre) || empty($email)) {
    echo json_encode(array("success" => false, "message" => "Nombre y email son requeridos."));
    exit();
}

// Validar formato de email
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    echo json_encode(array("success" => false, "message" => "Formato de email inválido."));
    exit();
}

// Preparar sentencia para evitar inyección SQL
$stmt = $conn->prepare("INSERT INTO clientes (nombre, email) VALUES (?, ?)");
$stmt->bind_param("ss", $nombre, $email);

// Ejecutar la consulta
if ($stmt->execute()) {
    echo json_encode(array("success" => true, "message" => "Registro exitoso."));
} else {
    // Manejar errores, como duplicados
    if ($conn->errno == 1062) { // Código de error para duplicado en MySQL
        echo json_encode(array("success" => false, "message" => "El email ya está registrado."));
    } else {
        echo json_encode(array("success" => false, "message" => "Error: " . $conn->error));
    }
}

$stmt->close();
$conn->close();
?>
