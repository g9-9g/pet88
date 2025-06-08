package com.framja.itss.reports.controller;

import com.framja.itss.reports.dto.OwnerReportDTO;
import com.framja.itss.reports.dto.DoctorReportDTO;
import com.framja.itss.reports.service.ReportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/owner")
    public ResponseEntity<OwnerReportDTO> getOwnerReport(@RequestParam Long id) {
        return reportService.getOwnerReport(id);
    }

    @GetMapping("/doctor")
    public ResponseEntity<DoctorReportDTO> getDoctorReport(@RequestParam Long id) {
        return reportService.getDoctorReport(id);
    }

    @GetMapping("/staff")
    public ResponseEntity<?> getStaffReport() {
        return reportService.getStaffReport();
    }
} 