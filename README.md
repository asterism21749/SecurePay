# SecurePay
#### UPOP银联国际SecurePay(商户接入)
* 本项目在银联demo([SecurePay 5.1 SDK for Merchant Jan 2019](https://developer.unionpayintl.com/cjweb/api/detail?apiSvcId=4))的基础上，进行开发
* 需要先在网站上注册开发者账号，才能下载demo并看到测试数据
* 暂时只有消费接口(purchase)，其他接口也都按照demo进行改写即可
#### API参考
[参考银联国际 SecurePay api-interface](https://developer.unionpayintl.com/cjweb/api/detail?apiSvcId=4#api-interface)


#### 项目结构
* 项目采用springboot框架，maven作为项目管理工具
* lib文件夹下是银联的jar包，需要将其打包，才能在pom.xml中引用依赖
* 后续银联可能会升级bcprov-jdk.jar包版本，请确认使用银联官方最新版本
* logs存放输出日志
* 数据库建表脚本：src/main/resources/sunmange.sql
* maven打包时没有采用springboot默认的打包方式，而是将jar包分离到/target/lib文件夹下，须与打包生成的unionpay-*.jar同时上传至服务器
* 使用拦截器做了IP白名单，在数据库表中进行配置

#### 交易流程（以消费交易为例）
1. 商户请求银联消费接口，页面重定向至银联支付页面
2. 用户在支付页面填写相关信息，并确认支付
3. 用户等待银联返回交易结果，然后跳转回商户页面
4. 用户点击跳转商户页面时，银联会请求商户的fronUrl，并异步请求backUrl
5. 交易状态以backUrl为准
6. 收到frontUrl时，重定向至商户订单界面，可以直接展示订单信息，也可以等待后台收到backUrl

##### 其他的接口资料
