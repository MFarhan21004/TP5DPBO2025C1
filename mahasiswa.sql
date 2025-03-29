-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 29, 2025 at 08:14 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_mahasiswa`
--

-- --------------------------------------------------------

--
-- Table structure for table `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `id` int(11) NOT NULL,
  `nim` varchar(255) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `jenis_kelamin` varchar(255) NOT NULL,
  `jalur_masuk` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mahasiswa`
--

INSERT INTO `mahasiswa` (`id`, `nim`, `nama`, `jenis_kelamin`, `jalur_masuk`) VALUES
(24, '2202346', 'Muhammad Rifky Afandi', 'Laki-laki', 'Mandiri'),
(25, '2210239', 'Muhammad Hanif Abdillah', 'Laki-laki', 'SNBT'),
(26, '2202046', 'Nurainun', 'Perempuan', 'SNBP'),
(27, '2205101', 'Kelvin Julian Putra', 'Laki-laki', 'Mandiri'),
(28, '2200163', 'Rifanny Lysara Annastasya', 'Perempuan', 'SNBP'),
(29, '2202869', 'Revana Faliha Salma', 'Perempuan', 'SNBT'),
(30, '2209489', 'Rakha Dhifiargo Hariadi', 'Laki-laki', 'Mandiri'),
(31, '2203142', 'Roshan Syalwan Nurilham', 'Laki-laki', 'SNBP'),
(32, '2200311', 'Raden Rahman Ismail', 'Laki-laki', 'SNBT'),
(33, '2200978', 'Ratu Syahirah Khairunnisa', 'Perempuan', 'Mandiri'),
(34, '2204509', 'Muhammad Fahreza Fauzan', 'Laki-laki', 'SNBP'),
(35, '2205027', 'Muhammad Rizki Revandi', 'Laki-laki', 'SNBT'),
(36, '2203484', 'Arya Aydin Margono', 'Laki-laki', 'Mandiri'),
(37, '2200481', 'Marvel Ravindra Dioputra', 'Laki-laki', 'SNBP'),
(38, '2209889', 'Muhammad Fadlul Hafiizh', 'Laki-laki', 'SNBT'),
(39, '2206697', 'Rifa Sania', 'Perempuan', 'Mandiri'),
(40, '2207260', 'Imam Chalish Rafidhul Haque', 'Laki-laki', 'SNBP'),
(48, '2309323', 'Muhammad Farhan', 'Laki-laki', 'SNBP');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
