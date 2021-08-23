package com.vaccinations.backend.repository;

import com.vaccinations.backend.model.Vaccination;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRepository extends MongoRepository<Vaccination, String> {
    @Query("{ $where: 'this.patients.length < 3' }")
    Vaccination findFirstAvailableVaccinationDate();
}
