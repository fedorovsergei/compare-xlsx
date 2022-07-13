package com.fedorov.demo.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class CorrectPosition {

    private String code;
    private Double count;
}
