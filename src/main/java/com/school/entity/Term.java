package com.school.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "term")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "academic_year", nullable = false, length = 9)
    private String academicYear;

    @OneToMany(mappedBy = "term", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Mark> marks = new ArrayList<>();
}
