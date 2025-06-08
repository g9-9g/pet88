package com.framja.itss.reports.dto;

import com.framja.itss.common.dto.CountDTO;
import lombok.Data;

@Data
public class DoctorReportDTO {
    private Long totalPets;
    private CountDTO appointmentCount;
} 