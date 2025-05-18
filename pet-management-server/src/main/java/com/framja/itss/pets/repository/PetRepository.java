package com.framja.itss.pets.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.framja.itss.pets.entity.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByOwner_Id(Long ownerId);
    
    @Query(value = "SELECT p FROM Pet p WHERE p.name LIKE %:name%")
    List<Pet> findByNameContaining(@Param("name") String name);
    
    @Query(value = "SELECT p FROM Pet p WHERE " +
            "(:name IS NULL OR p.name LIKE %:name%) AND " +
            "(:species IS NULL OR p.species = :species) AND " +
            "(:breed IS NULL OR p.breed = :breed) AND " +
            "(:gender IS NULL OR p.gender = :gender) AND " +
            "(:ownerId IS NULL OR p.owner.id = :ownerId) " +
            "ORDER BY " +
            "CASE WHEN :sortField = 'name' AND :sortDir = 'asc' THEN p.name END ASC, " +
            "CASE WHEN :sortField = 'name' AND :sortDir = 'desc' THEN p.name END DESC, " +
            "CASE WHEN :sortField = 'species' AND :sortDir = 'asc' THEN p.species END ASC, " +
            "CASE WHEN :sortField = 'species' AND :sortDir = 'desc' THEN p.species END DESC, " +
            "CASE WHEN :sortField = 'birthdate' AND :sortDir = 'asc' THEN p.birthdate END ASC, " +
            "CASE WHEN :sortField = 'birthdate' AND :sortDir = 'desc' THEN p.birthdate END DESC, " +
            "p.name ASC")
    List<Pet> findPetsWithFiltersAndSort(
            @Param("name") String name,
            @Param("species") String species,
            @Param("breed") String breed,
            @Param("gender") String gender,
            @Param("ownerId") Long ownerId,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir);
    
    @Query(value = "SELECT p FROM Pet p WHERE " +
            "(:name IS NULL OR p.name LIKE %:name%) AND " +
            "(:species IS NULL OR p.species = :species) AND " +
            "(:breed IS NULL OR p.breed = :breed) AND " +
            "(:gender IS NULL OR p.gender = :gender) AND " +
            "(:ownerId IS NULL OR p.owner.id = :ownerId)")
    Page<Pet> findPetsWithFiltersAndPagination(
            @Param("name") String name,
            @Param("species") String species,
            @Param("breed") String breed,
            @Param("gender") String gender,
            @Param("ownerId") Long ownerId,
            Pageable pageable);
    
    @Query(value = "SELECT * FROM pets p WHERE " +
            "(:name IS NULL OR p.name LIKE CONCAT('%', :name, '%')) AND " +
            "(:species IS NULL OR p.species = :species) AND " +
            "(:breed IS NULL OR p.breed = :breed) AND " +
            "(:gender IS NULL OR p.gender = :gender) AND " +
            "(:ownerId IS NULL OR p.user_id = :ownerId) " +
            "ORDER BY " +
            "CASE WHEN :sortField = 'name' AND :sortDir = 'asc' THEN p.name END ASC, " +
            "CASE WHEN :sortField = 'name' AND :sortDir = 'desc' THEN p.name END DESC, " +
            "CASE WHEN :sortField = 'species' AND :sortDir = 'asc' THEN p.species END ASC, " +
            "CASE WHEN :sortField = 'species' AND :sortDir = 'desc' THEN p.species END DESC, " +
            "CASE WHEN :sortField = 'birthdate' AND :sortDir = 'asc' THEN p.birthdate END ASC, " +
            "CASE WHEN :sortField = 'birthdate' AND :sortDir = 'desc' THEN p.birthdate END DESC, " +
            "p.name ASC", nativeQuery = true)
    List<Pet> findPetsWithFiltersAndSortNative(
            @Param("name") String name,
            @Param("species") String species,
            @Param("breed") String breed,
            @Param("gender") String gender,
            @Param("ownerId") Long ownerId,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir);

    @Query("SELECT DISTINCT p FROM Pet p JOIN MedicalAppointment a ON a.pet = p WHERE a.status = com.framja.itss.common.enums.AppointmentStatus.COMPLETED AND (:ownerId IS NULL OR p.owner.id = :ownerId)")
    List<Pet> findPetsWithCompletedAppointments(@Param("ownerId") Long ownerId);
} 