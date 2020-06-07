package ack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PreparedQuery {
	
	private String query;
	private String property;
	private String className;
	
	public PreparedQuery(@JsonProperty(value = "query", required = true) String query, 
			@JsonProperty(value = "property", required = false) String property, 
			@JsonProperty(value = "class", required = false) String className) {
		this.query = query;
		this.property = property;
		this.className = className;
	}
	
	public boolean isClass () {
		return className != null;
	}
	public boolean isProperty() {
		return property != null;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "PreparedQuery [query=" + query + ", property=" + property + ", className=" + className + "]";
	}
}
