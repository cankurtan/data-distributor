package ack.distribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;


public class PropertyDistributor {

	public static enum Type{COMPLETE, PARTIAL_RANDOM, PARTIAL_ORDERED};
	
	private final List<Property> properties = new ArrayList<>();
	private final Map<Resource, List<Statement>> resourceMap;
	private Model model;

	public PropertyDistributor(List<Property> properties, 
			Map<Resource, List<Statement>> resourceMap) {
		this.properties.addAll(properties);
		this.resourceMap = resourceMap;
	}

	public Model createSource(Type type, int n) {
		model = ModelFactory.createDefaultModel();
		switch (type) {
		case COMPLETE:
			createCompleteSourceForProperties(n);
			break;
		case PARTIAL_RANDOM:
			createPartialSourceForProperties(n, false);
			break;
		case PARTIAL_ORDERED:
			createPartialSourceForProperties(n, true);
			break;
		}
		return model;
	}

	/**
	 * Assigns the same {@code n} properties to each resource 
	 * if statement for a selected property is available.
	 * 
	 * @param n max number of capabilities
	 * @return
	 */
	private void createCompleteSourceForProperties(int n){
		List<Property> selectedProperties = getPartOfProperties(n);
		for(List<Statement> statements : resourceMap.values()) {
			addStatements(statements, selectedProperties);
		}
	}

	/**
	 * Assigns random number of properties to each entity.
	 * Random number is between 1 and max number of capabilities.
	 *
	 * @param n max number of capabilities
	 * @param isOrdered is the order of capabilities fixed
	 */
	private void createPartialSourceForProperties(int n, boolean isOrdered){
		List<Property> selectedProperties = getPartOfProperties(n);
		Collections.shuffle(selectedProperties);
		Random rnd = ThreadLocalRandom.current();
		for(List<Statement> statements : resourceMap.values()) {
			int x = 1 + rnd.nextInt(n);
			addStatements(statements, selectedProperties.subList(0, x));
			if(!isOrdered) {
				Collections.shuffle(selectedProperties);
			}
		}
	}
	
	private void addStatements(List<Statement> statements, List<Property> selectedProperties) {
		for(Statement st : statements) {
			if(selectedProperties.contains(st.getPredicate())) {
				model.add(st);
			}
		}
	}
	
	/**
	 * Selects {@code n} properties from the list of properties.
	 * Shuffles the used properties and adds to the end of the list.
	 * @param n the number of properties
	 * @return
	 */
	private List<Property> getPartOfProperties(int n){
		List<Property> list = new ArrayList<>();	
		list.addAll(properties.subList(0, n));
		properties.subList(0, n).clear();
		Collections.shuffle(list);
		properties.addAll(list);
		return list;
	}
}
