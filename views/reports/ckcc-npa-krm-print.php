<?php
/**
 * CKCC NPA / KRM OTS Scheme Field Report - Printable View
 * A4-optimized, dedicated layout for NPA accounts under KRM OTS Scheme.
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

$schemeLabel = static function (?string $scheme): string {
    return match ($scheme) {
        'krm_ots'     => 'KRM OTS',
        'general_ots' => 'General OTS',
        'other'       => 'Other',
        default       => '-',
    };
};

$responseLabel = static function (?string $response): string {
    return match ($response) {
        'agreed'               => 'Agreed',
        'requested_time'       => 'Requested Time',
        'financial_difficulty' => 'Financial Difficulty',
        'refused'              => 'Refused',
        'not_eligible'         => 'Not Eligible',
        default                => '-',
    };
};

$approvalLabel = static function (?string $status): string {
    return match ($status) {
        'pending'  => 'Pending',
        'approved' => 'Approved',
        'rejected' => 'Rejected',
        default    => '-',
    };
};
?>

<!-- Report Header -->
<div class="print-header">
    <div class="row align-items-center">
        <div class="col">
            <h1>CKCC NPA / KRM OTS Field Report</h1>
            <p class="subtitle">NPA Accounts under KRM OTS Scheme - Field Verification Report</p>
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
            <span class="field-value">NPA / KRM OTS Settlement</span>
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
            <span class="field-label">Borrower Name (Settlement)</span>
            <span class="field-value"><?= e($r['borrower_name'] ?? $r['customer_name'] ?? '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Loan Account Number</span>
            <span class="field-value"><?= e($r['loan_account_number'] ?? '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">NPA Date</span>
            <span class="field-value"><?= e($fmtDate($r['npa_date'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Eligible for OTS</span>
            <span class="field-value"><?= e($yesNo($r['eligible_for_ots'] ?? 0)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Scheme</span>
            <span class="field-value"><?= e($schemeLabel($r['scheme'] ?? null)) ?><?= ($r['scheme'] === 'other' && !empty($r['scheme_other_text'])) ? ' - ' . e($r['scheme_other_text']) : '' ?></span>
        </div>
    </div>
</div>

<!-- Settlement Calculation -->
<div class="print-section">
    <div class="print-section-title">Settlement Calculation</div>
    <div class="field-grid">
        <div class="field-item">
            <span class="field-label">Outstanding Amount</span>
            <span class="field-value"><?= e($fmtMoney($r['od_outstanding'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Relief/Waiver %</span>
            <span class="field-value"><?= e($r['relief_waiver_percent'] !== null ? number_format((float) $r['relief_waiver_percent'], 2) . '%' : '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">RLB Amount</span>
            <span class="field-value"><?= e($fmtMoney($r['rlb_amount'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Payable %</span>
            <span class="field-value"><?= e($r['payable_percent'] !== null ? number_format((float) $r['payable_percent'], 2) . '%' : '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Borrower Payable Amount</span>
            <span class="field-value"><?= e($fmtMoney($r['borrower_payable_amount'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Total Settlement Amount</span>
            <span class="field-value"><?= e($fmtMoney($r['total_settlement_amount'] ?? null)) ?></span>
        </div>
    </div>
</div>

<!-- Initial Deposit -->
<div class="print-section">
    <div class="print-section-title">Initial Deposit</div>
    <div class="field-grid">
        <div class="field-item">
            <span class="field-label">Initial Deposit %</span>
            <span class="field-value"><?= e($r['initial_deposit_percent'] !== null ? number_format((float) $r['initial_deposit_percent'], 2) . '%' : '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Required Deposit Amount</span>
            <span class="field-value"><?= e($fmtMoney($r['required_deposit_amount'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Deposit Received</span>
            <span class="field-value"><?= e($yesNo($r['deposit_received'] ?? 0)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Deposit Amount</span>
            <span class="field-value"><?= e($fmtMoney($r['deposit_amount'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Deposit Date</span>
            <span class="field-value"><?= e($fmtDate($r['deposit_date'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Deposit Reference</span>
            <span class="field-value"><?= e($r['deposit_reference'] ?? '-') ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Balance Payable</span>
            <span class="field-value"><?= e($fmtMoney($r['balance_payable'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Proposed Final Payment Date</span>
            <span class="field-value"><?= e($fmtDate($r['proposed_final_payment_date'] ?? null)) ?></span>
        </div>
    </div>
</div>

<!-- Approval & Validity -->
<div class="print-section">
    <div class="print-section-title">Approval &amp; Validity</div>
    <div class="field-grid">
        <div class="field-item">
            <span class="field-label">Approval Status</span>
            <span class="field-value"><?= e($approvalLabel($r['approval_status'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Validity From</span>
            <span class="field-value"><?= e($fmtDate($r['validity_from'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Validity To</span>
            <span class="field-value"><?= e($fmtDate($r['validity_to'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Expected Closure Date</span>
            <span class="field-value"><?= e($fmtDate($r['expected_closure_date'] ?? null)) ?></span>
        </div>
    </div>
</div>

<!-- Customer Response -->
<div class="print-section">
    <div class="print-section-title">Customer Response</div>
    <div class="field-grid">
        <div class="field-item">
            <span class="field-label">Borrower Accepted</span>
            <span class="field-value"><?= e($yesNo($r['borrower_accepted'] ?? 0)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Customer Response</span>
            <span class="field-value"><?= e($responseLabel($r['customer_response'] ?? null)) ?></span>
        </div>
        <div class="field-item">
            <span class="field-label">Expected Deposit Date</span>
            <span class="field-value"><?= e($fmtDate($r['expected_deposit_date'] ?? null)) ?></span>
        </div>
        <?php if (!empty($r['rejection_reason'])): ?>
        <div class="field-item">
            <span class="field-label">Rejection Reason</span>
            <span class="field-value"><?= e($r['rejection_reason']) ?></span>
        </div>
        <?php endif; ?>
    </div>
</div>

<!-- Recommendation -->
<div class="print-section">
    <div class="print-section-title">Recommendation</div>
    <div class="check-grid">
        <div class="check-item">
            <?= $checkIcon($r['rec_proposal_recommended'] ?? 0) ?>
            <span>Proposal Recommended</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['rec_followup_required'] ?? 0) ?>
            <span>Follow-up Required</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['rec_customer_refused'] ?? 0) ?>
            <span>Customer Refused</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['rec_not_eligible'] ?? 0) ?>
            <span>Not Eligible</span>
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
            <?= $checkIcon($r['st_ots_accepted'] ?? 0) ?>
            <span>OTS Accepted</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_ots_rejected'] ?? 0) ?>
            <span>OTS Rejected</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_initial_deposit_received'] ?? 0) ?>
            <span>Initial Deposit Received</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_ots_closed'] ?? 0) ?>
            <span>OTS Closed</span>
        </div>
        <div class="check-item">
            <?= $checkIcon($r['st_followup_required'] ?? 0) ?>
            <span>Follow-up Required</span>
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
