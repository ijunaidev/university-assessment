package com.genesis.university.repository;

import com.genesis.university.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    @Query("select u from University u where u.country = ?1")
    List<University> findAllByCountry(String country);

    @Query("select count(u) > 0 from University u where u.country = ?1")
    boolean existsByCountry(String country);

    @Query("select u from University u where u.country = ?1 and u.id = ?2")
    University findByCountryAndUniversityId(String country, Long id);

    @Query("select count(u) > 0 from University u where u.country = ?1 and u.id = ?2")
    boolean existsByCountryAndUniversityId(String country, Long id);
}
