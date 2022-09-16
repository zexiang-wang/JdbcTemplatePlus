package com.aze.enums;

/**
 * 过滤符号enum
 */
public enum OrderOperatorEnum {
    OPERATOR_FS("asc", "正向排序"),// Forward sort
    OPERATOR_RS("desc", "反向排序");// Reverse sort
    private final String code;
    private final String description;

    OrderOperatorEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }
}
