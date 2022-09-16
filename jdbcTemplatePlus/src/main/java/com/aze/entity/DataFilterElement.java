package com.aze.entity;

import com.aze.enums.FilterOperatorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataFilterElement {
    private FilterOperatorEnum filterOperatorEnum;
    private String fieldName;
    private Object val;
}
