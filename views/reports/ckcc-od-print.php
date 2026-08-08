<?php
/**
 * CKCC OD Field Report - Printable View
 * A4-optimized, dedicated layout for CKCC OD case verification.
 * No Borrower Signature section.
 * Supervisor name and BCBF Code in the header.
 *
 * @var array<string,mixed> $report
 */

$r = $report;

$yesNo = static function (mixed $value): string {
    return ((int) ($value ?? 0)) === 1 ? 'Yes' : 'No';
};

$checkIcon = static function (mixed $value): string {
    $checked = ((int) ($value ?? 0)) === 1;
    return '<span class="check-icon ' . ($checked ? 'checked' : '') . '">'
        . ($checked ? '&#10003;' : '')
        . '</span>';
};

$fmtDate = static function (mixed $value): string {
    if (empty($value)) return '-';
    $ts = strtotime((string) $value);
    return $ts ? date('d M Y', $ts) : (string) $value;
};

$fmtMoney = static function (mixed $value): string {
    if ($value === null || $value === '') return '-';
    return number_format((float) $value, 2);
};
?>

<!-- Report Header -->
<div class="print-header">
    <div class="row align-items-center">
        <div class="col">
            <h1>CKCC OD Field Report</h1>
            <p class="subtitle">Field Verification Report - CKCC Overdraft Case</p>
        </div>
        <div class="col-auto text-end">
            <div class="supervisor-badge">
                <strong>Supervisor:</strong> <?= e($r['supervisor_name'] ?: 'N/A') ?>
                | <strong>BCBF:</strong> <?= e($r['supervisor_bcbf_code'] ?: 'N/A') ?>
            </div>
        </div>
    </div>
</div>

<!-- Visit Information -->
<div class="print-section">
    <div class="print-section-title">Visit Information</div>
    <div class="field-grid">
        <div class="field-item">
            <span class="field-label">Visit Date</span>
            <span class="field-value"><?= e($fmtDate($r['visit_date'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">BC Supervisor</span>
            <span class="field-value"><?= e($r['agent_display_name'] ?? '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">BCBF Code</span>
            <span class="field-value"><?= e($r['agent_bcbf_code'] ?: '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Branch</span>
            <span class="field-value"><?= e($r['branch_display_name'] ?: '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Report Type</span>
            <span class="field-value">CKCC OD Renewal</span>
        </div>
        <div class="field-item">
            <span class="field-label">Report ID</span>
            <span class="field-value">#<?= e((string) ($r['id'] ?? '')) ?></span>
        </div>
    </div>
</div>

<!-- Customer / Account Details -->
<div class="print-section">
    <div class="print-section-title">Customer / Account Details</div>
    <div class="field-grid">
        <div class="field-item">
            <span class="field-label">Customer Name</span>
            <span class="field-value"><?= e($r['customer_name'] ?? '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Loan Account Number</span>
            <span class="field-value"><?= e($r['loan_account_number'] ?? '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">CIF Number</span>
            <span class="field-value"><?= e($r['cif_number'] ?? '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Sanction Date</span>
            <span class="field-value"><?= e($fmtDate($r['sanction_date'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Sanction Limit</span>
            <span class="field-value"><?= e($fmtMoney($r['sanction_limit'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Drawing Power</span>
            <span class="field-value"><?= e($fmtMoney($r['drawing_power'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Outstanding Amount</span>
            <span class="field-value"><?= e($fmtMoney($r['cd_outstanding'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Interest Overdue</span>
            <span class="field-value"><?= e($fmtMoney($r['interest_overdue'] ?? null)) ?></span>
        </div>
    </div>
</div>

<!-- Renewal Status -->
<div class="print-section">
    <div class="print-section-title">Renewal Status</div>
    <div class="field-grid">
        <div class="field-item">
            <span class="field-label">Renewal Due Date</span>
            <span class="field-value"><?= e($fmtDate($r['renewal_due_date'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Expected NPA Date</span>
            <span class="field-value"><?= e($fmtDate($r['expected_npa_date'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Days Remaining</span>
            <span class="field-value"><?= e((string) ($r['days_remaining'] ?? '-')) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Eligible for Renewal</span>
            <span class="field-value"><?= e($yesNo($r['eligible_for_renewal'] ?? 0)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">KYC Status</span>
            <span class="field-value"><?= e(ucfirst((string) ($r['kyc_status'] ?? '-'))) ?></span>
        </div>
    </div>
</div>

<!-- KYC & Verification -->
<div class="print-section">
    <div class="print-section-title">KYC &amp; Verification</div>
    <div class="check-grid">
        <div class="check-item">
            <?= $checkIcon($r['aadhaar_seeded'] ?? 0) ?>
            <span>Aadhaar Seeded</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['mobile_linked'] ?? 0) ?>
            <span>Mobile Linked</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['aadhaar_auth_completed'] ?? 0) ?>
            <span>Aadhaar Auth Completed</span>
        </div>
    </div>
</div>

<!-- Renewal Consent -->
<div class="print-section">
    <div class="print-section-title">Renewal Consent</div>
    <div class="check-grid">
        <div class="check-item">
            <?= $checkIcon($r['willing_to_renew'] ?? 0) ?>
            <span>Willing to Renew</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['documents_handed_over'] ?? 0) ?>
            <span>Documents Handed Over</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['renewal_form_signed'] ?? 0) ?>
            <span>Renewal Form Signed</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['ekyc_completed'] ?? 0) ?>
            <span>eKYC Completed</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['biometrics_completed'] ?? 0) ?>
            <span>Biometrics Completed</span>
        </div>
    </div>
</div>

<!-- Agent Observation -->
<?php if (!empty($r['agent_observation'])): ?>
<div class="print-section">
    <div class="print-section-title">Agent Observation</div>
    <p style="font-size:10pt;margin:0"><?= e($r['agent_observation']) ?></p>
</div>
<?php endif; ?>

<!-- Recommendation -->
<div class="print-section">
    <div class="print-section-title">Recommendation</div>
    <div class="check-grid">
        <div class="check-item">
            <?= $checkIcon($r['rec_renew_immediately'] ?? 0) ?>
            <span>Renew Immediately</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['rec_documents_submitted'] ?? 0) ?>
            <span>Documents Submitted</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['rec_pending_documents'] ?? 0) ?>
            <span>Pending Documents</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['rec_followup_required'] ?? 0) ?>
            <span>Follow-up Required</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['rec_not_interested'] ?? 0) ?>
            <span>Not Interested</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['rec_branch_contact_urgent'] ?? 0) ?>
            <span>Branch Contact Urgent</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['rec_others'] ?? 0) ?>
            <span>Others<?= !empty($r['rec_other_text']) ? ': ' . e($r['rec_other_text']) : '' ?></span>
        </div>
    </div>
</div>

<!-- Report Status -->
<div class="print-section">
    <div class="print-section-title">Report Status</div>
    <div class="check-grid">
        <div class="check-item">
            <?= $checkIcon($r['st_customer_contacted'] ?? 0) ?>
            <span>Customer Contacted</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_customer_verified'] ?? 0) ?>
            <span>Customer Verified</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_documents_collected'] ?? 0) ?>
            <span>Documents Collected</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_application_submitted'] ?? 0) ?>
            <span>Application Submitted</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_ckcc_renewed'] ?? 0) ?>
            <span>CKCC Renewed</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_pending_at_branch'] ?? 0) ?>
            <span>Pending at Branch</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_followup_required'] ?? 0) ?>
            <span>Follow-up Required</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_became_npa'] ?? 0) ?>
            <span>Became NPA</span>
        </div>
    </div>
</div>

<!-- Footer -->
<div class="print-section" style="margin-top:20px;border-top:1px solid #e5e7eb;padding-top:10px">
    <div class="row">
        <div class="col-6">
            <p style="font-size:8pt;color:#6b7280;margin:0">
                Report generated on <?= e(date('d M Y, h:i A')) ?><br>
                D2 Recovery Solutions &amp; Services - Confidential
            </p>
        </div>
        <div class="col-6 text-end">
            <p style="font-size:8pt;color:#6b7280;margin:0">
                Supervisor: <?= e($r['supervisor_name'] ?: '-') ?><br>
                BCBF Code: <?= e($r['supervisor_bcbf_code'] ?: '-') ?>
            </p>
        </div>
    </div>
</div>
