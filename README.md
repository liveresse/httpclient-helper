##Explain说明
####spring-httpclient-helper是将HttpClient与Spring整合的一个工具，HttpClient的一个帮助，对外只暴露一个多例的HttpClientApiService，该类封装了doGet和doPost等常用的Http请求方式使用了HttpClient的连接池技术，降低了延迟 支持了更大的并发，大大降低了繁琐的封装步骤
  
##Usage 使用

#####1.将项目编译成jar包依赖到自己的工程中
#####2.在applicationContext.xml 中引入httpclient.xml
       <import resource="classpath:META-INF/httpclient.xml"/>
#####3.集成的HttpClientApiService必须是多例的
        HttpClientApiService httpClientApiService = (HttpClientApiService) applicationContext.getBean("httpClientApiService");
        httpClientApiService.doGet(url);
        httpClientApiService.doPost(url,map);
        httpClientApiService.doPostJson(url,json);
##Setting 配置
       application.properties 中设置合适的参数，参考HttpClient官方配置 以下是默认值
       httpclient.maxTotal=500
       httpclient.defaultMaxPerRoute=500
       httpclient.connectTimeout=2000
       httpclient.connectionRequestTimeout=10000
       httpclient.socketTimeout=10000
       httpclient.idleConnectionEvictor=5000

##Contact me
#####Email:6371728@qq.com

    