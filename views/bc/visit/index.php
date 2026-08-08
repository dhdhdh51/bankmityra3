<?php
/**
 * @var \App\Core\Paginator $visits
 * @var string              $search
 * @var string              $sortBy
 * @var string              $sortDir
 */
?>

<div class="lrms-page-head">
    <div>
        <h1>BC Visits</h1>
        <p>BC Supervisor visits to BC Agent outlets</p>
    </div>
    <?php if (can('bc_visit.manage')): ?>
        <a href="<?= e(url('/bc/visit/create')) ?>" class="btn btn-primary btn-sm">
            <?= icon('plus') ?> Record Visit
        </a>
    <?php endif; ?>
</div>

<div class="lrms-card mb-3">
    <div class="lrms-card-body">
        <form method="get" action="<?= e(url('/bc/visit')) ?>">
            <?= sort_hidden($sortBy, $sortDir) ?>
            <div class="lrms-filters">
                <div>
                    <label class="form-label" for="v-search">Search</label>
                    <input type="search" class="form-control" id="v-search" name="search"
                           value="<?= e($search) ?>" placeholder="BCA name, branch, CBC, visiting official">
                </div>
                <div class="filter-actions">
                    <button type="submit" class="btn btn-primary"><?= icon('filter') ?> Filter</button>
                    <a href="<?= e(url('/bc/visit')) ?>" class="btn btn-outline-secondary">Reset</a>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="lrms-card">
    <?php if ($visits->isEmpty()): ?>
        <?= \App\Core\View::partial('partials/empty', [
            'heading'     => 'No BC visits recorded',
            'message'     => 'Record a BC Supervisor visit to a BC Agent outlet.',
            'iconName'    => 'clipboard',
            'actionLabel' => can('bc_visit.manage') ? 'Record Visit' : null,
            'actionUrl'   => can('bc_visit.manage') ? url('/bc/visit/create') : null,
        ]) ?>
    <?php else: ?>
        <div class="lrms-table-wrap">
            <table class="lrms-table">
                <thead>
                    <tr>
                        <th><?= sort_link('BCA Name', 'bca_name', $sortBy, $sortDir) ?></th>
                        <th><?= sort_link('Branch', 'branch_name', $sortBy, $sortDir) ?></th>
                        <th><?= sort_link('Visit Date', 'visit_date', $sortBy, $sortDir) ?></th>
                        <th><?= sort_link('Observation', 'observation', $sortBy, $sortDir) ?></th>
                        <th>Visiting Official</th>
                        <th><?= sort_link('Created', 'created_at', $sortBy, $sortDir) ?></th>
                        <th class="text-end">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($visits->items as $visit): ?>
                        <tr>
                            <td style="font-weight:550"><?= e((string) $visit['bca_name']) ?></td>
                            <td style="font-size:.8125rem"><?= e((string) $visit['branch_name']) ?></td>
                            <td style="font-size:.8125rem"><?= $visit['visit_date'] ? fmt_date($visit['visit_date']) : '-' ?></td>
                            <td>
                                <?php if ($visit['observation']): ?>
                                    <span class="lrms-badge <?php
                                        echo match($visit['observation']) {
                                            'excellent' => 'badge-visited',
                                            'good' => 'badge-visited',
                                            'satisfactory' => 'badge-pending',
                                            'poor' => 'badge-closed',
                                            default => ''
                                        };
                                    ?>"><?= e(ucfirst((string) $visit['observation'])) ?></span>
                                <?php else: ?>
                                    -
                                <?php endif; ?>
                            </td>
                            <td style="font-size:.8125rem"><?= nullable($visit['visiting_official_name']) ?></td>
                            <td style="font-size:.8125rem"><?= fmt_date($visit['created_at']) ?></td>
                            <td class="text-end nowrap">
                                <?php if (can('bc_visit.manage')): ?>
                                    <a href="<?= e(url('/bc/visit/' . (int) $visit['id'] . '/edit')) ?>"
                                       class="btn btn-ghost btn-sm btn-icon" title="Edit"
                                       data-bs-toggle="tooltip"><?= icon('edit') ?></a>
                                    <form method="post" class="d-inline m-0"
                                          action="<?= e(url('/bc/visit/' . (int) $visit['id'] . '/delete')) ?>"
                                          data-confirm="Delete this BC visit record for <?= e((string) $visit['bca_name']) ?>?">
                                        <?= csrf_field() ?>
                                        <button type="submit" class="btn btn-ghost btn-sm btn-icon text-danger"
                                                title="Delete" data-bs-toggle="tooltip"><?= icon('trash') ?></button>
                                    </form>
                                <?php endif; ?>
                            </td>
                        </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
        </div>

        <div class="lrms-card-foot">
            <?= \App\Core\View::partial('partials/pagination', ['paginator' => $visits, 'label' => 'visits']) ?>
        </div>
    <?php endif; ?>
</div>
