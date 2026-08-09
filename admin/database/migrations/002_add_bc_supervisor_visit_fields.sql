-- Migration: Add BC Supervisor field visit report type and columns
-- Date: 2026-08-09
-- Description: Support BC Supervisors to record visits to BC Agents with QR code tracking

-- Update report_type ENUM to include new types
ALTER TABLE `visit_reports` 
MODIFY COLUMN `report_type` ENUM('recovery','ots','ckcc_renewal','ckcc_od','ckcc_npa_ots','bc_supervisor_visit','pre_npa','post_npa','other')
NOT NULL DEFAULT 'recovery';

-- Add BC Supervisor Visit specific columns
ALTER TABLE `visit_reports`
ADD COLUMN `bc_supervisor_name` VARCHAR(150) DEFAULT NULL COMMENT 'Name of supervisor conducting the visit' AFTER `supervisor_verified_at`,
ADD COLUMN `bc_supervisor_bcbf_code` VARCHAR(20) DEFAULT NULL COMMENT 'BCBF Code of the supervisor' AFTER `bc_supervisor_name`,
ADD COLUMN `supervised_agent_name` VARCHAR(150) DEFAULT NULL COMMENT 'Name of BC Supervisor/Agent being visited' AFTER `bc_supervisor_bcbf_code`,
ADD COLUMN `supervised_agent_bc_code` VARCHAR(40) DEFAULT NULL COMMENT 'BC Code of the supervised agent' AFTER `supervised_agent_name`,
ADD COLUMN `supervised_agent_iibf_number` VARCHAR(20) DEFAULT NULL COMMENT 'IIBF Certificate number of supervised agent' AFTER `supervised_agent_bc_code`,
ADD COLUMN `supervisor_visit_qualification` VARCHAR(255) DEFAULT NULL COMMENT 'Educational qualification of visited agent' AFTER `supervised_agent_iibf_number`,
ADD COLUMN `supervisor_visit_age` INT UNSIGNED DEFAULT NULL COMMENT 'Age of visited agent' AFTER `supervisor_visit_qualification`,
ADD COLUMN `supervisor_visit_address` VARCHAR(500) DEFAULT NULL COMMENT 'Residential address of visited agent' AFTER `supervisor_visit_age`,
ADD COLUMN `supervisor_visit_board_available` TINYINT(1) DEFAULT NULL COMMENT 'Board/materials available flag' AFTER `supervisor_visit_address`,
ADD COLUMN `supervisor_visit_equipment_status` VARCHAR(255) DEFAULT NULL COMMENT 'Equipment condition assessment' AFTER `supervisor_visit_board_available`,
ADD COLUMN `supervisor_visit_remuneration` VARCHAR(255) DEFAULT NULL COMMENT 'Remuneration details' AFTER `supervisor_visit_equipment_status`,
ADD COLUMN `supervisor_visit_feedback` VARCHAR(1000) DEFAULT NULL COMMENT 'Feedback on agent performance' AFTER `supervisor_visit_remuneration`,
ADD COLUMN `supervisor_visit_observation` VARCHAR(1000) DEFAULT NULL COMMENT 'General observations' AFTER `supervisor_visit_feedback`,
ADD COLUMN `supervisor_visit_qr_code` VARCHAR(255) DEFAULT NULL COMMENT 'Path to QR code PDF with tracking info' AFTER `supervisor_visit_observation`;

-- Create index for BC Supervisor Visit reports
CREATE INDEX `idx_bc_supervisor_visit` ON `visit_reports` (`report_type`, `created_at`) 
WHERE `report_type` = 'bc_supervisor_visit';
