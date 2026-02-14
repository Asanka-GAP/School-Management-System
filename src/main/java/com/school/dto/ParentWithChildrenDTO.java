package com.school.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentWithChildrenDTO {
    private ParentDTO parent;
    private List<StudentDTO> children;
    private List<MarkDTO> marks;
}
