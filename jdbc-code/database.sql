CREATE DATABASE IF NOT EXISTS batch20;
USE batch20;

CREATE TABLE IF NOT EXISTS doctors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    specialization VARCHAR(50) NOT NULL,
    experience INT NOT NULL,
    gender VARCHAR(10),
    available_days VARCHAR(100),
    available_time TIME NOT NULL,
    address VARCHAR(255) NOT NULL,
    certificate VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    dob DATE,
    appointment DATETIME,
    gender VARCHAR(10),
    disease VARCHAR(100),
    blood_group VARCHAR(5),
    address VARCHAR(255),
    certificate VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
