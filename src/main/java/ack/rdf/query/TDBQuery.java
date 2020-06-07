package ack.rdf.query;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ReadWrite;

/**
 * Auto-closable local dataset query
 * 
 * @author cankurtan
 *
 */
public class TDBQuery extends DbQuery {

	private final Dataset ds;
	
	public TDBQuery(Query query, Dataset ds) {
		super(query);
		this.ds = ds;
	}

	protected QueryExecution initQueryExecution() {
		if(ds != null) {
			ds.begin(ReadWrite.READ) ;
			return QueryExecutionFactory.create(query, ds);
		} else {
			return null;
		}
	}
	
	@Override
	public void close() {
		if(ds != null) {
			super.close();
			ds.end();
		}
	}
}
