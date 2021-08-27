package com.kkk.sbgtest.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirRepository extends JpaRepository<AirVO, Long> {
    @Query(value = "SELECT * FROM Member WHERE sidoName = ?0", nativeQuery = true)
    List<AirVO> findByName(String sido_name);

}
