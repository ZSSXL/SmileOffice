package com.zss.smile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhoushs@dist.com.cn
 * @date 2021/11/22 11:11
 * @desc
 */
public class OtherTest {

    @Test
    public void test() {
        int i = 5;
        // 010
        // 101 = 3
        // 1 2  3  4   5   6   7   8
        // 1 10 11 100 101 110 111 1000
        // 0000 0000 0000 0000 0000 0101
        // 取反：1111 1111 1111 1111 1111 1010
        // 减一：1111 1111 1111 1111 1111 1001
        // 取反：1000 0000 0000 0000 0000 0110

        int res = ~i;
        System.out.println(res);
    }
}



