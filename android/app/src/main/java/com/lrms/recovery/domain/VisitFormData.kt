package com.lrms.recovery.domain

import java.io.File
import java.util.UUID

/**
 * The Digital BC Field Visit Report as the app collects it.
 *
 * Field names map one-to-one onto the API contract, so [toFieldMap] is the single
 * place the wire format is defined. The class is a plain data holder and is
 * covered by unit tests - it deliberately has no Android dependencies.
 *
 * [clientUuid] is generated once when the form is opened. The server treats it as
 * an idempotency key, so a retry after a dropped connection returns the original
 * report instead of filing a duplicate visit.
 */
class VisitFormData(
    val loanAccountId: Int,
) {


    /**
     * Which of the three field reports this is. Chosen from a dropdown at the top
     * of the form; the extra sections appear only for the type selected, so a
     * plain recovery call is not buried under sixty settlement fields.
     */
    var reportType: String = REPORT_RECOVERY

    /** Free text, and only meaningful when [reportType] is [REPORT_OTHER]. */
    var reportTypeOtherText: String = ""

    // ---- General -----------------------------------------------------------
    var visitDate: String = ""
    var visitTime: String = ""
    var village: String = ""
    var bcbfCode: String = ""

    // ---- 2. Borrower information -------------------------------------------
    // The identity fields the printed form asks for that the borrower record does not
    // already hold. All optional: an agent who cannot get a date of birth at the door
    // must still be able to file the visit that happened.
    var gender: String = ""
    var dateOfBirth: String = ""
    var panNumber: String = ""
    var addrVillage: String = ""
    var gramPanchayat: String = ""
    var tehsil: String = ""
    var addrDistrict: String = ""
    var state: String = ""
    var pinCode: String = ""

    // ---- 3. Loan account details -------------------------------------------
    // Pre-filled from the account where the bank's export supplied a figure, so the
    // agent confirms rather than copies a passbook by hand - and can correct what the
    // export got wrong.
    var cifNumber: String = ""
    var loanType: String = ""
    var loanTypeOtherText: String = ""
    var sanctionDate: String = ""
    var sanctionLimit: String = ""
    var drawingPower: String = ""
    var interestOverdue: String = ""
    var assetClassification: String = ""

    // ---- Customer contact --------------------------------------------------
    var customerMet: Boolean = false
    var familyMemberMet: Boolean = false
    var houseLocked: Boolean = false
    var phoneContact: Boolean = false
    var phoneSwitchedOff: Boolean = false
    var familyMemberName: String = ""
    var familyMemberRelationship: String = ""

    // ---- Physical verification ---------------------------------------------
    var borrowerAlive: Boolean = true
    var sameAddress: Boolean = true
    var shifted: Boolean = false

    /**
     * Both blank until answered, and blank is sent as nothing at all.
     *
     * "Not confirmed" is an assertion about a check somebody ran; silence is not, and a
     * form that recorded silence as a negative would accuse an agent of failing a check
     * nobody asked them for.
     */
    var residenceVerified: String = ""
    var neighbourVerification: String = ""

    var occupation: String = ""
    var occupationOtherText: String = ""

    // ---- 7. Documents verified ---------------------------------------------
    // What the borrower physically produced. Asked on EVERY case type: this checklist
    // used to sit inside the renewal section, so a recovery visit had nowhere to record
    // that an Aadhaar card was shown at all.
    var docAadhaar: Boolean = false
    var docPan: Boolean = false
    var docPassbook: Boolean = false
    var docLandRecord: Boolean = false
    var docKhatauni: Boolean = false
    var docElectricityBill: Boolean = false
    var docPhotograph: Boolean = false
    var docMobileVerified: Boolean = false
    var docRenewalForm: Boolean = false
    var docOtsConsentLetter: Boolean = false
    var docOthers: Boolean = false
    var docOtherText: String = ""

    // ---- 10. Evidence attached ---------------------------------------------
    // What the agent SAYS is attached, recorded separately from the files that actually
    // arrive. The gap between the two is the point.
    var evBorrowerPhoto: Boolean = false
    var evHousePhoto: Boolean = false
    var evLandPhoto: Boolean = false
    var evAadhaarCopy: Boolean = false
    var evPassbookCopy: Boolean = false
    var evGpsLocation: Boolean = false
    var evRenewalForm: Boolean = false
    var evOtsConsent: Boolean = false
    var evOthers: Boolean = false
    var evOtherText: String = ""

    // ---- 11. Declaration ---------------------------------------------------
    /**
     * Whether the agent accepted the RBI / Fair Practices Code declaration.
     *
     * Required before a submit, and stored rather than assumed: the declaration is
     * printed in full on every copy of the report, and a printed certification nobody
     * agreed to is worth nothing.
     */
    var declarationAccepted: Boolean = false

    // ---- Recovery possibility ----------------------------------------------
    var readyToPay: Boolean = false
    var notReady: Boolean = false
    var interestPayment: Boolean = false
    var ots: Boolean = false
    var promiseAmount: String = ""
    var promiseDate: String = ""

    // ---- Non-payment reason ------------------------------------------------
    var reasonFinancialProblem: Boolean = false
    var reasonCropLoss: Boolean = false
    var reasonAnimalLoss: Boolean = false
    var reasonIllness: Boolean = false
    var reasonUnemployment: Boolean = false
    var reasonDispute: Boolean = false
    var reasonOtherLoan: Boolean = false
    var reasonOthers: Boolean = false
    var reasonOtherText: String = ""

    // ---- Agent recommendation ----------------------------------------------
    var recRecoveryPossible: Boolean = false
    var recRegularFollowup: Boolean = false
    var recLegalAction: Boolean = false
    var recRc: Boolean = false
    var recOts: Boolean = false
    var recOthers: Boolean = false
    var recOtherText: String = ""

    /**
     * Section 9's free-prose box.
     *
     * Separate from [remarks], because the form asks two different questions: what was
     * seen, and what should be done about it. One field could not say which an answer was.
     */
    var generalRecommendation: String = ""

    // ---- Remarks -----------------------------------------------------------
    var remarks: String = ""

    // ---- Documents ---------------------------------------------------------
    var customerPhoto: File? = null
    var housePhoto: File? = null
    var aadhaarPhoto: File? = null
    /**
     * The agent's own photograph, taken at the door.
     *
     * Not the portrait on their user record: that one was uploaded once in a branch
     * office and proves only that they have a face. This one carries the fix that says
     * where they were standing, which is what a disputed visit actually turns on.
     */
    var agentPhoto: File? = null
    var otherDocuments: MutableList<File> = mutableListOf()

    // ---- KRM / OTS settlement (report_type = ots) ---------------------------
    var otsEligible: Boolean = false
    var otsScheme: String = ""
    var otsSchemeOtherText: String = ""
    var otsReliefPercent: String = ""
    var otsRlbAmount: String = ""
    var otsPayablePercent: String = DEFAULT_PAYABLE_PERCENT
    var otsPayableAmount: String = ""
    var otsTotalSettlement: String = ""
    var otsDepositPercent: String = DEFAULT_DEPOSIT_PERCENT
    var otsRequiredDeposit: String = ""
    var otsDepositReceived: Boolean = false
    var otsDepositAmount: String = ""
    var otsDepositDate: String = ""
    var otsDepositReference: String = ""
    var otsBalancePayable: String = ""
    var otsFinalPaymentDate: String = ""
    var otsApprovalStatus: String = OTS_STATUS_PENDING
    var otsValidityFrom: String = ""
    var otsValidityTo: String = ""
    var otsExpectedClosureDate: String = ""
    var otsBorrowerAccepted: Boolean = false
    /**
     * WHY the borrower answered as they did, which the boolean above cannot carry.
     *
     * "Asked for time" and "refused outright" both leave [otsBorrowerAccepted] false and
     * lead to entirely different next actions - another visit, a different scheme, or a
     * closed file.
     */
    var otsCustomerResponse: String = ""
    var otsRejectionReason: String = ""
    /** When they say they will deposit, as against [otsDepositDate], when they did. */
    var otsExpectedDepositDate: String = ""

    var otsRecProposalRecommended: Boolean = false
    var otsRecFollowupRequired: Boolean = false
    var otsRecCustomerRefused: Boolean = false
    var otsRecNotEligible: Boolean = false

    var otsStCustomerContacted: Boolean = false
    var otsStCustomerVerified: Boolean = false
    var otsStOtsAccepted: Boolean = false
    var otsStOtsRejected: Boolean = false
    var otsStInitialDepositReceived: Boolean = false
    var otsStOtsClosed: Boolean = false
    var otsStFollowupRequired: Boolean = false

    // ---- CKCC OD-2 renewal (report_type = ckcc_renewal) ---------------------
    var ckccCifNumber: String = ""
    var ckccSanctionDate: String = ""
    var ckccSanctionLimit: String = ""
    var ckccDrawingPower: String = ""
    var ckccOutstanding: String = ""
    var ckccInterestOverdue: String = ""
    var ckccRenewalDueDate: String = ""
    var ckccEligibleForRenewal: Boolean = false
    var ckccKycComplete: Boolean = false
    var ckccAadhaarSeeded: Boolean = false
    var ckccMobileLinked: Boolean = false
    var ckccAadhaarAuthCompleted: Boolean = false
    // The document checklist is NOT here any more - it is section 7 on the main form,
    // asked once for every case type. Two copies meant a renewal report answered the
    // same eleven boxes twice and could disagree with itself.
    var ckccWillingToRenew: Boolean = false
    var ckccDocumentsHandedOver: Boolean = false
    var ckccRenewalFormSigned: Boolean = false
    var ckccEkycCompleted: Boolean = false
    var ckccBiometricsCompleted: Boolean = false
    var ckccObservation: String = ""
    var ckccRecRenewImmediately: Boolean = false
    var ckccRecDocumentsSubmitted: Boolean = false
    /** The other half of "documents complete": one missing paper is a branch task. */
    var ckccRecPendingDocuments: Boolean = false
    var ckccRecFollowupRequired: Boolean = false
    var ckccRecNotInterested: Boolean = false
    var ckccRecBranchContactUrgent: Boolean = false
    var ckccRecOthers: Boolean = false
    var ckccRecOtherText: String = ""
    var ckccStCustomerContacted: Boolean = false
    var ckccStCustomerVerified: Boolean = false
    var ckccStDocumentsCollected: Boolean = false
    var ckccStApplicationSubmitted: Boolean = false
    var ckccStCkccRenewed: Boolean = false
    var ckccStPendingAtBranch: Boolean = false
    var ckccStFollowupRequired: Boolean = false
    var ckccStBecameNpa: Boolean = false

    // ---- CKCC OD field report (report_type = ckcc_od) -----------------------
    var ckccOdCifNumber: String = ""
    var ckccOdSanctionDate: String = ""
    var ckccOdSanctionLimit: String = ""
    var ckccOdDrawingPower: String = ""
    var ckccOdOutstanding: String = ""
    var ckccOdInterestOverdue: String = ""
    var ckccOdOdLimit: String = ""
    var ckccOdOdUtilization: String = ""
    var ckccOdLastCreditDate: String = ""
    var ckccOdEligibleForRenewal: Boolean = false
    var ckccOdKycComplete: Boolean = false
    var ckccOdAadhaarSeeded: Boolean = false
    var ckccOdMobileLinked: Boolean = false
    var ckccOdAadhaarAuth: Boolean = false
    var ckccOdWillingToRenew: Boolean = false
    var ckccOdDocumentsHandedOver: Boolean = false
    var ckccOdRenewalFormSigned: Boolean = false
    var ckccOdEkycCompleted: Boolean = false
    var ckccOdBiometricsCompleted: Boolean = false
    var ckccOdObservation: String = ""
    var ckccOdRecRenewImmediately: Boolean = false
    var ckccOdRecDocumentsSubmitted: Boolean = false
    var ckccOdRecPendingDocuments: Boolean = false
    var ckccOdRecFollowupRequired: Boolean = false
    var ckccOdRecNotInterested: Boolean = false
    var ckccOdRecBranchContactUrgent: Boolean = false
    var ckccOdRecOthers: Boolean = false
    var ckccOdRecOtherText: String = ""
    var ckccOdStCustomerContacted: Boolean = false
    var ckccOdStCustomerVerified: Boolean = false
    var ckccOdStDocumentsCollected: Boolean = false
    var ckccOdStApplicationSubmitted: Boolean = false
    var ckccOdStRenewed: Boolean = false
    var ckccOdStPendingAtBranch: Boolean = false
    var ckccOdStFollowupRequired: Boolean = false
    var ckccOdStBecameNpa: Boolean = false

    // ---- CKCC NPA KRM OTS scheme (report_type = ckcc_npa_ots) ---------------
    var npaOtsCifNumber: String = ""
    var npaOtsSanctionDate: String = ""
    var npaOtsSanctionLimit: String = ""
    var npaOtsDrawingPower: String = ""
    var npaOtsOutstanding: String = ""
    var npaOtsInterestOverdue: String = ""
    var npaOtsNpaDate: String = ""
    var npaOtsDaysPastDue: String = ""
    var npaOtsAssetClassification: String = ""
    var npaOtsEligibleForOts: Boolean = false
    var npaOtsScheme: String = ""
    var npaOtsSchemeOtherText: String = ""
    var npaOtsReliefPercent: String = ""
    var npaOtsRlbAmount: String = ""
    var npaOtsPayablePercent: String = ""
    var npaOtsPayableAmount: String = ""
    var npaOtsTotalSettlement: String = ""
    var npaOtsDepositPercent: String = ""
    var npaOtsRequiredDeposit: String = ""
    var npaOtsDepositReceived: Boolean = false
    var npaOtsDepositAmount: String = ""
    var npaOtsDepositDate: String = ""
    var npaOtsDepositReference: String = ""
    var npaOtsBalancePayable: String = ""
    var npaOtsFinalPaymentDate: String = ""
    var npaOtsApprovalStatus: String = ""
    var npaOtsValidityFrom: String = ""
    var npaOtsValidityTo: String = ""
    var npaOtsExpectedClosureDate: String = ""
    var npaOtsBorrowerResponse: String = ""
    var npaOtsRejectionReason: String = ""
    var npaOtsObservation: String = ""
    var npaOtsRecProposalRecommended: Boolean = false
    var npaOtsRecFollowupRequired: Boolean = false
    var npaOtsRecCustomerRefused: Boolean = false
    var npaOtsRecNotEligible: Boolean = false
    var npaOtsStCustomerContacted: Boolean = false
    var npaOtsStCustomerVerified: Boolean = false
    var npaOtsStOtsAccepted: Boolean = false
    var npaOtsStOtsRejected: Boolean = false
    var npaOtsStInitialDepositReceived: Boolean = false
    var npaOtsStOtsClosed: Boolean = false
    var npaOtsStFollowupRequired: Boolean = false

    // ---- Declaration -------------------------------------------------------
    var spCbcName: String = ""

    // ---- Extra evidence (CKCC) ---------------------------------------------
    var landPhoto: File? = null
    var passbookPhoto: File? = null
    var renewalFormPhoto: File? = null

    // ---- Where the report was filed ----------------------------------------
    /**
     * The agent's position when the report was completed, and per-photo positions
     * for the slots the camera filled.
     *
     * [gpsSource] carries `device`, `denied` or `unavailable` and is always sent,
     * because "we asked and were refused" and "there was no signal" are different
     * facts and the server records them differently. Sending nothing at all would
     * leave the report unable to distinguish either from an older app that never
     * collected a position.
     *
     * The photo stamps are separate from the report's own position on purpose: a
     * gallery-picked image has none, and inheriting the report's would assert that
     * an unknown photo was taken at the doorstep.
     */
    var gpsLatitude: Double? = null
    var gpsLongitude: Double? = null
    var gpsAccuracyMetres: Int? = null
    var gpsCapturedAt: String = ""
    var gpsSource: String = "unavailable"

    /** Slot name (`customer`, `house`, `aadhaar`) to "lat,lng,accuracyOrBlank". */
    var photoStamps: MutableMap<String, String> = mutableMapOf()

    /**
     * Slot name to "camera" or "gallery".
     *
     * Sent for every attached photograph, independent of whether it has coordinates.
     * The server stores it as photos.capture_source, which is what lets a printed
     * report distinguish a doorstep photograph from a gallery pick instead of
     * labelling every image "unknown".
     */
    var photoSources: MutableMap<String, String> = mutableMapOf()

    // ---- Meta --------------------------------------------------------------
    val clientUuid: String = UUID.randomUUID().toString()
    var appVersion: String = ""
    var deviceInfo: String = ""


    /**
     * Creates a shallow copy of this instance with optional field overrides.
     *
     * This replaces the compiler-generated data-class copy(), which is not available
     * because the constructor exceeds the JVM 255-parameter limit. Only the fields
     * that tests or callers actually override are exposed as parameters here.
     *
     * WARNING - MAINTENANCE REQUIRED:
     * When you add a new field to this class, you MUST also:
     *   1. Add a `clone.newField = this.newField` line in the body below.
     *   2. If tests need to override it, add it as a parameter to this method.
     * There is no compiler enforcement. Forgetting step 1 means copy() silently
     * drops the field's value, and tests that use copy() will pass on stale data.
     */
    fun copy(
        visitDate: String = this.visitDate,
        visitTime: String = this.visitTime,
        customerMet: Boolean = this.customerMet,
        familyMemberMet: Boolean = this.familyMemberMet,
        familyMemberName: String = this.familyMemberName,
        houseLocked: Boolean = this.houseLocked,
        phoneContact: Boolean = this.phoneContact,
        phoneSwitchedOff: Boolean = this.phoneSwitchedOff,
        promiseAmount: String = this.promiseAmount,
        promiseDate: String = this.promiseDate,
        village: String = this.village,
        remarks: String = this.remarks,
        occupation: String = this.occupation,
        occupationOtherText: String = this.occupationOtherText,
        reasonOthers: Boolean = this.reasonOthers,
        reasonOtherText: String = this.reasonOtherText,
        reasonCropLoss: Boolean = this.reasonCropLoss,
        recOthers: Boolean = this.recOthers,
        recOtherText: String = this.recOtherText,
        reportType: String = this.reportType,
        ckccOdObservation: String = this.ckccOdObservation,
        npaOtsObservation: String = this.npaOtsObservation,
    ): VisitFormData {
        val clone = VisitFormData(loanAccountId = this.loanAccountId)
        // Copy every mutable field from this instance
        clone.reportType = reportType
        clone.reportTypeOtherText = this.reportTypeOtherText
        clone.visitDate = visitDate
        clone.visitTime = visitTime
        clone.village = village
        clone.bcbfCode = this.bcbfCode
        clone.gender = this.gender
        clone.dateOfBirth = this.dateOfBirth
        clone.panNumber = this.panNumber
        clone.addrVillage = this.addrVillage
        clone.gramPanchayat = this.gramPanchayat
        clone.tehsil = this.tehsil
        clone.addrDistrict = this.addrDistrict
        clone.state = this.state
        clone.pinCode = this.pinCode
        clone.cifNumber = this.cifNumber
        clone.loanType = this.loanType
        clone.loanTypeOtherText = this.loanTypeOtherText
        clone.sanctionDate = this.sanctionDate
        clone.sanctionLimit = this.sanctionLimit
        clone.drawingPower = this.drawingPower
        clone.interestOverdue = this.interestOverdue
        clone.assetClassification = this.assetClassification
        clone.customerMet = customerMet
        clone.familyMemberMet = familyMemberMet
        clone.houseLocked = houseLocked
        clone.phoneContact = phoneContact
        clone.phoneSwitchedOff = phoneSwitchedOff
        clone.familyMemberName = familyMemberName
        clone.familyMemberRelationship = this.familyMemberRelationship
        clone.borrowerAlive = this.borrowerAlive
        clone.sameAddress = this.sameAddress
        clone.shifted = this.shifted
        clone.residenceVerified = this.residenceVerified
        clone.neighbourVerification = this.neighbourVerification
        clone.occupation = occupation
        clone.occupationOtherText = occupationOtherText
        clone.docAadhaar = this.docAadhaar
        clone.docPan = this.docPan
        clone.docPassbook = this.docPassbook
        clone.docLandRecord = this.docLandRecord
        clone.docKhatauni = this.docKhatauni
        clone.docElectricityBill = this.docElectricityBill
        clone.docPhotograph = this.docPhotograph
        clone.docMobileVerified = this.docMobileVerified
        clone.docRenewalForm = this.docRenewalForm
        clone.docOtsConsentLetter = this.docOtsConsentLetter
        clone.docOthers = this.docOthers
        clone.docOtherText = this.docOtherText
        clone.evBorrowerPhoto = this.evBorrowerPhoto
        clone.evHousePhoto = this.evHousePhoto
        clone.evLandPhoto = this.evLandPhoto
        clone.evAadhaarCopy = this.evAadhaarCopy
        clone.evPassbookCopy = this.evPassbookCopy
        clone.evGpsLocation = this.evGpsLocation
        clone.evRenewalForm = this.evRenewalForm
        clone.evOtsConsent = this.evOtsConsent
        clone.evOthers = this.evOthers
        clone.evOtherText = this.evOtherText
        clone.declarationAccepted = this.declarationAccepted
        clone.readyToPay = this.readyToPay
        clone.notReady = this.notReady
        clone.interestPayment = this.interestPayment
        clone.ots = this.ots
        clone.promiseAmount = promiseAmount
        clone.promiseDate = promiseDate
        clone.reasonFinancialProblem = this.reasonFinancialProblem
        clone.reasonCropLoss = reasonCropLoss
        clone.reasonAnimalLoss = this.reasonAnimalLoss
        clone.reasonIllness = this.reasonIllness
        clone.reasonUnemployment = this.reasonUnemployment
        clone.reasonDispute = this.reasonDispute
        clone.reasonOtherLoan = this.reasonOtherLoan
        clone.reasonOthers = reasonOthers
        clone.reasonOtherText = reasonOtherText
        clone.recRecoveryPossible = this.recRecoveryPossible
        clone.recRegularFollowup = this.recRegularFollowup
        clone.recLegalAction = this.recLegalAction
        clone.recRc = this.recRc
        clone.recOts = this.recOts
        clone.recOthers = recOthers
        clone.recOtherText = recOtherText
        clone.generalRecommendation = this.generalRecommendation
        clone.remarks = remarks
        clone.customerPhoto = this.customerPhoto
        clone.housePhoto = this.housePhoto
        clone.aadhaarPhoto = this.aadhaarPhoto
        clone.agentPhoto = this.agentPhoto
        clone.otherDocuments = this.otherDocuments.toMutableList()
        clone.otsEligible = this.otsEligible
        clone.otsScheme = this.otsScheme
        clone.otsSchemeOtherText = this.otsSchemeOtherText
        clone.otsReliefPercent = this.otsReliefPercent
        clone.otsRlbAmount = this.otsRlbAmount
        clone.otsPayablePercent = this.otsPayablePercent
        clone.otsPayableAmount = this.otsPayableAmount
        clone.otsTotalSettlement = this.otsTotalSettlement
        clone.otsDepositPercent = this.otsDepositPercent
        clone.otsRequiredDeposit = this.otsRequiredDeposit
        clone.otsDepositReceived = this.otsDepositReceived
        clone.otsDepositAmount = this.otsDepositAmount
        clone.otsDepositDate = this.otsDepositDate
        clone.otsDepositReference = this.otsDepositReference
        clone.otsBalancePayable = this.otsBalancePayable
        clone.otsFinalPaymentDate = this.otsFinalPaymentDate
        clone.otsApprovalStatus = this.otsApprovalStatus
        clone.otsValidityFrom = this.otsValidityFrom
        clone.otsValidityTo = this.otsValidityTo
        clone.otsExpectedClosureDate = this.otsExpectedClosureDate
        clone.otsBorrowerAccepted = this.otsBorrowerAccepted
        clone.otsCustomerResponse = this.otsCustomerResponse
        clone.otsRejectionReason = this.otsRejectionReason
        clone.otsExpectedDepositDate = this.otsExpectedDepositDate
        clone.otsRecProposalRecommended = this.otsRecProposalRecommended
        clone.otsRecFollowupRequired = this.otsRecFollowupRequired
        clone.otsRecCustomerRefused = this.otsRecCustomerRefused
        clone.otsRecNotEligible = this.otsRecNotEligible
        clone.otsStCustomerContacted = this.otsStCustomerContacted
        clone.otsStCustomerVerified = this.otsStCustomerVerified
        clone.otsStOtsAccepted = this.otsStOtsAccepted
        clone.otsStOtsRejected = this.otsStOtsRejected
        clone.otsStInitialDepositReceived = this.otsStInitialDepositReceived
        clone.otsStOtsClosed = this.otsStOtsClosed
        clone.otsStFollowupRequired = this.otsStFollowupRequired
        clone.ckccCifNumber = this.ckccCifNumber
        clone.ckccSanctionDate = this.ckccSanctionDate
        clone.ckccSanctionLimit = this.ckccSanctionLimit
        clone.ckccDrawingPower = this.ckccDrawingPower
        clone.ckccOutstanding = this.ckccOutstanding
        clone.ckccInterestOverdue = this.ckccInterestOverdue
        clone.ckccRenewalDueDate = this.ckccRenewalDueDate
        clone.ckccEligibleForRenewal = this.ckccEligibleForRenewal
        clone.ckccKycComplete = this.ckccKycComplete
        clone.ckccAadhaarSeeded = this.ckccAadhaarSeeded
        clone.ckccMobileLinked = this.ckccMobileLinked
        clone.ckccAadhaarAuthCompleted = this.ckccAadhaarAuthCompleted
        clone.ckccWillingToRenew = this.ckccWillingToRenew
        clone.ckccDocumentsHandedOver = this.ckccDocumentsHandedOver
        clone.ckccRenewalFormSigned = this.ckccRenewalFormSigned
        clone.ckccEkycCompleted = this.ckccEkycCompleted
        clone.ckccBiometricsCompleted = this.ckccBiometricsCompleted
        clone.ckccObservation = this.ckccObservation
        clone.ckccRecRenewImmediately = this.ckccRecRenewImmediately
        clone.ckccRecDocumentsSubmitted = this.ckccRecDocumentsSubmitted
        clone.ckccRecPendingDocuments = this.ckccRecPendingDocuments
        clone.ckccRecFollowupRequired = this.ckccRecFollowupRequired
        clone.ckccRecNotInterested = this.ckccRecNotInterested
        clone.ckccRecBranchContactUrgent = this.ckccRecBranchContactUrgent
        clone.ckccRecOthers = this.ckccRecOthers
        clone.ckccRecOtherText = this.ckccRecOtherText
        clone.ckccStCustomerContacted = this.ckccStCustomerContacted
        clone.ckccStCustomerVerified = this.ckccStCustomerVerified
        clone.ckccStDocumentsCollected = this.ckccStDocumentsCollected
        clone.ckccStApplicationSubmitted = this.ckccStApplicationSubmitted
        clone.ckccStCkccRenewed = this.ckccStCkccRenewed
        clone.ckccStPendingAtBranch = this.ckccStPendingAtBranch
        clone.ckccStFollowupRequired = this.ckccStFollowupRequired
        clone.ckccStBecameNpa = this.ckccStBecameNpa
        clone.ckccOdCifNumber = this.ckccOdCifNumber
        clone.ckccOdSanctionDate = this.ckccOdSanctionDate
        clone.ckccOdSanctionLimit = this.ckccOdSanctionLimit
        clone.ckccOdDrawingPower = this.ckccOdDrawingPower
        clone.ckccOdOutstanding = this.ckccOdOutstanding
        clone.ckccOdInterestOverdue = this.ckccOdInterestOverdue
        clone.ckccOdOdLimit = this.ckccOdOdLimit
        clone.ckccOdOdUtilization = this.ckccOdOdUtilization
        clone.ckccOdLastCreditDate = this.ckccOdLastCreditDate
        clone.ckccOdEligibleForRenewal = this.ckccOdEligibleForRenewal
        clone.ckccOdKycComplete = this.ckccOdKycComplete
        clone.ckccOdAadhaarSeeded = this.ckccOdAadhaarSeeded
        clone.ckccOdMobileLinked = this.ckccOdMobileLinked
        clone.ckccOdAadhaarAuth = this.ckccOdAadhaarAuth
        clone.ckccOdWillingToRenew = this.ckccOdWillingToRenew
        clone.ckccOdDocumentsHandedOver = this.ckccOdDocumentsHandedOver
        clone.ckccOdRenewalFormSigned = this.ckccOdRenewalFormSigned
        clone.ckccOdEkycCompleted = this.ckccOdEkycCompleted
        clone.ckccOdBiometricsCompleted = this.ckccOdBiometricsCompleted
        clone.ckccOdObservation = ckccOdObservation
        clone.ckccOdRecRenewImmediately = this.ckccOdRecRenewImmediately
        clone.ckccOdRecDocumentsSubmitted = this.ckccOdRecDocumentsSubmitted
        clone.ckccOdRecPendingDocuments = this.ckccOdRecPendingDocuments
        clone.ckccOdRecFollowupRequired = this.ckccOdRecFollowupRequired
        clone.ckccOdRecNotInterested = this.ckccOdRecNotInterested
        clone.ckccOdRecBranchContactUrgent = this.ckccOdRecBranchContactUrgent
        clone.ckccOdRecOthers = this.ckccOdRecOthers
        clone.ckccOdRecOtherText = this.ckccOdRecOtherText
        clone.ckccOdStCustomerContacted = this.ckccOdStCustomerContacted
        clone.ckccOdStCustomerVerified = this.ckccOdStCustomerVerified
        clone.ckccOdStDocumentsCollected = this.ckccOdStDocumentsCollected
        clone.ckccOdStApplicationSubmitted = this.ckccOdStApplicationSubmitted
        clone.ckccOdStRenewed = this.ckccOdStRenewed
        clone.ckccOdStPendingAtBranch = this.ckccOdStPendingAtBranch
        clone.ckccOdStFollowupRequired = this.ckccOdStFollowupRequired
        clone.ckccOdStBecameNpa = this.ckccOdStBecameNpa
        clone.npaOtsCifNumber = this.npaOtsCifNumber
        clone.npaOtsSanctionDate = this.npaOtsSanctionDate
        clone.npaOtsSanctionLimit = this.npaOtsSanctionLimit
        clone.npaOtsDrawingPower = this.npaOtsDrawingPower
        clone.npaOtsOutstanding = this.npaOtsOutstanding
        clone.npaOtsInterestOverdue = this.npaOtsInterestOverdue
        clone.npaOtsNpaDate = this.npaOtsNpaDate
        clone.npaOtsDaysPastDue = this.npaOtsDaysPastDue
        clone.npaOtsAssetClassification = this.npaOtsAssetClassification
        clone.npaOtsEligibleForOts = this.npaOtsEligibleForOts
        clone.npaOtsScheme = this.npaOtsScheme
        clone.npaOtsSchemeOtherText = this.npaOtsSchemeOtherText
        clone.npaOtsReliefPercent = this.npaOtsReliefPercent
        clone.npaOtsRlbAmount = this.npaOtsRlbAmount
        clone.npaOtsPayablePercent = this.npaOtsPayablePercent
        clone.npaOtsPayableAmount = this.npaOtsPayableAmount
        clone.npaOtsTotalSettlement = this.npaOtsTotalSettlement
        clone.npaOtsDepositPercent = this.npaOtsDepositPercent
        clone.npaOtsRequiredDeposit = this.npaOtsRequiredDeposit
        clone.npaOtsDepositReceived = this.npaOtsDepositReceived
        clone.npaOtsDepositAmount = this.npaOtsDepositAmount
        clone.npaOtsDepositDate = this.npaOtsDepositDate
        clone.npaOtsDepositReference = this.npaOtsDepositReference
        clone.npaOtsBalancePayable = this.npaOtsBalancePayable
        clone.npaOtsFinalPaymentDate = this.npaOtsFinalPaymentDate
        clone.npaOtsApprovalStatus = this.npaOtsApprovalStatus
        clone.npaOtsValidityFrom = this.npaOtsValidityFrom
        clone.npaOtsValidityTo = this.npaOtsValidityTo
        clone.npaOtsExpectedClosureDate = this.npaOtsExpectedClosureDate
        clone.npaOtsBorrowerResponse = this.npaOtsBorrowerResponse
        clone.npaOtsRejectionReason = this.npaOtsRejectionReason
        clone.npaOtsObservation = npaOtsObservation
        clone.npaOtsRecProposalRecommended = this.npaOtsRecProposalRecommended
        clone.npaOtsRecFollowupRequired = this.npaOtsRecFollowupRequired
        clone.npaOtsRecCustomerRefused = this.npaOtsRecCustomerRefused
        clone.npaOtsRecNotEligible = this.npaOtsRecNotEligible
        clone.npaOtsStCustomerContacted = this.npaOtsStCustomerContacted
        clone.npaOtsStCustomerVerified = this.npaOtsStCustomerVerified
        clone.npaOtsStOtsAccepted = this.npaOtsStOtsAccepted
        clone.npaOtsStOtsRejected = this.npaOtsStOtsRejected
        clone.npaOtsStInitialDepositReceived = this.npaOtsStInitialDepositReceived
        clone.npaOtsStOtsClosed = this.npaOtsStOtsClosed
        clone.npaOtsStFollowupRequired = this.npaOtsStFollowupRequired
        clone.spCbcName = this.spCbcName
        clone.landPhoto = this.landPhoto
        clone.passbookPhoto = this.passbookPhoto
        clone.renewalFormPhoto = this.renewalFormPhoto
        clone.gpsLatitude = this.gpsLatitude
        clone.gpsLongitude = this.gpsLongitude
        clone.gpsAccuracyMetres = this.gpsAccuracyMetres
        clone.gpsCapturedAt = this.gpsCapturedAt
        clone.gpsSource = this.gpsSource
        clone.photoStamps = this.photoStamps.toMutableMap()
        clone.photoSources = this.photoSources.toMutableMap()
        clone.appVersion = this.appVersion
        clone.deviceInfo = this.deviceInfo
        return clone
    }

    /**
     * Validates the report before submission.
     *
     * @return field name to message. Empty when the form is ready to send.
     */
    fun validate(): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (visitDate.isBlank()) {
            errors["visit_date"] = "Select the visit date"
        }
        if (visitTime.isBlank()) {
            errors["visit_time"] = "Select the visit time"
        }

        // At least one contact outcome must be recorded, otherwise the report
        // says nothing about what happened at the door.
        val anyContact = customerMet || familyMemberMet || houseLocked ||
            phoneContact || phoneSwitchedOff
        if (!anyContact) {
            errors["contact"] = "Select how the visit went"
        }

        if (familyMemberMet && familyMemberName.isBlank()) {
            errors["family_member_name"] = "Enter the family member's name"
        }

        // A promise is only a promise with both an amount and a date. Half a
        // promise would silently fail to create a promise case on the server.
        val amount = promiseAmountValue()
        val hasAmount = amount != null && amount > 0.0
        val hasDate = promiseDate.isNotBlank()

        if (hasAmount && !hasDate) {
            errors["promise_date"] = "Select the promised payment date"
        }
        if (hasDate && !hasAmount) {
            errors["promise_amount"] = "Enter the promised amount"
        }
        if (promiseAmount.isNotBlank() && amount == null) {
            errors["promise_amount"] = "Enter a valid amount"
        }

        if (reasonOthers && reasonOtherText.isBlank()) {
            errors["reason_other_text"] = "Describe the reason"
        }
        if (recOthers && recOtherText.isBlank()) {
            errors["rec_other_text"] = "Describe the recommendation"
        }
        if (occupation == OCCUPATION_OTHERS && occupationOtherText.isBlank()) {
            errors["occupation_other_text"] = "Specify the occupation"
        }

        if (docOthers && docOtherText.isBlank()) {
            errors["doc_other_text"] = "Name the other document"
        }
        if (evOthers && evOtherText.isBlank()) {
            errors["ev_other_text"] = "Name the other evidence"
        }
        if (reportType == REPORT_OTHER && reportTypeOtherText.isBlank()) {
            errors["report_type_other_text"] = "Describe the case type"
        }
        if (loanType == LOAN_TYPE_OTHER && loanTypeOtherText.isBlank()) {
            errors["loan_type_other_text"] = "Describe the loan type"
        }

        // Checked on the way in rather than left to the server, because a PAN typed
        // wrong is not a validation nuisance - it is a tax identifier attached to the
        // wrong person, and the agent is standing next to the card while they type it.
        if (panNumber.isNotBlank() && !isValidPan(panNumber)) {
            errors["pan_number"] = "A PAN is five letters, four digits and one letter"
        }
        if (pinCode.isNotBlank() && !Regex("^\\d{6}$").matches(pinCode.trim())) {
            errors["pin_code"] = "A PIN code is six digits"
        }

        for ((key, raw) in listOf(
            "sanction_limit" to sanctionLimit,
            "drawing_power" to drawingPower,
            "interest_overdue" to interestOverdue,
        )) {
            if (raw.isNotBlank() && number(raw) == null) {
                errors[key] = "Enter a valid amount"
            }
        }

        // The declaration is the last thing and a hard stop. Everything above it can be
        // left blank and the report is still worth filing; a report submitted by somebody
        // who did not certify it is a document that says something they never said.
        if (!declarationAccepted) {
            errors["declaration_accepted"] = "Tick the declaration before submitting"
        }

        errors += validateOts()
        errors += validateCkcc()
        errors += validateCkccOd()
        errors += validateCkccNpaOts()

        return errors
    }

    /** The shape of a PAN: five letters, four digits, one letter. */
    fun isValidPan(raw: String): Boolean =
        Regex("^[A-Za-z]{5}[0-9]{4}[A-Za-z]$")
            .matches(raw.replace(" ", "").replace("-", "").trim())

    /**
     * Settlement rules.
     *
     * Deliberately narrow: the branch's sanction letter is the authority, so this
     * refuses only combinations that are self-contradictory or that would record a
     * deposit nobody can trace. It never rejects a figure for failing to match our
     * own arithmetic.
     */
    private fun validateOts(): Map<String, String> {
        if (reportType != REPORT_OTS) return emptyMap()
        val errors = mutableMapOf<String, String>()

        if (otsEligible && otsScheme.isBlank()) {
            errors["ots_scheme"] = "Select the applicable scheme"
        }
        if (otsScheme == OTS_SCHEME_OTHER && otsSchemeOtherText.isBlank()) {
            errors["ots_scheme_other_text"] = "Name the scheme"
        }
        for ((key, raw) in listOf(
            "ots_relief_percent" to otsReliefPercent,
            "ots_payable_percent" to otsPayablePercent,
            "ots_deposit_percent" to otsDepositPercent,
        )) {
            val value = number(raw) ?: continue
            if (value < 0.0 || value > 100.0) {
                errors[key] = "Enter a percentage between 0 and 100"
            }
        }
        for ((key, raw) in listOf(
            "ots_rlb_amount" to otsRlbAmount,
            "ots_payable_amount" to otsPayableAmount,
            "ots_total_settlement" to otsTotalSettlement,
            "ots_deposit_amount" to otsDepositAmount,
        )) {
            if (raw.isNotBlank() && number(raw) == null) {
                errors[key] = "Enter a valid amount"
            }
        }

        // A deposit that cannot be traced back to a bank receipt is not evidence
        // of anything - and this system never collects money itself.
        if (otsDepositReceived) {
            if (number(otsDepositAmount).let { it == null || it <= 0.0 }) {
                errors["ots_deposit_amount"] = "Enter the amount the borrower deposited"
            }
            if (otsDepositDate.isBlank()) {
                errors["ots_deposit_date"] = "Enter the deposit date"
            }
            if (otsDepositReference.isBlank()) {
                errors["ots_deposit_reference"] = "Enter the bank receipt or transaction number"
            }
        }

        if (!otsBorrowerAccepted && otsRejectionReason.isBlank()) {
            errors["ots_rejection_reason"] = "Record why the borrower did not accept"
        }
        if (otsValidityFrom.isNotBlank() && otsValidityTo.isNotBlank() &&
            otsValidityTo < otsValidityFrom
        ) {
            errors["ots_validity_to"] = "The validity end cannot be before the start"
        }

        return errors
    }

    private fun validateCkcc(): Map<String, String> {
        if (reportType != REPORT_CKCC) return emptyMap()
        val errors = mutableMapOf<String, String>()

        // Without this date the report cannot say when the account turns NPA,
        // which is the entire reason a renewal report exists.
        if (ckccRenewalDueDate.isBlank()) {
            errors["ckcc_renewal_due_date"] = "Enter the CKCC renewal due date"
        }
        if (ckccRecOthers && ckccRecOtherText.isBlank()) {
            errors["ckcc_rec_other_text"] = "Describe the recommendation"
        }
        if (ckccRenewalFormSigned && !ckccWillingToRenew) {
            errors["ckcc_willing_to_renew"] =
                "A signed renewal form means the borrower is willing - tick that too"
        }
        for ((key, raw) in listOf(
            "ckcc_sanction_limit" to ckccSanctionLimit,
            "ckcc_drawing_power" to ckccDrawingPower,
            "ckcc_interest_overdue" to ckccInterestOverdue,
        )) {
            if (raw.isNotBlank() && number(raw) == null) {
                errors[key] = "Enter a valid amount"
            }
        }

        return errors
    }

    private fun validateCkccOd(): Map<String, String> {
        if (reportType != REPORT_CKCC_OD) return emptyMap()
        val errors = mutableMapOf<String, String>()

        if (ckccOdRecOthers && ckccOdRecOtherText.isBlank()) {
            errors["ckcc_od_rec_other_text"] = "Describe the recommendation"
        }
        for ((key, raw) in listOf(
            "ckcc_od_sanction_limit" to ckccOdSanctionLimit,
            "ckcc_od_drawing_power" to ckccOdDrawingPower,
            "ckcc_od_interest_overdue" to ckccOdInterestOverdue,
            "ckcc_od_od_limit" to ckccOdOdLimit,
            "ckcc_od_od_utilization" to ckccOdOdUtilization,
        )) {
            if (raw.isNotBlank() && number(raw) == null) {
                errors[key] = "Enter a valid amount"
            }
        }

        return errors
    }

    private fun validateCkccNpaOts(): Map<String, String> {
        if (reportType != REPORT_CKCC_NPA_OTS) return emptyMap()
        val errors = mutableMapOf<String, String>()

        if (npaOtsEligibleForOts && npaOtsScheme.isBlank()) {
            errors["npa_ots_scheme"] = "Select the applicable scheme"
        }
        if (npaOtsScheme == OTS_SCHEME_OTHER && npaOtsSchemeOtherText.isBlank()) {
            errors["npa_ots_scheme_other_text"] = "Name the scheme"
        }
        for ((key, raw) in listOf(
            "npa_ots_relief_percent" to npaOtsReliefPercent,
            "npa_ots_payable_percent" to npaOtsPayablePercent,
            "npa_ots_deposit_percent" to npaOtsDepositPercent,
        )) {
            val value = number(raw) ?: continue
            if (value < 0.0 || value > 100.0) {
                errors[key] = "Enter a percentage between 0 and 100"
            }
        }
        for ((key, raw) in listOf(
            "npa_ots_sanction_limit" to npaOtsSanctionLimit,
            "npa_ots_drawing_power" to npaOtsDrawingPower,
            "npa_ots_outstanding" to npaOtsOutstanding,
            "npa_ots_interest_overdue" to npaOtsInterestOverdue,
            "npa_ots_rlb_amount" to npaOtsRlbAmount,
            "npa_ots_payable_amount" to npaOtsPayableAmount,
            "npa_ots_total_settlement" to npaOtsTotalSettlement,
            "npa_ots_deposit_amount" to npaOtsDepositAmount,
            "npa_ots_required_deposit" to npaOtsRequiredDeposit,
            "npa_ots_balance_payable" to npaOtsBalancePayable,
        )) {
            if (raw.isNotBlank() && number(raw) == null) {
                errors[key] = "Enter a valid amount"
            }
        }

        if (npaOtsDepositReceived) {
            if (number(npaOtsDepositAmount).let { it == null || it <= 0.0 }) {
                errors["npa_ots_deposit_amount"] = "Enter the amount the borrower deposited"
            }
            if (npaOtsDepositDate.isBlank()) {
                errors["npa_ots_deposit_date"] = "Enter the deposit date"
            }
            if (npaOtsDepositReference.isBlank()) {
                errors["npa_ots_deposit_reference"] = "Enter the bank receipt or transaction number"
            }
        }

        if (npaOtsValidityFrom.isNotBlank() && npaOtsValidityTo.isNotBlank() &&
            npaOtsValidityTo < npaOtsValidityFrom
        ) {
            errors["npa_ots_validity_to"] = "The validity end cannot be before the start"
        }

        return errors
    }

    // -----------------------------------------------------------------------
    // Live suggestions
    //
    // These only ever fill a blank field or update a field the agent has not
    // edited. Nothing here overwrites a typed figure: the sanction letter wins,
    // and a form that silently rewrites a settlement amount under the agent's
    // hands would be worse than one that suggests nothing.
    // -----------------------------------------------------------------------

    /**
     * Relief and the borrower's share are the two halves of the same 100%, which is
     * how the worked example runs: 77.50% waived, 22.50% payable. Offered as a hint
     * only - a scheme could define them independently.
     */
    fun suggestedReliefPercent(): Double? {
        val payable = number(otsPayablePercent) ?: return null
        if (payable < 0.0 || payable > 100.0) return null
        return round2(100.0 - payable)
    }

    /** payable = RLB x payable_percent. Null when either input is missing. */
    fun suggestedPayable(): Double? {
        val rlb = number(otsRlbAmount) ?: return null
        val percent = number(otsPayablePercent) ?: return null
        return round2(rlb * percent / 100.0)
    }

    /** The initial deposit the scheme requires, as a share of the payable amount. */
    fun suggestedRequiredDeposit(): Double? {
        val payable = number(otsPayableAmount) ?: suggestedPayable() ?: return null
        val percent = number(otsDepositPercent) ?: return null
        return round2(payable * percent / 100.0)
    }

    /** What is still owed after the deposit already made. */
    fun suggestedBalancePayable(): Double? {
        val total = number(otsTotalSettlement) ?: number(otsPayableAmount) ?: return null
        val paid = number(otsDepositAmount) ?: 0.0
        return round2((total - paid).coerceAtLeast(0.0))
    }

    /**
     * Days between today and the renewal deadline, negative once overdue.
     *
     * Shown as a hint only. The stored value is computed on the server, because a
     * device with a wrong clock must not be able to write a misleading deadline
     * into a report a branch acts on.
     */
    fun daysToRenewal(today: java.time.LocalDate = java.time.LocalDate.now()): Long? {
        val due = runCatching { java.time.LocalDate.parse(ckccRenewalDueDate) }.getOrNull()
            ?: return null
        return java.time.temporal.ChronoUnit.DAYS.between(today, due)
    }

    /** The bucket the renewal falls in, matching the server's enum. */
    fun renewalBucket(today: java.time.LocalDate = java.time.LocalDate.now()): String? =
        when (val days = daysToRenewal(today)) {
            null -> null
            else -> when {
                days < 0 -> "overdue"
                days <= 7 -> "within_7"
                days <= 15 -> "within_15"
                else -> "within_30"
            }
        }

    /** Expected NPA date if the renewal is not completed: the day after the deadline. */
    fun expectedNpaDate(): String? =
        runCatching { java.time.LocalDate.parse(ckccRenewalDueDate).plusDays(1).toString() }
            .getOrNull()

    /** Parsed promise amount, tolerating grouped input like "12,500". */
    fun promiseAmountValue(): Double? {
        val cleaned = promiseAmount.replace(",", "").replace("\u20B9", "").trim()
        if (cleaned.isEmpty()) return null
        return cleaned.toDoubleOrNull()
    }

    /** True when this report will create a promise case. */
    fun createsPromise(): Boolean {
        val amount = promiseAmountValue()
        return amount != null && amount > 0.0 && promiseDate.isNotBlank()
    }

    /**
     * Serialises the form to the multipart field map the API expects.
     * Booleans go as "1"/"0"; blank optional fields are omitted entirely.
     */
    fun toFieldMap(): Map<String, String> {
        val fields = linkedMapOf<String, String>()

        fields["loan_account_id"] = loanAccountId.toString()
        fields["visit_date"] = visitDate
        fields["visit_time"] = visitTime
        fields["client_uuid"] = clientUuid

        putIfNotBlank(fields, "village", village)
        putIfNotBlank(fields, "bcbf_code", bcbfCode)

        fields["customer_met"] = bool(customerMet)
        fields["family_member_met"] = bool(familyMemberMet)
        fields["house_locked"] = bool(houseLocked)
        fields["phone_contact"] = bool(phoneContact)
        fields["phone_switched_off"] = bool(phoneSwitchedOff)
        putIfNotBlank(fields, "family_member_name", familyMemberName)
        putIfNotBlank(fields, "family_member_relationship", familyMemberRelationship)

        fields["borrower_alive"] = bool(borrowerAlive)
        fields["same_address"] = bool(sameAddress)
        fields["shifted"] = bool(shifted)
        // Omitted entirely when unanswered, so the server stores NULL rather than a
        // negative nobody asserted.
        putIfNotBlank(fields, "residence_verified", residenceVerified)
        putIfNotBlank(fields, "neighbour_verification", neighbourVerification)
        putIfNotBlank(fields, "occupation", occupation)
        putIfNotBlank(fields, "occupation_other_text", occupationOtherText)

        // ---- 2. Borrower information ---------------------------------------
        putIfNotBlank(fields, "gender", gender)
        putIfNotBlank(fields, "date_of_birth", dateOfBirth)
        // Normalised here so the same card always produces the same stored value,
        // whichever way the agent spaced it out.
        putIfNotBlank(fields, "pan_number", panNumber.replace(" ", "").replace("-", "").uppercase())
        putIfNotBlank(fields, "addr_village", addrVillage)
        putIfNotBlank(fields, "gram_panchayat", gramPanchayat)
        putIfNotBlank(fields, "tehsil", tehsil)
        putIfNotBlank(fields, "addr_district", addrDistrict)
        putIfNotBlank(fields, "state", state)
        putIfNotBlank(fields, "pin_code", pinCode)

        // ---- 3. Loan account details ---------------------------------------
        putIfNotBlank(fields, "cif_number", cifNumber)
        putIfNotBlank(fields, "loan_type", loanType)
        putIfNotBlank(fields, "loan_type_other_text", loanTypeOtherText)
        putIfNotBlank(fields, "sanction_date", sanctionDate)
        putAmount(fields, "sanction_limit", sanctionLimit)
        putAmount(fields, "drawing_power", drawingPower)
        putAmount(fields, "interest_overdue", interestOverdue)
        putIfNotBlank(fields, "asset_classification", assetClassification)

        // ---- 7. Documents verified -----------------------------------------
        fields["doc_aadhaar"] = bool(docAadhaar)
        fields["doc_pan"] = bool(docPan)
        fields["doc_passbook"] = bool(docPassbook)
        fields["doc_land_record"] = bool(docLandRecord)
        fields["doc_khatauni"] = bool(docKhatauni)
        fields["doc_electricity_bill"] = bool(docElectricityBill)
        fields["doc_photograph"] = bool(docPhotograph)
        fields["doc_mobile_verified"] = bool(docMobileVerified)
        fields["doc_renewal_form"] = bool(docRenewalForm)
        fields["doc_ots_consent_letter"] = bool(docOtsConsentLetter)
        fields["doc_others"] = bool(docOthers)
        putIfNotBlank(fields, "doc_other_text", docOtherText)

        // ---- 10. Evidence attached -----------------------------------------
        fields["ev_borrower_photo"] = bool(evBorrowerPhoto)
        fields["ev_house_photo"] = bool(evHousePhoto)
        fields["ev_land_photo"] = bool(evLandPhoto)
        fields["ev_aadhaar_copy"] = bool(evAadhaarCopy)
        fields["ev_passbook_copy"] = bool(evPassbookCopy)
        fields["ev_gps_location"] = bool(evGpsLocation)
        fields["ev_renewal_form"] = bool(evRenewalForm)
        fields["ev_ots_consent"] = bool(evOtsConsent)
        fields["ev_others"] = bool(evOthers)
        putIfNotBlank(fields, "ev_other_text", evOtherText)

        // ---- 11. Declaration -----------------------------------------------
        fields["declaration_accepted"] = bool(declarationAccepted)

        fields["ready_to_pay"] = bool(readyToPay)
        fields["not_ready"] = bool(notReady)
        fields["interest_payment"] = bool(interestPayment)
        fields["ots"] = bool(ots)

        // Only send a promise when it is complete, so the server never has to
        // guess at half a promise.
        if (createsPromise()) {
            fields["promise_amount"] = promiseAmountValue()!!.toString()
            fields["promise_date"] = promiseDate
        }

        fields["reason_financial_problem"] = bool(reasonFinancialProblem)
        fields["reason_crop_loss"] = bool(reasonCropLoss)
        fields["reason_animal_loss"] = bool(reasonAnimalLoss)
        fields["reason_illness"] = bool(reasonIllness)
        fields["reason_unemployment"] = bool(reasonUnemployment)
        fields["reason_dispute"] = bool(reasonDispute)
        fields["reason_other_loan"] = bool(reasonOtherLoan)
        fields["reason_others"] = bool(reasonOthers)
        putIfNotBlank(fields, "reason_other_text", reasonOtherText)

        fields["rec_recovery_possible"] = bool(recRecoveryPossible)
        fields["rec_regular_followup"] = bool(recRegularFollowup)
        fields["rec_legal_action"] = bool(recLegalAction)
        fields["rec_rc"] = bool(recRc)
        fields["rec_ots"] = bool(recOts)
        fields["rec_others"] = bool(recOthers)
        putIfNotBlank(fields, "rec_other_text", recOtherText)
        putIfNotBlank(fields, "general_recommendation", generalRecommendation)

        fields["report_type"] = reportType
        putIfNotBlank(fields, "report_type_other_text", reportTypeOtherText)
        putIfNotBlank(fields, "sp_cbc_name", spCbcName)

        // The detail sections are sent as flat `ots_details[field]` keys. The visit
        // goes up as multipart because it carries photos, and multipart has no
        // nesting - the server accepts either shape.
        if (reportType == REPORT_OTS) {
            fields["ots_details[eligible_for_ots]"] = bool(otsEligible)
            putIfNotBlank(fields, "ots_details[scheme]", otsScheme)
            putIfNotBlank(fields, "ots_details[scheme_other_text]", otsSchemeOtherText)
            putAmount(fields, "ots_details[relief_waiver_percent]", otsReliefPercent)
            putAmount(fields, "ots_details[rlb_amount]", otsRlbAmount)
            putAmount(fields, "ots_details[payable_percent]", otsPayablePercent)
            putAmount(fields, "ots_details[borrower_payable_amount]", otsPayableAmount)
            putAmount(fields, "ots_details[total_settlement_amount]", otsTotalSettlement)
            putAmount(fields, "ots_details[initial_deposit_percent]", otsDepositPercent)
            putAmount(fields, "ots_details[required_deposit_amount]", otsRequiredDeposit)
            fields["ots_details[deposit_received]"] = bool(otsDepositReceived)
            putAmount(fields, "ots_details[deposit_amount]", otsDepositAmount)
            putIfNotBlank(fields, "ots_details[deposit_date]", otsDepositDate)
            putIfNotBlank(fields, "ots_details[deposit_reference]", otsDepositReference)
            putAmount(fields, "ots_details[balance_payable]", otsBalancePayable)
            putIfNotBlank(fields, "ots_details[proposed_final_payment_date]", otsFinalPaymentDate)
            putIfNotBlank(fields, "ots_details[approval_status]", otsApprovalStatus)
            putIfNotBlank(fields, "ots_details[validity_from]", otsValidityFrom)
            putIfNotBlank(fields, "ots_details[validity_to]", otsValidityTo)
            putIfNotBlank(fields, "ots_details[expected_closure_date]", otsExpectedClosureDate)
            fields["ots_details[borrower_accepted]"] = bool(otsBorrowerAccepted)
            putIfNotBlank(fields, "ots_details[customer_response]", otsCustomerResponse)
            putIfNotBlank(fields, "ots_details[rejection_reason]", otsRejectionReason)
            putIfNotBlank(fields, "ots_details[expected_deposit_date]", otsExpectedDepositDate)

            fields["ots_details[rec_proposal_recommended]"] = bool(otsRecProposalRecommended)
            fields["ots_details[rec_followup_required]"] = bool(otsRecFollowupRequired)
            fields["ots_details[rec_customer_refused]"] = bool(otsRecCustomerRefused)
            fields["ots_details[rec_not_eligible]"] = bool(otsRecNotEligible)

            fields["ots_details[st_customer_contacted]"] = bool(otsStCustomerContacted)
            fields["ots_details[st_customer_verified]"] = bool(otsStCustomerVerified)
            fields["ots_details[st_ots_accepted]"] = bool(otsStOtsAccepted)
            fields["ots_details[st_ots_rejected]"] = bool(otsStOtsRejected)
            fields["ots_details[st_initial_deposit_received]"] = bool(otsStInitialDepositReceived)
            fields["ots_details[st_ots_closed]"] = bool(otsStOtsClosed)
            fields["ots_details[st_followup_required]"] = bool(otsStFollowupRequired)
        }

        if (reportType == REPORT_CKCC) {
            putIfNotBlank(fields, "ckcc_details[cif_number]", ckccCifNumber)
            putIfNotBlank(fields, "ckcc_details[sanction_date]", ckccSanctionDate)
            putAmount(fields, "ckcc_details[sanction_limit]", ckccSanctionLimit)
            putAmount(fields, "ckcc_details[drawing_power]", ckccDrawingPower)
            putAmount(fields, "ckcc_details[outstanding_amount]", ckccOutstanding)
            putAmount(fields, "ckcc_details[interest_overdue]", ckccInterestOverdue)
            putIfNotBlank(fields, "ckcc_details[renewal_due_date]", ckccRenewalDueDate)

            fields["ckcc_details[eligible_for_renewal]"] = bool(ckccEligibleForRenewal)
            // The server stores an enum; the form asks it as a single checkbox
            // because "pending" is simply the absence of "complete".
            fields["ckcc_details[kyc_status]"] = if (ckccKycComplete) "complete" else "pending"
            fields["ckcc_details[aadhaar_seeded]"] = bool(ckccAadhaarSeeded)
            fields["ckcc_details[mobile_linked]"] = bool(ckccMobileLinked)
            fields["ckcc_details[aadhaar_auth_completed]"] = bool(ckccAadhaarAuthCompleted)

            // No document checklist here: it goes up as the top-level doc_* fields
            // above, for every case type.
            fields["ckcc_details[willing_to_renew]"] = bool(ckccWillingToRenew)
            fields["ckcc_details[documents_handed_over]"] = bool(ckccDocumentsHandedOver)
            fields["ckcc_details[renewal_form_signed]"] = bool(ckccRenewalFormSigned)
            fields["ckcc_details[ekyc_completed]"] = bool(ckccEkycCompleted)
            fields["ckcc_details[biometrics_completed]"] = bool(ckccBiometricsCompleted)

            putIfNotBlank(fields, "ckcc_details[agent_observation]", ckccObservation)
            fields["ckcc_details[rec_renew_immediately]"] = bool(ckccRecRenewImmediately)
            fields["ckcc_details[rec_documents_submitted]"] = bool(ckccRecDocumentsSubmitted)
            fields["ckcc_details[rec_pending_documents]"] = bool(ckccRecPendingDocuments)
            fields["ckcc_details[rec_followup_required]"] = bool(ckccRecFollowupRequired)
            fields["ckcc_details[rec_not_interested]"] = bool(ckccRecNotInterested)
            fields["ckcc_details[rec_branch_contact_urgent]"] = bool(ckccRecBranchContactUrgent)
            fields["ckcc_details[rec_others]"] = bool(ckccRecOthers)
            putIfNotBlank(fields, "ckcc_details[rec_other_text]", ckccRecOtherText)

            fields["ckcc_details[st_customer_contacted]"] = bool(ckccStCustomerContacted)
            fields["ckcc_details[st_customer_verified]"] = bool(ckccStCustomerVerified)
            fields["ckcc_details[st_documents_collected]"] = bool(ckccStDocumentsCollected)
            fields["ckcc_details[st_application_submitted]"] = bool(ckccStApplicationSubmitted)
            fields["ckcc_details[st_ckcc_renewed]"] = bool(ckccStCkccRenewed)
            fields["ckcc_details[st_pending_at_branch]"] = bool(ckccStPendingAtBranch)
            fields["ckcc_details[st_followup_required]"] = bool(ckccStFollowupRequired)
            fields["ckcc_details[st_became_npa]"] = bool(ckccStBecameNpa)
        }

        if (reportType == REPORT_CKCC_OD) {
            putIfNotBlank(fields, "ckcc_od_details[cif_number]", ckccOdCifNumber)
            putIfNotBlank(fields, "ckcc_od_details[sanction_date]", ckccOdSanctionDate)
            putAmount(fields, "ckcc_od_details[sanction_limit]", ckccOdSanctionLimit)
            putAmount(fields, "ckcc_od_details[drawing_power]", ckccOdDrawingPower)
            putAmount(fields, "ckcc_od_details[outstanding_amount]", ckccOdOutstanding)
            putAmount(fields, "ckcc_od_details[interest_overdue]", ckccOdInterestOverdue)
            putAmount(fields, "ckcc_od_details[od_limit]", ckccOdOdLimit)
            putAmount(fields, "ckcc_od_details[od_utilization]", ckccOdOdUtilization)
            putIfNotBlank(fields, "ckcc_od_details[last_credit_date]", ckccOdLastCreditDate)

            fields["ckcc_od_details[eligible_for_renewal]"] = bool(ckccOdEligibleForRenewal)
            fields["ckcc_od_details[kyc_status]"] = if (ckccOdKycComplete) "complete" else "pending"
            fields["ckcc_od_details[aadhaar_seeded]"] = bool(ckccOdAadhaarSeeded)
            fields["ckcc_od_details[mobile_linked]"] = bool(ckccOdMobileLinked)
            fields["ckcc_od_details[aadhaar_auth_completed]"] = bool(ckccOdAadhaarAuth)

            fields["ckcc_od_details[willing_to_renew]"] = bool(ckccOdWillingToRenew)
            fields["ckcc_od_details[documents_handed_over]"] = bool(ckccOdDocumentsHandedOver)
            fields["ckcc_od_details[renewal_form_signed]"] = bool(ckccOdRenewalFormSigned)
            fields["ckcc_od_details[ekyc_completed]"] = bool(ckccOdEkycCompleted)
            fields["ckcc_od_details[biometrics_completed]"] = bool(ckccOdBiometricsCompleted)

            putIfNotBlank(fields, "ckcc_od_details[agent_observation]", ckccOdObservation)
            fields["ckcc_od_details[rec_renew_immediately]"] = bool(ckccOdRecRenewImmediately)
            fields["ckcc_od_details[rec_documents_submitted]"] = bool(ckccOdRecDocumentsSubmitted)
            fields["ckcc_od_details[rec_pending_documents]"] = bool(ckccOdRecPendingDocuments)
            fields["ckcc_od_details[rec_followup_required]"] = bool(ckccOdRecFollowupRequired)
            fields["ckcc_od_details[rec_not_interested]"] = bool(ckccOdRecNotInterested)
            fields["ckcc_od_details[rec_branch_contact_urgent]"] = bool(ckccOdRecBranchContactUrgent)
            fields["ckcc_od_details[rec_others]"] = bool(ckccOdRecOthers)
            putIfNotBlank(fields, "ckcc_od_details[rec_other_text]", ckccOdRecOtherText)

            fields["ckcc_od_details[st_customer_contacted]"] = bool(ckccOdStCustomerContacted)
            fields["ckcc_od_details[st_customer_verified]"] = bool(ckccOdStCustomerVerified)
            fields["ckcc_od_details[st_documents_collected]"] = bool(ckccOdStDocumentsCollected)
            fields["ckcc_od_details[st_application_submitted]"] = bool(ckccOdStApplicationSubmitted)
            fields["ckcc_od_details[st_renewed]"] = bool(ckccOdStRenewed)
            fields["ckcc_od_details[st_pending_at_branch]"] = bool(ckccOdStPendingAtBranch)
            fields["ckcc_od_details[st_followup_required]"] = bool(ckccOdStFollowupRequired)
            fields["ckcc_od_details[st_became_npa]"] = bool(ckccOdStBecameNpa)
        }

        if (reportType == REPORT_CKCC_NPA_OTS) {
            putIfNotBlank(fields, "npa_ots_details[cif_number]", npaOtsCifNumber)
            putIfNotBlank(fields, "npa_ots_details[sanction_date]", npaOtsSanctionDate)
            putAmount(fields, "npa_ots_details[sanction_limit]", npaOtsSanctionLimit)
            putAmount(fields, "npa_ots_details[drawing_power]", npaOtsDrawingPower)
            putAmount(fields, "npa_ots_details[outstanding_amount]", npaOtsOutstanding)
            putAmount(fields, "npa_ots_details[interest_overdue]", npaOtsInterestOverdue)
            putIfNotBlank(fields, "npa_ots_details[npa_date]", npaOtsNpaDate)
            putIfNotBlank(fields, "npa_ots_details[days_past_due]", npaOtsDaysPastDue)
            putIfNotBlank(fields, "npa_ots_details[asset_classification]", npaOtsAssetClassification)

            fields["npa_ots_details[eligible_for_ots]"] = bool(npaOtsEligibleForOts)
            putIfNotBlank(fields, "npa_ots_details[scheme]", npaOtsScheme)
            putIfNotBlank(fields, "npa_ots_details[scheme_other_text]", npaOtsSchemeOtherText)
            putAmount(fields, "npa_ots_details[relief_percent]", npaOtsReliefPercent)
            putAmount(fields, "npa_ots_details[rlb_amount]", npaOtsRlbAmount)
            putAmount(fields, "npa_ots_details[payable_percent]", npaOtsPayablePercent)
            putAmount(fields, "npa_ots_details[payable_amount]", npaOtsPayableAmount)
            putAmount(fields, "npa_ots_details[total_settlement]", npaOtsTotalSettlement)
            putAmount(fields, "npa_ots_details[deposit_percent]", npaOtsDepositPercent)
            putAmount(fields, "npa_ots_details[required_deposit]", npaOtsRequiredDeposit)
            fields["npa_ots_details[deposit_received]"] = bool(npaOtsDepositReceived)
            putAmount(fields, "npa_ots_details[deposit_amount]", npaOtsDepositAmount)
            putIfNotBlank(fields, "npa_ots_details[deposit_date]", npaOtsDepositDate)
            putIfNotBlank(fields, "npa_ots_details[deposit_reference]", npaOtsDepositReference)
            putAmount(fields, "npa_ots_details[balance_payable]", npaOtsBalancePayable)
            putIfNotBlank(fields, "npa_ots_details[final_payment_date]", npaOtsFinalPaymentDate)
            putIfNotBlank(fields, "npa_ots_details[approval_status]", npaOtsApprovalStatus)
            putIfNotBlank(fields, "npa_ots_details[validity_from]", npaOtsValidityFrom)
            putIfNotBlank(fields, "npa_ots_details[validity_to]", npaOtsValidityTo)
            putIfNotBlank(fields, "npa_ots_details[expected_closure_date]", npaOtsExpectedClosureDate)
            putIfNotBlank(fields, "npa_ots_details[borrower_response]", npaOtsBorrowerResponse)
            putIfNotBlank(fields, "npa_ots_details[rejection_reason]", npaOtsRejectionReason)
            putIfNotBlank(fields, "npa_ots_details[observation]", npaOtsObservation)

            fields["npa_ots_details[rec_proposal_recommended]"] = bool(npaOtsRecProposalRecommended)
            fields["npa_ots_details[rec_followup_required]"] = bool(npaOtsRecFollowupRequired)
            fields["npa_ots_details[rec_customer_refused]"] = bool(npaOtsRecCustomerRefused)
            fields["npa_ots_details[rec_not_eligible]"] = bool(npaOtsRecNotEligible)

            fields["npa_ots_details[st_customer_contacted]"] = bool(npaOtsStCustomerContacted)
            fields["npa_ots_details[st_customer_verified]"] = bool(npaOtsStCustomerVerified)
            fields["npa_ots_details[st_ots_accepted]"] = bool(npaOtsStOtsAccepted)
            fields["npa_ots_details[st_ots_rejected]"] = bool(npaOtsStOtsRejected)
            fields["npa_ots_details[st_initial_deposit_received]"] = bool(npaOtsStInitialDepositReceived)
            fields["npa_ots_details[st_ots_closed]"] = bool(npaOtsStOtsClosed)
            fields["npa_ots_details[st_followup_required]"] = bool(npaOtsStFollowupRequired)
        }

        putIfNotBlank(fields, "remarks", remarks)
        putIfNotBlank(fields, "app_version", appVersion)
        putIfNotBlank(fields, "device_info", deviceInfo)

        // Always sent, even when there is nothing to report, so the server can tell
        // "no fix" apart from "an app that does not collect one".
        fields["gps_source"] = gpsSource

        if (gpsSource == "device" && gpsLatitude != null && gpsLongitude != null) {
            fields["gps_latitude"] = gpsLatitude.toString()
            fields["gps_longitude"] = gpsLongitude.toString()
            gpsAccuracyMetres?.let { fields["gps_accuracy_m"] = it.toString() }
            putIfNotBlank(fields, "gps_captured_at", gpsCapturedAt)
        }

        photoSources.forEach { (slot, source) ->
            if (source.isNotBlank()) {
                fields["${slot}_photo_source"] = source
            }
        }

        photoStamps.forEach { (slot, packed) ->
            val fix = unpackFix(packed) ?: return@forEach

            fields["${slot}_photo_gps_source"] = "camera"
            fields["${slot}_photo_latitude"] = fix.latitude
            fields["${slot}_photo_longitude"] = fix.longitude
            fix.accuracyMetres?.let { fields["${slot}_photo_accuracy_m"] = it }
            // The server records this in photos.captured_at, which held NULL for every
            // photograph ever filed until it was sent. A printed report said where a
            // photograph was taken but never when, so two photographs of the same door
            // an hour apart were indistinguishable.
            fix.capturedAt?.let { fields["${slot}_photo_captured_at"] = it }
        }

        return fields
    }

    /** A fix unpacked from its wire string. */
    private data class UnpackedFix(
        val latitude: String,
        val longitude: String,
        val accuracyMetres: String?,
        val capturedAt: String?,
    )

    /**
     * Reads "lat,lng,accuracyOrBlank,capturedAt" back apart.
     *
     * Returns null unless both coordinates are present, so a half-filled stamp is
     * dropped rather than sent as a position with a missing half. Tolerates a
     * three-part string because that is what earlier builds packed, and a stamp
     * without a capture time is still a usable position.
     */
    private fun unpackFix(packed: String): UnpackedFix? {
        if (packed.isBlank()) return null

        val parts = packed.split(',')
        if (parts.size < 2 || parts[0].isBlank() || parts[1].isBlank()) return null

        return UnpackedFix(
            latitude = parts[0],
            longitude = parts[1],
            accuracyMetres = parts.getOrNull(2)?.takeIf { it.isNotBlank() },
            capturedAt = parts.drop(3).joinToString(",").takeIf { it.isNotBlank() },
        )
    }

    /** Typed photo files keyed by their API multipart field name. */
    fun photoFiles(): Map<String, File> = buildMap {
        customerPhoto?.let { put("customer_photo", it) }
        housePhoto?.let { put("house_photo", it) }
        aadhaarPhoto?.let { put("aadhaar_photo", it) }
        agentPhoto?.let { put("agent_photo", it) }
        landPhoto?.let { put("land_photo", it) }
        passbookPhoto?.let { put("passbook_photo", it) }
        renewalFormPhoto?.let { put("renewal_form_photo", it) }
    }

    fun attachmentCount(): Int = photoFiles().size + otherDocuments.size

    /** True when the user has entered anything worth warning about on exit. */
    fun hasUnsavedInput(): Boolean =
        customerMet || familyMemberMet || houseLocked || phoneContact || phoneSwitchedOff ||
            shifted || !borrowerAlive || !sameAddress ||
            readyToPay || notReady || interestPayment || ots ||
            promiseAmount.isNotBlank() || promiseDate.isNotBlank() ||
            remarks.isNotBlank() || occupation.isNotBlank() ||
            reasonFinancialProblem || reasonCropLoss || reasonAnimalLoss || reasonIllness ||
            reasonUnemployment || reasonDispute || reasonOtherLoan || reasonOthers ||
            recRecoveryPossible || recRegularFollowup || recLegalAction || recRc ||
            recOts || recOthers ||
            reportType != REPORT_RECOVERY ||
            otsRlbAmount.isNotBlank() || otsPayableAmount.isNotBlank() ||
            otsDepositAmount.isNotBlank() || otsScheme.isNotBlank() ||
            ckccRenewalDueDate.isNotBlank() || ckccObservation.isNotBlank() ||
            ckccOdObservation.isNotBlank() || ckccOdOdLimit.isNotBlank() ||
            ckccOdSanctionLimit.isNotBlank() ||
            npaOtsObservation.isNotBlank() || npaOtsRlbAmount.isNotBlank() ||
            npaOtsSanctionLimit.isNotBlank() || npaOtsScheme.isNotBlank() ||
            gender.isNotBlank() || dateOfBirth.isNotBlank() || panNumber.isNotBlank() ||
            addrVillage.isNotBlank() || gramPanchayat.isNotBlank() || tehsil.isNotBlank() ||
            addrDistrict.isNotBlank() || state.isNotBlank() || pinCode.isNotBlank() ||
            cifNumber.isNotBlank() || loanType.isNotBlank() || assetClassification.isNotBlank() ||
            residenceVerified.isNotBlank() || neighbourVerification.isNotBlank() ||
            docAadhaar || docPan || docPassbook || docLandRecord || docKhatauni ||
            docElectricityBill || docPhotograph || docMobileVerified || docRenewalForm ||
            docOtsConsentLetter || docOthers ||
            evBorrowerPhoto || evHousePhoto || evLandPhoto || evAadhaarCopy ||
            evPassbookCopy || evGpsLocation || evRenewalForm || evOtsConsent || evOthers ||
            declarationAccepted || generalRecommendation.isNotBlank() ||
            attachmentCount() > 0

    private fun bool(value: Boolean): String = if (value) "1" else "0"

    /** Parses a money or percentage field, tolerating grouping and a rupee sign. */
    fun number(raw: String): Double? {
        val cleaned = raw.replace(",", "").replace("\u20B9", "").trim()
        if (cleaned.isEmpty()) return null
        return cleaned.toDoubleOrNull()
    }

    private fun round2(value: Double): Double = Math.round(value * 100.0) / 100.0

    /** Sends a numeric field only when it parses, so "abc" never reaches the API. */
    private fun putAmount(target: MutableMap<String, String>, key: String, raw: String) {
        number(raw)?.let { target[key] = it.toString() }
    }

    private fun putIfNotBlank(target: MutableMap<String, String>, key: String, value: String) {
        val trimmed = value.trim()
        if (trimmed.isNotEmpty()) {
            target[key] = trimmed
        }
    }

    companion object {
        const val REPORT_RECOVERY = "recovery"
        const val REPORT_OTS = "ots"
        const val REPORT_CKCC = "ckcc_renewal"
        const val REPORT_CKCC_OD = "ckcc_od"
        const val REPORT_CKCC_NPA_OTS = "ckcc_npa_ots"
        const val REPORT_PRE_NPA = "pre_npa"
        const val REPORT_POST_NPA = "post_npa"
        const val REPORT_OTHER = "other"

        const val OTS_STATUS_PENDING = "pending"
        const val OTS_SCHEME_OTHER = "other"
        const val LOAN_TYPE_OTHER = "other"

        /** Scheme defaults; both stay editable per case. */
        const val DEFAULT_PAYABLE_PERCENT = "22.5"
        const val DEFAULT_DEPOSIT_PERCENT = "10"

        /**
         * The Case Type row on the printed form, in its order.
         *
         * Pre-NPA and Post-NPA verification are ordinary doorstep checks, neither
         * settlement nor renewal work. Before they existed they were filed as plain
         * recovery calls, which made the pre-NPA worklist - the one that exists to stop
         * an account going bad - unbuildable from the reports themselves.
         */
        val REPORT_TYPES = listOf(
            REPORT_OTS to "KRM OTS",
            REPORT_CKCC to "CKCC OD-2 Renewal",
            REPORT_CKCC_OD to "CKCC OD Field Report",
            REPORT_CKCC_NPA_OTS to "CKCC NPA - KRM OTS Scheme",
            REPORT_RECOVERY to "Recovery Follow-up",
            REPORT_PRE_NPA to "Pre-NPA Verification",
            REPORT_POST_NPA to "Post-NPA Verification",
            REPORT_OTHER to "Other",
        )

        val OTS_SCHEMES = listOf(
            "krm_ots" to "KRM OTS",
            "general_ots" to "General OTS",
            OTS_SCHEME_OTHER to "Other",
        )

        val OTS_APPROVAL_STATUSES = listOf(
            "pending" to "Pending",
            "approved" to "Approved",
            "rejected" to "Rejected",
        )

        val NPA_OTS_BORROWER_RESPONSES = listOf(
            "accepted" to "Accepted OTS",
            "requested_time" to "Requested Time",
            "financial_difficulty" to "Financial Difficulty",
            "refused" to "Refused OTS",
            "not_available" to "Not Available",
        )

        val NPA_OTS_SCHEMES = listOf(
            "krm_ots" to "KRM OTS",
            "general_ots" to "General OTS",
            OTS_SCHEME_OTHER to "Other",
        )

        /** Section 4's Customer Response row. */
        val OTS_CUSTOMER_RESPONSES = listOf(
            "agreed" to "Agreed for OTS",
            "requested_time" to "Requested Time",
            "financial_difficulty" to "Financial Difficulty",
            "refused" to "Refused OTS",
            "not_eligible" to "Not Eligible",
        )

        val GENDERS = listOf(
            "male" to "Male",
            "female" to "Female",
            "other" to "Other",
        )

        /** Section 3's Loan Type row. */
        val LOAN_TYPES = listOf(
            "ckcc" to "CKCC",
            "agri_term" to "Agriculture Term Loan",
            "od" to "OD",
            "cc" to "CC",
            "msme" to "MSME",
            "housing" to "Housing",
            LOAN_TYPE_OTHER to "Other",
        )

        val ASSET_CLASSIFICATIONS = listOf(
            "standard" to "Standard",
            "sma_0" to "SMA-0",
            "sma_1" to "SMA-1",
            "sma_2" to "SMA-2",
            "npa" to "NPA",
        )

        val RESIDENCE_VERIFICATION = listOf(
            "confirmed" to "Confirmed",
            "not_confirmed" to "Not Confirmed",
        )

        val NEIGHBOUR_VERIFICATION = listOf(
            "conducted" to "Conducted",
            "not_conducted" to "Not Conducted",
        )

        const val OCCUPATION_OTHERS = "others"

        /**
         * The occupation enum, matching the server.
         *
         * 'service' rather than 'job': the printed form says Service, and in this
         * context the two words mean the same thing while only one of them is
         * distinguishable from Labour at a glance.
         */
        val OCCUPATIONS = listOf(
            "agriculture" to "Agriculture",
            "dairy" to "Dairy",
            "business" to "Business",
            "labour" to "Labour",
            "service" to "Service",
            OCCUPATION_OTHERS to "Other",
        )
    }
}
