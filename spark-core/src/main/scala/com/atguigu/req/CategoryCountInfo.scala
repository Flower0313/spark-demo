package com.atguigu.req

/**
 * @ClassName ScalaDemo-CategoryCountInfo 
 * @Author Holden_—__——___———____————_____Xiao
 * @Create 2021年10月26日23:26 - 周二
 * @Describe
 */
//样例类的属性默认是val修饰，不能修改；需要修改属性，需要采用var修饰
case class CategoryCountInfo(var categoryId: String,//品类id
                             var clickCount: Long,//点击次数
                             var orderCount: Long,//订单次数
                             var payCount: Long)//支付次数
