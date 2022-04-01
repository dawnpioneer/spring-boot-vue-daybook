package com.nagino.daybook.repository;

import com.nagino.daybook.model.Daybook;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DaybookRepository extends JpaRepository<Daybook, Long> {
	List<Daybook> findAll(Specification specification);
	Optional<Daybook> findByIdAndOwnerId(Long id, Long ownerId);
	void deleteByIdAndOwnerId(Long id, Long ownerId);

	@Query(value = "SELECT DISTINCT DATE_FORMAT(recordDate, '%Y') AS year FROM Daybook WHERE ownerId = :ownerId ORDER BY year")
	List<Integer> getYearList(@Param("ownerId") Long ownerId);
}
