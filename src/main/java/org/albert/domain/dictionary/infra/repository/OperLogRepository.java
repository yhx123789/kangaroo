package org.albert.domain.dictionary.infra.repository;


import org.albert.domain.dictionary.core.domain.OperLog;
import org.albert.domain.dictionary.core.repository.IOperLogRepository;
import org.albert.domain.dictionary.infra.repository.sql.OperLogMapper;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * Created by Troy.Guo on 16/1/20.
 */
@Named
public class OperLogRepository implements IOperLogRepository {


    @Inject
    private OperLogMapper operLogMapper;

    /**
     * 查询操作日志
     * @param pageIndex
     * @param pageSize
     * @param startDate
     * @param endDate
     * @return
     */
    public List<OperLog> selectOperLog(int pageIndex, int pageSize, String startDate, String endDate,String personName) {
        return operLogMapper.selectOperLog(pageIndex, pageSize, startDate, endDate, personName);
    }

    /**
     * 插入操作日志
     * @param list
     * @return
     */
    public int insertOperLogBatch(List<OperLog> list) {
        return operLogMapper.insertOperLogBatch(list);
    }

    public List<OperLog> selectOperLogByDictionaryIdBatch(List<String> dictionaryIds) {
        return operLogMapper.selectOperLogByDictionaryIdBatch(dictionaryIds);
    }

    public int deleteOperLogByDictionaryIdBatch(List<String> dictionaryIds) {
        return operLogMapper.deleteOperLogByDictionaryIdBatch(dictionaryIds);
    }

    public List<OperLog> pageByCondition(Map<String, Object> params) {
        return operLogMapper.pageQueryByCondition(params);
    }

    public Long selectCountByCondition(Map<String, Object> params) {
        return operLogMapper.selectCountByCondition(params);
    }
}
