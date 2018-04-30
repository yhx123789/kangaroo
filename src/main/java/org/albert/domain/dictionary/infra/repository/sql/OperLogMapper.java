package org.albert.domain.dictionary.infra.repository.sql;

import org.albert.domain.dictionary.core.domain.OperLog;
import org.apache.ibatis.annotations.Param;

import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
public interface OperLogMapper {

    /**
     * 查询操作日志
     *
     * @return
     */
    List<OperLog> selectOperLog(@Param("pageIndex") int pageIndex, @Param("pageSize") int pageSize,
                                @Param("startDate") String startDate, @Param("endDate") String endDate,
                                @Param("personName") String personName);

    /**
     * 插入操作日志
     *
     * @return
     */
    int insertOperLogBatch(@Param("list") List<OperLog> list);

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
     * 根据条件查询总数
     * @param params
     * @return
     */
    Long selectCountByCondition(@Param("selectCondition") Map<String, Object> params);

    /**
     * 分页查询
     * @param params
     * @return
     */
    List<OperLog> pageQueryByCondition(@Param("selectCondition") Map<String, Object> params);
}