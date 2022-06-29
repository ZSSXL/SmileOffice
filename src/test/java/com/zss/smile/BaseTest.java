package com.zss.smile;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhoushs@dist.com.cn
 * @date 2021/11/15 10:59
 * @desc
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmileOfficeApplication.class)
public class BaseTest {

    @Before
    public void before (){
        System.out.println("========================= 开始测试 =========================");
    }

    @After
    public void after (){
        System.out.println("========================= 结束测试 =========================");
    }
}
