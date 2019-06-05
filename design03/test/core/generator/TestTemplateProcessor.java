package core.generator;
import core.common.*;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(PowerMockRunner.class)
@PrepareForTest(DataSourceConfig.class)
@PowerMockIgnore("javax.management.*")
public class TestTemplateProcessor implements DataSourceType{
	//待测试类(SUT)的一个实例。
	private TemplateProcessor tp;
	//依赖类(DOC)的一个实例。
	private DataSourceConfig dsc;

	@Test
	public void testStaticVarExtract() throws Exception {
		//设置待测试类的状态（测试目标方法）
		tp.staticVarExtract("resource/newtemplatezzz.doc");
		//以下进行检查点设置
		DataSource ds = dsc.getConstDataSource();

		List<DataHolder> dhs = ds.getVars();
		DataHolder dh1 = ds.getDataHolder("sex");
		assertNotNull("变量sex解析为空", dh1);
		assertEquals("变量sex值获取错误","Female",dh1.getValue());

		DataHolder dh2 = ds.getDataHolder("readme");
		assertNotNull("变量readme解析为空", dh2);
		assertEquals("变量readme值获取错误","5",dh2.getValue());

		DataHolder dh3 = ds.getDataHolder("testexpr");
		assertNotNull("变量testexpr", dh3);
		assertEquals("变量testexpr的表达式解析错误","${num}+${readme}",dh3.getExpr());
		dh3.fillValue();
		assertEquals("变量testexpr","5.0",dh3.getValue());

		//检测SUT的实际行为模式是否符合预期
		//出现问题
		//DataSourceConfig.getDataSources(): expected: 1, actual: 0
		//DataSourceConfig.getFilename(): expected: 1, actual: 0
		//DataSourceConfig.getDataSource(null): expected: 1, actual: 0
//		Unexpected method call DataSourceConfig.newInstance():
//		DataSourceConfig.newInstance(): expected: 1, actual: 2

		PowerMock.verifyAll();
	}

	@Before
	public void setUp() throws Exception {
		//以下采用Mock对象的方式，做测试前的准备。
		//与以上方法比较，好处是降低SUT（TemplateProcessor类）与DOC（DataSourceConfig类）之间的耦合性，解耦它们。
		//从而使得定位缺陷变得容易。
		//参照流程：
		//1. 使用EasyMock建立一个DataSourceConfig类的一个Mock对象实例；
		//2. 录制该实例的STUB模式和行为模式（针对的是非静态方法）；
		dsc = EasyMock.createMock(DataSourceConfig.class);
		//根据DataSource中的内容创建ArrayList<DataHolder> 即挂载变量
		ArrayList<DataHolder> dataHolders = new ArrayList<>();
		ArrayList<DataSource> dataSources = new ArrayList<>();

		DataHolder dh1 = EasyMock.createMock(DataHolder.class);
		DataHolder dh2 = EasyMock.createMock(DataHolder.class);
		DataHolder dh3 = EasyMock.createMock(DataHolder.class);
		dh1.setName("sex");
		dh2.setName("readme");
		dh3.setName("testexpr");
		EasyMock.expect(dh1.getValue()).andStubReturn("Female");
		EasyMock.expect(dh2.getValue()).andStubReturn("5");
		EasyMock.expect(dh3.getExpr()).andStubReturn("${num}+${readme}");
		EasyMock.expect(dh3.fillValue()).andStubReturn(null);
		EasyMock.expect(dh3.getValue()).andStubReturn("5.0");

		dataHolders.add(dh1);
		dataHolders.add(dh2);
		dataHolders.add(dh3);

		//获取DataSource实例
		ConstDataSource ds = EasyMock.createMock(ConstDataSource.class);
		//设置挂载参数
		ds.setVars(dataHolders);
		EasyMock.expect(ds.getVars()).andStubReturn(dataHolders);
		EasyMock.expect(ds.getDataHolder("sex")).andStubReturn(dh1);
		EasyMock.expect(ds.getDataHolder("readme")).andStubReturn(dh2);
		EasyMock.expect(ds.getDataHolder("testexpr")).andStubReturn(dh3);
		EasyMock.expect(ds.getType()).andStubReturn("");
		//查看函数dsc.getConstDataSource();
		dataSources.add(ds);


		/**dsc中的非静态方法
		 * getDataSources()
		 * getFilename()
		 * getConstDataSource()
		 * getDataSource(String name)
		 * getDataHolder(String name)在上面进行
		 */
		EasyMock.expect(dsc.getDataSources()).andStubReturn(dataSources);
		EasyMock.expect(dsc.getFilename()).andStubReturn("UwrTest");
		EasyMock.expect(dsc.getConstDataSource()).andStubReturn(ds);
		EasyMock.expect(dsc.getDataSource(null)).andStubReturn(ds);
		//重放录制内容，不重放就会报空
		EasyMock.replay(ds, dh1, dh2, dh3);
		//System.out.println(dsc.getConstDataSource());
		//3. 使用PowerMock建立DataSourceConfig类的静态Mock；
		//4. 录制该静态Mock的行为模式（针对的是静态方法）；
		//使用PowerMock的静态mock
		PowerMock.mockStatic(DataSourceConfig.class);
		//对静态方法进行测试
		EasyMock.expect(DataSourceConfig.newInstance()).andStubReturn(dsc);
		//------------------------------------------------
		//5. 重放所有的行为。



		PowerMock.replayAll(dsc);
		//初始化一个待测试类（SUT）的实例
		tp = new TemplateProcessor();
	}
}
