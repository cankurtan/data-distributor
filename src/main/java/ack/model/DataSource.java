package ack.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb2.TDB2Factory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Data Source that can be initiated as a connection to 
 * local TDB2, NT, or TURTLE file
 * 
 * @author cankurtan
 */
public class DataSource {
	
	public static enum Type {
		TURTLE, NT, TDB2;
	}

	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private Dataset dataset;
	private Model model;
	private final String name;
	private final Type type;
	private final String location;
	private final List<PreparedQuery> queries = new ArrayList<>();
	
	public DataSource(@JsonProperty(value = "name", required = true) String name, 
			@JsonProperty(value = "type", required = true) Type type, 
			@JsonProperty(value = "location", required = true) String location,
			@JsonProperty(value = "query-file", required = true) String queryFileName) {
		this.name = name;
		this.type = type;
		this.location = location;
		prepareAndAddQueries(queryFileName);
		setup();
	}
	
	private void setup() {
		if(type == Type.TDB2) {
			LOGGER.info(String.format("TDB2 connection to %s", this.location));
			this.dataset = TDB2Factory.connectDataset(location);
		} else if(type == Type.TURTLE || type == Type.NT) {
			this.model = ModelFactory.createDefaultModel();
			this.model.read(location);
			LOGGER.log(Level.INFO, String.format("model reading for type %s from %s", this.type, this.location));
		} else {
			LOGGER.log(Level.INFO, String.format("%s connection to %s", this.type, this.location));
		}
	}
	
	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}
	
	public void addQueries(Collection<PreparedQuery> collection) {
		this.queries.addAll(collection);
	}

	public void prepareAndAddQueries(String queryFileName) {
		if(queryFileName != null) {
			try {
				File file = new File(queryFileName);
				ObjectMapper mapper = new ObjectMapper();
				PreparedQuery[] queries = mapper.readValue(file, PreparedQuery[].class);
				addQueries(Arrays.asList(queries));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Dataset getDataset() {
		return dataset;
	}

	public List<PreparedQuery> getQueries() {
		return queries;
	}

	public Model getRDFModel() {
		return model;
	}

	@Override
	public String toString() {
		return "DataSource [name=" + name + ", type=" + type + ", location=" + location + "]";
	}
}
