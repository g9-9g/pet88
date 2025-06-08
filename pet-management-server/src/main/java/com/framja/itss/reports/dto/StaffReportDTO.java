package com.framja.itss.reports.dto;

import com.framja.itss.common.dto.CountDTO;
import lombok.Data;
import java.util.Map;



@Data
public class StaffReportDTO {
    private Long petCount;
    private CountDTO medicalRequestCount;
    private CountDTO appointmentCount;
    private CountDTO groomingRequestCount;
    private CountDTO booking;
    private Long serviceCount;
    private CountDTO roomCount;
}