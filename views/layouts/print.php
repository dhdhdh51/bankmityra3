<?php
/**
 * Print-optimized layout for dedicated field report printable views.
 * A4-optimized, no sidebar, no navigation.
 *
 * @var string $content
 * @var string $title
 */

use App\Core\Settings;

$appName = Settings::get('app_name', 'D2 Recovery Solutions & Services') ?? 'D2 Recovery Solutions & Services';
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="noindex, nofollow">
    <title><?= e(($title ?? 'Report') . ' - ' . $appName) ?></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400..700&display=swap" rel="stylesheet">
    <link href="<?= e(asset('css/app.css')) ?>" rel="stylesheet">

    <style>
        body {
            font-family: 'Inter', sans-serif;
            font-size: 11pt;
            line-height: 1.4;
            color: #1f2937;
            background: #fff;
        }

        .print-container {
            max-width: 210mm;
            margin: 0 auto;
            padding: 10mm;
        }

        .print-header {
            border-bottom: 2px solid #1e3a5f;
            padding-bottom: 8px;
            margin-bottom: 16px;
        }

        .print-header h1 {
            font-size: 14pt;
            font-weight: 700;
            color: #1e3a5f;
            margin: 0;
        }

        .print-header .subtitle {
            font-size: 9pt;
            color: #4b5563;
            margin: 2px 0 0;
        }

        .print-section {
            margin-bottom: 14px;
            break-inside: avoid;
        }

        .print-section-title {
            font-size: 10pt;
            font-weight: 700;
            color: #1e3a5f;
            border-bottom: 1px solid #d1d5db;
            padding-bottom: 4px;
            margin-bottom: 8px;
        }

        .field-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 6px 16px;
        }

        .field-item {
            display: flex;
            flex-direction: column;
        }

        .field-label {
            font-size: 8pt;
            font-weight: 600;
            color: #6b7280;
            text-transform: uppercase;
            letter-spacing: 0.03em;
        }

        .field-value {
            font-size: 10pt;
            color: #1f2937;
            font-weight: 500;
        }

        .check-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
            gap: 4px 12px;
        }

        .check-item {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 9pt;
        }

        .check-icon {
            width: 14px;
            height: 14px;
            border: 1.5px solid #d1d5db;
            border-radius: 3px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 8pt;
            flex-shrink: 0;
        }

        .check-icon.checked {
            background: #059669;
            border-color: #059669;
            color: #fff;
        }

        .supervisor-badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: #f0f9ff;
            border: 1px solid #bae6fd;
            border-radius: 4px;
            padding: 4px 10px;
            font-size: 9pt;
        }

        .no-print {
            display: block;
        }

        @media print {
            body { margin: 0; padding: 0; }
            .print-container { max-width: none; padding: 5mm; }
            .no-print { display: none !important; }
            @page {
                size: A4;
                margin: 10mm;
            }
        }

        @media screen and (max-width: 576px) {
            .print-container { padding: 12px; }
            .field-grid { grid-template-columns: 1fr 1fr; }
            .check-grid { grid-template-columns: 1fr; }
        }

        @media screen and (min-width: 577px) and (max-width: 768px) {
            .print-container { padding: 16px; max-width: 420px; margin: 0 auto; }
        }

        @media screen and (min-width: 769px) {
            .print-container { max-width: 210mm; margin: 0 auto; padding: 20px; }
        }
    </style>
</head>
<body>
    <div class="print-container">
        <div class="no-print mb-3 d-flex gap-2 flex-wrap">
            <button type="button" class="btn btn-primary btn-sm" onclick="window.print()">
                Print Report
            </button>
            <a href="<?= e(url('/reports')) ?>" class="btn btn-outline-secondary btn-sm">
                Back to Reports
            </a>
        </div>

        <?= $content ?>
    </div>
</body>
</html>
