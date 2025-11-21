package com.rootcore.common.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SampleMapperTest {
	@Autowired
	SampleMapper sampleMapper;

	@Test
	public void test() {
		System.out.println(sampleMapper.test());
	}
}
