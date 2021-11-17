package com.atguigu.tansfrom.value;

import java.util.Random;

/**
 * @ClassName ScalaDemo-transTest_java
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月23日11:52 - 周六
 * @Describe
 */
public class transTest_java {
    public static void main(String[] args) {
        // 随机算法相同，种子相同，那么随机数就相同
        Random r1 = new Random(100);
        // 不输入参数，种子取的当前时间的纳秒值，所以随机结果就不相同了
        //Random r1 = new Random();

        for (int i = 0; i < 5; i++) {

            System.out.println(r1.nextInt(10));
        }

        System.out.println("--------------");

        Random r2 = new Random(100);
        //Random r2 = new Random();

        for (int i = 0; i < 5; i++) {

            System.out.println(r2.nextInt(10));
        }

    }
}
