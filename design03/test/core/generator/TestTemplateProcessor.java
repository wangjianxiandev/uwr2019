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
import static org.powermock.api.easymock.PowerMock.*;

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
		verifyAll();
	}

	@Before
	public void setUp() throws Exception {

		//使用EasyMock建立一个DataSourceConfig和DataHolder的实例
		dsc = EasyMock.mock(DataSourceConfig.class);
		ArrayList<DataHolder> dataHolders = new ArrayList<>();
		DataHolder dh1 = EasyMock.mock(DataHolder.class);
		DataHolder dh2 = EasyMock.mock(DataHolder.class);
		DataHolder dh3 = EasyMock.mock(DataHolder.class);
		dh1.setName("sex");
		dh2.setName("readme");
		dh3.setName("testexpr");
		EasyMock.expect(dh1.getValue()).andReturn("Female");
		EasyMock.expect(dh2.getValue()).andReturn("5");
		EasyMock.expect(dh3.getExpr()).andReturn("${num}+${readme}");
		EasyMock.expect(dh3.fillValue()).andReturn("5.0");

		dataHolders.add(dh1);
		dataHolders.add(dh2);
		dataHolders.add(dh3);

		//获取DataSource实例
		DataSource ds = EasyMock.mock(DataSource.class);



		//使用PowerMock的静态mock
		PowerMock.mockStatic(DataSourceConfig.class);


		replayAll(dsc);
		//初始化一个待测试类（SUT）的实例
		tp = new TemplateProcessor();
	}
}
