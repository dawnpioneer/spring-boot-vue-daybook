package com.nagino.daybook.repository;

import com.nagino.daybook.model.DaybookCategory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DaybookCategoryRepository extends JpaRepository<DaybookCategory, Long> {
	List<DaybookCategory> findAll(Specification specification);
	Optional<DaybookCategory> findByIdAndOwnerId(Long id, Long ownerId);
	void deleteByIdAndOwnerId(Long id, Long ownerId);
}
