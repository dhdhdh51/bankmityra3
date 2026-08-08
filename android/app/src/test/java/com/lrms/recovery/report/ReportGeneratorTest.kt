package com.lrms.recovery.report

import com.lrms.recovery.domain.VisitFormData
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [ReportGenerator].
 *
 * Verifies that the generated HTML contains the expected section headers,
 * naming conventions, and field values, and does NOT contain deprecated
 * terminology or a borrower signature section.
 */
class ReportGeneratorTest {

    private fun sampleData() = ReportGenerator.SupplementaryData(
        borrowerName = "Ramesh Kumar",
        fatherHusbandName = "Suresh Kumar",
        loanAccountNumber = "123456789012",
        mobile = "9876543210",
        alternateMobile = "9876543211",
        aadhaarLast4 = "1234",
        branchName = "Jaipur Main",
        branchCode = "BR001",
        regionalOffice = "Rajasthan RO",
        zone = "Western Zone",
        cbcName = "Jaipur CBC",
        bcSupervisorName = "Mahesh Sharma",
        bcCode = "BC-1001",
        linkedBranch = "Jaipur City",
        district = "Jaipur",
        iibfNumber = "IIBF-2024-001",
        outstandingAmount = "450000",
        overdueAmount = "125000",
        npaDate = "2024-01-15",
    )

    private fun sampleForm() = VisitFormData(loanAccountId = 1).apply {
        reportType = VisitFormData.REPORT_CKCC_NPA_OTS
        visitDate = "2025-01-20"
        visitTime = "14:30"
        village = "Sanganer"
        bcbfCode = "BCBF-5001"
        gender = "male"
        dateOfBirth = "1985-06-15"
        panNumber = "ABCDE1234F"
        addrVillage = "Sanganer"
        gramPanchayat = "Sanganer GP"
        tehsil = "Sanganer"
        addrDistrict = "Jaipur"
        state = "Rajasthan"
        pinCode = "302029"
        cifNumber = "CIF-001"
        loanType = "ckcc"
        sanctionDate = "2020-04-01"
        sanctionLimit = "500000"
        drawingPower = "450000"
        interestOverdue = "25000"
        assetClassification = "npa"
        customerMet = true
        borrowerAlive = true
        sameAddress = true
        residenceVerified = "confirmed"
        neighbourVerification = "conducted"
        occupation = "agriculture"
        npaOtsEligibleForOts = true
        npaOtsScheme = "krm_ots"
        npaOtsOutstanding = "450000"
        npaOtsTotalSettlement = "350000"
        npaOtsPayableAmount = "100000"
        npaOtsRequiredDeposit = "10000"
        npaOtsBorrowerResponse = "accepted"
        npaOtsObservation = "Borrower cooperative and willing to settle."
        npaOtsRecProposalRecommended = true
        generalRecommendation = "Proceed with OTS settlement"
        declarationAccepted = true
    }

    @Test
    fun `html contains company name`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("D2 RECOVERY SOLUTIONS"))
    }

    @Test
    fun `html contains report title`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("FIELD VISIT VERIFICATION REPORT"))
    }

    @Test
    fun `html uses BC Supervisor not BC Agent`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("BC Supervisor"))
        assertFalse(html.contains("BC Agent"))
    }

    @Test
    fun `html uses BCBF Code not Employee ID`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("BCBF Code"))
        assertFalse(html.contains("Employee ID"))
    }

    @Test
    fun `html does not contain Borrower Signature`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertFalse(html.contains("Borrower Signature"))
    }

    @Test
    fun `html contains all section headers`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("SECTION 1: GENERAL INFORMATION"))
        assertTrue(html.contains("SECTION 2: BORROWER INFORMATION"))
        assertTrue(html.contains("SECTION 3: LOAN ACCOUNT DETAILS"))
        assertTrue(html.contains("SECTION 4: KRM OTS DETAILS"))
        assertTrue(html.contains("SECTION 6: PHYSICAL VERIFICATION"))
        assertTrue(html.contains("SECTION 8: BC SUPERVISOR / DRA OBSERVATIONS"))
        assertTrue(html.contains("SECTION 9: RECOMMENDATION"))
        assertTrue(html.contains("SECTION 11: DECLARATION"))
        assertTrue(html.contains("SECTION 12: CERTIFICATION"))
    }

    @Test
    fun `html contains borrower name from supplementary data`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("Ramesh Kumar"))
    }

    @Test
    fun `html contains declaration text`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("I hereby declare and certify that"))
        assertTrue(html.contains("personally visited the borrower"))
        assertTrue(html.contains("RBI's Fair Practices Code"))
    }

    @Test
    fun `html contains supervisor name in header`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        // The supervisor name should appear in the header block
        assertTrue(html.contains("Mahesh Sharma"))
    }

    @Test
    fun `html contains BCBF code in header`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("BCBF-5001"))
    }

    @Test
    fun `html contains A4 page styling`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("@page"))
        assertTrue(html.contains("size: A4"))
        assertTrue(html.contains("margin: 15mm"))
    }

    @Test
    fun `html contains unicode checkbox characters`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        // Should contain at least one checked and one unchecked ballot box
        assertTrue(html.contains("&#9745;"))  // checked
        assertTrue(html.contains("&#9744;"))  // unchecked
    }

    @Test
    fun `html contains formatted amounts with rupee symbol`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        // Outstanding amount 450000 should be formatted as Indian grouping
        assertTrue(html.contains("\u20B9"))
        assertTrue(html.contains("4,50,000"))
    }

    @Test
    fun `html contains important note about system-generated report`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("system-generated report"))
        assertTrue(html.contains("BC Supervisor/DRA"))
    }

    @Test
    fun `OTS section not included for recovery report type`() {
        val form = sampleForm().apply { reportType = VisitFormData.REPORT_RECOVERY }
        val html = ReportGenerator.generate(form, sampleData())
        assertFalse(html.contains("SECTION 4: KRM OTS DETAILS"))
    }

    @Test
    fun `OTS section included for OTS report type`() {
        val form = sampleForm().apply { reportType = VisitFormData.REPORT_OTS }
        val html = ReportGenerator.generate(form, sampleData())
        assertTrue(html.contains("SECTION 4: KRM OTS DETAILS"))
    }

    @Test
    fun `html contains observation text`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("Borrower cooperative and willing to settle"))
    }

    @Test
    fun `html contains general recommendation`() {
        val html = ReportGenerator.generate(sampleForm(), sampleData())
        assertTrue(html.contains("Proceed with OTS settlement"))
    }

    @Test
    fun `html escapes special characters`() {
        val data = sampleData().copy(borrowerName = "Ram <script>alert('x')</script>")
        val html = ReportGenerator.generate(sampleForm(), data)
        assertFalse(html.contains("<script>"))
        assertTrue(html.contains("&lt;script&gt;"))
    }
}
