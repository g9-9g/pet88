package com.framja.itss.pets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framja.itss.pets.dto.PetDto;

/**
 * Service để kiểm tra quyền của người dùng đối với pet
 */
@Service
public class PetAuthorizationService {

    @Autowired
    private PetService petService;
    
    /**
     * Kiểm tra xem người dùng có phải là chủ sở hữu của pet không
     * 
     * @param petId ID của pet cần kiểm tra
     * @param userId ID của người dùng
     * @return true nếu người dùng là chủ sở hữu, false nếu không phải
     */
    public boolean isOwner(Long petId, Long userId) {
        return petService.getPetById(petId)
                .map(petDto -> petDto.getOwnerId().equals(userId))
                .orElse(false);
    }
} 