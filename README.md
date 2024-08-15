<!--
 * @Date: 2023-06-23 17:37:37
 * @LastEditors: MajorTomMan 765719516@qq.com
 * @LastEditTime: 2024-07-28 21:49:55
 * @FilePath: \Guli\README.md
 * @Description: MajorTomMan @版权声明 保留文件所有权利
-->

# guli-market

## 谷粒商城

个人学习用项目

## 各微服务端口

因为各服务固定端口会产生各种冲突崩溃
为避免这种情况,所有微服务统一将port设置为0让springboot去寻找空闲端口来启动应用
前端通过访问网关来获取转发后的数据
网关通过访问nacos来获知其他微服务的端口并获取数据
