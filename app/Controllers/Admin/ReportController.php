<?php

declare(strict_types=1);

namespace App\Controllers\Admin;

use App\Core\Auth;
use App\Core\Request;
use App\Core\Response;
use App\Models\Branch;
use App\Models\LoanAccount;
use App\Models\User;
use App\Services\ReportService;

final class ReportController extends Controller
{
    /** The report picker grid. */
    public function index(Request $request): void
    {
        $this->guard($request, 'reports.view');

        $this->view($request, 'reports/index', [
            'title' => 'Reports',
            'types' => ReportService::TYPES,
        ]);
    }

    public function show(Request $request): void
    {
        $this->guard($request, 'reports.view');

        $type = (string) $request->param('type');
        if (!ReportService::isValidType($type)) {
            $this->back('/reports', 'danger', 'Unknown report type.');
        }

        $filters = $this->filters($request);
        $report = ReportService::build($type, $filters);

        $this->logView('Reports', sprintf('Viewed %s report', $type));

        $scoped = Auth::scopedBranchId();

        $this->view($request, 'reports/show', [
            'title'     => (string) ReportService::TYPES[$type]['label'],
            'type'      => $type,
            'report'    => $report,
            'filters'   => $filters,
            'branches'  => Branch::options($scoped),
            'agents'    => User::agents($scoped ?? ($filters['branch_id'] ?? null)),
            'villages'  => LoanAccount::villages($scoped),
            'loanTypes' => LoanAccount::loanTypes($scoped),
            'types'     => ReportService::TYPES,
        ]);
    }

    /** Excel / PDF export of the currently filtered report. */
    public function export(Request $request): void
    {
        $this->guard($request, 'reports.export');

        $type = (string) $request->param('type');
        if (!ReportService::isValidType($type)) {
            $this->back('/reports', 'danger', 'Unknown report type.');
        }

        $format = strtolower($request->str('format', 'excel'));
        $report = ReportService::build($type, $this->filters($request));

        [$content, $filename, $mime] = $format === 'pdf'
            ? ReportService::toPdf($report)
            : ReportService::toExcel($report);

        $this->logExport('Reports', sprintf(
            'Exported %s report as %s (%d row(s))',
            $type,
            $format === 'pdf' ? 'PDF' : 'Excel',
            count($report['rows'])
        ));

        Response::download($content, $filename, $mime);
    }

    /** Printable CKCC OD Field Report for a single visit report. */
    public function printCkccOd(Request $request): void
    {
        $this->guard($request, 'reports.view');

        $id = (int) $request->param('id');
        $data = self::loadCkccOdPrintData($id);

        if ($data === null) {
            $this->back('/reports/ckcc-od', 'danger', 'Report not found.');
        }

        $this->logView('Reports', sprintf('Viewed CKCC OD printable report #%d', $id));

        \App\Core\View::render('reports/ckcc-od-print', [
            'title'  => 'CKCC OD Field Report',
            'report' => $data,
        ], 'layouts/print');
    }

    /** Printable CKCC NPA / KRM OTS Field Report for a single visit report. */
    public function printCkccNpaKrm(Request $request): void
    {
        $this->guard($request, 'reports.view');

        $id = (int) $request->param('id');
        $data = self::loadCkccNpaKrmPrintData($id);

        if ($data === null) {
            $this->back('/reports/ckcc-npa-krm', 'danger', 'Report not found.');
        }

        $this->logView('Reports', sprintf('Viewed CKCC NPA/KRM printable report #%d', $id));

        \App\Core\View::render('reports/ckcc-npa-krm-print', [
            'title'  => 'CKCC NPA / KRM OTS Field Report',
            'report' => $data,
        ], 'layouts/print');
    }

    /**
     * Load a single CKCC OD visit report with all details for printing.
     *
     * @return array<string,mixed>|null
     */
    private static function loadCkccOdPrintData(int $id): ?array
    {
        $db = \App\Core\Database::instance();

        $row = $db->first(
            "SELECT vr.*,
                    cd.cif_number, cd.sanction_date, cd.sanction_limit, cd.drawing_power,
                    cd.outstanding_amount AS cd_outstanding, cd.interest_overdue,
                    cd.renewal_due_date, cd.expected_npa_date, cd.days_remaining,
                    cd.eligible_for_renewal, cd.kyc_status,
                    cd.aadhaar_seeded, cd.mobile_linked, cd.aadhaar_auth_completed,
                    cd.willing_to_renew, cd.documents_handed_over, cd.renewal_form_signed,
                    cd.ekyc_completed, cd.biometrics_completed,
                    cd.agent_observation,
                    cd.rec_renew_immediately, cd.rec_documents_submitted,
                    cd.rec_pending_documents, cd.rec_followup_required,
                    cd.rec_not_interested, cd.rec_branch_contact_urgent,
                    cd.rec_others, cd.rec_other_text,
                    cd.st_customer_contacted, cd.st_customer_verified,
                    cd.st_documents_collected, cd.st_application_submitted,
                    cd.st_ckcc_renewed, cd.st_pending_at_branch,
                    cd.st_followup_required, cd.st_became_npa,
                    COALESCE(u.bcbf_code, '') AS agent_bcbf_code,
                    COALESCE(u.name, vr.agent_name) AS agent_display_name,
                    COALESCE(b.name, '') AS branch_display_name,
                    COALESCE(vr.supervisor_name, '') AS supervisor_name,
                    COALESCE(vr.supervisor_employee_id, '') AS supervisor_bcbf_code
               FROM visit_reports vr
               JOIN visit_ckcc_details cd ON cd.visit_report_id = vr.id
               LEFT JOIN users u ON u.id = vr.agent_id
               LEFT JOIN branches b ON b.id = vr.branch_id
              WHERE vr.id = ? AND vr.report_type = 'ckcc_renewal'",
            [$id]
        );

        return $row ?: null;
    }

    /**
     * Load a single CKCC NPA / KRM OTS visit report with all details for printing.
     *
     * @return array<string,mixed>|null
     */
    private static function loadCkccNpaKrmPrintData(int $id): ?array
    {
        $db = \App\Core\Database::instance();

        $row = $db->first(
            "SELECT vr.*,
                    od.eligible_for_ots, od.scheme, od.scheme_other_text,
                    od.npa_date, od.borrower_name, od.outstanding_amount AS od_outstanding,
                    od.relief_waiver_percent, od.rlb_amount, od.payable_percent,
                    od.borrower_payable_amount, od.total_settlement_amount,
                    od.initial_deposit_percent, od.required_deposit_amount,
                    od.deposit_received, od.deposit_amount, od.deposit_date,
                    od.deposit_reference, od.balance_payable,
                    od.proposed_final_payment_date,
                    od.approval_status, od.validity_from, od.validity_to,
                    od.expected_closure_date,
                    od.borrower_accepted, od.customer_response, od.rejection_reason,
                    od.expected_deposit_date,
                    od.rec_proposal_recommended, od.rec_followup_required,
                    od.rec_customer_refused, od.rec_not_eligible,
                    od.st_customer_contacted, od.st_customer_verified,
                    od.st_ots_accepted, od.st_ots_rejected,
                    od.st_initial_deposit_received, od.st_ots_closed,
                    od.st_followup_required,
                    COALESCE(u.bcbf_code, '') AS agent_bcbf_code,
                    COALESCE(u.name, vr.agent_name) AS agent_display_name,
                    COALESCE(b.name, '') AS branch_display_name,
                    COALESCE(vr.supervisor_name, '') AS supervisor_name,
                    COALESCE(vr.supervisor_employee_id, '') AS supervisor_bcbf_code
               FROM visit_reports vr
               JOIN visit_ots_details od ON od.visit_report_id = vr.id
               LEFT JOIN users u ON u.id = vr.agent_id
               LEFT JOIN branches b ON b.id = vr.branch_id
              WHERE vr.id = ? AND vr.report_type = 'ots'",
            [$id]
        );

        return $row ?: null;
    }

    // -----------------------------------------------------------------------

    /**
     * Report filters. branch_id/agent_id go through the base-class helpers so a
     * branch manager cannot widen their scope with a query parameter.
     *
     * @return array<string,mixed>
     */
    private function filters(Request $request): array
    {
        return [
            'date'           => $request->str('date', date('Y-m-d')),
            'date_from'      => $request->str('date_from'),
            'date_to'        => $request->str('date_to'),
            'week'           => $request->str('week'),
            'month'          => $request->str('month', date('Y-m')),
            'branch_id'      => $this->branchFilter($request),
            'agent_id'       => $this->agentFilter($request),
            'status'         => $request->str('status'),
            'village'        => $request->str('village'),
            'loan_type'      => $request->str('loan_type'),
            'promise_status' => $request->str('promise_status'),
            'npa_only'       => $request->bool('npa_only'),
        ];
    }
}
