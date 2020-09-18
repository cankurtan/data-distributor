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

import ack.model.SourceTemplate;


public class PropertyDistributor {
	
	private final List<Property> properties = new ArrayList<>();
	private Map<Resource, List<Statement>> resourceMap;
	private Model model;

	public PropertyDistributor(List<Property> properties) {
		this.properties.addAll(properties);
		Collections.shuffle(properties);
	}

	public Model createSource(SourceTemplate template, 
			Map<Resource, List<Statement>> resourceMap) {
		this.resourceMap = resourceMap;
		model = ModelFactory.createDefaultModel();
		switch (template.getType()) {
		case COMPLETE:
			createCompleteSourceForProperties(template.getMinNumberOfProperties());
			break;
		case PARTIAL_RANDOM:
			createPartialSourceForProperties(template.getMinNumberOfProperties(), 
					template.getMaxNumberOfProperties(), false);
			break;
		case PARTIAL_ORDERED:
			createPartialSourceForProperties(template.getMinNumberOfProperties(), 
					template.getMaxNumberOfProperties(), true);
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
	private void createPartialSourceForProperties(int min, int max, boolean isOrdered){
		List<Property> selectedProperties = getPartOfProperties(max);
		Collections.shuffle(selectedProperties);
		Random rnd = ThreadLocalRandom.current();
		for(List<Statement> statements : resourceMap.values()) {
			int x = min + rnd.nextInt(max-min);
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
