package com.coolsen.common;

import com.coolsen.common.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonApplicationTests {

    @Test
    public void contextLoads() {

        System.out.println(DateUtils.dateToString(new Date()));
        System.out.println(DateUtils.dateToString(DateUtils.addDays(new Date(), 180)));
    }

}

