package Assignment.WebSemantico.sparql;

import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

import Assignment.WebSemantico.utils.logger.Logger;
import Assignment.WebSemantico.utils.logger.MessageType;
import io.reactivex.functions.Consumer;

public class SparqlUtils {
	private static String className = "SparqlUtils";
		
	public static Model createModelFromTurtleFile(String turtleFilePath) { 
		return FileManager.get().loadModel(turtleFilePath, null, "TTL");
	}
	
	public static Query createQuery(String query) {
		return QueryFactory.create(query);
	}
	
	public static void execOnceTheQueryFromWebModel(String service, String defaultGraphName, 
															Query query, Consumer<ResultSet> consumer) {
		// Create a query execution over DBpedia
		QueryExecutionFactoryHttp qexec = new QueryExecutionFactoryHttp(service, defaultGraphName);
		// Create a QueryExecution object from a query string ...
		QueryExecution qe = qexec.createQueryExecution(query);
		try {
			consumer.accept(qe.execSelect());
		} catch (Exception e) {
			Logger.log(MessageType.ERROR, className, "Error occurred during the consume of the ResultSet.");
			if (!Logger.disableStackTrace) e.printStackTrace();
		} finally {
			qe.close();
		    qexec.close();
		}
	}
	
	public static void execOnceTheQuery(Model model, Query query, Consumer<ResultSet> consumer) {
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			consumer.accept(qexec.execSelect());
		} catch (Exception e) {
			Logger.log(MessageType.ERROR, className, "Error occurred during the consume of the ResultSet.");
			if(!Logger.disableStackTrace) e.printStackTrace();
		} finally {
			qexec.close();
		}
	}
	
	public static void printTheModel(ResultSet model, Query query) {
		ResultSetFormatter.out(System.out, model, query);   
	}
	
	public static long count(ResultSet results) {
		int count = 0;
		while (results.hasNext()) {
			count++;
			results.next();
		}
		return count;
	}
}
