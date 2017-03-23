/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbt.orient.bridge.core.test.performance;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
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
            for(int i=0; i<10; i++) {
                Map<String,Object> propMap = new HashMap<>();
                propMap.put("eId", UUID.randomUUID().toString());
                propMap.put("externalId", UUID.randomUUID().toString());
                propMap.put("name", "owner"+i);
                final OrientVertex owner = graph.addVertex("class:Owner",propMap);
                for(int j=0; j<100; j++) {
                    Map<String,Object> propMap2 = new HashMap<>();
                    propMap2.put("eId", UUID.randomUUID().toString());
                    propMap2.put("name", "car"+j); 
                    final OrientVertex car = graph.addVertex("class:Car",propMap2);
                    final OrientEdge edge = graph.addEdge("class:Category", owner, car, "category");
                    edge.setProperty("name", "edge"+j);
                }
            }
            long t1 = System.currentTimeMillis();
            graph.commit();
            long t2 = System.currentTimeMillis();
            System.out.println("In milliseconds 10 owners insertion take "+(t2-t0));
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
                OCommandSQL oCommandSQL = new OCommandSQL("DELETE EDGE Category");
                graph.command(oCommandSQL).execute();
                oCommandSQL = new OCommandSQL("DELETE VERTEX Car");
                graph.command(oCommandSQL).execute();
                oCommandSQL = new OCommandSQL("DELETE VERTEX Owner");
                graph.command(oCommandSQL).execute();
                graph.commit();                
                graph.shutdown();
            }
        }
    }
}
