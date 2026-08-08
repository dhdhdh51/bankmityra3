package com.lrms.recovery.report

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lrms.recovery.R
import com.lrms.recovery.databinding.ActivityReportViewBinding
import com.lrms.recovery.ui.BaseActivity

/**
 * Displays a generated HTML report in a WebView and provides buttons for
 * printing, saving as PDF (via Android print dialog), and exporting to CSV.
 */
class ReportViewActivity : BaseActivity() {

    private lateinit var binding: ActivityReportViewBinding

    private var htmlContent: String = ""
    private var formFields: HashMap<String, String> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        htmlContent = intent.getStringExtra(EXTRA_HTML_CONTENT).orEmpty()

        @Suppress("UNCHECKED_CAST", "DEPRECATION")
        formFields = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_FORM_FIELDS, HashMap::class.java) as? HashMap<String, String>
                ?: hashMapOf()
        } else {
            (intent.getSerializableExtra(EXTRA_FORM_FIELDS) as? HashMap<String, String>)
                ?: hashMapOf()
        }

        setupWebView()
        setupButtons()
    }

    private fun setupWebView() {
        binding.webView.settings.apply {
            @Suppress("SetJavaScriptEnabled")
            javaScriptEnabled = false
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = true
            displayZoomControls = false
        }
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    private fun setupButtons() {
        binding.buttonPrintPdf.setOnClickListener { printReport() }
        binding.buttonExcel.setOnClickListener { exportExcel() }
    }

    private fun printReport() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as? PrintManager
        if (printManager == null) {
            showMessage(R.string.report_no_print_service, binding.root)
            return
        }

        val jobName = getString(R.string.report_view_title)
        val printAdapter = binding.webView.createPrintDocumentAdapter(jobName)
        printManager.print(jobName, printAdapter, null)
    }

    private fun exportExcel() {
        if (formFields.isEmpty()) {
            showMessage(R.string.report_exported, binding.root)
            return
        }

        val fileName = "visit_report_${System.currentTimeMillis()}.csv"
        val file = ExcelExporter.export(this, formFields, fileName)
        val intent = ExcelExporter.shareIntent(this, file)

        startActivity(Intent.createChooser(intent, getString(R.string.report_share_csv)))
        showMessage(R.string.report_exported, binding.root)
    }

    companion object {
        const val EXTRA_HTML_CONTENT = "html_content"
        const val EXTRA_FORM_FIELDS = "form_fields"

        /**
         * Factory method to create an intent for ReportViewActivity.
         *
         * @param context calling context
         * @param html the full HTML report content
         * @param fields map of field keys to values for CSV export
         */
        fun intent(
            context: Context,
            html: String,
            fields: Map<String, String> = emptyMap(),
        ): Intent = Intent(context, ReportViewActivity::class.java).apply {
            putExtra(EXTRA_HTML_CONTENT, html)
            putExtra(EXTRA_FORM_FIELDS, HashMap(fields))
        }
    }
}
