package com.framja.itss.reports.dto;

import com.framja.itss.common.dto.CountDTO;
import lombok.Data;
import java.util.Map;



@Data
public class OwnerReportDTO {
    private Long petCount;
    private CountDTO medicalRequestCount;
    private CountDTO appointmentCount;
    private CountDTO groomingRequestCount;
    private CountDTO booking;

}