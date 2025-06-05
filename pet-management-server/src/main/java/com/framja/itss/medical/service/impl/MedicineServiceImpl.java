package com.framja.itss.medical.service.impl;

import com.framja.itss.medical.entity.Medicine;
import com.framja.itss.medical.dto.medicine.MedicineDto;
import com.framja.itss.medical.dto.medicine.MedicineRequest;
import com.framja.itss.medical.repository.MedicineRepository;
import com.framja.itss.medical.service.MedicineService;
import com.framja.itss.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineServiceImpl implements MedicineService {
    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    @Override
    public List<MedicineDto> getAllMedicines() {
        return findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Medicine findById(Long id) {
        return medicineRepository.findById(id).orElse(null);
    }

    @Override
    public MedicineDto save(MedicineRequest medicineRequest) {
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
    public MedicineDto update(Long id, MedicineRequest updateRequest) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        if (updateRequest.getName() != null) {
            medicine.setName(updateRequest.getName());
        }
        if (updateRequest.getDescription() != null) {
            medicine.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getUnit() != null) {
            medicine.setUnit(updateRequest.getUnit());
        }
        if (updateRequest.getUnitPrice() != null) {
            medicine.setUnitPrice(updateRequest.getUnitPrice());
        }

        Medicine updatedMedicine = medicineRepository.save(medicine);
        return convertToDto(updatedMedicine);
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