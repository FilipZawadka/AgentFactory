package com.pw.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Material {
    private String name;
    private Integer weight;
}
