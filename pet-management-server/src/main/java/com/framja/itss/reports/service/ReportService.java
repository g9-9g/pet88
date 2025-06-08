package com.framja.itss.reports.service;

import com.framja.itss.reports.dto.OwnerReportDTO;
import com.framja.itss.reports.dto.DoctorReportDTO;
import com.framja.itss.reports.dto.StaffReportDTO;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity<OwnerReportDTO> getOwnerReport(Long id);
    ResponseEntity<DoctorReportDTO> getDoctorReport(Long id);
    ResponseEntity<StaffReportDTO> getStaffReport();
} 