//package org.example.RM;
//
//import org.example.demo.NetworkDevice;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Controller
//public class ReportController {
//
//    @Autowired
//    private ReportService reportService;
//
//    // Endpoint to generate the report
//    @GetMapping("/generateReport")
//    public String generateReport(
//            @RequestParam(value = "reportName", defaultValue = "Sample Report") String reportName,
//            @RequestParam(value = "reportType", defaultValue = "PDF") String reportType,
//            @RequestParam(value = "chartType", defaultValue = "BAR") String chartType,
//            @RequestParam(value = "description", defaultValue = "This is a sample report.") String description,
//            Model model) {
//
//        // Convert chartType to enum
//        ChartType chartEnum = ChartType.valueOf(chartType.toUpperCase());
//
//        // Generate the report object
//        Report report = reportService.generateReport(reportName, reportType, chartEnum, description);
//
//        // Add the report object to the model
//        model.addAttribute("report", report);
//
//        // Check if the report type is PDF
//        if (report.getReportType().equalsIgnoreCase("PDF")) {
//            // Generate PDF and return as a byte array
//            byte[] pdfBytes = reportService.generatePdfFromReport(report);
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
//                    .contentType(MediaType.APPLICATION_PDF)
//                    .body(pdfBytes);
//        } else {
//            // If it's HTML, return the template name
//            return "report-template";  // Ensure you have the template in src/main/resources/templates/report-template.html
//        }
//    }
//}
