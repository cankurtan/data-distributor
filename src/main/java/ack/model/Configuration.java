package ack.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

import ack.util.Utils;

public class Configuration {
	private final String query;
	private final DataSource[] sources;
	
	public Configuration(@JsonProperty(value = "query", required = true) String query, 
			@JsonProperty(value = "sources", required = true) DataSource[] sources) {
		super();
		this.query = query;
		this.sources = sources;
	}

	public String getQuery() {
		return query;
	}

	public DataSource[] getSources() {
		return sources;
	}

	@Override
	public String toString() {
		return "Configuration [query=" + query + ", sources:\n" + 
				Utils.printify(Arrays.asList(sources), System.lineSeparator()) + "]";
	};
	
}
