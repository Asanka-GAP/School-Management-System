package com.school.repository;

import com.school.entity.Batch;
import com.school.enums.BatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    List<Batch> findByType(BatchType type);
}
