package atom.tools.very.simple.mapper;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Random;

import org.junit.Test;

import atom.generated.comparator.Mapper_Info;
import atom.generated.factory.Equals_Info;
import atom.generated.factory.Factory_Info;
import atom.generated.factory.ToString_Info;
import atom.tools.very.simple.mapper.p1.Info;

public class MapperTest {

	@Test
	public void test() throws Exception {
		try {
			Info info1 = Factory_Info.createNewInstanceRandom();
			Info info2 = Factory_Info.createNewInstanceRandom();
			assertNotNull(info1);
		//	boolean b = Equals_Info.equals(info1, info1);
		//	assertTrue(Equals_Info.equals(info1, info1));
			System.out.println("info1 "+info1.getA()+"   "+info1.hashCode());
			System.out.println("info2 "+info2.getA()+"   "+info2.hashCode());
			assertTrue(Equals_Info.equals(info1, info2));
			atom.tools.very.simple.mapper.p2.Info i = Mapper_Info.map(info1);
			assertNotNull(i);
			System.out.println(" -------------------------------------------------------------- 1");
			System.out.println(ToString_Info.toString(info1));
			Info info_1_1 = Mapper_Info.map(i);
			System.out.println(" -------------------------------------------------------------- 2");
			System.out.println(ToString_Info.toString(info_1_1));
			boolean b =Equals_Info.equals(info1, info_1_1);
			assertFalse(b);
			System.out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Random random = new Random();

}
