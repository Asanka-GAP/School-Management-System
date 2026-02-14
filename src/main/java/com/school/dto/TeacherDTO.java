package com.school.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialization;
}
