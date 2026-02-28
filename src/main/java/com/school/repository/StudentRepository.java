package com.school.repository;

import com.school.entity.Student;
import com.school.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByParentId(Long parentId);
    List<Student> findByGender(Gender gender);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.gender = :gender")
    Long countByGender(Gender gender);
}
