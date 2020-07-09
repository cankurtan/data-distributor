package ack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SourceTemplate {

	public static enum Type{COMPLETE, PARTIAL_RANDOM, PARTIAL_ORDERED};
	
	private Type type;
	private int minNumberOfProperties;
	private int maxNumberOfProperties;
	public SourceTemplate(@JsonProperty(value = "type", required = true) Type type, 
			@JsonProperty(value = "min", required = true) int minNumberOfProperties, 
			@JsonProperty(value = "max", required = true) int maxNumberOfProperties) {
		super();
		this.type = type;
		this.minNumberOfProperties = minNumberOfProperties;
		this.maxNumberOfProperties = maxNumberOfProperties;
	}
	public Type getType() {
		return type;
	}
	public int getMinNumberOfProperties() {
		return minNumberOfProperties;
	}
	public int getMaxNumberOfProperties() {
		return maxNumberOfProperties;
	}
}
