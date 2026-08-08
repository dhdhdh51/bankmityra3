<?php

declare(strict_types=1);

namespace App\Models;

use App\Core\Database;
use App\Core\Paginator;

/**
 * BC Visit - records a BC Supervisor's visit to a BC Agent outlet.
 *
 * SQL schema (run manually or via migration):
 * ------------------------------------------------------------------
 * CREATE TABLE bc_visits (
 *     id              INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
 *     bca_name        VARCHAR(200) NOT NULL COMMENT 'Name Of Business Correspondent Agents (BCA)',
 *     branch_name     VARCHAR(200) NOT NULL COMMENT 'Branch Name',
 *     cbc_name        VARCHAR(200) DEFAULT NULL COMMENT 'Name Of CBC (Corporate Business Correspondent)',
 *     qualification   VARCHAR(200) DEFAULT NULL COMMENT 'Qualification of BCA',
 *     age             SMALLINT UNSIGNED DEFAULT NULL COMMENT 'Age',
 *     address_contact VARCHAR(500) DEFAULT NULL COMMENT 'Address with Contact No.',
 *     bc_certification_iibf ENUM('yes','no') DEFAULT NULL COMMENT 'BC Certification (IIBF)',
 *     iibf_certificate_no VARCHAR(100) DEFAULT NULL COMMENT 'IIBF Certificate Number',
 *     bc_working_since DATE DEFAULT NULL COMMENT 'BC working since at BC Outlet (Date)',
 *     appointment_letter ENUM('yes','no') DEFAULT NULL COMMENT 'Appointment Letter from Bank/CBC (Y/N)',
 *     identity_card ENUM('yes','no') DEFAULT NULL COMMENT 'Identity Card available (Y/N)',
 *     district_coordinator_name VARCHAR(300) DEFAULT NULL COMMENT 'Name Of District Coordinator / BC Supervisor and contact number',
 *     ssa_non_ssa     VARCHAR(300) DEFAULT NULL COMMENT 'Name Of SSA/Non SSA, Number of villages covered',
 *     board_available ENUM('yes','no') DEFAULT NULL COMMENT 'Board of CBC/Bank Available (Y/N)',
 *     dos_donts_board ENUM('yes','no') DEFAULT NULL COMMENT 'Availability and Display of Dos and Donts Board',
 *     services_list_display ENUM('yes','no') DEFAULT NULL COMMENT 'List of Services offered at BC points',
 *     sign_board      ENUM('yes','no') DEFAULT NULL COMMENT 'Sign Board at BC Points',
 *     board_bank_name VARCHAR(200) DEFAULT NULL COMMENT 'Bank Name on Board',
 *     board_link_br_name VARCHAR(200) DEFAULT NULL COMMENT 'Link Branch Name on Board',
 *     board_contact_br VARCHAR(100) DEFAULT NULL COMMENT 'Contact number of Branch',
 *     board_working_time VARCHAR(200) DEFAULT NULL COMMENT 'Business/Working time of BC outlet',
 *     transactions_previous_day TEXT DEFAULT NULL COMMENT 'Number of Transactions on previous day details',
 *     services_provided TEXT DEFAULT NULL COMMENT 'Number of services provided at BC points out of 39',
 *     bc_awareness_sss TEXT DEFAULT NULL COMMENT 'BC awareness of SSS and Bank Products',
 *     complaint_register ENUM('yes','no') DEFAULT NULL COMMENT 'Complaint Box/Complaint Register (Y/N)',
 *     transactions_register ENUM('yes','no') DEFAULT NULL COMMENT 'Transactions Register (Y/N)',
 *     visit_register ENUM('yes','no') DEFAULT NULL COMMENT 'Visit Register (Y/N)',
 *     register_other VARCHAR(300) DEFAULT NULL COMMENT 'Other register info',
 *     equip_laptop ENUM('yes','no') DEFAULT NULL COMMENT 'Laptop/Desktop',
 *     equip_biometric ENUM('yes','no') DEFAULT NULL COMMENT 'Biometric device',
 *     equip_pinpad ENUM('yes','no') DEFAULT NULL COMMENT 'Pin Pad Device',
 *     equip_receipt_machine ENUM('yes','no') DEFAULT NULL COMMENT 'Receipt generating Machine',
 *     equip_printer ENUM('yes','no') DEFAULT NULL COMMENT 'Printer',
 *     remuneration_month1 VARCHAR(100) DEFAULT NULL COMMENT 'Month 1 name',
 *     remuneration_amount1 DECIMAL(12,2) DEFAULT NULL COMMENT 'Month 1 remuneration',
 *     remuneration_month2 VARCHAR(100) DEFAULT NULL COMMENT 'Month 2 name',
 *     remuneration_amount2 DECIMAL(12,2) DEFAULT NULL COMMENT 'Month 2 remuneration',
 *     remuneration_month3 VARCHAR(100) DEFAULT NULL COMMENT 'Month 3 name',
 *     remuneration_amount3 DECIMAL(12,2) DEFAULT NULL COMMENT 'Month 3 remuneration',
 *     feedback_villagers TEXT DEFAULT NULL COMMENT 'Feedback from the villagers about BC services',
 *     bc_working_location TEXT DEFAULT NULL COMMENT 'BC is working in allotted location. If no then where?',
 *     other_info TEXT DEFAULT NULL COMMENT 'Other Information (if any)',
 *     photos_selfie VARCHAR(500) DEFAULT NULL COMMENT 'Photos/Selfie of BC Point (file path)',
 *     observation ENUM('excellent','good','satisfactory','poor') DEFAULT NULL COMMENT 'Observation rating',
 *     visiting_official_name VARCHAR(300) DEFAULT NULL COMMENT 'Name & contact no. of Visiting official - BC Supervisor',
 *     visiting_official_signature VARCHAR(300) DEFAULT NULL COMMENT 'Signature of Visiting Officials - BC Supervisor',
 *     visit_date DATE DEFAULT NULL COMMENT 'Date of visit / signature date',
 *     other_info_2 TEXT DEFAULT NULL COMMENT 'Other Information 2 (if any)',
 *     created_by INT UNSIGNED DEFAULT NULL COMMENT 'User who created the record',
 *     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 *     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 *     INDEX idx_branch_name (branch_name),
 *     INDEX idx_visit_date (visit_date),
 *     INDEX idx_created_by (created_by)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 * ------------------------------------------------------------------
 */
final class BcVisit
{
    /** Columns a user may sort by. */
    public const SORTABLE = ['bca_name', 'branch_name', 'visit_date', 'observation', 'created_at'];

    /** Observation options. */
    public const OBSERVATIONS = ['excellent', 'good', 'satisfactory', 'poor'];

    public static function find(int $id): ?array
    {
        return Database::instance()->first('SELECT * FROM bc_visits WHERE id = ? LIMIT 1', [$id]);
    }

    public static function paginate(
        string $search,
        string $sortBy,
        string $sortDir,
        int $page,
        int $perPage
    ): Paginator {
        $where = ['1 = 1'];
        $params = [];

        if ($search !== '') {
            $where[] = '(bca_name LIKE ? OR branch_name LIKE ? OR cbc_name LIKE ? OR visiting_official_name LIKE ?)';
            $like = '%' . $search . '%';
            array_push($params, $like, $like, $like, $like);
        }

        $clause = implode(' AND ', $where);
        $orderColumn = in_array($sortBy, self::SORTABLE, true) ? $sortBy : 'created_at';
        $direction = strtoupper($sortDir) === 'ASC' ? 'ASC' : 'DESC';

        return Paginator::fromQuery(
            "SELECT COUNT(*) FROM bc_visits WHERE {$clause}",
            "SELECT * FROM bc_visits WHERE {$clause} ORDER BY `{$orderColumn}` {$direction}, id DESC",
            $params,
            $page,
            $perPage
        );
    }

    /** @param array<string,mixed> $data */
    public static function create(array $data): int
    {
        return Database::instance()->insert('bc_visits', $data);
    }

    /** @param array<string,mixed> $data */
    public static function update(int $id, array $data): void
    {
        Database::instance()->update('bc_visits', $data, ['id' => $id]);
    }

    public static function delete(int $id): void
    {
        Database::instance()->delete('bc_visits', ['id' => $id]);
    }
}
