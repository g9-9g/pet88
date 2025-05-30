package com.framja.itss.medical.service.impl;

import com.framja.itss.medical.entity.Medicine;
import com.framja.itss.medical.dto.medicine.MedicineDto;
import com.framja.itss.medical.dto.medicine.CreateMedicineRequest;
import com.framja.itss.medical.repository.MedicineRepository;
import com.framja.itss.medical.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineServiceImpl implements MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    @Override
    public Medicine findById(Long id) {
        return medicineRepository.findById(id).orElse(null);
    }

    @Override
    public MedicineDto save(CreateMedicineRequest medicineRequest) {
        Medicine medicine = Medicine.builder()
                .name(medicineRequest.getName())
                .description(medicineRequest.getDescription())
                .unit(medicineRequest.getUnit())
                .unitPrice(medicineRequest.getUnitPrice())
                .build();
        
        Medicine savedMedicine = medicineRepository.save(medicine);
        return convertToDto(savedMedicine);
    }

    @Override
    public void deleteById(Long id) {
        medicineRepository.deleteById(id);
    }
    
    private MedicineDto convertToDto(Medicine medicine) {
        return MedicineDto.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .description(medicine.getDescription())
                .unit(medicine.getUnit())
                .unitPrice(medicine.getUnitPrice())
                .build();
    }
} 