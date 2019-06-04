#### 在easymock中使用anderturn与abdstubreturn
#### 在什么情况下,使用andstubreturn（），其中andreturn（）无法实现相同的结果
* andstubReturn（）用于那些我们不关心模拟对象测试的方法，但是我们需要模拟返回，否则代码将不起作用。
easymock没有验证andstubreturn（）方法，而andreturn（）方法是经过验证的。