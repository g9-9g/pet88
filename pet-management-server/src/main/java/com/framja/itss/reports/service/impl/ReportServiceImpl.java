package com.framja.itss.reports.service.impl;

import com.framja.itss.reports.dto.OwnerReportDTO;
import com.framja.itss.common.dto.CountDTO;
import com.framja.itss.reports.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.framja.itss.pets.service.PetService;
import com.framja.itss.medical.service.MedicalRequestService;
import com.framja.itss.medical.service.MedicalAppointmentService;
import com.framja.itss.grooming.service.GroomingRequestService;
import com.framja.itss.booking.service.BookingService;
import com.framja.itss.grooming.service.GroomingServiceService;
import com.framja.itss.booking.service.RoomService;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.framja.itss.reports.dto.DoctorReportDTO;
import com.framja.itss.reports.dto.StaffReportDTO;
import com.framja.itss.booking.service.RoomService;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private PetService petService;

    @Autowired
    private MedicalRequestService medicalRequestService;

    @Autowired
    private MedicalAppointmentService appointmentService;

    @Autowired
    private GroomingRequestService groomingRequestService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private GroomingServiceService groomingServiceService;

    @Autowired
    private RoomService roomService;

    @Override
    public ResponseEntity<OwnerReportDTO> getOwnerReport(Long id) {
        OwnerReportDTO report = new OwnerReportDTO();
        
        // Count pets
        report.setPetCount(petService.getPetsCountByOwnerId(id));
        
        // Medical requests
        CountDTO medicalCount = medicalRequestService.getRequestCountsByOwnerId(id);
        report.setMedicalRequestCount(medicalCount);
        
        // Appointments
        CountDTO appointmentCount = appointmentService.getAppointmentCountsByOwnerId(id);
        report.setAppointmentCount(appointmentCount);
        
        // Grooming requests
        CountDTO groomingCount = groomingRequestService.getRequestCountsByOwnerId(id);
        report.setGroomingRequestCount(groomingCount);
        
        // Bookings
        CountDTO bookingCount = bookingService.getBookingCountsByOwnerId(id);
        report.setBooking(bookingCount);
        
        return ResponseEntity.ok(report);
    }

    @Override
    public ResponseEntity<DoctorReportDTO> getDoctorReport(Long id) {
        DoctorReportDTO report = new DoctorReportDTO();
        
        // Get total pets count
        report.setTotalPets(petService.getTotalPetsCount());
        
        CountDTO appointmentCount = appointmentService.getAppointmentCountsByDoctorId(id);
        report.setAppointmentCount(appointmentCount);
        
        return ResponseEntity.ok(report);
    }

    @Override
    public ResponseEntity<StaffReportDTO> getStaffReport() {
        StaffReportDTO report = new StaffReportDTO();
        
        // Count pets
        report.setPetCount(petService.getTotalPetsCount());
        
        // Medical requests
        CountDTO medicalCount = medicalRequestService.getRequestCountsAll();
        report.setMedicalRequestCount(medicalCount);
        
        // Appointments
        CountDTO appointmentCount = appointmentService.getAppointmentCountsAll();
        report.setAppointmentCount(appointmentCount);
        
        // Grooming requests
        CountDTO groomingCount = groomingRequestService.getRequestCountsAll();
        report.setGroomingRequestCount(groomingCount);
        
        // Bookings
        CountDTO bookingCount = bookingService.getBookingCountsAll();
        report.setBooking(bookingCount);

        // Service
        report.setServiceCount(groomingServiceService.getServiceCountsAll());
        
        // Room
        CountDTO roomCount = roomService.getRoomCountsByType();
        report.setRoomCount(roomCount);
        
        return ResponseEntity.ok(report);
    }
} 