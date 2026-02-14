package com.school.dto;

import com.school.enums.BatchType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchDTO {
    private Long id;
    private String name;
    private BatchType type;
    private String description;
}
