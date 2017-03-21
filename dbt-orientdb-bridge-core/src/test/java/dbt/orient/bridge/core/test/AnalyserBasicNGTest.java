/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbt.orient.bridge.core.test;

import com.adition.middleware.dbt.orientdb.bridge.core.EntityAnalyzer;
import dbt.orient.bridge.core.test.entities.SimpleEntity;
import dbt.orient.bridge.core.test.entities.SimpleEntityWithTransient;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author dnikiforov
 */
public class AnalyserBasicNGTest {

	public AnalyserBasicNGTest() {
	}

	// TODO add test methods here.
	// The methods must be annotated with annotation @Test. For example:
	//
	// @Test
	// public void hello() {}
	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeMethod
	public void setUpMethod() throws Exception {
	}

	@AfterMethod
	public void tearDownMethod() throws Exception {
	}

	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void testNonEntityClass() {
		EntityAnalyzer.newInstance().createSchema(this.getClass());
	}
	
	@Test()
	public void testSimpleEntity() {
		final EntityAnalyzer.GraphSchema schema = EntityAnalyzer.newInstance().createSchema(SimpleEntity.class);
		assertEquals(schema.getEntityClass(),SimpleEntity.class);
		assertEquals(schema.getSetOfFields().size(),3);
	}

	@Test()
	public void testSimpleEntityWithTransient() {
		final EntityAnalyzer.GraphSchema schema = EntityAnalyzer.newInstance().createSchema(SimpleEntityWithTransient.class);
		assertEquals(schema.getEntityClass(),SimpleEntityWithTransient.class);
		assertEquals(schema.getSetOfFields().size(),3);
		assertEquals(schema.getTotalFieldCount(),4);
	}	

}
