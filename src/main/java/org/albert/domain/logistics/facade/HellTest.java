package org.albert.domain.logistics.facade;


import org.springframework.stereotype.Component;

@Component
public class HellTest {

	public void say(String str, String traceId) {
		System.out.println("hello " + str);
	}

}
