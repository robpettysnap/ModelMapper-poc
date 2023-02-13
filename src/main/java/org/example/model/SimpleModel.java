package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.SimpleDto;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleModel {
    private String string;
    private String notOnDTO;
    private int integer;
    private boolean bool;
}
