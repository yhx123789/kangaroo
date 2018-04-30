package org.albert.domain.dictionary.core.repository;

import org.albert.domain.dictionary.core.domain.OperLog;

import java.util.List;
import java.util.Map;


/**
 * Created by Troy.Guo on 16/1/20.
 */
public interface IOperLogRepository {

    /**
     * 查询操作日志
     *
     * @return
     */
    List<OperLog> selectOperLog(int pageIndex, int pageSize,
                                String startDate, String endDate,String personName);

    /**
     * 插入操作日志
     *
     * @return
     */
    int insertOperLogBatch(List<OperLog> list);

    /**
     * 批量根据dictionaryId获取操作日志
     * @param dictionaryIds
     * @return
     */
    List<OperLog> selectOperLogByDictionaryIdBatch(List<String> dictionaryIds);

    /**
     * 批量根据dictionaryIds删除日志
     * @param dictionaryIds
     * @return
     */
    int deleteOperLogByDictionaryIdBatch(List<String> dictionaryIds);

    /**
     * 根据条件查找总数，用于分页
     * @param params
     * @return
     */
    Long selectCountByCondition(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    List<OperLog> pageByCondition(Map<String, Object> params);

}
