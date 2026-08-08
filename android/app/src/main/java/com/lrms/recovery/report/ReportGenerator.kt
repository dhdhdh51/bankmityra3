package com.lrms.recovery.report

import com.lrms.recovery.domain.VisitFormData
import com.lrms.recovery.util.Formatters

/**
 * Generates A4-formatted HTML for the Field Visit Verification Report.
 *
 * The output is a self-contained HTML document that renders in a WebView and can
 * be printed to PDF via Android's PrintManager. It has no Android dependencies
 * itself, so it is unit-testable without Robolectric.
 */
object ReportGenerator {

    /**
     * Supplementary data that the form does not carry but the printed report needs.
     *
     * These come from the borrower/loan record on the server, passed in by the
     * calling Activity when it builds the preview.
     */
    data class SupplementaryData(
        val borrowerName: String = "",
        val fatherHusbandName: String = "",
        val loanAccountNumber: String = "",
        val mobile: String = "",
        val alternateMobile: String = "",
        val aadhaarLast4: String = "",
        val branchName: String = "",
        val branchCode: String = "",
        val regionalOffice: String = "",
        val zone: String = "",
        val cbcName: String = "",
        val bcSupervisorName: String = "",
        val bcCode: String = "",
        val linkedBranch: String = "",
        val district: String = "",
        val iibfNumber: String = "",
        val outstandingAmount: String = "",
        val overdueAmount: String = "",
        val npaDate: String = "",
    )

    // Unicode ballot box characters
    private const val CHECKED = "&#9745;"
    private const val UNCHECKED = "&#9744;"

    /**
     * Generates the complete HTML report.
     */
    fun generate(form: VisitFormData, data: SupplementaryData): String {
        val sb = StringBuilder()
        sb.append(htmlHead())
        sb.append(headerSection(form, data))
        sb.append(section1GeneralInfo(form, data))
        sb.append(section2BorrowerInfo(form, data))
        sb.append(section3LoanDetails(form, data))
        if (form.reportType == VisitFormData.REPORT_OTS ||
            form.reportType == VisitFormData.REPORT_CKCC_NPA_OTS
        ) {
            sb.append(section4OtsDetails(form, data))
        }
        sb.append(section6PhysicalVerification(form))
        sb.append(section8Observations(form))
        sb.append(section9Recommendation(form))
        sb.append(section11Declaration())
        sb.append(section12Certification(form, data))
        sb.append(htmlFoot())
        return sb.toString()
    }

    // -----------------------------------------------------------------------
    // HTML structure
    // -----------------------------------------------------------------------

    private fun htmlHead(): String = """<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Field Visit Verification Report</title>
<style>
@page { size: A4; margin: 15mm; }
* { box-sizing: border-box; }
body {
    font-family: Arial, sans-serif;
    font-size: 11px;
    color: #333;
    margin: 0;
    padding: 15mm;
    line-height: 1.4;
}
table {
    border-collapse: collapse;
    width: 100%;
    margin-bottom: 8px;
    border: 1px solid #333;
}
td, th {
    border: 1px solid #333;
    padding: 4px 6px;
    vertical-align: top;
    font-size: 11px;
}
th {
    background: #f0f0f0;
    font-weight: bold;
    text-align: center;
}
.section-header {
    background: #f0f0f0;
    font-weight: bold;
    text-align: center;
    padding: 6px;
    font-size: 12px;
}
.header-block {
    text-align: center;
    margin-bottom: 12px;
}
.header-block h1 {
    font-size: 16px;
    margin: 0 0 4px 0;
    color: #222;
}
.header-block h2 {
    font-size: 13px;
    margin: 0 0 3px 0;
    color: #333;
}
.header-block p {
    font-size: 11px;
    margin: 2px 0;
    color: #555;
}
.header-block .supervisor-info {
    font-size: 11px;
    margin-top: 6px;
    font-weight: bold;
}
.label { font-weight: bold; white-space: nowrap; }
.checkbox { font-size: 13px; }
.declaration-text { font-size: 10px; line-height: 1.5; }
.note { font-size: 10px; font-style: italic; color: #555; margin-top: 8px; }
.sig-line { border-bottom: 1px solid #333; min-width: 150px; display: inline-block; margin: 0 8px; }
</style>
</head>
<body>
"""

    private fun htmlFoot(): String = """</body>
</html>"""

    // -----------------------------------------------------------------------
    // Sections
    // -----------------------------------------------------------------------

    private fun headerSection(form: VisitFormData, data: SupplementaryData): String = """
<div class="header-block">
    <h1>D2 RECOVERY SOLUTIONS &amp; SERVICES</h1>
    <h2>FIELD VISIT VERIFICATION REPORT</h2>
    <p>(KRM OTS / CKCC OD-2 Renewal / Recovery Verification Report)</p>
    <p><strong>RBI Guidelines &amp; Bank's Code of Conduct Compliant Format</strong></p>
    <div class="supervisor-info">
        BC Supervisor: ${esc(data.bcSupervisorName)} &nbsp; | &nbsp; BCBF Code: ${esc(form.bcbfCode.ifBlank { data.bcCode })}
    </div>
</div>
"""

    private fun section1GeneralInfo(form: VisitFormData, data: SupplementaryData): String {
        val sb = StringBuilder()
        sb.append("""<table>
<tr><th class="section-header" colspan="4">SECTION 1: GENERAL INFORMATION</th></tr>
<tr>
    <td class="label">Visit Date</td><td>${esc(form.visitDate)}</td>
    <td class="label">Visit Time</td><td>${esc(form.visitTime)}</td>
</tr>
<tr>
    <td class="label">Case Type</td>
    <td colspan="3" class="checkbox">""")

        // Case type checkboxes
        val caseTypes = listOf(
            VisitFormData.REPORT_OTS to "KRM OTS",
            VisitFormData.REPORT_CKCC to "CKCC OD-2 Renewal",
            VisitFormData.REPORT_CKCC_NPA_OTS to "CKCC NPA - KRM OTS",
            VisitFormData.REPORT_RECOVERY to "Recovery Follow-up",
            VisitFormData.REPORT_PRE_NPA to "Pre-NPA Verification",
            VisitFormData.REPORT_POST_NPA to "Post-NPA Verification",
            VisitFormData.REPORT_OTHER to "Other",
        )
        for ((value, label) in caseTypes) {
            val icon = if (form.reportType == value) CHECKED else UNCHECKED
            sb.append("$icon $label &nbsp; ")
        }

        sb.append("""</td>
</tr>
<tr>
    <td class="label">Branch Name</td><td>${esc(data.branchName)}</td>
    <td class="label">Branch Code</td><td>${esc(data.branchCode)}</td>
</tr>
<tr>
    <td class="label">Regional Office</td><td>${esc(data.regionalOffice)}</td>
    <td class="label">Zone</td><td>${esc(data.zone)}</td>
</tr>
<tr>
    <td class="label">CBC Name</td><td>${esc(data.cbcName)}</td>
    <td class="label">BC Supervisor Name</td><td>${esc(data.bcSupervisorName)}</td>
</tr>
<tr>
    <td class="label">BC Code</td><td>${esc(data.bcCode)}</td>
    <td class="label">BCBF Code</td><td>${esc(form.bcbfCode.ifBlank { data.bcCode })}</td>
</tr>
<tr>
    <td class="label">Linked Branch</td><td>${esc(data.linkedBranch)}</td>
    <td class="label">District</td><td>${esc(data.district)}</td>
</tr>
<tr>
    <td class="label">Village/Location</td><td>${esc(form.village)}</td>
    <td class="label">Mobile Number</td><td>${esc(data.mobile)}</td>
</tr>
<tr>
    <td class="label">IIBF Number</td><td colspan="3">${esc(data.iibfNumber)}</td>
</tr>
</table>
""")
        return sb.toString()
    }

    private fun section2BorrowerInfo(form: VisitFormData, data: SupplementaryData): String {
        val genderDisplay = VisitFormData.GENDERS.firstOrNull { it.first == form.gender }?.second ?: form.gender
        return """<table>
<tr><th class="section-header" colspan="4">SECTION 2: BORROWER INFORMATION</th></tr>
<tr>
    <td class="label">Borrower Name</td><td>${esc(data.borrowerName)}</td>
    <td class="label">Father's/Husband's Name</td><td>${esc(data.fatherHusbandName)}</td>
</tr>
<tr>
    <td class="label">Gender</td><td>${esc(genderDisplay)}</td>
    <td class="label">Date of Birth</td><td>${esc(form.dateOfBirth)}</td>
</tr>
<tr>
    <td class="label">Mobile Number</td><td>${esc(data.mobile)}</td>
    <td class="label">Alternate Mobile</td><td>${esc(data.alternateMobile)}</td>
</tr>
<tr>
    <td class="label">Aadhaar (Last 4 Digits)</td><td>${esc(data.aadhaarLast4)}</td>
    <td class="label">PAN Number</td><td>${esc(form.panNumber)}</td>
</tr>
<tr><td class="label" colspan="4" style="background:#f9f9f9;">Address</td></tr>
<tr>
    <td class="label">Village</td><td>${esc(form.addrVillage)}</td>
    <td class="label">Gram Panchayat</td><td>${esc(form.gramPanchayat)}</td>
</tr>
<tr>
    <td class="label">Tehsil</td><td>${esc(form.tehsil)}</td>
    <td class="label">District</td><td>${esc(form.addrDistrict)}</td>
</tr>
<tr>
    <td class="label">State</td><td>${esc(form.state)}</td>
    <td class="label">PIN Code</td><td>${esc(form.pinCode)}</td>
</tr>
</table>
"""
    }

    private fun section3LoanDetails(form: VisitFormData, data: SupplementaryData): String {
        val sb = StringBuilder()
        sb.append("""<table>
<tr><th class="section-header" colspan="4">SECTION 3: LOAN ACCOUNT DETAILS</th></tr>
<tr>
    <td class="label">Loan Account Number</td><td>${esc(data.loanAccountNumber)}</td>
    <td class="label">CIF Number</td><td>${esc(form.cifNumber)}</td>
</tr>
<tr>
    <td class="label">Loan Type</td>
    <td colspan="3" class="checkbox">""")

        for ((value, label) in VisitFormData.LOAN_TYPES) {
            val icon = if (form.loanType == value) CHECKED else UNCHECKED
            sb.append("$icon $label &nbsp; ")
        }

        sb.append("""</td>
</tr>
<tr>
    <td class="label">Sanction Date</td><td>${esc(form.sanctionDate)}</td>
    <td class="label">Sanction Limit</td><td>${formatAmount(form.sanctionLimit)}</td>
</tr>
<tr>
    <td class="label">Drawing Power</td><td>${formatAmount(form.drawingPower)}</td>
    <td class="label">Outstanding Amount</td><td>${formatAmount(data.outstandingAmount)}</td>
</tr>
<tr>
    <td class="label">Interest Overdue</td><td>${formatAmount(form.interestOverdue)}</td>
    <td class="label">Overdue Amount</td><td>${formatAmount(data.overdueAmount)}</td>
</tr>
<tr>
    <td class="label">NPA Date</td><td>${esc(data.npaDate)}</td>
    <td class="label">Asset Classification</td>
    <td class="checkbox">""")

        for ((value, label) in VisitFormData.ASSET_CLASSIFICATIONS) {
            val icon = if (form.assetClassification == value) CHECKED else UNCHECKED
            sb.append("$icon $label &nbsp; ")
        }

        sb.append("""</td>
</tr>
</table>
""")
        return sb.toString()
    }

    private fun section4OtsDetails(form: VisitFormData, data: SupplementaryData): String {
        // Use either OTS fields or NPA OTS fields depending on report type
        val isNpaOts = form.reportType == VisitFormData.REPORT_CKCC_NPA_OTS

        val eligible = if (isNpaOts) form.npaOtsEligibleForOts else form.otsEligible
        val scheme = if (isNpaOts) form.npaOtsScheme else form.otsScheme
        val outstanding = if (isNpaOts) form.npaOtsOutstanding else data.outstandingAmount
        val settlement = if (isNpaOts) form.npaOtsTotalSettlement else form.otsTotalSettlement
        val borrowerShare = if (isNpaOts) form.npaOtsPayableAmount else form.otsPayableAmount
        val deposit = if (isNpaOts) form.npaOtsRequiredDeposit else form.otsRequiredDeposit
        val customerResponse = if (isNpaOts) form.npaOtsBorrowerResponse else form.otsCustomerResponse

        // KYC and renewal fields (from NPA OTS or main CKCC fields)
        val kycComplete = if (isNpaOts) false else form.ckccKycComplete
        val aadhaarSeeded = if (isNpaOts) false else form.ckccAadhaarSeeded
        val mobileLinked = if (isNpaOts) false else form.ckccMobileLinked
        val aadhaarAuth = if (isNpaOts) false else form.ckccAadhaarAuthCompleted
        val willingToRenew = if (isNpaOts) false else form.ckccWillingToRenew

        val schemes = if (isNpaOts) VisitFormData.NPA_OTS_SCHEMES else VisitFormData.OTS_SCHEMES
        val schemeDisplay = schemes.firstOrNull { it.first == scheme }?.second ?: scheme

        val responses = if (isNpaOts) VisitFormData.NPA_OTS_BORROWER_RESPONSES else VisitFormData.OTS_CUSTOMER_RESPONSES
        val responseDisplay = responses.firstOrNull { it.first == customerResponse }?.second ?: customerResponse

        return """<table>
<tr><th class="section-header" colspan="4">SECTION 4: KRM OTS DETAILS (IF APPLICABLE)</th></tr>
<tr>
    <td class="label">OTS Eligibility</td><td>${yesNo(eligible)}</td>
    <td class="label">Applicable Scheme</td><td>${esc(schemeDisplay)}</td>
</tr>
<tr>
    <td class="label">Outstanding Amount</td><td>${formatAmount(outstanding)}</td>
    <td class="label">Proposed Settlement</td><td>${formatAmount(settlement)}</td>
</tr>
<tr>
    <td class="label">Borrower's Share</td><td>${formatAmount(borrowerShare)}</td>
    <td class="label">Initial Deposit Required</td><td>${formatAmount(deposit)}</td>
</tr>
<tr>
    <td class="label">Customer Response</td><td colspan="3">${esc(responseDisplay)}</td>
</tr>
<tr>
    <td class="label">KYC Status</td><td>${if (kycComplete) "Complete" else "Pending"}</td>
    <td class="label">Aadhaar Seeded</td><td>${yesNo(aadhaarSeeded)}</td>
</tr>
<tr>
    <td class="label">Mobile Linked</td><td>${yesNo(mobileLinked)}</td>
    <td class="label">Aadhaar Authentication</td><td>${if (aadhaarAuth) "Completed" else "Pending"}</td>
</tr>
<tr>
    <td class="label">Renewal Consent</td><td colspan="3">Borrower Willing to Renew: ${yesNo(willingToRenew)}</td>
</tr>
</table>
"""
    }

    private fun section6PhysicalVerification(form: VisitFormData): String {
        val addressStatus = when {
            form.shifted -> "Shifted"
            form.sameAddress -> "Same"
            else -> "-"
        }
        val residenceDisplay = VisitFormData.RESIDENCE_VERIFICATION
            .firstOrNull { it.first == form.residenceVerified }?.second ?: form.residenceVerified
        val neighbourDisplay = VisitFormData.NEIGHBOUR_VERIFICATION
            .firstOrNull { it.first == form.neighbourVerification }?.second ?: form.neighbourVerification

        val sb = StringBuilder()
        sb.append("""<table>
<tr><th class="section-header" colspan="4">SECTION 6: PHYSICAL VERIFICATION</th></tr>
<tr>
    <td class="label">Borrower Met</td><td>${yesNo(form.customerMet)}</td>
    <td class="label">Family Member Met</td><td>${yesNo(form.familyMemberMet)}</td>
</tr>
<tr>
    <td class="label">House Locked</td><td>${yesNo(form.houseLocked)}</td>
    <td class="label">Borrower Alive</td><td>${yesNo(form.borrowerAlive)}</td>
</tr>
<tr>
    <td class="label">Current Address</td><td>${esc(addressStatus)}</td>
    <td class="label">Mobile Contacted</td><td>${yesNo(form.phoneContact)}</td>
</tr>
<tr>
    <td class="label">Residence Verification</td><td>${esc(residenceDisplay)}</td>
    <td class="label">Neighbour Verification</td><td>${esc(neighbourDisplay)}</td>
</tr>
<tr>
    <td class="label">Current Occupation</td>
    <td colspan="3" class="checkbox">""")

        for ((value, label) in VisitFormData.OCCUPATIONS) {
            val icon = if (form.occupation == value) CHECKED else UNCHECKED
            sb.append("$icon $label &nbsp; ")
        }

        sb.append("""</td>
</tr>
</table>
""")
        return sb.toString()
    }

    private fun section8Observations(form: VisitFormData): String {
        val observation = when (form.reportType) {
            VisitFormData.REPORT_CKCC_NPA_OTS -> form.npaOtsObservation
            VisitFormData.REPORT_CKCC_OD -> form.ckccOdObservation
            VisitFormData.REPORT_CKCC -> form.ckccObservation
            else -> form.remarks
        }
        return """<table>
<tr><th class="section-header" colspan="1">SECTION 8: BC SUPERVISOR / DRA OBSERVATIONS</th></tr>
<tr>
    <td style="min-height:60px; padding:8px;">${esc(observation)}</td>
</tr>
</table>
"""
    }

    private fun section9Recommendation(form: VisitFormData): String {
        val isOtsType = form.reportType == VisitFormData.REPORT_OTS ||
            form.reportType == VisitFormData.REPORT_CKCC_NPA_OTS

        val recProposal: Boolean
        val recFollowup: Boolean
        val recRefused: Boolean
        val recNotEligible: Boolean

        if (form.reportType == VisitFormData.REPORT_CKCC_NPA_OTS) {
            recProposal = form.npaOtsRecProposalRecommended
            recFollowup = form.npaOtsRecFollowupRequired
            recRefused = form.npaOtsRecCustomerRefused
            recNotEligible = form.npaOtsRecNotEligible
        } else {
            recProposal = form.otsRecProposalRecommended
            recFollowup = form.otsRecFollowupRequired
            recRefused = form.otsRecCustomerRefused
            recNotEligible = form.otsRecNotEligible
        }

        val sb = StringBuilder()
        sb.append("""<table>
<tr><th class="section-header" colspan="4">SECTION 9: RECOMMENDATION</th></tr>""")

        if (isOtsType) {
            sb.append("""
<tr>
    <td class="label">KRM OTS</td>
    <td colspan="3" class="checkbox">
        ${cb(recProposal)} OTS Proposal Recommended &nbsp;
        ${cb(recFollowup)} Follow-up Required &nbsp;
        ${cb(recRefused)} Customer Refused &nbsp;
        ${cb(recNotEligible)} Not Eligible
    </td>
</tr>""")
        }

        sb.append("""
<tr>
    <td class="label">General Recommendation</td>
    <td colspan="3">${esc(form.generalRecommendation)}</td>
</tr>
</table>
""")
        return sb.toString()
    }

    private fun section11Declaration(): String = """<table>
<tr><th class="section-header" colspan="1">SECTION 11: DECLARATION</th></tr>
<tr>
    <td class="declaration-text">
        I hereby declare and certify that:<br>
        (1) I have personally visited the borrower at their residence/business address as mentioned above.<br>
        (2) All information recorded in this report is true, accurate, and collected during my own field visit.<br>
        (3) I have conducted this visit in compliance with: RBI's Fair Practices Code, Bank's Recovery Policy, Indian Banks' Association Code of Conduct, and all applicable guidelines for field verification.<br>
        (4) I have not used any coercive, threatening, or abusive language during the visit.<br>
        (5) I have maintained the privacy and dignity of the borrower.<br>
        (6) I understand that any false information may result in disciplinary action.
    </td>
</tr>
</table>
"""

    private fun section12Certification(form: VisitFormData, data: SupplementaryData): String = """<table>
<tr><th class="section-header" colspan="4">SECTION 12: CERTIFICATION</th></tr>
<tr>
    <td class="label" colspan="2">BC Supervisor</td>
    <td class="label" colspan="2">BC Supervisor Verification</td>
</tr>
<tr>
    <td class="label">Signature</td>
    <td><span class="sig-line">&nbsp;</span></td>
    <td class="label">Name</td>
    <td>${esc(data.bcSupervisorName)}</td>
</tr>
<tr>
    <td class="label">Date</td>
    <td>${esc(form.visitDate)}</td>
    <td class="label">DRA ID</td>
    <td>${esc(form.bcbfCode.ifBlank { data.bcCode })}</td>
</tr>
<tr>
    <td colspan="2">&nbsp;</td>
    <td class="label">Date</td>
    <td>${esc(form.visitDate)}</td>
</tr>
<tr>
    <td colspan="2">&nbsp;</td>
    <td class="label">Signature</td>
    <td><span class="sig-line">&nbsp;</span></td>
</tr>
</table>
<p class="note"><strong>Important Note:</strong> This is a system-generated report based on field visit data collected by the authorized BC Supervisor/DRA. The report must be verified and countersigned by the supervising authority.</p>
"""

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private fun esc(value: String): String =
        value.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")

    private fun yesNo(value: Boolean): String = if (value) "Yes" else "No"

    private fun cb(checked: Boolean): String = if (checked) CHECKED else UNCHECKED

    /**
     * Formats an amount string using Indian grouping via [Formatters.money].
     * Falls back to the raw string when the value cannot be parsed.
     */
    private fun formatAmount(raw: String): String {
        if (raw.isBlank()) return "-"
        val cleaned = raw.replace(",", "").replace("\u20B9", "").trim()
        val parsed = cleaned.toDoubleOrNull() ?: return esc(raw)
        return "\u20B9" + Formatters.money(parsed)
    }
}
