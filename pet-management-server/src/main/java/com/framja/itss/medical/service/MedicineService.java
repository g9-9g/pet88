package com.framja.itss.medical.service;

import com.framja.itss.medical.entity.Medicine;
import java.util.List;

public interface MedicineService {
    List<Medicine> findAll();
    Medicine findById(Long id);
    Medicine save(Medicine medicine);
    void deleteById(Long id);
} 