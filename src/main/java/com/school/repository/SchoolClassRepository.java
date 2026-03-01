package com.school.repository;

import com.school.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    Optional<SchoolClass> findByClassName(String className);
    List<SchoolClass> findByGrade(Integer grade);
    List<SchoolClass> findBySupervisorId(Long supervisorId);
}
