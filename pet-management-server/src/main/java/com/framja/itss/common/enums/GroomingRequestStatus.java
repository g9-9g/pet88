package com.framja.itss.common.enums;

public enum GroomingRequestStatus {
    PENDING,     // Initial state when request is created
    APPROVED,    // Request is approved by staff
    REJECTED,    // Request is rejected by staff
    SCHEDULED,   // Approved and scheduled for a specific time
    IN_PROGRESS, // Service is currently being performed
    COMPLETED,   // Service has been completed
    CANCELLED    // Request was cancelled by the pet owner
} 