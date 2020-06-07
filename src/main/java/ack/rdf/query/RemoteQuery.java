package ack.rdf.query;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;

/**
 * Auto-closable remote data source query class
 * 
 * @author cankurtan
 */
public class RemoteQuery extends DbQuery {

	private String endpoint;
	
	public RemoteQuery(Query query, String endpoint) {
		super(query);
		this.endpoint = endpoint;
	}

	protected QueryExecution initQueryExecution() {
		final QueryExecution ex = QueryExecutionFactory.sparqlService(endpoint, query);
		ex.setTimeout(10000L);
		return ex;
	}
}
