package com.aze.enums;

/**
 * 过滤符号enum
 */
public enum FilterOperatorEnum {
    OPERATOR_EQUAL("=", "等于"),
    //    OPERATOR_IN("In","包含"),
    OPERATOR_LT("<", "小于"),
    OPERATOR_GT(">", "大于"),
    OPERATOR_NOT("!=", "不等于"),
    OPERATOR_GE(">=", "大于等于"),
    OPERATOR_LE("<=", "小于等于"),
    OPERATOR_LEFT_LIKE("leftLike", "左模糊查询"),
    OPERATOR_RIGHT_LIKE("rightLike", "右模糊查询"),
    OPERATOR_LIKE("like", "模糊查询");
    private final String code;
    private final String description;

    FilterOperatorEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }
}
