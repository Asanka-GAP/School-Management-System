package com.school.dto;

import com.school.enums.Gender;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private Long id;
    private String admissionNumber;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate admissionDate;
    private String status;
    private Gender gender;
    private Long parentId;
    private Long classId;
    private String className;
}
