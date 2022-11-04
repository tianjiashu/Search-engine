# Search-engine
项目名称：仿搜索引擎项目 【纯后端项目（之后再加上前端）】



[TOC]



## 项目运行
1. 创建两个数据库 favorites 和 my_search_engine。然后导入sql文件中的两个sql文件到对应的数据库中。初始化数据库

2. 修改SpringBoot配置文件的数据源配置。


## 技术栈
Spring Boot、Spring Security、MyBatis-plus、Redis、Pytorch、ShardingSphere、Spring-Aop、布隆过滤器、jieba分词、JWT、lombok、Socket

## 功能说明

1. 支持搜索存文本信息,用户可通过"- 过滤词"自定义关键字过滤。
3. 支持搜索结果按条目分页展示，实现了关联度算法，把关联度高的信息优先展示。
5. 支持“相关搜索”功能。
6. 支持搜索时搜索下拉框联想词推荐。
7. 支持用户注册、登录、注销。
8. 支持用户收藏夹功能。
   - 用户可以新增、删除、重命名个人收藏夹。
   - 用户可以收藏搜索结果里的一条或多条结果，放入其中一个收藏夹。
   - 用户可以删除一个或多个收藏夹里的内容。
9. 支持搜索图片
   - 用户输入纯文本，根据纯文本搜索出与文本语义相关的图片数据。
10. 支持以图搜图
    - 用户上传一张图片，可以搜索出与上传图片语义相似的图片。
9. 支持实时显示热门搜索





## 技术说明

1. 分词采用开源的[jieba](https://github.com/huaban/jieba-analysis)分词库。首先将[悟空数据集](https://wukong-dataset.github.io/wukong-dataset/download.html)所有的文本数据进行分词创建倒排索引，并建立分词-文本关系表。
   - 说明：其中涉及到了动态创建数据库，后期加入了ShardingSphere进行分词配置。所以项目中的初始化分词建表的代码不能使用，但不影响项目运行，这属于项目初期的工作。为此直接提供了数据库Sql文件，此工作可直接跳过。
2. 搜索结果按条目分页展示采用了mybatis-plus分页插件。
3. 关联度算法采用jieba分词库自带的[tfidf算法](https://zh.m.wikipedia.org/zh-cn/Tf-idf)
4. 关键词过滤类似谷歌搜索"-过滤词"的方式，使用正则匹配校验是否为过滤词，支持多个过滤词。
5. 为了加快搜索引擎的搜索速度，使用B+树索引以及分表来进行优化。
6. 搜索时搜索下拉框联想词推荐使用[Trie树](https://zh.m.wikipedia.org/zh-cn/Trie)（也称前缀树）实现。
7. 以图搜图采用[CLIP](https://github.com/openai/CLIP)预训练模型对图像进行编码提取特征，通过计算图像嵌入编码之间余弦相似度搜索相似图片。
   - 说明：为了加快搜索速度，采用了CUDA加速模型计算，并提前将图像库每一张图像计算了图像嵌入编码并持久化为pickle文件。
8. 登录、注销采用Spring Security + JWT + Redis，利用用户的id生成token签名，并存储在Redis中，前端每一次请求都要检查token签名是否存在。
9. 用户收藏夹功能也同样采用了分表，一个用户对应一个收藏夹数据库表【数据库表名为用户的id】。每当用户注册的时，就会动态新建一个msql数据表，用来存储用户的收藏夹以及内容。
   - 说明：由于动态建表和ShardingSphere冲突，因此创建两个数据源，一个ShardingSphere数据源，一个默认数据源。
10. 采用Spring-aop来对每一个需要解析token的方法进行增强。
11. 热搜功能 模仿微博热搜 采用了定时查询功能。
12. 为了之后前后端的分离开发，项目解决了跨域请求的问题。



## 其他说明

由于没有前端页面，我们提供了后端的postman测试文件。[百度云盘链接](https://pan.baidu.com/s/1QTzOkwtdR1P4w-G2rXHUpQ)提取码：y04f

欢迎访问本人的个人博客：http://luckydog123.top
