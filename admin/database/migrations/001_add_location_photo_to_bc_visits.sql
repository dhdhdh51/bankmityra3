-- Migration: Add location (latitude/longitude) and photo to bc_visits table
-- Date: 2026-08-09
-- Description: Enable BC Supervisor visits to capture GPS location and photos

ALTER TABLE `bc_visits`
ADD COLUMN `latitude` DECIMAL(10, 8) DEFAULT NULL COMMENT 'GPS latitude of visit location' AFTER `visiting_official_name`,
ADD COLUMN `longitude` DECIMAL(11, 8) DEFAULT NULL COMMENT 'GPS longitude of visit location' AFTER `latitude`,
ADD COLUMN `photo_path` VARCHAR(255) DEFAULT NULL COMMENT 'Path to visit photo (optional)' AFTER `longitude`;

-- Create index for searching by location
CREATE INDEX `idx_location` ON `bc_visits` (`latitude`, `longitude`);
