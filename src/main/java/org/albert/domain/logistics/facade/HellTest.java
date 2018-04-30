package org.albert.domain.logistics.facade;


import java.util.Map;

import javax.inject.Inject;

import org.albert.common.domain.Page;
import org.albert.domain.dictionary.core.domain.OperLog;
import org.springframework.stereotype.Component;

@Component
public class HellTest {
	
    @Inject
    private OperLog operLog;

	public void say(String str, String traceId) {
		System.out.println("hello " + str);
	}
	
    public Page<OperLog> selectOperLogPage(int currentPage, int pageSize, Map<String, Object> params,String traceId) {
        return operLog.selecOperLogpage(currentPage, pageSize, params);
    } 

}
