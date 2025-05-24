package com.framja.itss.medical.service;

import com.framja.itss.medical.entity.Medicine;
import com.framja.itss.medical.dto.medicine.MedicineDto;
import com.framja.itss.medical.dto.medicine.CreateMedicineRequest;
import java.util.List;

public interface MedicineService {
    List<Medicine> findAll();
    Medicine findById(Long id);
    MedicineDto save(CreateMedicineRequest medicineRequest);
    void deleteById(Long id);
} 