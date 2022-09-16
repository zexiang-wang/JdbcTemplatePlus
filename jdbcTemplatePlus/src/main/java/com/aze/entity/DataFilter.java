package com.aze.entity;

import cn.hutool.core.collection.CollectionUtil;
import com.aze.enums.FilterOperatorEnum;
import com.aze.enums.OrderOperatorEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class DataFilter<T> {
    private List<DataFilterElement> filterElementList = new ArrayList<>();
    private DataOrderElement dataOrderElement = new DataOrderElement();
    private Class cls;

    public DataFilter(Class<T> cls) {
        this.cls = cls;
    }

    /**
     * 校验属性字符串是否存在
     *
     * @param fieldName
     */
    private void validProperty(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            throw new RuntimeException(cls.getName() + "属性不能为空字符串");
        }
        try {
            Field f = cls.getDeclaredField(fieldName);
            if (null == f) {
                throw new RuntimeException(cls.getName() + "中不含有字段：" + fieldName);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public void eq(String fieldName, Object val) {
        validProperty(fieldName);
        filterElementList.add(new DataFilterElement(FilterOperatorEnum.OPERATOR_EQUAL, fieldName, val));
    }

    public void lt(String fieldName, Object val) {
        validProperty(fieldName);
        filterElementList.add(new DataFilterElement(FilterOperatorEnum.OPERATOR_LT, fieldName, val));
    }

    public void gt(String fieldName, Object val) {
        validProperty(fieldName);
        filterElementList.add(new DataFilterElement(FilterOperatorEnum.OPERATOR_GT, fieldName, val));
    }

    public void not(String fieldName, Object val) {
        validProperty(fieldName);
        filterElementList.add(new DataFilterElement(FilterOperatorEnum.OPERATOR_NOT, fieldName, val));
    }

    public void ge(String fieldName, Object val) {
        validProperty(fieldName);
        filterElementList.add(new DataFilterElement(FilterOperatorEnum.OPERATOR_GE, fieldName, val));
    }

    public void le(String fieldName, Object val) {
        validProperty(fieldName);
        filterElementList.add(new DataFilterElement(FilterOperatorEnum.OPERATOR_LE, fieldName, val));
    }

    public void like(String fieldName, Object val) {
        validProperty(fieldName);
        filterElementList.add(new DataFilterElement(FilterOperatorEnum.OPERATOR_LIKE, fieldName, val));
    }

    public void leftLike(String fieldName, Object val) {
        validProperty(fieldName);
        filterElementList.add(new DataFilterElement(FilterOperatorEnum.OPERATOR_LEFT_LIKE, fieldName, val));
    }

    public void rightLike(String fieldName, Object val) {
        validProperty(fieldName);
        filterElementList.add(new DataFilterElement(FilterOperatorEnum.OPERATOR_RIGHT_LIKE, fieldName, val));
    }

    private void order(String fieldNames, boolean isForwardOrder) {
        if (null != dataOrderElement.getOrderOperatorEnum() || CollectionUtil.isNotEmpty(dataOrderElement.getFieldNames())) {
            throw new RuntimeException("不支持多次排序");
        }
        Arrays.asList(fieldNames.split(",")).forEach(fieldName -> validProperty(fieldName));
        this.dataOrderElement.setOrderOperatorEnum(isForwardOrder ? OrderOperatorEnum.OPERATOR_FS : OrderOperatorEnum.OPERATOR_RS);
        this.dataOrderElement.setFieldNames(Arrays.asList(fieldNames.split(",")));
    }

    public void orderByAsc(String fieldNames) {
        order(fieldNames, true);
    }

    public void orderByDesc(String fieldNames) {
        order(fieldNames, false);
    }



}
