package com.dhu.web;

import com.dhu.model.DO.ListShowDO;
import com.dhu.model.VO.WeiBo.WeiBoListVO;
import com.dhu.service.WeiBoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {
	@Autowired
	WeiBoService weiBoService;

	@Test
	public void contextLoads() {
		ListShowDO showDO = new ListShowDO();
		showDO.setIndex(1);
		showDO.setSize(10);
		showDO.setKey("洗钱");
		WeiBoListVO informationForList = weiBoService.getInformationForList(showDO);
		System.out.println(informationForList.getVoList().toString());
	}

}

