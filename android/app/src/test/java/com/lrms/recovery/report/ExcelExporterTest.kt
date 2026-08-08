package com.lrms.recovery.report

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [ExcelExporter].
 *
 * Verifies CSV generation, field label mapping, and proper escaping of
 * special characters.
 */
class ExcelExporterTest {

    @Test
    fun `generateCsv produces header row`() {
        val csv = ExcelExporter.generateCsv(emptyMap())
        val lines = csv.lines().filter { it.isNotBlank() }
        assertEquals("Field,Value", lines.first())
    }

    @Test
    fun `generateCsv includes all fields`() {
        val fields = linkedMapOf(
            "visit_date" to "2024-12-15",
            "visit_time" to "10:30",
            "customer_met" to "true",
        )
        val csv = ExcelExporter.generateCsv(fields)
        val lines = csv.lines().filter { it.isNotBlank() }

        // Header + 3 data rows
        assertEquals(4, lines.size)
        assertTrue(lines[1].contains("Visit Date"))
        assertTrue(lines[1].contains("2024-12-15"))
        assertTrue(lines[2].contains("Visit Time"))
        assertTrue(lines[2].contains("10:30"))
        assertTrue(lines[3].contains("Borrower Met"))
        assertTrue(lines[3].contains("true"))
    }

    @Test
    fun `labelFor converts known keys to human-readable labels`() {
        assertEquals("Visit Date", ExcelExporter.labelFor("visit_date"))
        assertEquals("Borrower Met", ExcelExporter.labelFor("customer_met"))
        assertEquals("BCBF Code", ExcelExporter.labelFor("bcbf_code"))
        assertEquals("General Recommendation", ExcelExporter.labelFor("general_recommendation"))
        assertEquals("OTS Eligibility", ExcelExporter.labelFor("ots_eligible"))
    }

    @Test
    fun `labelFor falls back to formatted key for unknown fields`() {
        val label = ExcelExporter.labelFor("some_unknown_field")
        assertEquals("Some unknown field", label)
    }

    @Test
    fun `csvEscape wraps values containing commas in quotes`() {
        val result = ExcelExporter.csvEscape("Jaipur, Rajasthan")
        assertEquals("\"Jaipur, Rajasthan\"", result)
    }

    @Test
    fun `csvEscape wraps values containing quotes and doubles them`() {
        val result = ExcelExporter.csvEscape("He said \"hello\"")
        assertEquals("\"He said \"\"hello\"\"\"", result)
    }

    @Test
    fun `csvEscape wraps values containing newlines`() {
        val result = ExcelExporter.csvEscape("Line 1\nLine 2")
        assertEquals("\"Line 1\nLine 2\"", result)
    }

    @Test
    fun `csvEscape leaves plain values unchanged`() {
        val result = ExcelExporter.csvEscape("simple value")
        assertEquals("simple value", result)
    }

    @Test
    fun `generateCsv properly escapes values with commas`() {
        val fields = linkedMapOf(
            "remarks" to "borrower said, will pay next week",
        )
        val csv = ExcelExporter.generateCsv(fields)
        val lines = csv.lines().filter { it.isNotBlank() }

        // The value should be quoted
        assertTrue(lines[1].contains("\"borrower said, will pay next week\""))
    }

    @Test
    fun `generateCsv field order matches input map order`() {
        val fields = linkedMapOf(
            "visit_date" to "2024-01-01",
            "visit_time" to "09:00",
            "village" to "Rampur",
        )
        val csv = ExcelExporter.generateCsv(fields)
        val lines = csv.lines().filter { it.isNotBlank() }

        assertTrue(lines[1].startsWith("Visit Date"))
        assertTrue(lines[2].startsWith("Visit Time"))
        assertTrue(lines[3].startsWith("Village/Location"))
    }
}
