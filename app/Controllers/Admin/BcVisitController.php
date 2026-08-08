<?php

declare(strict_types=1);

namespace App\Controllers\Admin;

use App\Core\Auth;
use App\Core\Logger;
use App\Core\Request;
use App\Core\Validator;
use App\Models\BcVisit;

/**
 * BC Visit - records a BC Supervisor's visit to a BC Agent outlet.
 *
 * This is a standalone form, completely separate from the loan visit reports.
 * A supervisor physically visits the BC agent and fills in the 27-field checklist.
 */
final class BcVisitController extends Controller
{
    public function index(Request $request): void
    {
        $this->guard($request, 'bc_visit.view');

        [$sortBy, $sortDir] = $request->sort(BcVisit::SORTABLE, 'created_at', 'DESC');

        $visits = BcVisit::paginate(
            $request->str('search'),
            $sortBy,
            $sortDir,
            $request->page(),
            $this->perPage($request)
        );

        $this->view($request, 'bc/visit/index', [
            'title'  => 'BC Visits',
            'visits' => $visits,
            'search' => $request->str('search'),
            'sortBy' => $sortBy,
            'sortDir' => $sortDir,
        ]);
    }

    public function create(Request $request): void
    {
        $this->guard($request, 'bc_visit.manage');

        if (!$request->isPost()) {
            $this->view($request, 'bc/visit/form', [
                'title' => 'BC Supervisor - Record Visit',
                'visit' => null,
            ]);
        }

        $validator = $this->validate($request);
        if ($validator->fails()) {
            $this->backWithErrors('/bc/visit/create', $validator->errors(), $request->all());
        }

        $data = $this->payload($request);
        $data['created_by'] = Auth::id();

        $id = BcVisit::create($data);

        Logger::audit('create', 'bc_visit', $id, null, $data, sprintf('Recorded BC visit for %s', $data['bca_name']));

        $this->back('/bc/visit', 'success', 'BC Visit recorded successfully.');
    }

    public function show(Request $request): void
    {
        $this->guard($request, 'bc_visit.view');

        $id = $request->paramInt('id');
        $visit = BcVisit::find($id);

        if ($visit === null) {
            $this->back('/bc/visit', 'danger', 'That BC visit could not be found.');
        }

        $this->view($request, 'bc/visit/form', [
            'title' => 'BC Supervisor - View Visit',
            'visit' => $visit,
        ]);
    }

    public function edit(Request $request): void
    {
        $this->guard($request, 'bc_visit.manage');

        $id = $request->paramInt('id');
        $visit = BcVisit::find($id);

        if ($visit === null) {
            $this->back('/bc/visit', 'danger', 'That BC visit could not be found.');
        }

        if (!$request->isPost()) {
            $this->view($request, 'bc/visit/form', [
                'title' => 'BC Supervisor - Edit Visit',
                'visit' => $visit,
            ]);
        }

        $validator = $this->validate($request);
        if ($validator->fails()) {
            $this->backWithErrors('/bc/visit/' . $id . '/edit', $validator->errors(), $request->all());
        }

        $data = $this->payload($request);
        BcVisit::update($id, $data);

        Logger::auditDiff('bc_visit', $id, $visit, $data, sprintf('Updated BC visit for %s', $data['bca_name']));

        $this->back('/bc/visit', 'success', 'BC Visit updated successfully.');
    }

    public function delete(Request $request): void
    {
        $this->guard($request, 'bc_visit.manage');

        $id = $request->paramInt('id');
        $visit = BcVisit::find($id);

        if ($visit === null) {
            $this->back('/bc/visit', 'danger', 'That BC visit could not be found.');
        }

        BcVisit::delete($id);

        Logger::audit('delete', 'bc_visit', $id, $visit, null, sprintf('Deleted BC visit for %s', (string) $visit['bca_name']));

        $this->back('/bc/visit', 'success', 'BC Visit deleted.');
    }

    // -----------------------------------------------------------------------

    private function validate(Request $request): Validator
    {
        return Validator::make($request->all(), [
            'bca_name'    => 'required|max:200',
            'branch_name' => 'required|max:200',
            'cbc_name'    => 'nullable|max:200',
            'qualification' => 'nullable|max:200',
            'age'         => 'nullable|integer|min_value:18|max_value:100',
            'address_contact' => 'nullable|max:500',
            'bc_certification_iibf' => 'nullable|in:yes,no',
            'iibf_certificate_no' => 'nullable|max:100',
            'bc_working_since' => 'nullable|date',
            'appointment_letter' => 'nullable|in:yes,no',
            'identity_card' => 'nullable|in:yes,no',
            'district_coordinator_name' => 'nullable|max:300',
            'ssa_non_ssa' => 'nullable|max:300',
            'board_available' => 'nullable|in:yes,no',
            'dos_donts_board' => 'nullable|in:yes,no',
            'services_list_display' => 'nullable|in:yes,no',
            'sign_board' => 'nullable|in:yes,no',
            'board_bank_name' => 'nullable|max:200',
            'board_link_br_name' => 'nullable|max:200',
            'board_contact_br' => 'nullable|max:100',
            'board_working_time' => 'nullable|max:200',
            'transactions_previous_day' => 'nullable|max:2000',
            'services_provided' => 'nullable|max:2000',
            'bc_awareness_sss' => 'nullable|max:2000',
            'complaint_register' => 'nullable|in:yes,no',
            'transactions_register' => 'nullable|in:yes,no',
            'visit_register' => 'nullable|in:yes,no',
            'register_other' => 'nullable|max:300',
            'equip_laptop' => 'nullable|in:yes,no',
            'equip_biometric' => 'nullable|in:yes,no',
            'equip_pinpad' => 'nullable|in:yes,no',
            'equip_receipt_machine' => 'nullable|in:yes,no',
            'equip_printer' => 'nullable|in:yes,no',
            'remuneration_month1' => 'nullable|max:100',
            'remuneration_amount1' => 'nullable|numeric',
            'remuneration_month2' => 'nullable|max:100',
            'remuneration_amount2' => 'nullable|numeric',
            'remuneration_month3' => 'nullable|max:100',
            'remuneration_amount3' => 'nullable|numeric',
            'feedback_villagers' => 'nullable|max:2000',
            'bc_working_location' => 'nullable|max:2000',
            'other_info' => 'nullable|max:2000',
            'observation' => 'nullable|in:excellent,good,satisfactory,poor',
            'visiting_official_name' => 'nullable|max:300',
            'visiting_official_signature' => 'nullable|max:300',
            'visit_date' => 'nullable|date',
            'other_info_2' => 'nullable|max:2000',
        ], [
            'bca_name' => 'BCA Name',
            'branch_name' => 'Branch Name',
            'cbc_name' => 'CBC Name',
            'bc_certification_iibf' => 'BC Certification (IIBF)',
            'iibf_certificate_no' => 'Certificate Number',
            'bc_working_since' => 'BC Working Since',
            'district_coordinator_name' => 'District Coordinator Name',
            'ssa_non_ssa' => 'SSA/Non SSA',
        ]);
    }

    /** @return array<string,mixed> */
    private function payload(Request $request): array
    {
        return [
            'bca_name'          => $request->str('bca_name'),
            'branch_name'       => $request->str('branch_name'),
            'cbc_name'          => $request->nullableStr('cbc_name'),
            'qualification'     => $request->nullableStr('qualification'),
            'age'               => $request->nullableInt('age'),
            'address_contact'   => $request->nullableStr('address_contact'),
            'bc_certification_iibf' => $request->nullableStr('bc_certification_iibf'),
            'iibf_certificate_no'   => $request->nullableStr('iibf_certificate_no'),
            'bc_working_since'      => $request->nullableStr('bc_working_since'),
            'appointment_letter'    => $request->nullableStr('appointment_letter'),
            'identity_card'         => $request->nullableStr('identity_card'),
            'district_coordinator_name' => $request->nullableStr('district_coordinator_name'),
            'ssa_non_ssa'               => $request->nullableStr('ssa_non_ssa'),
            'board_available'       => $request->nullableStr('board_available'),
            'dos_donts_board'       => $request->nullableStr('dos_donts_board'),
            'services_list_display' => $request->nullableStr('services_list_display'),
            'sign_board'            => $request->nullableStr('sign_board'),
            'board_bank_name'       => $request->nullableStr('board_bank_name'),
            'board_link_br_name'    => $request->nullableStr('board_link_br_name'),
            'board_contact_br'      => $request->nullableStr('board_contact_br'),
            'board_working_time'    => $request->nullableStr('board_working_time'),
            'transactions_previous_day' => $request->nullableStr('transactions_previous_day'),
            'services_provided'         => $request->nullableStr('services_provided'),
            'bc_awareness_sss'          => $request->nullableStr('bc_awareness_sss'),
            'complaint_register'    => $request->nullableStr('complaint_register'),
            'transactions_register' => $request->nullableStr('transactions_register'),
            'visit_register'        => $request->nullableStr('visit_register'),
            'register_other'        => $request->nullableStr('register_other'),
            'equip_laptop'          => $request->nullableStr('equip_laptop'),
            'equip_biometric'       => $request->nullableStr('equip_biometric'),
            'equip_pinpad'          => $request->nullableStr('equip_pinpad'),
            'equip_receipt_machine' => $request->nullableStr('equip_receipt_machine'),
            'equip_printer'         => $request->nullableStr('equip_printer'),
            'remuneration_month1'   => $request->nullableStr('remuneration_month1'),
            'remuneration_amount1'  => $request->nullableStr('remuneration_amount1'),
            'remuneration_month2'   => $request->nullableStr('remuneration_month2'),
            'remuneration_amount2'  => $request->nullableStr('remuneration_amount2'),
            'remuneration_month3'   => $request->nullableStr('remuneration_month3'),
            'remuneration_amount3'  => $request->nullableStr('remuneration_amount3'),
            'feedback_villagers'    => $request->nullableStr('feedback_villagers'),
            'bc_working_location'   => $request->nullableStr('bc_working_location'),
            'other_info'            => $request->nullableStr('other_info'),
            'observation'           => $request->nullableStr('observation'),
            'visiting_official_name'      => $request->nullableStr('visiting_official_name'),
            'visiting_official_signature' => $request->nullableStr('visiting_official_signature'),
            'visit_date'            => $request->nullableStr('visit_date'),
            'other_info_2'          => $request->nullableStr('other_info_2'),
        ];
    }
}
