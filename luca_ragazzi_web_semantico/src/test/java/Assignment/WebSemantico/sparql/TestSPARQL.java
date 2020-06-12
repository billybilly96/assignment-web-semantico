package Assignment.WebSemantico.sparql;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.jena.arq.querybuilder.SelectBuilder;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import Assignment.WebSemantico.Main;
import Assignment.WebSemantico.utils.file.FilesUtils;
import Assignment.WebSemantico.utils.ontology.OWLOntologyWithTools;
import Assignment.WebSemantico.utils.ontology.OntologyLoadUtils;
import io.reactivex.functions.Consumer;

public class TestSPARQL {
	private static final String fileNameForTestMyOnto = "Test_Sparql_MyDataPath.ttl";
	private static final String completeOntoFilePath = OntologyLoadUtils.ontologyPath + FilesUtils.fileSeparator + fileNameForTestMyOnto;
	
	@BeforeClass public static void init() {
		try {
			OWLOntologyWithTools ontology = Main.createMyPathDataOntology();
			Main.setEgoVehiclePathFromSouthToNorth(ontology);
			Main.addGeneralSwrlRulesToOntology(ontology);
			OntologyLoadUtils.saveOntologyIntoFileUsingTurtleSyntax(fileNameForTestMyOnto, ontology);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test public void testQueryDBPedia() {
		Query query = QueryFactory.create("Select ?s { ?s a <http://dbpedia.org/ontology/City> } Limit 10");
		SparqlUtils.execOnceTheQueryFromWebModel("http://dbpedia.org/sparql", "http://dbpedia.org", 
				query,	new Consumer<ResultSet>() {
							@Override
							public void accept(ResultSet t) throws Exception {
								assertTrue("Query bdPedia asking for 10 cities should retun '10' cities.", SparqlUtils.count(t) == 10);
							}
						});
    }
	
	@Test public void testSearchMapIntersections() throws IOException, InterruptedException {
		Model model = FileManager.get().loadModel(completeOntoFilePath, null, "TTL");
        // Ask for of intersections
		Query queryIntersection = QueryFactory.create("Select ?s { ?s a <http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Map#Intersection> }");
        SparqlUtils.execOnceTheQuery(model, queryIntersection, new Consumer<ResultSet>() {
			@Override
			public void accept(ResultSet t) throws Exception {
				assertTrue("Query MyOntology asking for intersections should return '1' intersection.", SparqlUtils.count(t) == 1);
			}
		});
    }
	
	@Test public void testSearchMapRoadSegments() throws IOException, InterruptedException {
		Model model = FileManager.get().loadModel(completeOntoFilePath, null, "TTL");
		 // Ask for the road segments
        Query queryRoadSegments = QueryFactory.create("Select ?s { ?s a <http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Map#RoadSegment> }");
        SparqlUtils.execOnceTheQuery(model, queryRoadSegments, new Consumer<ResultSet>() {
			@Override
			public void accept(ResultSet t) throws Exception {
				assertTrue("Query MyOntology asking for road segments should return '4' road Segments.", SparqlUtils.count(t) == 4);
			}
		});
    }
	
	@Test public void testSearchOneWayLanes() throws IOException, InterruptedException {
		// Ask for the one way lane
		Model model = FileManager.get().loadModel(completeOntoFilePath, null, "TTL");
		Query queryOneWayLane = QueryFactory.create("Select ?s { ?s a <http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Map#OneWayLane> }");
		SparqlUtils.execOnceTheQuery(model, queryOneWayLane, new Consumer<ResultSet>() {
			@Override
			public void accept(ResultSet t) throws Exception {
				assertTrue("Query MyOnto asking for road segments should return '8' one way lanes.", SparqlUtils.count(t) == 8);
			}
		});
    }
	
	@Test public void testSearchRoadsConnetedToIntersection() throws IOException, InterruptedException {
		Model model = FileManager.get().loadModel(completeOntoFilePath, null, "TTL");
        // Ask for the subjects of the triples <?s, maponto:isConnectedTo, myonto:indivIntersection> 
        SelectBuilder selectBuilder = new SelectBuilder();
        selectBuilder.addPrefix("maponto", "http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Map#")
          			 .addPrefix("myonto", "http://assignment-ws/my-ontology#")
                     .addVar("?s")
                     .addWhere("?s", "maponto:isConnectedTo", "myonto:indivIntersection");
        
        SparqlUtils.execOnceTheQuery(model, selectBuilder.build(), new Consumer<ResultSet>() {
			@Override
			public void accept(ResultSet t) throws Exception {
				assertTrue("Query MyOntology for the subjects of the triples <?s, maponto:isConnectedTo, myonto:indivIntersection> "
						+ "should return '4' elements.", SparqlUtils.count(t) == 4);
			}
		});
    }
	
	@Test public void testLanesOfTheRoadConnectedToTheIntersection() throws IOException, InterruptedException {
      // Ask for the one way lane
	  Model model = FileManager.get().loadModel(completeOntoFilePath, null, "TTL");
      // Ask for the objects of the triples <?s, maponto:isConnectedTo, myonto:indivIntersection> and <?s, maponto:hasLane, ?o>
      SelectBuilder selectBuilder = new SelectBuilder();
      selectBuilder.addPrefix("maponto", "http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Map#")
                   .addPrefix("myonto", "http://assignment-ws/my-ontology#")
                   .addVar("?o")
                   .addWhere("?s", "maponto:isConnectedTo", "myonto:indivIntersection")
                   .addWhere("?s", "maponto:hasLane", "?o");
        
      SparqlUtils.execOnceTheQuery(model, selectBuilder.build(), new Consumer<ResultSet>() {
    	  @Override
    	  public void accept(ResultSet t) throws Exception {
    		  assertTrue("Query MyOntology or the object of the triples <?s, maponto:isConnectedTo, myonto:indivIntersection> "
						+ "and <?s, maponto:hasLane, ?o> should return '8' elements.", SparqlUtils.count(t) == 8);
    	  }
      });
    }

	@Test public void testSearchSpecialVehicle() throws IOException, InterruptedException {
		// Ask for the special vehicle
		Model model = FileManager.get().loadModel(completeOntoFilePath, null, "TTL");
		Query querySpecialVehicle = QueryFactory.create("Select ?s { ?s a <http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Car#SpecialVehicle> }");
		SparqlUtils.execOnceTheQuery(model, querySpecialVehicle, new Consumer<ResultSet>() {
			@Override
			public void accept(ResultSet t) throws Exception {
				assertTrue("Query MyOnto asking for special vehicle should return '1'.", SparqlUtils.count(t) == 1);
			}
		});
	}

	@Test public void testSearchSpeedLimit() throws IOException, InterruptedException {
		// Ask for the speed limit
		Model model = FileManager.get().loadModel(completeOntoFilePath, null, "TTL");
		Query querySpeedLimitValue = QueryFactory.create("Select ?s { ?so a <http://www.toyota-ti.ac.jp/Lab/Denshi/COIN/Map#SpeedLimit> }");
		SparqlUtils.execOnceTheQuery(model, querySpeedLimitValue, new Consumer<ResultSet>() {
			@Override
			public void accept(ResultSet t) throws Exception {
				assertTrue("Query MyOnto asking for speed limit should return '1'.", SparqlUtils.count(t) == 1);
			}
		});
	}
	
	@AfterClass public static void clean() {
		FilesUtils.deleteFile(new File(completeOntoFilePath));
	}
}
