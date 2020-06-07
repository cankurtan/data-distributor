package ack.rdf.query;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;

/**
 * Auto-closable abstract database query to prepare query execution
 * 
 * @author cankurtan
 *
 */
public abstract class DbQuery implements AutoCloseable {

	private QueryExecution queryExecution;
	protected final Query query;
	
	public DbQuery(Query query) {
		this.query = query;
	}

	protected abstract QueryExecution initQueryExecution();

	public QueryExecution getQueryExecution() {
		if(queryExecution == null) {
			this.queryExecution = initQueryExecution();
		}
		return queryExecution;
	}

	public Query getQuery() {
		return query;
	}

	@Override
	public void close() {
		this.queryExecution.close();
	}
}
