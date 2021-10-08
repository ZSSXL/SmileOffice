# SmileOffice

**Desc:** 文档编辑器

**Author:** ZSS

---

### 项目说明

> 这是一个文档编辑器项目，基于开源软件`ONLYOFFICE Docs Community Edition`进行二次开发，实现对文档的创建、编辑、分享、查看，多人协同等操作...



###  开源说明

系统 100%开源，本软件遵循 MIT 开源协议



### 功能介绍

1. 用户可以新建文档、表格、幻灯片等文件
2. 用户可以上传自己的文件
3. 可对文件进行在线看查看和编辑
4. 支持文件重命名
5. 统计所拥有的各类文档数量
6. 文档收藏
7. 回收站功能
8. 可将文件分享他人 -- not yet
9. 可以共同编辑 -- not yet
10. 提供对外第三方文件预览接口
11. ......

### 运行环境

> OS: CentOS 7
>
> JDK: 1.8
>
> Docker: 18.06.3-ce
>
> Onlyoffice: 6.4

### 开发环境

> OS: Windows 10
>
> IDE: IntelliJ IDEA 2021.2 (Ultimate Edition)
>
> JDK: 1.8
>
> Onlyoffice: 6.4

### 关于Onlyoffice

以下是有关于Onlyoffice的简单介绍和安装以及相关注意事项

​		本次选的是`ONLYOFFICE Docs Community Edition - 6.4`，该程序是免费软件。 可以根据自由软件基金会发布的 GNU Affero 通用公共许可证 (AGPL) 第 3 版的条款重新分发和/或修改它。
​		使用Docker对其进行安装，以下是Docker安装命令：

```shell
docker run -d -p 6180:80 --name my_onlyoffice \
    -v /usr/local/onlyoffice/logs:/var/log/onlyoffice  \
    -v /usr/local/onlyoffice/data:/var/www/onlyoffice/Data  \
    -v /usr/local/onlyoffice/lib:/var/lib/onlyoffice \
	-v /usr/local/onlyoffice/apps:/var/www/onlyoffice/documentserver/web-apps/apps \
	-v /usr/local/onlyoffice/fonts:/var/www/onlyoffice/documentserver/core-fonts \
    -v /usr/local/onlyoffice/db:/var/lib/postgresql  onlyoffice/documentserver:6.4
```

其中`..\fonts`文件夹是存放中文字体，`..\apps`是对文档界面进行一些自定义的修改，如Logo等。



