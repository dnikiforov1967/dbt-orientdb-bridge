/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbt.orient.bridge.core.test.performance;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author dmitry
 */
public class OrientDBCRUDNGTest {
    
    static private OrientGraphFactory factory;
    
    public OrientDBCRUDNGTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @BeforeClass
    public static void setUpClass() throws Exception {
        factory = new OrientGraphFactory("remote:localhost/demo", "admin", "admin");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (factory != null) {
            factory.close();
        }    
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
    
    @Test
    public void testInsert() {
        OrientGraph graph = null;
        try {
            long t0 = System.currentTimeMillis();
            graph = factory.getTx();
            long tr = System.currentTimeMillis();
            for(int i=0; i<100; i++) {
                Map<String,Object> propMap = new HashMap<>();
                propMap.put("eId", UUID.randomUUID().toString());
                propMap.put("externalId", UUID.randomUUID().toString());
                propMap.put("name", "name"+i);
                final OrientVertex owner = graph.addVertex("class:Owner",propMap);
            }
            long t1 = System.currentTimeMillis();
            graph.commit();
            long t2 = System.currentTimeMillis();
            System.out.println("In milliseconds 100 owners insertion take "+(t2-t0));
            System.out.println("In milliseconds getTx takes "+(tr-t0));
            System.out.println("In milliseconds commit takes "+(t2-t1));
        }
        catch(Exception e) {
            if (graph!=null) {
                graph.rollback();
            }
            throw e;
        }
        finally {
            if (graph!=null) {
                graph.shutdown();
            }
        }
    }
}
