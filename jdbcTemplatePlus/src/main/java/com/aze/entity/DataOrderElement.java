package com.aze.entity;

import com.aze.enums.OrderOperatorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataOrderElement {
    private OrderOperatorEnum orderOperatorEnum;
    private List<String> fieldNames;
}
