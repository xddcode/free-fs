# Free-Fs — 开源文件管理系统

---


<div align="center">

[![GitHub stars](https://img.shields.io/github/stars/dh-free/free-fs?logo=github)](https://github.com/dh-free/free-fs/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/dh-free/free-fs?logo=github)](https://github.com/dh-free/free-fs/network)
[![star](https://gitee.com/dh_free/free-fs/badge/star.svg?theme=dark)](https://gitee.com/dh_free/free-fs/stargazers)
[![fork](https://gitee.com/dh_free/free-fs/badge/fork.svg?theme=dark)](https://gitee.com/dh_free/free-fs/members)
[![AUR](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://gitee.com/dh_free/free-fs/blob/master/LICENSE)
</div>

##  :tw-1f4cb: 项目简介
Free-Fs 开源文件管理系统：基于 SpringBoot2.x + MyBatisPlus + MySQL + Shiro+ Layui等搭配七牛云存储实现的云资源原理系统。包含文件上传、删除、预览、云资源列表查询、下载、文件移动、重命名、目录管理、登录、注册、以及权限控制等功能。

#### 源码链接：

- Gitee：[https://gitee.com/dh_free/free-fs](https://gitee.com/dh_free/free-fs)
- Github：[https://github.com/dh-free/free-fs](https://github.com/dh-free/free-fs)

#### 开发文档： [https://xxxxxx]()

#### 体验地址： [http://47.105.128.150:8081/](http://47.105.128.150:8081/)

#### 内置账号

|     |   账号  |   密码|
|---  |--- | --- |
|  管理员   |  admin   |  admin   |
|  普通用户   |  fs|  fs   |

##  :tw-1f4cc: 项目技术栈

后端：

- Spring Boot 2.4.3
- orm: MyBatis Plus
- 数据库：MySQL 5.7
- 权限安全控制：shiro
- 本地缓存：ehcache
- 文件上传：七牛云 OSS

前端：

- Thymeleaf
- Layui v2.5.5
- Jquery
- Ajax

##  :tw-1f4bb: 项目开发环境
- 操作系统：Windows 10
- 构建工具：Maven
- 开发工具：Intellij IDEA
- 应用服务器：Apache Tomcat
- 接口测试工具：Postman
- 压力测试工具：Apache JMeter
- 版本控制工具：Gitee
- Java 版本：8
- idea开发插件: lombok
- 七牛云存储

##  :book: 项目结构
```
- sql                         项目数据库文件
- src
  - main
    - java
    - com.free.fs
      - common                公共模块
        - aspect              自定义切面
        - config              系统基本配置，web跨域和七牛云配置
        - constant            系统常量
        - exception           全局异常处理和自定义异常
        - properties          资源读取类
        - shiro               shiro配置与自定义认证和授权
        - utils               系统工具类
      - controller            控制层
      - mapper                mapper接口层
      - model                 实体模型层
      - service               业务接口
        -impl                 业务接口实现
      - xxApplication.java    启动类
  - resources
    - mapper                  mybatis mapper.xml
    - shiro                   缓存配置文件
    - static                  静态资源包存放js css 第三方插件
    - templates               静态页面 html
    - application.yml         配置文件
    - application-dev.yml     开发环境配置
    - application-pro.yml     生产环境配置
    - application-preview.yml 演示环境配置

```
##  :tw-1f4f7: 界面预览
登录页：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0317/141545_ecc0619a_4951941.png "login.png")
注册页：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0317/141609_07de32bf_4951941.png "reg.png")
主页：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0317/141739_a327df31_4951941.png "index.png")
资源操作：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0317/141814_39f4030f_4951941.png "view.png")
目录管理：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0317/141837_3d08629c_4951941.png "dir.png")
重命名：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0317/141853_e968e4a5_4951941.png "rename.png")
添加文件夹：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0317/141910_c98bcc58_4951941.png "addFalod.png")
移动文件或目录：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0317/141929_f5dc52ec_4951941.png "move.png")
删除文件：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0317/141941_f77cd13e_4951941.png "delete.png")

##  :tw-2764: 特别鸣谢
- 感谢 [synchronized](https://eleadmin.com/) 大佬提供的前端模板

####  :fa-paperclip: 友情链接
- [EleAdmin](https://eleadmin.com/) 通用型后台管理模板，界面美观、开箱即用拥有丰富的扩展组件和模板页面，适合各类中后台应用。
- [Echo](https://gitee.com/veal98/Echo) 一套前后端不分离的开源社区系统。
##  :speech_balloon: 反馈交流
- QQ交流群：739596094

##  :tw-1f4de: 联系我
- 有什么问题也可以添加我的微信，请注明来意！
![输入图片说明](https://images.gitee.com/uploads/images/2021/0317/145547_114a3b71_4951941.jpeg "wechat.jpg")

