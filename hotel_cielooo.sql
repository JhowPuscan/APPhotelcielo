-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3307
-- Tiempo de generación: 19-10-2024 a las 19:23:51
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `hotel_cielo`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `codigopostal` int(11) DEFAULT NULL,
  `pais` varchar(255) NOT NULL,
  `telefono` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `nombre`, `email`, `direccion`, `codigopostal`, `pais`, `telefono`) VALUES
(1, 'leonardo valenzuela', 'leovc148@gmail.com', NULL, NULL, '', NULL),
(2, 'leonardo vc', 'leovc143@gmail.com', NULL, NULL, '', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `habitaciones`
--

CREATE TABLE `habitaciones` (
  `id` int(11) NOT NULL,
  `numero_habitacion` varchar(10) NOT NULL,
  `capacidad_maxima` int(11) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `disponibilidad` tinyint(1) NOT NULL,
  `precio_por_noche` decimal(10,2) NOT NULL,
  `urlimagen` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `habitaciones`
--

INSERT INTO `habitaciones` (`id`, `numero_habitacion`, `capacidad_maxima`, `categoria`, `disponibilidad`, `precio_por_noche`, `urlimagen`) VALUES
(1, '101', 4, 'Presidencial', 1, 200.00, 'https://images.mirai.com/HOST/500255/room-24672.jpg'),
(2, '102', 8, 'Presidencial', 1, 800.00, 'https://images.mirai.com/HOST/500255/room-24672.jpg'),
(3, '103', 2, 'Matrimonial', 1, 150.00, 'https://www.kingdomhotelperu.com/wp-content/uploads/2019/11/MATRIMONIAL-scaled.jpg'),
(4, '104', 4, 'Matrimonial', 1, 150.00, 'https://www.kingdomhotelperu.com/wp-content/uploads/2019/11/MATRIMONIAL-scaled.jpg'),
(5, '105', 5, 'Familiar', 1, 300.00, 'https://www.harlingfordhotel.com/wp-content/uploads/2020/02/st-family-room-4.jpg'),
(6, '106', 7, 'Familiar', 1, 300.00, 'https://www.harlingfordhotel.com/wp-content/uploads/2020/02/st-family-room-4.jpg'),
(7, '201', 6, 'Presidencial', 1, 250.00, 'https://images.mirai.com/HOST/500255/room-24672.jpg'),
(8, '202', 3, 'Presidencial', 1, 250.00, 'https://images.mirai.com/HOST/500255/room-24672.jpg'),
(9, '203', 2, 'Matrimonial', 1, 150.00, 'https://www.kingdomhotelperu.com/wp-content/uploads/2019/11/MATRIMONIAL-scaled.jpg'),
(10, '204', 2, 'Matrimonial', 1, 150.00, 'https://www.kingdomhotelperu.com/wp-content/uploads/2019/11/MATRIMONIAL-scaled.jpg'),
(11, '205', 9, 'Familiar', 1, 300.00, 'https://www.harlingfordhotel.com/wp-content/uploads/2020/02/st-family-room-4.jpg'),
(12, '206', 10, 'Familiar', 1, 540.00, 'https://www.harlingfordhotel.com/wp-content/uploads/2020/02/st-family-room-4.jpg'),
(13, '301', 4, 'Presidencial', 1, 500.00, 'https://images.mirai.com/HOST/500255/room-24672.jpg'),
(14, '302', 9, 'Presidencial', 1, 400.00, 'https://images.mirai.com/HOST/500255/room-24672.jpg'),
(15, '303', 2, 'Matrimonial', 1, 600.00, 'https://www.kingdomhotelperu.com/wp-content/uploads/2019/11/MATRIMONIAL-scaled.jpg'),
(16, '304', 2, 'Matrimonial', 1, 600.00, 'https://www.kingdomhotelperu.com/wp-content/uploads/2019/11/MATRIMONIAL-scaled.jpg'),
(17, '305', 6, 'Familiar', 1, 400.00, 'https://www.harlingfordhotel.com/wp-content/uploads/2020/02/st-family-room-4.jpg'),
(18, '306', 6, 'Familiar', 1, 400.00, 'https://www.harlingfordhotel.com/wp-content/uploads/2020/02/st-family-room-4.jpg'),
(19, '401', 2, 'Pequeña', 1, 100.00, 'https://images.mirai.com/INFOROOMS/10030559/NzLXA8mGnfQhLUeZXSKJ/NzLXA8mGnfQhLUeZXSKJ_large.jpg'),
(20, '402', 4, 'Pequeña', 1, 250.00, 'https://images.mirai.com/INFOROOMS/10030559/NzLXA8mGnfQhLUeZXSKJ/NzLXA8mGnfQhLUeZXSKJ_large.jpg'),
(21, '403', 2, 'Pequeña', 1, 100.00, 'https://images.mirai.com/INFOROOMS/10030559/NzLXA8mGnfQhLUeZXSKJ/NzLXA8mGnfQhLUeZXSKJ_large.jpg'),
(22, '404', 3, 'Pequeña', 1, 150.00, 'https://images.mirai.com/INFOROOMS/10030559/NzLXA8mGnfQhLUeZXSKJ/NzLXA8mGnfQhLUeZXSKJ_large.jpg'),
(23, '405', 2, 'Pequeña', 1, 100.00, 'https://images.mirai.com/INFOROOMS/10030559/NzLXA8mGnfQhLUeZXSKJ/NzLXA8mGnfQhLUeZXSKJ_large.jpg'),
(24, '406', 3, 'Pequeña', 1, 150.00, 'https://images.mirai.com/INFOROOMS/10030559/NzLXA8mGnfQhLUeZXSKJ/NzLXA8mGnfQhLUeZXSKJ_large.jpg');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pagos`
--

CREATE TABLE `pagos` (
  `id` int(11) NOT NULL,
  `reserva_id` int(11) DEFAULT NULL,
  `monto` decimal(10,2) NOT NULL,
  `metodo_pago` varchar(50) NOT NULL,
  `fecha_pago` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reservas`
--

CREATE TABLE `reservas` (
  `id` int(11) NOT NULL,
  `cliente_id` int(11) DEFAULT NULL,
  `habitacion_id` int(11) DEFAULT NULL,
  `fecha_reserva` datetime NOT NULL,
  `fecha_entrada` datetime NOT NULL,
  `fecha_salida` datetime NOT NULL,
  `estado` varchar(50) NOT NULL,
  `monto_total` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `reservas`
--

INSERT INTO `reservas` (`id`, `cliente_id`, `habitacion_id`, `fecha_reserva`, `fecha_entrada`, `fecha_salida`, `estado`, `monto_total`) VALUES
(1, 1, 1, '2024-10-15 01:41:42', '2024-10-25 01:41:42', '2024-10-30 01:41:42', 'en espera', 200.00);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indices de la tabla `habitaciones`
--
ALTER TABLE `habitaciones`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `pagos`
--
ALTER TABLE `pagos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `reserva_id` (`reserva_id`);

--
-- Indices de la tabla `reservas`
--
ALTER TABLE `reservas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cliente_id` (`cliente_id`),
  ADD KEY `habitacion_id` (`habitacion_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT de la tabla `habitaciones`
--
ALTER TABLE `habitaciones`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de la tabla `pagos`
--
ALTER TABLE `pagos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `reservas`
--
ALTER TABLE `reservas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `pagos`
--
ALTER TABLE `pagos`
  ADD CONSTRAINT `pagos_ibfk_1` FOREIGN KEY (`reserva_id`) REFERENCES `reservas` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `reservas`
--
ALTER TABLE `reservas`
  ADD CONSTRAINT `reservas_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `reservas_ibfk_2` FOREIGN KEY (`habitacion_id`) REFERENCES `habitaciones` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
