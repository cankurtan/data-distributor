package ack.rdf.query;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;

/**
 * Auto-closable query of local model
 * 
 * @author cankurtan
 *
 */
public class RDFModelQuery extends DbQuery {

	private Model model;

	public RDFModelQuery(Query query, Model model) {
		super(query);
		this.model = model;
	}

	@Override
	protected QueryExecution initQueryExecution() {
		QueryExecution ex = QueryExecutionFactory.create(this.query, this.model);
		return ex;
	}

}
