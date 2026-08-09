<?php

declare(strict_types=1);

namespace App\Models;

use App\Core\Database;
use App\Core\Paginator;

/**
 * BC Supervisor visit reports.
 *
 * Supervisors visit BC Agents to assess their performance, equipment, documentation,
 * and remuneration. Each visit captures agent details (auto-populated from the user record)
 * and supervisor observations and feedback.
 */
final class BcVisit
{
    public const SORTABLE = ['visit_date', 'bca_name', 'branch_name', 'created_at'];

    /**
     * Find a specific BC visit by ID.
     */
    public static function find(int $id): ?array
    {
        return Database::instance()->first(
            'SELECT v.*,'
            . '       u.name AS agent_name, u.employee_code,'
            . '       s.name AS supervisor_name, s.employee_code AS supervisor_code,'
            . '       b.name AS branch_full_name'
            . '  FROM bc_visits v'
            . '  LEFT JOIN users u ON u.id = v.user_id'
            . '  LEFT JOIN users s ON s.id = v.supervisor_id'
            . '  LEFT JOIN branches b ON b.name = v.branch_name'
            . ' WHERE v.id = ? LIMIT 1',
            [$id],
        );
    }

    /**
     * Paginate BC visits with filters.
     */
    public static function paginate(
        string $search,
        ?int $userId,
        ?int $supervisorId,
        ?int $branchId,
        string $dateFrom,
        string $dateTo,
        string $sortBy,
        string $sortDir,
        int $page,
        int $perPage,
    ): Paginator {
        $where = ['1 = 1'];
        $params = [];

        if ($search !== '') {
            $where[] = '(v.bca_name LIKE ? OR v.bc_code LIKE ? OR u.employee_code LIKE ?)';
            $like = '%' . $search . '%';
            array_push($params, $like, $like, $like);
        }

        if ($userId !== null) {
            $where[] = 'v.user_id = ?';
            $params[] = $userId;
        }

        if ($supervisorId !== null) {
            $where[] = 'v.supervisor_id = ?';
            $params[] = $supervisorId;
        }

        if ($branchId !== null) {
            // Match by branch_name since it's stored denormalized
            $branchName = Database::instance()->scalar(
                'SELECT name FROM branches WHERE id = ? LIMIT 1',
                [$branchId]
            );
            if ($branchName !== null) {
                $where[] = 'v.branch_name = ?';
                $params[] = $branchName;
            }
        }

        if ($dateFrom !== '') {
            $where[] = 'v.visit_date >= ?';
            $params[] = $dateFrom;
        }

        if ($dateTo !== '') {
            $where[] = 'v.visit_date <= ?';
            $params[] = $dateTo;
        }

        $clause = implode(' AND ', $where);

        $sortable = [
            'visit_date' => 'v.visit_date',
            'bca_name' => 'v.bca_name',
            'branch_name' => 'v.branch_name',
            'created_at' => 'v.created_at',
        ];

        $orderColumn = $sortable[$sortBy] ?? 'v.visit_date';
        $direction = strtoupper($sortDir) === 'ASC' ? 'ASC' : 'DESC';

        return Paginator::fromQuery(
            'SELECT COUNT(*) FROM bc_visits v LEFT JOIN users u ON u.id = v.user_id WHERE ' . $clause,
            'SELECT v.*, u.name AS agent_name, u.employee_code, s.name AS supervisor_name'
            . '  FROM bc_visits v'
            . '  LEFT JOIN users u ON u.id = v.user_id'
            . '  LEFT JOIN users s ON s.id = v.supervisor_id'
            . ' WHERE ' . $clause
            . ' ORDER BY ' . $orderColumn . ' ' . $direction . ', v.id DESC',
            $params,
            $page,
            $perPage,
        );
    }

    /**
     * Create a new BC visit record.
     *
     * @param array<string,mixed> $data
     */
    public static function create(array $data): int
    {
        return Database::instance()->insert('bc_visits', $data);
    }

    /**
     * Update a BC visit record.
     *
     * @param array<string,mixed> $data
     */
    public static function update(int $id, array $data): void
    {
        Database::instance()->update('bc_visits', $data, ['id' => $id]);
    }

    /**
     * Delete a BC visit record.
     */
    public static function delete(int $id): void
    {
        Database::instance()->delete('bc_visits', ['id' => $id]);
    }

    /**
     * Get agent details for auto-population in the form.
     * Returns: {id, name, bc_code, sp_cbc_name, branch_name, iibf_number, ssa, link_branch, region_ro}
     */
    public static function agentDetails(int $userId): ?array
    {
        return Database::instance()->first(
            'SELECT u.id,'
            . '       u.name,'
            . '       u.bc_code,'
            . '       u.sp_cbc_name,'
            . '       u.iibf_number,'
            . '       u.ssa,'
            . '       u.link_branch,'
            . '       u.region_ro,'
            . '       b.name AS branch_name'
            . '  FROM users u'
            . '  LEFT JOIN branches b ON b.id = u.branch_id'
            . ' WHERE u.id = ? AND u.role_id = (SELECT id FROM roles WHERE slug = "agent") LIMIT 1',
            [$userId],
        );
    }
}
