package ack.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;

import ack.distribution.ResourceDistributor;
import ack.distribution.PropertyDistributor;
import ack.model.DataSource;
import ack.model.DataSource.Type;
import ack.rdf.RDFModelHandler;
import ack.util.Utils;

public class Application {

	public static void main(String[] args) {
		generate();
	}
	
	public static void generate() {
		String[] propertiesNames = {"http://xmlns.com/foaf/0.1/name", "http://schema.org/sameAs",
				"http://schema.org/birthDate", "http://schema.org/deathDate"};
		List<Property> properties = new ArrayList<>();
		for (int i = 0; i < propertiesNames.length; i++) {
			Property p = ResourceFactory.createProperty(propertiesNames[i]);
			properties.add(p);
		}
		
		DataSource source = new DataSource("Ecartico", Type.NT, Utils.ECARTICO, null);
		Model model = source.getRDFModel();
		//split(model, properties);
		checkProperties(model);
	}

	public static void split(Model model, List<Property> properties) {
		RDFModelHandler handler = new RDFModelHandler(model);
		Map<Resource, List<Statement>> resourceMap = handler.mapStatementsToSubjects();
		ResourceDistributor ed = new ResourceDistributor(resourceMap);
		int numberOfSources = 2;
		for (int i = 0; i < numberOfSources; i++) {
			for(PropertyDistributor.Type type : PropertyDistributor.Type.values()) {
				Map<Resource, List<Statement>> selected = ed.createRandomly(0.3*(i+1));
				PropertyDistributor pd = new PropertyDistributor(properties, selected);
				Model sourceModel = pd.createSource(type, (1 + i%3));
				RDFModelHandler sourceModelHandler = new RDFModelHandler(sourceModel);
				sourceModelHandler.dump(String.format("src/main/resources/local-databases/Ecartico-%s-%d", type, i));
			}
		}
	}
	
	public static void checkProperties(Model model) {
		RDFModelHandler handler = new RDFModelHandler(model);
		Map<Property, List<Statement>> propertyMap = handler.mapStatementsToProperties();
		propertyMap.forEach((k,v) -> {
			if(v.size() > 10) { 
				System.out.println(k + ", " + v.size());
			}
		});
	}
}
