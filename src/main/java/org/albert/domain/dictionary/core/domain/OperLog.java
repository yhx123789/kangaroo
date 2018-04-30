package org.albert.domain.dictionary.core.domain;

import org.albert.common.domain.Entity;
import org.albert.common.domain.Page;
import org.albert.domain.dictionary.core.repository.IOperLogRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named
public class OperLog extends Entity {

	@Inject
	private IOperLogRepository operLogRepository;

	private String dictionaryId;
	private Long operId;
	private String operName;
	private String description;
	private String content;
	private String modifyContent;
	private Date createAt;

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(String dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getModifyContent() {
		return modifyContent;
	}

	public void setModifyContent(String modifyContent) {
		this.modifyContent = modifyContent;
	}

	public Long getOperId() {
		return operId;
	}

	public void setOperId(Long operId) {
		this.operId = operId;
	}

	/**
	 * 查询操作日志
	 *
	 * @return
	 */
	public List<OperLog> selectOperLog(int pageIndex, int pageSize, String startDate, String endDate,
			String personName) {
		return operLogRepository.selectOperLog(pageIndex, pageSize, startDate, endDate, personName);
	}

	/**
	 * 插入操作日志
	 *
	 * @return
	 */
	public int insertOperLogBatch(List<OperLog> list) {
		return operLogRepository.insertOperLogBatch(list);
	}

	/**
	 * 插入操作日志
	 *
	 * @return
	 */
	public int insertOperLog(OperLog log) {
		List<OperLog> list = new ArrayList<OperLog>();

		list.add(log);
		return operLogRepository.insertOperLogBatch(list);
	}

	/**
	 * 批量根据dictionaryId获取操作日志
	 *
	 * @param dictionaryIds
	 * @return
	 */
	public List<OperLog> selectOperLogByDictionaryIdBatch(List<String> dictionaryIds) {
		return operLogRepository.selectOperLogByDictionaryIdBatch(dictionaryIds);
	}

	/**
	 * 批量根据dictionaryIds删除日志
	 *
	 * @param dictionaryIds
	 * @return
	 */
	public int deleteOperLogByDictionaryIdBatch(List<String> dictionaryIds) {
		return operLogRepository.deleteOperLogByDictionaryIdBatch(dictionaryIds);
	}

	/**
	 * 根据dictionaryId获取操作日志
	 *
	 * @param dictionaryId
	 * @return
	 */
	public List<OperLog> selectOperLogByDictionaryId(String dictionaryId) {
		List<String> ids = new ArrayList<String>();
		ids.add(dictionaryId);
		return this.operLogRepository.selectOperLogByDictionaryIdBatch(ids);
	}

	/**
	 * 根据dictionaryId删除日志
	 *
	 * @param dictionaryId
	 * @return
	 */
	public int deleteOperLogByDictionaryId(String dictionaryId) {
		List<String> ids = new ArrayList<String>();
		ids.add(dictionaryId);
		return this.operLogRepository.deleteOperLogByDictionaryIdBatch(ids);
	}

	/**
	 * 分页查询日志记录
	 *
	 * @param pageIndex
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public Page<OperLog> selecOperLogpage(int pageIndex, int pageSize, Map<String, Object> params) {
		if (params.containsKey("sort")) {
			params.put("sort", key2Column(String.valueOf(params.get("sort"))));
		} else {
			params.put("sort", key2Column(""));
		}
		if (params.containsKey("order")) {
			params.put("order", "desc");
		}
		Page<OperLog> page = new Page<OperLog>();
		page.setPage(pageSize, pageIndex);
		Long count = this.operLogRepository.selectCountByCondition(params);
		if (count != null) {
			page.setResultCount(count);
		}
		params.put("pageIndex", page.getStart());
		params.put("pageSize", page.getPageSize());
		List<OperLog> list = this.operLogRepository.pageByCondition(params);
		page.setData(list);
		return page;
	}

	public static String key2Column(String key) {
		String column;
		if ("createdAt".equals(key)) {
			column = "created_at";
		} else if ("id".equals(key)) {
			column = "id";
		} else {
			column = "created_at";
		}
		return column;
	}
}