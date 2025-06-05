package com.framja.itss.medical.service;

import com.framja.itss.medical.entity.Medicine;
import com.framja.itss.medical.dto.medicine.MedicineDto;
import com.framja.itss.medical.dto.medicine.MedicineRequest;
import java.util.List;

public interface MedicineService {
    List<Medicine> findAll();
    List<MedicineDto> getAllMedicines();
    Medicine findById(Long id);
    MedicineDto save(MedicineRequest medicineRequest);
    MedicineDto update(Long id, MedicineRequest updateRequest);
    void deleteById(Long id);
} 