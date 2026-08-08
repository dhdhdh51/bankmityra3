package com.lrms.recovery.report

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

/**
 * Exports form field data as a CSV file compatible with Excel.
 *
 * The CSV has two columns: "Field" and "Value". Field keys are mapped to
 * human-readable labels matching the printed report section order.
 */
object ExcelExporter {

    /**
     * Key-to-label mapping for all known form fields.
     * Order follows the report sections.
     */
    private val FIELD_LABELS: Map<String, String> = linkedMapOf(
        // Section 1: General Information
        "report_type" to "Report Type",
        "report_type_other_text" to "Report Type (Other)",
        "visit_date" to "Visit Date",
        "visit_time" to "Visit Time",
        "bcbf_code" to "BCBF Code",
        "village" to "Village/Location",

        // Section 2: Borrower Information
        "gender" to "Gender",
        "date_of_birth" to "Date of Birth",
        "pan_number" to "PAN Number",
        "addr_village" to "Address - Village",
        "gram_panchayat" to "Gram Panchayat",
        "tehsil" to "Tehsil",
        "addr_district" to "Address - District",
        "state" to "State",
        "pin_code" to "PIN Code",

        // Section 3: Loan Account Details
        "cif_number" to "CIF Number",
        "loan_type" to "Loan Type",
        "loan_type_other_text" to "Loan Type (Other)",
        "sanction_date" to "Sanction Date",
        "sanction_limit" to "Sanction Limit",
        "drawing_power" to "Drawing Power",
        "interest_overdue" to "Interest Overdue",
        "asset_classification" to "Asset Classification",

        // Section 4: KRM OTS Details
        "ots_eligible" to "OTS Eligibility",
        "ots_scheme" to "OTS Scheme",
        "ots_scheme_other_text" to "OTS Scheme (Other)",
        "ots_rlb_amount" to "RLB Amount",
        "ots_relief_percent" to "OTS Relief %",
        "ots_payable_percent" to "OTS Payable %",
        "ots_payable_amount" to "OTS Payable Amount",
        "ots_total_settlement" to "Total OTS Settlement",
        "ots_deposit_percent" to "Deposit %",
        "ots_required_deposit" to "Required Initial Deposit",
        "ots_deposit_received" to "Deposit Received",
        "ots_deposit_amount" to "Deposit Amount",
        "ots_deposit_date" to "Deposit Date",
        "ots_deposit_reference" to "Deposit Reference",
        "ots_balance_payable" to "Balance Payable",
        "ots_final_payment_date" to "Final Payment Date",
        "ots_approval_status" to "OTS Approval Status",
        "ots_validity_from" to "OTS Validity From",
        "ots_validity_to" to "OTS Validity To",
        "ots_expected_closure" to "Expected Closure Date",
        "ots_borrower_accepted" to "Borrower Accepted OTS",
        "ots_rejection_reason" to "OTS Rejection Reason",
        "ots_customer_response" to "Customer Response",

        // NPA OTS specific
        "npa_ots_eligible" to "OTS Eligibility",
        "npa_ots_scheme" to "OTS Scheme",
        "npa_ots_scheme_other" to "OTS Scheme (Other)",
        "npa_ots_outstanding" to "Outstanding Amount",
        "npa_ots_rlb_amount" to "RLB Amount",
        "npa_ots_relief_percent" to "Relief %",
        "npa_ots_payable_percent" to "Payable %",
        "npa_ots_payable_amount" to "Payable Amount",
        "npa_ots_total_settlement" to "Total Settlement",
        "npa_ots_deposit_percent" to "Deposit %",
        "npa_ots_required_deposit" to "Required Deposit",
        "npa_ots_deposit_received" to "Deposit Received",
        "npa_ots_deposit_amount" to "Deposit Amount",
        "npa_ots_deposit_date" to "Deposit Date",
        "npa_ots_deposit_reference" to "Deposit Reference",
        "npa_ots_balance_payable" to "Balance Payable",
        "npa_ots_final_payment_date" to "Final Payment Date",
        "npa_ots_approval_status" to "Approval Status",
        "npa_ots_validity_from" to "Valid From",
        "npa_ots_validity_to" to "Valid To",
        "npa_ots_expected_closure" to "Expected Closure",
        "npa_ots_borrower_response" to "Borrower Response",
        "npa_ots_rejection_reason" to "Rejection Reason",
        "npa_ots_observation" to "Observation",
        "npa_ots_rec_proposal_recommended" to "OTS Proposal Recommended",
        "npa_ots_rec_followup_required" to "Follow-up Required",
        "npa_ots_rec_customer_refused" to "Customer Refused",
        "npa_ots_rec_not_eligible" to "Not Eligible",

        // CKCC fields
        "ckcc_cif" to "CIF Number",
        "ckcc_sanction_date" to "CKCC Sanction Date",
        "ckcc_sanction_limit" to "CKCC Sanction Limit",
        "ckcc_drawing_power" to "CKCC Drawing Power",
        "ckcc_outstanding" to "CKCC Outstanding",
        "ckcc_interest_overdue" to "CKCC Interest Overdue",
        "ckcc_renewal_due" to "Renewal Due Date",
        "ckcc_eligible" to "Eligible for Renewal",
        "ckcc_kyc_complete" to "KYC Complete",
        "ckcc_aadhaar_seeded" to "Aadhaar Seeded",
        "ckcc_mobile_linked" to "Mobile Linked",
        "ckcc_aadhaar_auth" to "Aadhaar Auth Completed",
        "ckcc_willing_to_renew" to "Willing to Renew",
        "ckcc_documents_handed" to "Documents Handed",
        "ckcc_form_signed" to "Renewal Form Signed",
        "ckcc_ekyc" to "e-KYC Completed",
        "ckcc_biometrics" to "Biometrics Completed",
        "ckcc_observation" to "CKCC Observation",

        // Section 6: Physical Verification
        "customer_met" to "Borrower Met",
        "family_member_met" to "Family Member Met",
        "house_locked" to "House Locked",
        "phone_contact" to "Mobile Contacted",
        "phone_switched_off" to "Phone Switched Off",
        "family_member_name" to "Family Member Name",
        "family_member_relationship" to "Family Member Relationship",
        "borrower_alive" to "Borrower Alive",
        "same_address" to "Same Address",
        "shifted" to "Shifted",
        "occupation" to "Occupation",
        "occupation_other_text" to "Occupation (Other)",
        "residence_verified" to "Residence Verification",
        "neighbour_verification" to "Neighbour Verification",

        // Section 8: Observations
        "remarks" to "BC Supervisor Observations",
        "general_recommendation" to "General Recommendation",

        // Section 9: Recommendation
        "rec_recovery_possible" to "Recovery Possible",
        "rec_regular_followup" to "Regular Follow-up",
        "rec_legal_action" to "Legal Action",
        "rec_rc" to "RC",
        "rec_ots" to "OTS",
        "rec_other_text" to "Recommendation (Other)",

        // Recovery
        "ready_to_pay" to "Ready to Pay",
        "not_ready" to "Not Ready",
        "interest_payment" to "Interest Payment",
        "ots_recovery" to "OTS (Recovery)",
        "promise_amount" to "Promise Amount",
        "promise_date" to "Promise Date",

        // Non-payment reasons
        "reason_financial" to "Financial Problem",
        "reason_crop_loss" to "Crop Loss",
        "reason_animal_loss" to "Animal Loss",
        "reason_illness" to "Illness",
        "reason_unemployment" to "Unemployment",
        "reason_dispute" to "Dispute",
        "reason_other_loan" to "Other Loan",
        "reason_other_text" to "Reason (Other)",

        // Declaration
        "declaration_accepted" to "Declaration Accepted",
    )

    /**
     * Converts a field key to a human-readable label.
     */
    fun labelFor(key: String): String {
        return FIELD_LABELS[key] ?: key
            .replace("_", " ")
            .replaceFirstChar { it.uppercaseChar() }
    }

    /**
     * Generates CSV content from a field map.
     *
     * @param fields map of field keys to their string values
     * @return the CSV string with "Field,Value" header
     */
    fun generateCsv(fields: Map<String, String>): String {
        val sb = StringBuilder()
        sb.appendLine("Field,Value")
        for ((key, value) in fields) {
            val label = csvEscape(labelFor(key))
            val escaped = csvEscape(value)
            sb.appendLine("$label,$escaped")
        }
        return sb.toString()
    }

    /**
     * Exports form field data as a CSV file.
     *
     * @param context Android context for file operations
     * @param fields map of field keys to their string values
     * @param fileName name for the output CSV file
     * @return the File reference to the written CSV
     */
    fun export(context: Context, fields: Map<String, String>, fileName: String): File {
        val exportDir = File(context.cacheDir, "exports")
        if (!exportDir.exists()) exportDir.mkdirs()

        val file = File(exportDir, fileName)
        file.writeText(generateCsv(fields))
        return file
    }

    /**
     * Creates an ACTION_SEND intent to share the CSV file.
     *
     * @param context Android context for FileProvider URI resolution
     * @param file the CSV file to share
     * @return an Intent that can be started with startActivity or wrapped in a chooser
     */
    fun shareIntent(context: Context, file: File): Intent {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file,
        )
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Field Visit Report Data")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    /**
     * Escapes a value for CSV: wraps in quotes if it contains a comma, quote, or newline.
     */
    internal fun csvEscape(value: String): String {
        return if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }
}
