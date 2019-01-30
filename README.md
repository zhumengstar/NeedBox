# NeedBox校园信息共享平台
__________________________

## 一、项目简介
互联网的迅速发展，使得信息的同城化和本地化成为互联网信息服务行业发展的一个趋势，为了能给我校学生和周边市民的日常生活带来更多的便利，设计了NeedBox校园信息共享平台。一方面能够对平台信息进行实时展示，另一方面也为用户提供了开店服务，让他们自助发布校园信息。通过这样一个平台，从而达到了消息发布者和消息获得者互惠互利、创造双赢的目的。

## 二、项目功能
- 用户登录、用户注册
- 修改密码、绑定帐号
- 浏览店铺、查看商品
- 店铺搜索、商品搜索
- 店铺管理（创建、修改）
- 商品类别管理（创建、删除）
- 商品管理（添加、修改、上下架）
- 微信访问

## 三、项目技术
- 使用Maven管理jar包
- 整合SSM(Spring+SpringMVC+MyBatis)框架进行业务逻辑处理
- 配置c3p0数据库连接池
- 使用拦截器进行登录校验和权限校验
- 使用开源工具Thumbnail处理图片
- 引入组件Kaptcha实现验证码功能
- 使用Logback进行日志管理
- 使用DES对数据库配置文件明文信息进行加密
- 使用MD5对用户密码进行加密
- 将项目部署到云服务器上
- 添加微信自动注册登录入口
- ......

## 四、项目结构
![](https://s1.ax1x.com/2018/12/10/FGvdET.png)
- src/main/java：java代码
- src/main/resources：项目所用到的资源文件，比如spring、mybatis、日志等配置文件
  - src/main/resources/spring：spring相关的配置文件
  - src/main/reources/mapper：dao中每个方法对应的sql，mybatis帮我们实现
- src/main/resources/webapp：web应用的目录，包括WEB-INF，js，css等静态资源
  - src/main/resources/webapp/resources：前端的静态资源（js、css、图片）
  - src/main/resources/webapp/WEB-INF：web应用的安全目录（html）
- src/test/java：单元测试的java代码
- src/test/resources：单元测试所用到的资源文件
