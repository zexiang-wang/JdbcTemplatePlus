package com.aze;

import com.aze.entity.DataFilter;
import com.aze.entity.FilterResult;
import com.aze.entity.InsertResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.aze.JdbcTemplateUtil.*;

@Component
public class MyJdbcTemplate {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据过滤条件和排序条件查询数据
     *
     * @param dataFilter
     * @param <T>
     * @return
     */
    public <T> List<T> list(DataFilter<T> dataFilter) {
        String listSql = getListSql(dataFilter);
        Object[] params = getFilterResult(dataFilter).getFilterParams().toArray();
        return jdbcTemplate.query(listSql, new BeanPropertyRowMapper<T>(dataFilter.getCls()), params);
    }

    /**
     * 根据过滤条件和新值修改数据
     *
     * @param t
     * @param dataFilter
     * @param <T>
     * @return
     */
    public <T> int update(T t, DataFilter<T> dataFilter) {
        String updateSql = getUpdateSql(t, dataFilter);
        List<Object> params = new ArrayList<>();
        params.addAll(getModifyResult(t).getModifyParams());
        params.addAll(getFilterResult(dataFilter).getFilterParams());
        return jdbcTemplate.update(updateSql, params.toArray());
    }

    /**
     * 插入对象到数据库
     *
     * @param t
     * @param <T>
     * @return
     */
    public <T> int insert(T t) {
        InsertResult insertResult = getInsertResult(t);
        return jdbcTemplate.update(insertResult.getInsertSql(), insertResult.getInsertParams().toArray());
    }

    /**
     * 批量插入对象到数据库
     *
     * @param tList
     * @param <T>
     * @return
     */
    public <T> int batchInsert(List<T> tList) {
        InsertResult insertResult = getBatchInsertResult(tList);
        return jdbcTemplate.update(insertResult.getInsertSql(), insertResult.getInsertParams().toArray());
    }

    /**
     * 批量删除对象
     *
     * @param dataFilter
     * @param <T>
     * @return
     */
    public <T> int delete(DataFilter<T> dataFilter) {
        FilterResult filterResult = getFilterResult(dataFilter);
        String deleteSql = getDeleteSql(dataFilter) + filterResult.getFilterSql();
        return jdbcTemplate.update(deleteSql, filterResult.getFilterParams().toArray());
    }

}
