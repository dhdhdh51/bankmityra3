<?php
/**
 * BC Supervisor Visit Form - 27 field checklist.
 *
 * @var array<string,mixed>|null   $visit  null when creating
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

$sel = static function (string $key, string $option) use ($old, $visit): string {
    $current = '';
    if (array_key_exists($key, $old)) {
        $current = (string) $old[$key];
    } elseif ($visit !== null && array_key_exists($key, $visit)) {
        $current = (string) $visit[$key];
    }
    return $current === $option ? 'selected' : '';
};
?>

<div class="lrms-page-head">
    <div>
        <nav aria-label="Breadcrumb" class="mb-1" style="font-size:.75rem">
            <a href="<?= e(url('/bc/visit')) ?>" class="text-muted">BC Visits</a>
            <span class="text-muted mx-1">/</span>
            <span class="text-muted"><?= $isEdit ? 'Edit' : 'New' ?></span>
        </nav>
        <h1>BC Supervisor - <?= $isEdit ? 'Edit Visit' : 'Record Visit' ?></h1>
        <p>BC Supervisor visit to BC Agent outlet - complete the checklist below</p>
    </div>
</div>

<div class="row">
    <div class="col-lg-10 col-xl-8">
        <form method="post" action="<?= e($action) ?>" novalidate data-no-double-submit>
            <?= csrf_field() ?>

            <!-- Section 1: BCA Basic Information -->
            <div class="lrms-card mb-3">
                <div class="lrms-card-body">
                    <h6 class="fw-bold mb-3">BCA Basic Information</h6>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label" for="bca_name">1. Name Of Business Correspondent Agents (BCA) <span class="req">*</span></label>
                            <input type="text" class="form-control<?= has_error($errors, 'bca_name') ?>"
                                   id="bca_name" name="bca_name" value="<?= $value('bca_name') ?>"
                                   maxlength="200" required autofocus>
                            <?= field_error($errors, 'bca_name') ?>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="branch_name">2. Branch Name <span class="req">*</span></label>
                            <input type="text" class="form-control<?= has_error($errors, 'branch_name') ?>"
                                   id="branch_name" name="branch_name" value="<?= $value('branch_name') ?>"
                                   maxlength="200" required>
                            <?= field_error($errors, 'branch_name') ?>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="cbc_name">3. Name Of CBC (Corporate Business Correspondent)</label>
                            <input type="text" class="form-control<?= has_error($errors, 'cbc_name') ?>"
                                   id="cbc_name" name="cbc_name" value="<?= $value('cbc_name') ?>" maxlength="200">
                            <?= field_error($errors, 'cbc_name') ?>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="qualification">4. Qualification of BCA</label>
                            <input type="text" class="form-control<?= has_error($errors, 'qualification') ?>"
                                   id="qualification" name="qualification" value="<?= $value('qualification') ?>" maxlength="200">
                            <?= field_error($errors, 'qualification') ?>
                        </div>

                        <div class="col-md-3">
                            <label class="form-label" for="age">5. Age</label>
                            <input type="number" class="form-control<?= has_error($errors, 'age') ?>"
                                   id="age" name="age" value="<?= $value('age') ?>" min="18" max="100">
                            <?= field_error($errors, 'age') ?>
                        </div>

                        <div class="col-md-9">
                            <label class="form-label" for="address_contact">6. Address with Contact No.</label>
                            <input type="text" class="form-control<?= has_error($errors, 'address_contact') ?>"
                                   id="address_contact" name="address_contact" value="<?= $value('address_contact') ?>" maxlength="500">
                            <?= field_error($errors, 'address_contact') ?>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Section 2: Certification & Appointment -->
            <div class="lrms-card mb-3">
                <div class="lrms-card-body">
                    <h6 class="fw-bold mb-3">Certification &amp; Appointment</h6>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label" for="bc_certification_iibf">7. BC Certification (IIBF)</label>
                            <select class="form-select<?= has_error($errors, 'bc_certification_iibf') ?>"
                                    id="bc_certification_iibf" name="bc_certification_iibf">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('bc_certification_iibf', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('bc_certification_iibf', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'bc_certification_iibf') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="iibf_certificate_no">Certificate No. (if Yes)</label>
                            <input type="text" class="form-control<?= has_error($errors, 'iibf_certificate_no') ?>"
                                   id="iibf_certificate_no" name="iibf_certificate_no" value="<?= $value('iibf_certificate_no') ?>" maxlength="100">
                            <?= field_error($errors, 'iibf_certificate_no') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="bc_working_since">8. BC Working Since at BC Outlet</label>
                            <input type="date" class="form-control<?= has_error($errors, 'bc_working_since') ?>"
                                   id="bc_working_since" name="bc_working_since" value="<?= $value('bc_working_since') ?>">
                            <?= field_error($errors, 'bc_working_since') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="appointment_letter">9. Appointment Letter from Bank/CBC</label>
                            <select class="form-select<?= has_error($errors, 'appointment_letter') ?>"
                                    id="appointment_letter" name="appointment_letter">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('appointment_letter', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('appointment_letter', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'appointment_letter') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="identity_card">10. Identity Card Available</label>
                            <select class="form-select<?= has_error($errors, 'identity_card') ?>"
                                    id="identity_card" name="identity_card">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('identity_card', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('identity_card', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'identity_card') ?>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Section 3: Coordinator & Coverage -->
            <div class="lrms-card mb-3">
                <div class="lrms-card-body">
                    <h6 class="fw-bold mb-3">Coordinator &amp; Coverage</h6>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label" for="district_coordinator_name">11. Name Of District Coordinator / BC Supervisor and Contact Number</label>
                            <input type="text" class="form-control<?= has_error($errors, 'district_coordinator_name') ?>"
                                   id="district_coordinator_name" name="district_coordinator_name" value="<?= $value('district_coordinator_name') ?>" maxlength="300">
                            <?= field_error($errors, 'district_coordinator_name') ?>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="ssa_non_ssa">12. Name Of SSA/Non SSA, Number of Villages Covered</label>
                            <input type="text" class="form-control<?= has_error($errors, 'ssa_non_ssa') ?>"
                                   id="ssa_non_ssa" name="ssa_non_ssa" value="<?= $value('ssa_non_ssa') ?>" maxlength="300">
                            <?= field_error($errors, 'ssa_non_ssa') ?>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Section 4: Board & Display -->
            <div class="lrms-card mb-3">
                <div class="lrms-card-body">
                    <h6 class="fw-bold mb-3">13. Board of CBC/Bank Available</h6>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label" for="board_available">Board Available (Y/N)</label>
                            <select class="form-select<?= has_error($errors, 'board_available') ?>"
                                    id="board_available" name="board_available">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('board_available', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('board_available', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'board_available') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="dos_donts_board">Do's and Don'ts Board</label>
                            <select class="form-select<?= has_error($errors, 'dos_donts_board') ?>"
                                    id="dos_donts_board" name="dos_donts_board">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('dos_donts_board', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('dos_donts_board', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'dos_donts_board') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="services_list_display">List of Services Offered</label>
                            <select class="form-select<?= has_error($errors, 'services_list_display') ?>"
                                    id="services_list_display" name="services_list_display">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('services_list_display', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('services_list_display', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'services_list_display') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="sign_board">Sign Board at BC Points</label>
                            <select class="form-select<?= has_error($errors, 'sign_board') ?>"
                                    id="sign_board" name="sign_board">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('sign_board', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('sign_board', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'sign_board') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="board_bank_name">Bank Name</label>
                            <input type="text" class="form-control<?= has_error($errors, 'board_bank_name') ?>"
                                   id="board_bank_name" name="board_bank_name" value="<?= $value('board_bank_name') ?>" maxlength="200">
                            <?= field_error($errors, 'board_bank_name') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="board_link_br_name">Link Branch Name</label>
                            <input type="text" class="form-control<?= has_error($errors, 'board_link_br_name') ?>"
                                   id="board_link_br_name" name="board_link_br_name" value="<?= $value('board_link_br_name') ?>" maxlength="200">
                            <?= field_error($errors, 'board_link_br_name') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="board_contact_br">Contact Number of Branch</label>
                            <input type="text" class="form-control<?= has_error($errors, 'board_contact_br') ?>"
                                   id="board_contact_br" name="board_contact_br" value="<?= $value('board_contact_br') ?>" maxlength="100">
                            <?= field_error($errors, 'board_contact_br') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="board_working_time">Business/Working Time of BC Outlet</label>
                            <input type="text" class="form-control<?= has_error($errors, 'board_working_time') ?>"
                                   id="board_working_time" name="board_working_time" value="<?= $value('board_working_time') ?>" maxlength="200">
                            <?= field_error($errors, 'board_working_time') ?>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Section 5: Transactions & Services -->
            <div class="lrms-card mb-3">
                <div class="lrms-card-body">
                    <h6 class="fw-bold mb-3">Transactions &amp; Services</h6>
                    <div class="row g-3">
                        <div class="col-12">
                            <label class="form-label" for="transactions_previous_day">14. Number of Transactions on Previous Day (Cash Deposit/Payment, Fund Transfer, etc.) &amp; How to Improve if Below 50</label>
                            <textarea class="form-control<?= has_error($errors, 'transactions_previous_day') ?>"
                                      id="transactions_previous_day" name="transactions_previous_day" rows="3" maxlength="2000"><?= $value('transactions_previous_day') ?></textarea>
                            <?= field_error($errors, 'transactions_previous_day') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="services_provided">15. Number of Services Provided at BC Points out of the 39 Services</label>
                            <textarea class="form-control<?= has_error($errors, 'services_provided') ?>"
                                      id="services_provided" name="services_provided" rows="3" maxlength="2000"><?= $value('services_provided') ?></textarea>
                            <?= field_error($errors, 'services_provided') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="bc_awareness_sss">16. BC Awareness of SSS (Social Security Schemes) and Bank Products</label>
                            <textarea class="form-control<?= has_error($errors, 'bc_awareness_sss') ?>"
                                      id="bc_awareness_sss" name="bc_awareness_sss" rows="3" maxlength="2000"><?= $value('bc_awareness_sss') ?></textarea>
                            <?= field_error($errors, 'bc_awareness_sss') ?>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Section 6: Mandatory Registers -->
            <div class="lrms-card mb-3">
                <div class="lrms-card-body">
                    <h6 class="fw-bold mb-3">17. Availability of Mandatory Registers</h6>
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label class="form-label" for="complaint_register">Complaint Box/Register</label>
                            <select class="form-select<?= has_error($errors, 'complaint_register') ?>"
                                    id="complaint_register" name="complaint_register">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('complaint_register', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('complaint_register', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'complaint_register') ?>
                        </div>

                        <div class="col-md-3">
                            <label class="form-label" for="transactions_register">Transactions Register</label>
                            <select class="form-select<?= has_error($errors, 'transactions_register') ?>"
                                    id="transactions_register" name="transactions_register">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('transactions_register', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('transactions_register', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'transactions_register') ?>
                        </div>

                        <div class="col-md-3">
                            <label class="form-label" for="visit_register">Visit Register</label>
                            <select class="form-select<?= has_error($errors, 'visit_register') ?>"
                                    id="visit_register" name="visit_register">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('visit_register', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('visit_register', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'visit_register') ?>
                        </div>

                        <div class="col-md-3">
                            <label class="form-label" for="register_other">Other</label>
                            <input type="text" class="form-control<?= has_error($errors, 'register_other') ?>"
                                   id="register_other" name="register_other" value="<?= $value('register_other') ?>" maxlength="300">
                            <?= field_error($errors, 'register_other') ?>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Section 7: Equipment -->
            <div class="lrms-card mb-3">
                <div class="lrms-card-body">
                    <h6 class="fw-bold mb-3">18. Availability of Equipment</h6>
                    <div class="row g-3">
                        <div class="col-md-4 col-lg-2">
                            <label class="form-label" for="equip_laptop">Laptop/Desktop</label>
                            <select class="form-select<?= has_error($errors, 'equip_laptop') ?>"
                                    id="equip_laptop" name="equip_laptop">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('equip_laptop', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('equip_laptop', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'equip_laptop') ?>
                        </div>

                        <div class="col-md-4 col-lg-2">
                            <label class="form-label" for="equip_biometric">Biometric Device</label>
                            <select class="form-select<?= has_error($errors, 'equip_biometric') ?>"
                                    id="equip_biometric" name="equip_biometric">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('equip_biometric', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('equip_biometric', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'equip_biometric') ?>
                        </div>

                        <div class="col-md-4 col-lg-2">
                            <label class="form-label" for="equip_pinpad">Pin Pad Device</label>
                            <select class="form-select<?= has_error($errors, 'equip_pinpad') ?>"
                                    id="equip_pinpad" name="equip_pinpad">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('equip_pinpad', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('equip_pinpad', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'equip_pinpad') ?>
                        </div>

                        <div class="col-md-4 col-lg-3">
                            <label class="form-label" for="equip_receipt_machine">Receipt Machine</label>
                            <select class="form-select<?= has_error($errors, 'equip_receipt_machine') ?>"
                                    id="equip_receipt_machine" name="equip_receipt_machine">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('equip_receipt_machine', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('equip_receipt_machine', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'equip_receipt_machine') ?>
                        </div>

                        <div class="col-md-4 col-lg-2">
                            <label class="form-label" for="equip_printer">Printer</label>
                            <select class="form-select<?= has_error($errors, 'equip_printer') ?>"
                                    id="equip_printer" name="equip_printer">
                                <option value="">Select...</option>
                                <option value="yes" <?= $sel('equip_printer', 'yes') ?>>Yes</option>
                                <option value="no" <?= $sel('equip_printer', 'no') ?>>No</option>
                            </select>
                            <?= field_error($errors, 'equip_printer') ?>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Section 8: Remuneration -->
            <div class="lrms-card mb-3">
                <div class="lrms-card-body">
                    <h6 class="fw-bold mb-3">19. BC Remuneration Earned from Last Three Months</h6>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label" for="remuneration_month1">Month 1 Name</label>
                            <input type="text" class="form-control<?= has_error($errors, 'remuneration_month1') ?>"
                                   id="remuneration_month1" name="remuneration_month1" value="<?= $value('remuneration_month1') ?>" maxlength="100" placeholder="e.g. January 2025">
                            <?= field_error($errors, 'remuneration_month1') ?>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label" for="remuneration_amount1">Amount (Rs.)</label>
                            <input type="number" class="form-control<?= has_error($errors, 'remuneration_amount1') ?>"
                                   id="remuneration_amount1" name="remuneration_amount1" value="<?= $value('remuneration_amount1') ?>" min="0" step="0.01">
                            <?= field_error($errors, 'remuneration_amount1') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="remuneration_month2">Month 2 Name</label>
                            <input type="text" class="form-control<?= has_error($errors, 'remuneration_month2') ?>"
                                   id="remuneration_month2" name="remuneration_month2" value="<?= $value('remuneration_month2') ?>" maxlength="100" placeholder="e.g. February 2025">
                            <?= field_error($errors, 'remuneration_month2') ?>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label" for="remuneration_amount2">Amount (Rs.)</label>
                            <input type="number" class="form-control<?= has_error($errors, 'remuneration_amount2') ?>"
                                   id="remuneration_amount2" name="remuneration_amount2" value="<?= $value('remuneration_amount2') ?>" min="0" step="0.01">
                            <?= field_error($errors, 'remuneration_amount2') ?>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label" for="remuneration_month3">Month 3 Name</label>
                            <input type="text" class="form-control<?= has_error($errors, 'remuneration_month3') ?>"
                                   id="remuneration_month3" name="remuneration_month3" value="<?= $value('remuneration_month3') ?>" maxlength="100" placeholder="e.g. March 2025">
                            <?= field_error($errors, 'remuneration_month3') ?>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label" for="remuneration_amount3">Amount (Rs.)</label>
                            <input type="number" class="form-control<?= has_error($errors, 'remuneration_amount3') ?>"
                                   id="remuneration_amount3" name="remuneration_amount3" value="<?= $value('remuneration_amount3') ?>" min="0" step="0.01">
                            <?= field_error($errors, 'remuneration_amount3') ?>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Section 9: Feedback & Location -->
            <div class="lrms-card mb-3">
                <div class="lrms-card-body">
                    <h6 class="fw-bold mb-3">Feedback &amp; Location</h6>
                    <div class="row g-3">
                        <div class="col-12">
                            <label class="form-label" for="feedback_villagers">20. Feedback from the Villagers about the Services of BC (At Least 1 A/c Holder Detail)</label>
                            <textarea class="form-control<?= has_error($errors, 'feedback_villagers') ?>"
                                      id="feedback_villagers" name="feedback_villagers" rows="3" maxlength="2000"><?= $value('feedback_villagers') ?></textarea>
                            <?= field_error($errors, 'feedback_villagers') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="bc_working_location">21. BC is Working in Allotted Location. If No Then Where?</label>
                            <textarea class="form-control<?= has_error($errors, 'bc_working_location') ?>"
                                      id="bc_working_location" name="bc_working_location" rows="2" maxlength="2000"><?= $value('bc_working_location') ?></textarea>
                            <?= field_error($errors, 'bc_working_location') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="other_info">22. Other Information (if any)</label>
                            <textarea class="form-control<?= has_error($errors, 'other_info') ?>"
                                      id="other_info" name="other_info" rows="2" maxlength="2000"><?= $value('other_info') ?></textarea>
                            <?= field_error($errors, 'other_info') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="photos_selfie">23. Photos/Selfie of BC Point</label>
                            <div class="form-text">Upload photos separately via the media section if needed.</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Section 10: Observation & Visiting Official -->
            <div class="lrms-card mb-3">
                <div class="lrms-card-body">
                    <h6 class="fw-bold mb-3">Observation &amp; Visiting Official</h6>
                    <div class="row g-3">
                        <div class="col-md-4">
                            <label class="form-label" for="observation">24. Observation</label>
                            <select class="form-select<?= has_error($errors, 'observation') ?>"
                                    id="observation" name="observation">
                                <option value="">Select...</option>
                                <option value="excellent" <?= $sel('observation', 'excellent') ?>>Excellent</option>
                                <option value="good" <?= $sel('observation', 'good') ?>>Good</option>
                                <option value="satisfactory" <?= $sel('observation', 'satisfactory') ?>>Satisfactory</option>
                                <option value="poor" <?= $sel('observation', 'poor') ?>>Poor</option>
                            </select>
                            <?= field_error($errors, 'observation') ?>
                        </div>

                        <div class="col-md-8">
                            <label class="form-label" for="visiting_official_name">25. Name &amp; Contact No. of Visiting Official - BC Supervisor</label>
                            <input type="text" class="form-control<?= has_error($errors, 'visiting_official_name') ?>"
                                   id="visiting_official_name" name="visiting_official_name" value="<?= $value('visiting_official_name') ?>" maxlength="300">
                            <?= field_error($errors, 'visiting_official_name') ?>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label" for="visiting_official_signature">26. Signature of Visiting Officials - BC Supervisor</label>
                            <input type="text" class="form-control<?= has_error($errors, 'visiting_official_signature') ?>"
                                   id="visiting_official_signature" name="visiting_official_signature" value="<?= $value('visiting_official_signature') ?>" maxlength="300" placeholder="Type name as signature">
                            <?= field_error($errors, 'visiting_official_signature') ?>
                        </div>

                        <div class="col-md-3">
                            <label class="form-label" for="visit_date">Date</label>
                            <input type="date" class="form-control<?= has_error($errors, 'visit_date') ?>"
                                   id="visit_date" name="visit_date" value="<?= $value('visit_date', date('Y-m-d')) ?>">
                            <?= field_error($errors, 'visit_date') ?>
                        </div>

                        <div class="col-12">
                            <label class="form-label" for="other_info_2">27. Other Information (if any)</label>
                            <textarea class="form-control<?= has_error($errors, 'other_info_2') ?>"
                                      id="other_info_2" name="other_info_2" rows="2" maxlength="2000"><?= $value('other_info_2') ?></textarea>
                            <?= field_error($errors, 'other_info_2') ?>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Submit -->
            <div class="lrms-card">
                <div class="lrms-card-foot d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <?= icon('check') ?> <?= $isEdit ? 'Save Changes' : 'Submit BC Visit' ?>
                    </button>
                    <a href="<?= e(url('/bc/visit')) ?>" class="btn btn-outline-secondary">Cancel</a>
                </div>
            </div>

        </form>
    </div>
</div>
