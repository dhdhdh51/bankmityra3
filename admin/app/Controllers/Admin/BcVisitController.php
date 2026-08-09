<?php

declare(strict_types=1);

namespace App\Controllers\Admin;

use App\Core\Auth;
use App\Core\Logger;
use App\Core\Request;
use App\Core\Response;
use App\Core\Validator;
use App\Models\BcVisit;
use App\Models\Branch;
use App\Models\User;

/**
 * BC Supervisor visit reports.
 *
 * Supervisors visit BC Agents to assess their performance, equipment, documentation,
 * and remuneration. This controller manages the collection and viewing of these visits,
 * with auto-population of BC Agent details from their user record.
 */
final class BcVisitController extends Controller
{
    public function index(Request $request): void
    {
        $this->guard($request, 'bc_visits.view');

        [$sortBy, $sortDir] = $request->sort(BcVisit::SORTABLE, 'visit_date', 'DESC');

        $branchId = $this->branchFilter($request);
        $userId = $request->nullableInt('user_id');
        $supervisorId = $request->nullableInt('supervisor_id');
        $dateFrom = $request->str('date_from');
        $dateTo = $request->str('date_to');

        $visits = BcVisit::paginate(
            $request->str('search'),
            $userId,
            $supervisorId,
            $branchId,
            $dateFrom,
            $dateTo,
            $sortBy,
            $sortDir,
            $request->page(),
            $this->perPage($request),
        );

        $this->view($request, 'bc/visit/index', [
            'title' => 'BC Supervisor Visits',
            'visits' => $visits,
            'search' => $request->str('search'),
            'userId' => $userId,
            'supervisorId' => $supervisorId,
            'branchId' => $branchId,
            'dateFrom' => $dateFrom,
            'dateTo' => $dateTo,
            'branches' => Branch::options($branchId ?? Auth::scopedBranchId()),
            'agents' => User::agents($branchId ?? Auth::scopedBranchId()),
            'supervisors' => User::agents($branchId ?? Auth::scopedBranchId()),
            'sortBy' => $sortBy,
            'sortDir' => $sortDir,
        ]);
    }

    public function create(Request $request): void
    {
        $this->guard($request, 'bc_visits.manage');

        if (!$request->isPost()) {
            $this->view($request, 'bc/visit/form', [
                'title' => 'Record BC Supervisor Visit',
                'visit' => null,
                'agents' => User::agents(Auth::scopedBranchId()),
            ]);
        }

        $validator = $this->validate($request);
        if ($validator->fails()) {
            $this->backWithErrors('/bc/visit/create', $validator->errors(), $request->all());
        }

        // Validate that user_id is an active agent
        $userId = $request->nullableInt('user_id');
        if ($userId !== null) {
            $agent = User::find($userId);
            if ($agent === null || $agent['role_slug'] !== 'agent' || $agent['status'] !== 'active') {
                $this->backWithErrors(
                    '/bc/visit/create',
                    ['user_id' => ['Selected agent is not an active agent.']],
                    $request->all(),
                );
            }
        }

        $payload = $this->payload($request);
        $payload['supervisor_id'] = Auth::id();

        $id = BcVisit::create($payload);

        Logger::audit(
            'create',
            'bc_visit',
            $id,
            null,
            $payload,
            'Created BC Supervisor visit record',
        );

        $this->back('/bc/visit', 'success', 'Visit recorded.');
    }

    public function show(Request $request): void
    {
        $this->guard($request, 'bc_visits.view');

        $id = $request->paramInt('id');
        $visit = BcVisit::find($id);

        if ($visit === null) {
            $this->back('/bc/visit', 'danger', 'That visit could not be found.');
        }

        $this->view($request, 'bc/visit/show', [
            'title' => 'BC Supervisor Visit',
            'visit' => $visit,
        ]);
    }

    public function edit(Request $request): void
    {
        $this->guard($request, 'bc_visits.manage');

        $id = $request->paramInt('id');
        $visit = BcVisit::find($id);

        if ($visit === null) {
            $this->back('/bc/visit', 'danger', 'That visit could not be found.');
        }

        if (!$request->isPost()) {
            $this->view($request, 'bc/visit/form', [
                'title' => 'Edit BC Supervisor Visit',
                'visit' => $visit,
                'agents' => User::agents(Auth::scopedBranchId()),
            ]);
        }

        $validator = $this->validate($request, true);
        if ($validator->fails()) {
            $this->backWithErrors('/bc/visit/' . $id . '/edit', $validator->errors(), $request->all());
        }

        $payload = $this->payload($request);

        BcVisit::update($id, $payload);

        Logger::auditDiff(
            'bc_visit',
            $id,
            $visit,
            $payload,
            'Updated BC Supervisor visit record',
        );

        $this->back('/bc/visit', 'success', 'Visit updated.');
    }

    public function delete(Request $request): void
    {
        $this->guard($request, 'bc_visits.manage');

        $id = $request->paramInt('id');
        $visit = BcVisit::find($id);

        if ($visit === null) {
            $this->back('/bc/visit', 'danger', 'That visit could not be found.');
        }

        BcVisit::delete($id);

        Logger::audit(
            'delete',
            'bc_visit',
            $id,
            $visit,
            null,
            'Deleted BC Supervisor visit record',
        );

        $this->back('/bc/visit', 'success', 'Visit removed.');
    }

    /**
     * API endpoint to load agent details and return as JSON.
     * Called via AJAX when a BC Agent is selected in the form.
     *
     * Returns: {id, name, bc_code, sp_cbc_name, branch_name, iibf_number, ssa, link_branch, region_ro}
     */
    public function apiAgentLoad(Request $request): void
    {
        $this->guard($request);

        $userId = $request->paramInt('id');

        // Verify the user is an active agent
        $agent = User::find($userId);
        if ($agent === null || $agent['role_slug'] !== 'agent' || $agent['status'] !== 'active') {
            Response::notFound('Agent not found or is not active');
        }

        $details = BcVisit::agentDetails($userId);
        if ($details === null) {
            Response::notFound('Agent details not found');
        }

        Response::json($details);
    }

    // -----------------------------------------------------------------------

    private function validate(Request $request, bool $isEdit = false): Validator
    {
        $rules = [
            'visit_date' => 'required|date',
            'visit_time' => 'nullable|time',
            'user_id' => 'nullable|integer',
            'qualification' => 'nullable|string|max:255',
            'age' => 'nullable|integer|min_value:18|max_value:100',
            'address_contact' => 'nullable|string|max:500',
            'board_available' => 'nullable|boolean',
            'equipment_status' => 'nullable|string|max:255',
            'remuneration' => 'nullable|string|max:255',
            'feedback' => 'nullable|string|max:1000',
            'observation' => 'nullable|string|max:1000',
            'visiting_official_name' => 'nullable|string|max:150',
        ];

        $labels = [
            'visit_date' => 'Visit date',
            'visit_time' => 'Visit time',
            'user_id' => 'BC Agent',
            'qualification' => 'Qualification',
            'age' => 'Age',
            'address_contact' => 'Address/Contact',
            'board_available' => 'Board available',
            'equipment_status' => 'Equipment status',
            'remuneration' => 'Remuneration',
            'feedback' => 'Feedback',
            'observation' => 'Observation',
            'visiting_official_name' => 'Visiting official name',
        ];

        return Validator::make($request->all(), $rules, $labels);
    }

    /**
     * Extract and prepare the payload for create/update.
     *
     * @return array<string,mixed>
     */
    private function payload(Request $request): array
    {
        $userId = $request->nullableInt('user_id');

        // If an agent is selected, auto-populate their details
        $agentDetails = null;
        if ($userId !== null) {
            $agentDetails = BcVisit::agentDetails($userId);
        }

        return [
            'user_id' => $userId,
            'visit_date' => $request->str('visit_date'),
            'visit_time' => $request->nullableStr('visit_time'),
            'bca_name' => $agentDetails['name'] ?? null,
            'bc_code' => $agentDetails['bc_code'] ?? null,
            'cbc_name' => $agentDetails['sp_cbc_name'] ?? null,
            'branch_name' => $agentDetails['branch_name'] ?? null,
            'iibf_certificate_no' => $agentDetails['iibf_number'] ?? null,
            'ssa_non_ssa' => $agentDetails['ssa'] ?? null,
            'board_link_br_name' => $agentDetails['link_branch'] ?? null,
            'qualification' => $request->nullableStr('qualification'),
            'age' => $request->nullableInt('age'),
            'address_contact' => $request->nullableStr('address_contact'),
            'board_available' => $request->nullableInt('board_available'),
            'equipment_status' => $request->nullableStr('equipment_status'),
            'remuneration' => $request->nullableStr('remuneration'),
            'feedback' => $request->nullableStr('feedback'),
            'observation' => $request->nullableStr('observation'),
            'visiting_official_name' => $request->nullableStr('visiting_official_name'),
        ];
    }
}
