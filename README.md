# uwr
Universal Word Report

1、项目配置：
### 修改newDatasource文件中的path为自己的项目路径
```
<?xml version="1.0" encoding="UTF-8"?>
<DataSourceConfig>
    <datasource type="const">
        <var type="value" name="name">工程名</var>
    </datasource>
    <datasource type="img" name="javalogo">     
        <path>E:/IdeaProjects/uwr2019/design03/resource/Java-logo.jpg</path>
    </datasource>
    <datasource type="xml" name="xml1">    
        <file>E:/IdeaProjects/uwr2019/design03/resource/employees.xml</file>
    </datasource>
 </DataSourceConfig>
```

#### 运行项目：

* run ReportGenerator，将命令行参数.txt中的命令粘贴到Edit Configurations中的项目参数中