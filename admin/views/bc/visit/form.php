<?php
/**
 * BC Supervisor Visit Form - with auto-population of BC Agent details
 *
 * @var array<string,mixed>|null   $visit   null when creating
 * @var list<array<string,mixed>>  $agents
 * @var array<string,mixed>        $old
 * @var array<string,list<string>> $errors
 */

$isEdit = $visit !== null;
$action = $isEdit ? url('/bc/visit/' . (int) $visit['id'] . '/edit') : url('/bc/visit/create');

$value = static function (string $key, mixed $fallback = '') use ($old, $visit): string {
    if (array_key_exists($key, $old)) {
        return e($old[$key]);
    }
    if ($visit !== null && array_key_exists($key, $visit)) {
        return e($visit[$key]);
    }
    return e($fallback);
};

$checked = static function (string $key, mixed $expected = 1) use ($old, $visit): bool {
    if (array_key_exists($key, $old)) {
        return $old[$key] == $expected;
    }
    if ($visit !== null && array_key_exists($key, $visit)) {
        return $visit[$key] == $expected;
    }
    return false;
};
?>

<div class="lrms-page-head">
    <div>
        <nav aria-label="Breadcrumb" class="mb-1" style="font-size:.75rem">
            <a href="<?= e(url('/bc/visit')) ?>" class="text-muted">BC Supervisor Visits</a>
            <span class="text-muted mx-1">/</span>
            <span class="text-muted"><?= $isEdit ? 'Edit' : 'New' ?></span>
        </nav>
        <h1><?= $isEdit ? 'Edit BC Supervisor Visit' : 'Record BC Supervisor Visit' ?></h1>
    </div>
</div>

<div class="row">
    <div class="col-lg-9 col-xl-8">
        <form method="post" action="<?= e($action) ?>" novalidate data-no-double-submit>
            <?= csrf_field() ?>

            <div class="lrms-card">
                <div class="lrms-card-header">
                    <h5>Visit Information</h5>
                </div>
                <div class="lrms-card-body">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label" for="visit_date">Visit Date <span class="req">*</span></label>
                            <input type="date" class="form-control<?= has_error($errors, 'visit_date') ?>"
                                   id="visit_date" name="visit_date"
                                   value="<?= $value('visit_date', date('Y-m-d')) ?>" required autofocus>
                            <?= field_error($errors, 'visit_date') ?>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="visit_time">Visit Time</label>
                            <input type="time" class="form-control<?= has_error($errors, 'visit_time') ?>"
                                   id="visit_time" name="visit_time"
                                   value="<?= $value('visit_time') ?>">
                            <?= field_error($errors, 'visit_time') ?>
                        </div>
                    </div>
                </div>
            </div>

            <!-- BC Agent Selection - Only on Create -->
            <?php if (!$isEdit): ?>
            <div class="lrms-card">
                <div class="lrms-card-header">
                    <h5>Select BC Agent to Visit</h5>
                </div>
                <div class="lrms-card-body">
                    <div class="row g-3">
                        <div class="col-12">
                            <label class="form-label" for="user_id">BC Agent <span class="req">*</span></label>
                            <?php $selectedAgent = $value('user_id'); ?>
                            <select class="form-select<?= has_error($errors, 'user_id') ?>"
                                    id="user_id" name="user_id" required>
                                <option value="">Select a BC Agent to visit&hellip;</option>
                                <?php foreach ($agents as $agent): ?>
                                    <option value="<?= (int) $agent['id'] ?>"
                                        <?= $selectedAgent === (string) $agent['id'] ? 'selected' : '' ?>>
                                        <?= e((string) $agent['name']) ?>
                                        (<?= e((string) $agent['employee_code']) ?>)
                                        <?php if ($agent['bc_code']): ?>
                                            - <?= e((string) $agent['bc_code']) ?>
                                        <?php endif; ?>
                                    </option>
                                <?php endforeach; ?>
                            </select>
                            <?= field_error($errors, 'user_id') ?>
                            <div class="form-text">When you select an agent, their details will auto-populate below.</div>
                        </div>
                    </div>
                </div>
            </div>
            <?php endif; ?>

            <!-- Auto-populated BC Agent Details (Read-only) -->
            <div class="lrms-card">
                <div class="lrms-card-header">
                    <h5>BC Agent Details (Auto-populated)</h5>
                </div>
                <div class="lrms-card-body">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label" for="bca_name">BC Agent Name</label>
                            <input type="text" class="form-control" id="bca_name" name="bca_name"
                                   value="<?= $value('bca_name') ?>" readonly>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="bc_code">BC Code</label>
                            <input type="text" class="form-control" id="bc_code" name="bc_code"
                                   value="<?= $value('bc_code') ?>" readonly>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="cbc_name">SP/CBC Name</label>
                            <input type="text" class="form-control" id="cbc_name" name="cbc_name"
                                   value="<?= $value('cbc_name') ?>" readonly>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="branch_name">Branch Name</label>
                            <input type="text" class="form-control" id="branch_name" name="branch_name"
                                   value="<?= $value('branch_name') ?>" readonly>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="iibf_certificate_no">IIBF Certificate Number</label>
                            <input type="text" class="form-control" id="iibf_certificate_no" name="iibf_certificate_no"
                                   value="<?= $value('iibf_certificate_no') ?>" readonly>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="ssa_non_ssa">SSA / Non-SSA</label>
                            <input type="text" class="form-control" id="ssa_non_ssa" name="ssa_non_ssa"
                                   value="<?= $value('ssa_non_ssa') ?>" readonly>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="board_link_br_name">Link Branch Name</label>
                            <input type="text" class="form-control" id="board_link_br_name" name="board_link_br_name"
                                   value="<?= $value('board_link_br_name') ?>" readonly>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Supervisor Observations (Manual Entry) -->
            <div class="lrms-card">
                <div class="lrms-card-header">
                    <h5>Supervisor Observations</h5>
                </div>
                <div class="lrms-card-body">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label" for="qualification">Qualification</label>
                            <input type="text" class="form-control<?= has_error($errors, 'qualification') ?>"
                                   id="qualification" name="qualification"
                                   value="<?= $value('qualification') ?>"
                                   placeholder="e.g., B.Com, MBA" maxlength="255">
                            <?= field_error($errors, 'qualification') ?>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="age">Age</label>
                            <input type="number" class="form-control<?= has_error($errors, 'age') ?>"
                                   id="age" name="age"
                                   value="<?= $value('age') ?>"
                                   min="18" max="100" inputmode="numeric">
                            <?= field_error($errors, 'age') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="address_contact">Address / Contact</label>
                            <textarea class="form-control<?= has_error($errors, 'address_contact') ?>"
                                      id="address_contact" name="address_contact"
                                      rows="2" maxlength="500" placeholder="Residential address and contact details"><?= $value('address_contact') ?></textarea>
                            <?= field_error($errors, 'address_contact') ?>
                        </div>

                        <div class="col-md-6">
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" id="board_available"
                                       name="board_available" value="1"
                                       <?= $checked('board_available') ? 'checked' : '' ?>>
                                <label class="form-check-label" for="board_available">
                                    Board/Materials Available
                                </label>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="equipment_status">Equipment Status</label>
                            <input type="text" class="form-control<?= has_error($errors, 'equipment_status') ?>"
                                   id="equipment_status" name="equipment_status"
                                   value="<?= $value('equipment_status') ?>"
                                   placeholder="e.g., Good, Needs repair" maxlength="255">
                            <?= field_error($errors, 'equipment_status') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="remuneration">Remuneration</label>
                            <textarea class="form-control<?= has_error($errors, 'remuneration') ?>"
                                      id="remuneration" name="remuneration"
                                      rows="2" maxlength="255" placeholder="Remuneration details and status"><?= $value('remuneration') ?></textarea>
                            <?= field_error($errors, 'remuneration') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="feedback">Feedback</label>
                            <textarea class="form-control<?= has_error($errors, 'feedback') ?>"
                                      id="feedback" name="feedback"
                                      rows="3" maxlength="1000" placeholder="Feedback on BC Agent performance"><?= $value('feedback') ?></textarea>
                            <?= field_error($errors, 'feedback') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="observation">General Observation</label>
                            <textarea class="form-control<?= has_error($errors, 'observation') ?>"
                                      id="observation" name="observation"
                                      rows="3" maxlength="1000" placeholder="General observations from the visit"><?= $value('observation') ?></textarea>
                            <?= field_error($errors, 'observation') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="visiting_official_name">Visiting Official Name</label>
                            <input type="text" class="form-control<?= has_error($errors, 'visiting_official_name') ?>"
                                   id="visiting_official_name" name="visiting_official_name"
                                   value="<?= $value('visiting_official_name') ?>"
                                   placeholder="Name of the visiting official" maxlength="150">
                            <?= field_error($errors, 'visiting_official_name') ?>
                        </div>
                    </div>
                </div>
            </div>

            <div class="d-flex gap-2 my-3">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i>
                    <?= $isEdit ? 'Update' : 'Save' ?>
                </button>
                <a href="<?= e(url('/bc/visit')) ?>" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i>
                    Cancel
                </a>
            </div>
        </form>
    </div>
</div>

<!-- AJAX Script for auto-populating agent details -->
<?php if (!$isEdit): ?>
<script>
document.getElementById('user_id').addEventListener('change', function() {
    const userId = this.value;

    if (!userId) {
        // Clear all auto-populated fields
        document.getElementById('bca_name').value = '';
        document.getElementById('bc_code').value = '';
        document.getElementById('cbc_name').value = '';
        document.getElementById('branch_name').value = '';
        document.getElementById('iibf_certificate_no').value = '';
        document.getElementById('ssa_non_ssa').value = '';
        document.getElementById('board_link_br_name').value = '';
        return;
    }

    // Fetch agent details via AJAX
    fetch('<?= e(url('/bc/visit/api/agent')) ?>/' + userId, {
        headers: {
            'Accept': 'application/json',
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to load agent details');
        }
        return response.json();
    })
    .then(data => {
        // Populate the read-only fields
        document.getElementById('bca_name').value = data.name || '';
        document.getElementById('bc_code').value = data.bc_code || '';
        document.getElementById('cbc_name').value = data.sp_cbc_name || '';
        document.getElementById('branch_name').value = data.branch_name || '';
        document.getElementById('iibf_certificate_no').value = data.iibf_number || '';
        document.getElementById('ssa_non_ssa').value = data.ssa || '';
        document.getElementById('board_link_br_name').value = data.link_branch || '';
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to load agent details. Please try again.');
    });
});
</script>
<?php endif; ?>
