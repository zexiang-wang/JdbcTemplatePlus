package com.aze.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ModifyResult {
    private String modifySql;
    private List<Object> modifyParams;
}
