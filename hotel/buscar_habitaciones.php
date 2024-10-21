<?php
header('Content-Type: application/json');
include 'conexion.php';

// Obtener parámetros de la solicitud GET
$tipos_habitacion = isset($_GET['tiposHabitacion']) ? explode(',', $_GET['tiposHabitacion']) : [];
$capacidad_maxima = isset($_GET['capacidadMaxima']) ? intval($_GET['capacidadMaxima']) : 0;
$check_in = isset($_GET['checkIn']) ? $conn->real_escape_string($_GET['checkIn']) : '';
$check_out = isset($_GET['checkOut']) ? $conn->real_escape_string($_GET['checkOut']) : '';

// Inicializar la consulta SQL
$sql = "SELECT * FROM habitaciones WHERE disponibilidad = 1";

// Agregar filtros según los parámetros recibidos
if (!empty($tipos_habitacion)) {
    $tipos_habitacion_sql = "'" . implode("','", $tipos_habitacion) . "'";
    $sql .= " AND categoria IN ($tipos_habitacion_sql)";
}

if (!empty($check_in) && !empty($check_out)) {
    // Excluir habitaciones que ya están reservadas en el rango de fechas solicitado
    $sql .= " AND id NOT IN (
                SELECT habitacion_id FROM reservas 
                WHERE 
                    (fecha_entrada <= '$check_out' AND fecha_salida >= '$check_in')
              )";
}

if ($capacidad_maxima > 0) {
    $sql .= " AND capacidad_maxima >= $capacidad_maxima";
}

// Ejecutar la consulta
$result = $conn->query($sql);

if ($result) {
    if ($result->num_rows > 0) {
        $habitaciones = array();
        while ($row = $result->fetch_assoc()) {
            // Agregar cada habitación al arreglo de resultados
            $habitaciones[] = array(
                "id" => intval($row['id']),
                "numeroHabitacion" => $row['numero_habitacion'],
                "capacidadMaxima" => intval($row['capacidad_maxima']),
                "categoria" => $row['categoria'],
                "disponibilidad" => boolval($row['disponibilidad']),
                "precioPorNoche" => floatval($row['precio_por_noche']),
		"urlimagen" => $row['urlimagen'],
            );
        }
        // Devolver la respuesta JSON con éxito
        echo json_encode([
            "success" => true,
            "habitaciones" => $habitaciones
        ]);
    } else {
        // No se encontraron habitaciones que coincidan con los filtros
        echo json_encode([
            "success" => false,
            "message" => "No se encontraron habitaciones disponibles según los filtros seleccionados."
        ]);
    }
} else {
    // Error en la consulta SQL
    echo json_encode([
        "success" => false,
        "message" => "Error en la consulta: " . $conn->error
    ]);
}

// Cerrar la conexión
$conn->close();
?>
