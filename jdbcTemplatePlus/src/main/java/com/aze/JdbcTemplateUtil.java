package com.aze;

import com.aze.entity.*;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.aze.util.StringUtil.toUnderlineCase;

public class JdbcTemplateUtil {

    /**
     * 根据类获取数据库表字段集合
     *
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<String> getFieldList(Class<T> cls) {
        try {
            List<String> fieldNameList = new ArrayList<>();
            T obj = cls.newInstance();
            Field[] fieldArr = cls.getDeclaredFields();
            for (Field f : fieldArr) {
                String name = f.getName();
                FieldName fieldName = f.getAnnotation(FieldName.class);
                // 有FieldName注解取注解中的字段名，默认驼峰转下划线
                if (null != fieldName) {
                    name = fieldName.value();
                } else {
                    name = toUnderlineCase(name);
                }
                fieldNameList.add(name);
            }
            return fieldNameList;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取实体类所有属性名称
     *
     * @param cls
     * @param <T>
     * @return 所有属性名称
     */
    public static <T> List<String> getProperties(Class<T> cls) {
        List<String> properties = new ArrayList<>();
        Field[] fieldArr = cls.getDeclaredFields();
        for (Field f : fieldArr) {
            String name = f.getName();
            properties.add(name);
        }
        return properties;
    }

    /**
     * 获取实体类属性类型列表
     *
     * @param cls
     * @param <T>
     * @return 所有属性类型
     */
    public static <T> List<Class> getClassList(Class<T> cls) {
        List<Class> clsList = new ArrayList<>();
        Field[] fieldArr = cls.getDeclaredFields();
        for (Field f : fieldArr) {
            Class c = f.getType();
            clsList.add(c);
        }
        return clsList;
    }

    /**
     * 根据class的TableName注解获取表名
     *
     * @param cls
     * @param <T>
     * @return 表名称
     */
    public static <T> String getTableName(Class<T> cls) {
        TableName tn = cls.getAnnotation(TableName.class);
        if (null == tn) {
            throw new RuntimeException("TableName注解不能为空");
        }
        return tn.value();
    }

    /**
     * 拼接select语句
     *
     * @param cls
     * @param <T>
     * @return select 字段1,字段2 from 表名
     */

    public static <T> String getSelectSql(Class<T> cls) {
        List<String> fieldList = getFieldList(cls);
        String selectSql = "SELECT %s from %s";
        return String.format(selectSql, StringUtils.join(fieldList, ","), getTableName(cls));
    }

    /**
     * 拼接update语句、获取update参数
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> ModifyResult getModifyResult(T t) {
        ModifyResult modifyResult = new ModifyResult();
        Class cls = t.getClass();
        Field[] fields = cls.getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        List<Object> modifyParams = new ArrayList<>();
        sb.append("update " + getTableName(cls) + " set ");
        for (Field f : fields) {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), cls);
                Method getMethod = pd.getReadMethod();
                Object valObj = ReflectionUtils.invokeMethod(getMethod, t);
                if (null != valObj) {
                    String name = f.getName();
                    FieldName fieldName = f.getAnnotation(FieldName.class);
                    if (null != fieldName) {
                        name = fieldName.value();
                    } else {
                        name = toUnderlineCase(name);
                    }
                    sb.append(name + "=?,");
                    modifyParams.add(valObj);
                }
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        modifyResult.setModifySql(sb.toString());
        modifyResult.setModifyParams(modifyParams);
        return modifyResult;
    }

    /**
     * 拼接insert语句,获取insert参数
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> InsertResult getInsertResult(T t) {
        if(ObjectUtils.isEmpty(t)){
            throw new RuntimeException("不能插入为空的对象");
        }
        InsertResult insertResult = new InsertResult();
        Class cls = t.getClass();
        Field[] fields = cls.getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        List<Object> inserParams = new ArrayList<>();
        List<String> fieldList = getFieldList(cls);
        sb.append("insert into " + getTableName(cls) + "(" + StringUtils.join(fieldList, ",") + ")");
        sb.append("values (");
        for (Field f : fields) {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), cls);
                Method getMethod = pd.getReadMethod();
                Object valObj = ReflectionUtils.invokeMethod(getMethod, t);
                inserParams.add(valObj);
                sb.append("?,");
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        insertResult.setInsertSql(sb.toString());
        insertResult.setInsertParams(inserParams);
        return insertResult;
    }

    public static <T> InsertResult getBatchInsertResult(List<T> tList) {
        if(CollectionUtils.isEmpty(tList)){
            throw new RuntimeException("集合不能为空");
        }
        InsertResult insertResult = new InsertResult();
        Class cls = tList.get(0).getClass();
        Field[] fields = cls.getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        List<Object> inserParams = new ArrayList<>();
        List<String> fieldList = getFieldList(cls);
        sb.append("insert into " + getTableName(cls) + "(" + StringUtils.join(fieldList, ",") + ")");
        sb.append("values ");
        for (T t : tList) {
            sb.append("(");
            for (Field f : fields) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(f.getName(), cls);
                    Method getMethod = pd.getReadMethod();
                    Object valObj = ReflectionUtils.invokeMethod(getMethod, t);
                    inserParams.add(valObj);
                    sb.append("?,");
                } catch (IntrospectionException e) {
                    throw new RuntimeException(e);
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("),");
        }
        sb.deleteCharAt(sb.length() - 1);
        insertResult.setInsertSql(sb.toString());
        insertResult.setInsertParams(inserParams);
        return insertResult;
    }


    /**
     * 拼接where语句，获取where参数
     *
     * @param dataFilter
     * @return
     */
    public static FilterResult getFilterResult(DataFilter dataFilter) {
        List<DataFilterElement> filterElementList = dataFilter.getFilterElementList();
        if(CollectionUtils.isEmpty(filterElementList) || null == dataFilter.getDataOrderElement()){
            return new FilterResult("",new ArrayList<>());
        }
        FilterResult fr = new FilterResult();
        StringBuffer sb = new StringBuffer();
        List<Object> objects = new ArrayList<>();
        filterElementList.forEach(e -> {
            Field field = null;
            try {
                field = dataFilter.getCls().getDeclaredField(e.getFieldName());
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }
            // 获取字段类型
            Class fieldCls = field.getType();
            if (sb.toString().equals("")) {
                sb.append(" where ");
            } else {
                sb.append(" and ");
            }
            switch (e.getFilterOperatorEnum()) {
                case OPERATOR_LIKE:
                    sb.append(" ").append(toUnderlineCase(e.getFieldName())).append(" like '%").append("?%' ");
                    break;
                case OPERATOR_LEFT_LIKE:
                    sb.append(" ").append(toUnderlineCase(e.getFieldName())).append(" like '").append("?%' ");
                    break;
                case OPERATOR_RIGHT_LIKE:
                    sb.append(" ").append(toUnderlineCase(e.getFieldName())).append(" like '%").append("?' ");
                    break;
                default:
                    sb.append(" ").append(toUnderlineCase(e.getFieldName())).append(" ").append(e.getFilterOperatorEnum().getCode()).append(" ? ");
                    break;
            }
            objects.add(e.getVal());
        });
        fr.setFilterSql(sb.toString());
        fr.setFilterParams(objects);
        return fr;
    }

    /**
     * 拼接排序语句，获取where参数
     *
     * @param dataFilter
     * @return
     */
    public static String getOrderSql(DataFilter dataFilter) {
        DataOrderElement orderElement = dataFilter.getDataOrderElement();
        if(null == orderElement.getOrderOperatorEnum() || CollectionUtils.isEmpty(orderElement.getFieldNames())) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(" order by ");
        orderElement.getFieldNames().forEach(fieldName -> {
            try {
                sb.append(dataFilter.getCls().getDeclaredField(fieldName)).append(",");
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ").append(dataFilter.getDataOrderElement().getOrderOperatorEnum().getCode()).append(" ");
        return sb.toString();
    }

    /**
     * 获取完整对象集合查询语句
     *
     * @param dataFilter
     * @return
     */
    public static String getListSql(DataFilter dataFilter) {
        String selectSql = getSelectSql(dataFilter.getCls());
        FilterResult filterResult = getFilterResult(dataFilter);
        String orderSql = getOrderSql(dataFilter);
        return selectSql + filterResult.getFilterSql() + orderSql;
    }

    /**
     * 获取完整修改对象语句
     *
     * @param t
     * @param dataFilter
     * @param <T>
     * @return
     */
    public static <T> String getUpdateSql(T t, DataFilter dataFilter) {
        ModifyResult modifyResult = getModifyResult(t);
        FilterResult filterResult = getFilterResult((dataFilter));
        return modifyResult.getModifySql() + filterResult.getFilterSql();
    }

    /**
     * 拼接删除sql
     *
     * @param dataFilter
     * @param <T>
     * @return
     */
    public static <T> String getDeleteSql(DataFilter dataFilter) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from " + getTableName(dataFilter.getCls()));
        return sb.toString();
    }


    public static void main(String[] args) {
//        DataFilter<ExperimentData> filter = new DataFilter<>(ExperimentData.class);
//        ExperimentData data = new ExperimentData();
//        data.setObjId("4219");
//        data.setEquipId("029020000000000002092534");
//        data.setTestTime(new Date(System.currentTimeMillis()));
//        JdbcTemplate jdbcTemplate1 = new JdbcTemplate();
    }

}
