# BankMityra Admin Panel
**Version**: 1.0.0  
**Updated**: August 9, 2026  
**Status**: Production Ready ✅

---

## 📋 Overview

This repository contains the **Admin Panel** for the BankMityra Field Visit Recovery System. The admin panel manages field visit reports, BC Supervisor verifications, and recovery case management for bank loan recovery operations.

**Key Features:**
- 📊 Field Visit Report Management
- 👥 BC Supervisor & Agent Management  
- 📱 Visit Verification & Approval Workflow
- 📈 Recovery Analytics & Reporting
- 🔐 Role-Based Access Control
- 📍 GPS Location Tracking
- 📸 Photo Documentation
- 🏦 Branch & Regional Management

---

## 📁 Directory Structure

```
bankmityra-admin/
├── app/                          # Application source code
│   ├── Controllers/
│   │   ├── Admin/               # Admin panel controllers
│   │   │   ├── BcVisitController.php    # NEW: BC Supervisor visits
│   │   │   ├── VisitController.php      # UPDATED: Report filtering
│   │   │   └── ... (20+ other controllers)
│   │   ├── Api/                 # REST API controllers
│   │   │   ├── VisitController.php      # UPDATED: Report type support
│   │   │   └── ... (10+ API endpoints)
│   │   └── Admin/Controller.php         # Base controller
│   │
│   ├── Models/                  # Data models
│   │   ├── BcVisit.php          # NEW: BC visit model
│   │   ├── VisitReport.php      # UPDATED: Report handling
│   │   └── ... (10+ other models)
│   │
│   ├── Core/                    # Core framework
│   │   ├── Database.php
│   │   ├── Request.php
│   │   ├── Response.php
│   │   └── ... (15+ utilities)
│   │
│   ├── Services/
│   │   ├── VisitService.php     # UPDATED: Report type support
│   │   └── ... (5+ services)
│   │
│   └── routes/                  # Route definitions
│       └── web.php              # UPDATED: BC visit routes
│
├── views/                        # View templates
│   ├── bc/visit/
│   │   ├── form.php             # NEW: BC visit form with GPS/photo
│   │   ├── show.php             # NEW: BC visit detail view
│   │   └── index.php            # NEW: BC visits list
│   │
│   ├── visits/                  # Field visit reports
│   │   ├── index.php
│   │   ├── show.php             # UPDATED: Report filtering
│   │   └── ... (other views)
│   │
│   └── ... (40+ view files)
│
├── database/
│   └── migrations/
│       ├── 001_add_location_photo_to_bc_visits.sql
│       └── 002_add_bc_supervisor_visit_fields.sql
│
├── config/                       # Configuration files
├── cron/                        # Scheduled tasks
├── storage/                     # Temp storage
├── uploads/                     # Uploaded files
├── assets/                      # CSS, JS, images
│
├── schema.sql                   # Database schema
├── index.php                    # Entry point
├── PROJECT_SUMMARY.md           # Implementation details
├── DEPLOYMENT_GUIDE.md          # Deployment instructions
├── FINAL_SUMMARY.txt            # Project completion report
└── README.md                    # This file
```

---

## 🚀 Quick Start

### 1. Prerequisites
- PHP 8.0+
- MySQL 8.0+
- Composer
- Apache/Nginx with mod_rewrite

### 2. Installation

```bash
# Clone repository
git clone https://github.com/dhdhdh51/bankmityra-admin.git
cd bankmityra-admin

# Install dependencies (if using Composer)
composer install

# Create uploads directory
mkdir -p storage/uploads/bc-visits
chmod 755 storage/uploads/bc-visits

# Create symlink (optional)
ln -s storage/uploads public/uploads
```

### 3. Database Setup

```bash
# Execute migrations
mysql -u root -p your_database < database/migrations/001_add_location_photo_to_bc_visits.sql
mysql -u root -p your_database < database/migrations/002_add_bc_supervisor_visit_fields.sql

# Or run full schema
mysql -u root -p your_database < schema.sql
```

### 4. Configuration

Edit `config/` files with your settings:
- Database connection
- API endpoints  
- File upload paths
- Email settings
- GPS services

### 5. Web Server

```apache
# Apache .htaccess is in place
# Just ensure mod_rewrite is enabled

# Or Nginx:
location / {
    try_files $uri $uri/ /index.php?$query_string;
}
```

---

## 📊 Key Features

### BC Supervisor Visit Management ✅
- **Create Visit**: Admin form to record BC Supervisor visit
- **Auto-Populate**: BC Agent details load automatically
- **GPS Location**: Auto-capture device location
- **Photo Upload**: Capture visit evidence (optional)
- **Observation Fields**: 10 manual entry fields
- **List & Filter**: Search, sort, date filtering
- **Detail View**: Full visit record with maps link

### Field Visit Report Management ✅
- **9 Report Types**: Recovery, OTS, CKCC OD-2, CKCC OD, CKCC NPA OTS, Pre-NPA, Post-NPA, BC Supervisor, Other
- **Type Filtering**: OTS excluded from CKCC OD-2 reports
- **PDF Generation**: A4-formatted reports with QR codes
- **Approval Workflow**: Pending → Approved/Rejected
- **Corrections**: Track all changes in revision history
- **Media Attachments**: Photos, documents linked to report

### Recovery Management ✅
- **Loan Account Tracking**: Link reports to customer accounts
- **Promise Management**: Record settlement promises
- **Timeline Tracking**: View all visit/action history
- **Notification System**: Alerts for due promises
- **Export Reports**: PDF, Excel, CSV formats

### Branch & User Management ✅
- **Role-Based Access**: Super Admin, Branch Manager, Agent, Auditor
- **Branch Scoping**: Managers see own branch only
- **User Permissions**: Granular permission control
- **Audit Logging**: Track all admin actions

---

## 🔒 Security Features

✅ **Authentication**
- Session-based login
- Password hashing (bcrypt)
- CSRF token protection
- SQL injection prevention

✅ **Authorization**
- Role-based access control (RBAC)
- Branch-level scoping
- Permission validation on every action
- Audit trail for sensitive operations

✅ **File Upload Security**
- MIME type validation
- File size restrictions (5MB for photos)
- Unique filename generation
- Non-web-accessible storage

✅ **Data Protection**
- Encrypted sensitive fields (mobile, Aadhaar, PAN)
- HTTPS required in production
- Database encryption at rest
- Secure session management

---

## 📱 Responsive Design

✅ **Desktop** (1200px+)
- Full feature layout
- Multi-column tables
- Sidebar navigation

✅ **Tablet** (768px - 1199px)
- Responsive grid
- Stacked navigation
- Touch-friendly buttons

✅ **Mobile** (420px - 767px)
- Single column layout
- Horizontal scroll tables
- Large touch targets

✅ **Print**
- A4-optimized PDF reports
- QR code embedding
- Signature blocks
- Certification sections

---

## 📡 API Endpoints

### Visits API
```
GET    /api/visits                 # List visits
POST   /api/visits                 # Create visit
GET    /api/visits/{id}            # Get visit detail
GET    /api/visits/form-options    # Get dropdown options

New Report Types:
- bc_supervisor_visit (NEW)
- ckcc_od (NEW)
- ckcc_npa_ots (NEW)
```

### BC Visits API (NEW)
```
GET    /api/bc/visits              # List BC supervisor visits
POST   /api/bc/visits              # Create BC visit
GET    /api/bc/visits/{id}         # Get BC visit detail
GET    /bc/visit/api/agent/{id}    # Auto-load agent details
```

### Response Format
```json
{
  "success": true,
  "data": { /* visit data */ },
  "message": "Visit created successfully",
  "warnings": []
}
```

---

## 📊 Database Schema Updates

### New Columns in visit_reports
- `latitude` - GPS latitude (DECIMAL 10,8)
- `longitude` - GPS longitude (DECIMAL 11,8)  
- `gps_source` - GPS status (device/unavailable/denied)
- `bc_supervisor_name` - Visiting supervisor name
- `bc_supervisor_bcbf_code` - Supervisor BCBF code
- `supervised_agent_*` - 3 columns for agent details
- `supervisor_visit_*` - 9 columns for observations

### New Table: bc_visits
- Stores BC Supervisor visit records
- Links supervisor to supervised agent
- Stores location, photo, observations
- Supports future QR code tracking

---

## 🧪 Testing

### Unit Tests
```bash
# Run PHP unit tests
./vendor/bin/phpunit tests/

# Check specific test
./vendor/bin/phpunit tests/Controllers/Admin/BcVisitControllerTest.php
```

### Manual Testing Checklist
- [ ] Create BC Supervisor visit with GPS
- [ ] Upload photo to visit
- [ ] Generate PDF with QR code
- [ ] Filter CKCC reports (OTS excluded from renewal)
- [ ] Test all report types dropdown
- [ ] Verify location auto-capture
- [ ] Test mobile responsive view
- [ ] Verify permission-based access

---

## 📈 Deployment Checklist

- [ ] Database migrations executed
- [ ] PHP files uploaded to server
- [ ] /storage/uploads directory created and writable
- [ ] .htaccess in place (mod_rewrite enabled)
- [ ] Configuration files updated
- [ ] Web server SSL certificate valid
- [ ] Email service configured
- [ ] Backup system in place
- [ ] Monitoring alerts configured
- [ ] Admin user created

---

## 📚 Documentation

1. **PROJECT_SUMMARY.md** - Detailed implementation overview (9 tasks, architecture, features)
2. **DEPLOYMENT_GUIDE.md** - Step-by-step production deployment (5 phases, SQL commands)
3. **FINAL_SUMMARY.txt** - Project completion report (statistics, verification)

---

## 🔗 Related Repositories

- **Android App**: `https://github.com/dhdhdh51/bankmityra-app` (BC Supervisor field visits)
- **Main Repo**: `https://github.com/dhdhdh51/bankmityra3` (Full project with both codebases)

---

## 🆘 Support & Troubleshooting

### Common Issues

**GPS Location Not Capturing**
- Ensure HTTPS connection (Geolocation requires secure context)
- Browser must grant location permission
- Check browser console for errors

**Photo Upload Fails**
- Verify /storage/uploads/bc-visits is writable
- Check file size (max 5MB)
- Supported formats: JPG, PNG, GIF, WebP

**Report PDF Generation Error**
- Ensure ReportGenerator has correct report_type
- Check QR code Base64 string length
- Verify ZXing library dependency

**QR Code Not Displaying**
- Confirm qrCodeBase64 parameter passed to ReportGenerator
- Check PDF rendering in WebView
- Verify report_type = 'bc_supervisor_visit'

---

## 📞 Contact & Support

**Development Team**  
Email: dev@bankmityra.local  
Status: 🟢 Production Ready

---

## 📄 License

Proprietary - All rights reserved to D2 Recovery Solutions & Services

---

## ✅ Project Status

**Version**: 1.0.0 Production  
**Last Updated**: August 9, 2026  
**Status**: ✅ Complete & Ready  
**Tasks Completed**: 9/9  
**Build**: ✅ Verified  
**Tests**: ✅ Passed  
**Documentation**: ✅ Comprehensive  

