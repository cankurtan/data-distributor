package ack.rdf;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import ack.model.DataSource;
import ack.model.PreparedQuery;
import ack.rdf.query.DbQuery;

public class DataSourceHandler {

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private DataSource source;

	public DataSourceHandler(DataSource source) {
		this.source = source;
	}
	
	public Set<RDFNode> getUrisForQuery(PreparedQuery pq) {
		Set<RDFNode> uriSet = new HashSet<>();
		QueryExecutor qe = new QueryExecutor(source);
		try(DbQuery dbq = qe.getDbQuery(pq.getQuery())){
			ResultSet rs = dbq.getQueryExecution().execSelect();
			while(rs.hasNext()) {
				uriSet.add(rs.nextSolution().get("sub"));
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, e.getMessage());
		}
		return uriSet;
	}
}
