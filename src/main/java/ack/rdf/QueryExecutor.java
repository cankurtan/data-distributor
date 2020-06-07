package ack.rdf;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import ack.model.DataSource;
import ack.rdf.query.DbQuery;
import ack.rdf.query.TDBQuery;
import ack.rdf.query.RDFModelQuery;

public class QueryExecutor {
	
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private final DataSource source;

	public QueryExecutor(DataSource source) {
		super();
		this.source = source;
	}

	public void test(){
		
		String testQ = "select distinct ?sub where {?sub ?pred ?obj} LIMIT 2";
		try(DbQuery q = getDbQuery(testQ)){
			ResultSet rs = q.getQueryExecution().execSelect();
			if(rs != null) {
				StringBuilder sb = new StringBuilder();
				sb.append("Test Query Results: ");
				while(rs.hasNext()) {
					sb.append(rs.nextSolution().get("sub").toString() + " ");
				}
				LOGGER.log(Level.INFO, sb.toString());
			}
			else {
				LOGGER.log(Level.INFO, "Test has failed for " + source.getName());
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public Model executeConstruct(String query) {
		try(DbQuery q = getDbQuery(query)) {
			Model model = q.getQueryExecution().execConstruct();
			return model;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			LOGGER.log(Level.SEVERE, "Query couldn't be created for " + query);
			return null;
		}
	}
	
	public DbQuery getDbQuery(String query) {
		Query q = QueryFactory.create(query);
		switch (source.getType()) {
		case TDB2:
			return new TDBQuery(q, source.getDataset());
		default:
			return new RDFModelQuery(q, source.getRDFModel());
		}
	}
	
}
