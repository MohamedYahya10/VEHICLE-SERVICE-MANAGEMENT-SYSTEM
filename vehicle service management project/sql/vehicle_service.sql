-- ============================================================
--  Vehicle Service Management System  -  Database Setup Script
--  Database : vehicle_service
--  Table    : service_records
-- ============================================================
--  HOW TO RUN THIS FILE:
--  Option A (MySQL Workbench):  Open this file, then click the lightning
--            "Execute" button (make sure the whole script is selected).
--  Option B (Command line):     mysql -u root -p < vehicle_service.sql
--
--  It is SAFE to run more than once. If the database/table already exist
--  nothing is lost.
-- ============================================================

-- 1) Create the database (only if it does not already exist)
CREATE DATABASE IF NOT EXISTS vehicle_service
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

-- Use the database for everything that follows
USE vehicle_service;

-- 2) Create the service_records table (only if it does not already exist)
CREATE TABLE IF NOT EXISTS service_records (
    id             INT AUTO_INCREMENT PRIMARY KEY,   -- unique id for every record
    vehicle_number VARCHAR(20)  NOT NULL,            -- e.g. KA01AB1234
    owner_name     VARCHAR(100) NOT NULL,            -- customer name
    vehicle_model  VARCHAR(100),                     -- e.g. Maruti Swift
    service_type   VARCHAR(100),                     -- e.g. Oil Change
    service_date   VARCHAR(20),                      -- date as yyyy-MM-dd (kept simple)
    cost           DECIMAL(10,2) DEFAULT 0.00,       -- service charge
    status         VARCHAR(30)  DEFAULT 'Pending',   -- Pending / In Progress / Completed
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3) (Optional) Insert a couple of sample rows so the app is not empty.
--    These only run if the table is currently empty, so re-running is safe.
INSERT INTO service_records
    (vehicle_number, owner_name, vehicle_model, service_type, service_date, cost, status)
SELECT * FROM (
    SELECT 'KA01AB1234' AS vn, 'Ravi Kumar'   AS on_, 'Maruti Swift'  AS vm, 'Oil Change'    AS st, '2026-10-01' AS sd, 2500.00 AS c, 'Completed'  AS ss
    UNION ALL
    SELECT 'KA05CD5678',      'Priya Sharma',  'Hyundai i20',  'Brake Service',  '2026-10-05',        4200.00,          'In Progress'
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM service_records);

-- 4) Show what we ended up with (handy confirmation in Workbench / CLI)
SELECT * FROM service_records;
