package com.school.repository;

import com.school.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    
    List<Mark> findByStudentId(Long studentId);
    
    List<Mark> findByStudentIdAndTermId(Long studentId, Long termId);
    
    @Query(value = """
        SELECT s.id as subject_id, s.name as subject_name, s.code as subject_code, 
               AVG(m.score) as average_score
        FROM mark m
        JOIN subject s ON m.subject_id = s.id
        WHERE m.student_id = :studentId
        GROUP BY s.id, s.name, s.code
        ORDER BY s.name
        """, nativeQuery = true)
    List<Object[]> calculateAverageScorePerSubject(@Param("studentId") Long studentId);
}
