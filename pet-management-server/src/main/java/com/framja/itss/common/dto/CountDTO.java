package com.framja.itss.common.dto;

import lombok.Data;
import java.util.Map;

@Data
public class CountDTO {
    private Long total;
    private Map<String, Long> statusCounts;
} 