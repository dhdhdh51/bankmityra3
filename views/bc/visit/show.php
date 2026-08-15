<?php
/**
 * BC Supervisor Visit Detail View
 *
 * @var array<string,mixed> $visit
 */
?>

<div class="lrms-page-head">
    <div>
        <nav aria-label="Breadcrumb" class="mb-1" style="font-size:.75rem">
            <a href="<?= e(url('/bc/visit')) ?>" class="text-muted">BC Supervisor Visits</a>
            <span class="text-muted mx-1">/</span>
            <span class="text-muted">View</span>
        </nav>
        <h1>BC Supervisor Visit</h1>
        <p>
            <?= e(date('d F Y', (int) strtotime((string) $visit['visit_date']))) ?>
            <?php if ($visit['visit_time']): ?>
                at <?= e((string) $visit['visit_time']) ?>
            <?php endif; ?>
        </p>
    </div>
    <div>
        <a href="<?= e(url('/bc/visit/' . (int) $visit['id'] . '/edit')) ?>" class="btn btn-secondary">
            <i class="fas fa-edit"></i>
            Edit
        </a>
    </div>
</div>

<div class="row">
    <div class="col-lg-9 col-xl-8">
        <!-- Visit Information -->
        <div class="lrms-card">
            <div class="lrms-card-header">
                <h5>Visit Information</h5>
            </div>
            <div class="lrms-card-body">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Visit Date</label>
                        <p class="form-value"><?= e(date('d F Y', (int) strtotime((string) $visit['visit_date']))) ?></p>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Visit Time</label>
                        <p class="form-value"><?= $visit['visit_time'] ? e((string) $visit['visit_time']) : '—' ?></p>
                    </div>
                </div>
            </div>
        </div>

        <!-- BC Supervisor Details -->
        <div class="lrms-card">
            <div class="lrms-card-header">
                <h5>BC Supervisor Details</h5>
            </div>
            <div class="lrms-card-body">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">BC Supervisor Name</label>
                        <p class="form-value"><?= $visit['bca_name'] ? e((string) $visit['bca_name']) : '—' ?></p>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">BC Code</label>
                        <p class="form-value"><?= $visit['bc_code'] ? e((string) $visit['bc_code']) : '—' ?></p>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">SP/CBC Name</label>
                        <p class="form-value"><?= $visit['cbc_name'] ? e((string) $visit['cbc_name']) : '—' ?></p>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Branch Name</label>
                        <p class="form-value"><?= $visit['branch_name'] ? e((string) $visit['branch_name']) : '—' ?></p>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">IIBF Certificate Number</label>
                        <p class="form-value"><?= $visit['iibf_certificate_no'] ? e((string) $visit['iibf_certificate_no']) : '—' ?></p>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">SSA / Non-SSA</label>
                        <p class="form-value"><?= $visit['ssa_non_ssa'] ? e((string) $visit['ssa_non_ssa']) : '—' ?></p>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Link Branch Name</label>
                        <p class="form-value"><?= $visit['board_link_br_name'] ? e((string) $visit['board_link_br_name']) : '—' ?></p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Supervisor Observations -->
        <div class="lrms-card">
            <div class="lrms-card-header">
                <h5>Supervisor Observations</h5>
            </div>
            <div class="lrms-card-body">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Qualification</label>
                        <p class="form-value"><?= $visit['qualification'] ? e((string) $visit['qualification']) : '—' ?></p>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Age</label>
                        <p class="form-value"><?= $visit['age'] ? e((string) $visit['age']) : '—' ?></p>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Address / Contact</label>
                        <p class="form-value"><?= $visit['address_contact'] ? nl2br(e((string) $visit['address_contact'])) : '—' ?></p>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Board/Materials Available</label>
                        <p class="form-value">
                            <?php if ($visit['board_available']): ?>
                                <span class="badge bg-success">Yes</span>
                            <?php else: ?>
                                <span class="badge bg-secondary">No / Not recorded</span>
                            <?php endif; ?>
                        </p>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Equipment Status</label>
                        <p class="form-value"><?= $visit['equipment_status'] ? e((string) $visit['equipment_status']) : '—' ?></p>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Remuneration</label>
                        <p class="form-value"><?= $visit['remuneration'] ? nl2br(e((string) $visit['remuneration'])) : '—' ?></p>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Feedback</label>
                        <p class="form-value"><?= $visit['feedback'] ? nl2br(e((string) $visit['feedback'])) : '—' ?></p>
                    </div>
                    <div class="col-12">
                        <label class="form-label">General Observation</label>
                        <p class="form-value"><?= $visit['observation'] ? nl2br(e((string) $visit['observation'])) : '—' ?></p>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Visiting Official Name</label>
                        <p class="form-value"><?= $visit['visiting_official_name'] ? e((string) $visit['visiting_official_name']) : '—' ?></p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Metadata -->
        <div class="lrms-card">
            <div class="lrms-card-header">
                <h5>Record Information</h5>
            </div>
            <div class="lrms-card-body">
                <div class="row g-3" style="font-size:.9rem">
                    <div class="col-md-6">
                        <label class="form-label">Created</label>
                        <p class="form-value text-muted"><?= e(date('d M Y, H:i', (int) strtotime((string) $visit['created_at']))) ?></p>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Last Updated</label>
                        <p class="form-value text-muted"><?= e(date('d M Y, H:i', (int) strtotime((string) $visit['updated_at']))) ?></p>
                    </div>
                </div>
            </div>
        </div>

        <div class="d-flex gap-2 my-3">
            <a href="<?= e(url('/bc/visit/' . (int) $visit['id'] . '/edit')) ?>" class="btn btn-secondary">
                <i class="fas fa-edit"></i>
                Edit
            </a>
            <a href="<?= e(url('/bc/visit')) ?>" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left"></i>
                Back
            </a>
        </div>
    </div>
</div>
