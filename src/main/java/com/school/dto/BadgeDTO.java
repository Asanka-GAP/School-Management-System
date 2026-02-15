package com.school.dto;

import com.school.enums.BadgeType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeDTO {
    private Long id;
    private String name;
    private BadgeType type;
    private String description;
}
