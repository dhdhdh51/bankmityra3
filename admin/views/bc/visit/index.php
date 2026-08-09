<?php
/**
 * BC Supervisor Visits List
 *
 * @var App\Core\Paginator $visits
 * @var string $search
 * @var int|null $userId
 * @var int|null $supervisorId
 * @var int|null $branchId
 * @var string $dateFrom
 * @var string $dateTo
 * @var list<array<string,mixed>> $branches
 * @var list<array<string,mixed>> $agents
 * @var list<array<string,mixed>> $supervisors
 * @var string $sortBy
 * @var string $sortDir
 * @var array<string,mixed> $old
 * @var array<string,list<string>> $errors
 */

$sortUrl = static function (string $field): string {
    global $sortBy, $sortDir;
    $newDir = ($sortBy === $field && $sortDir === 'ASC') ? 'DESC' : 'ASC';
    return url('/bc/visit?sort=' . e($field) . '&order=' . e($newDir));
};

$sortIcon = static function (string $field): string {
    global $sortBy, $sortDir;
    if ($sortBy !== $field) {
        return '';
    }
    return $sortDir === 'ASC'
        ? ' <i class="fas fa-arrow-up" style="font-size:.75rem"></i>'
        : ' <i class="fas fa-arrow-down" style="font-size:.75rem"></i>';
};
?>

<div class="lrms-page-head">
    <div>
        <h1>BC Supervisor Visits</h1>
        <p>Supervisor visit reports for BC Agents assessment</p>
    </div>
    <div>
        <a href="<?= e(url('/bc/visit/create')) ?>" class="btn btn-primary">
            <i class="fas fa-plus"></i>
            New Visit
        </a>
    </div>
</div>

<div class="lrms-filters">
    <form method="get" action="<?= e(url('/bc/visit')) ?>" class="row g-2">
        <div class="col-lg-3">
            <input type="text" class="form-control form-control-sm" name="search"
                   value="<?= e($search) ?>"
                   placeholder="Search by name, code, employee ID&hellip;">
        </div>

        <div class="col-lg-2">
            <select class="form-select form-select-sm" name="branch_id">
                <option value="">All branches</option>
                <?php foreach ($branches as $branch): ?>
                    <option value="<?= (int) $branch['id'] ?>"
                        <?= ($branchId ?? 0) === (int) $branch['id'] ? 'selected' : '' ?>>
                        <?= e((string) $branch['name']) ?>
                    </option>
                <?php endforeach; ?>
            </select>
        </div>

        <div class="col-lg-2">
            <input type="date" class="form-control form-control-sm" name="date_from"
                   value="<?= e($dateFrom) ?>"
                   placeholder="From date&hellip;">
        </div>

        <div class="col-lg-2">
            <input type="date" class="form-control form-control-sm" name="date_to"
                   value="<?= e($dateTo) ?>"
                   placeholder="To date&hellip;">
        </div>

        <div class="col-lg-auto">
            <button type="submit" class="btn btn-outline-secondary btn-sm">
                <i class="fas fa-search"></i>
                Filter
            </button>
            <a href="<?= e(url('/bc/visit')) ?>" class="btn btn-outline-secondary btn-sm">
                <i class="fas fa-times"></i>
            </a>
        </div>
    </form>
</div>

<?php if ($visits->count() === 0): ?>
    <div class="alert alert-info" role="alert">
        <i class="fas fa-info-circle"></i>
        No BC Supervisor visits found.
        <a href="<?= e(url('/bc/visit/create')) ?>">Create one now</a>
    </div>
<?php else: ?>
    <div class="table-responsive">
        <table class="table table-hover table-striped mb-0">
            <thead>
                <tr>
                    <th><a href="<?= e($sortUrl('visit_date')) ?>" class="text-decoration-none">Visit Date<?= $sortIcon('visit_date') ?></a></th>
                    <th><a href="<?= e($sortUrl('bca_name')) ?>" class="text-decoration-none">BC Agent<?= $sortIcon('bca_name') ?></a></th>
                    <th><a href="<?= e($sortUrl('branch_name')) ?>" class="text-decoration-none">Branch<?= $sortIcon('branch_name') ?></a></th>
                    <th>BC Code</th>
                    <th style="width:120px">Actions</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($visits as $visit): ?>
                    <tr>
                        <td>
                            <span class="badge bg-light text-dark">
                                <?= e(date('d M Y', (int) strtotime((string) $visit['visit_date']))) ?>
                            </span>
                        </td>
                        <td>
                            <strong><?= e((string) ($visit['bca_name'] ?? '')) ?></strong>
                            <?php if ($visit['agent_name']): ?>
                                <br><small class="text-muted"><?= e((string) $visit['agent_name']) ?></small>
                            <?php endif; ?>
                        </td>
                        <td><?= e((string) ($visit['branch_name'] ?? '—')) ?></td>
                        <td><?= e((string) ($visit['bc_code'] ?? '—')) ?></td>
                        <td>
                            <a href="<?= e(url('/bc/visit/' . (int) $visit['id'])) ?>" class="btn btn-sm btn-outline-primary" title="View">
                                <i class="fas fa-eye"></i>
                            </a>
                            <a href="<?= e(url('/bc/visit/' . (int) $visit['id'] . '/edit')) ?>" class="btn btn-sm btn-outline-secondary" title="Edit">
                                <i class="fas fa-edit"></i>
                            </a>
                            <form method="post" action="<?= e(url('/bc/visit/' . (int) $visit['id'] . '/delete')) ?>" style="display:inline"
                                  onsubmit="return confirm('Are you sure?')">
                                <?= csrf_field() ?>
                                <button type="submit" class="btn btn-sm btn-outline-danger" title="Delete">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    </div>

    <?= $visits->render() ?>
<?php endif; ?>
