package ack.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;

import ack.distribution.SourceGenerator;
import ack.model.DataSource;
import ack.model.DataSource.ConnectionType;
import ack.rdf.RDFModelHandler;
import ack.util.Utils;

public class Application {

	public static void main(String[] args) throws IOException {

		generate();
	}
	
	public static void generate() throws IOException {
		String[] propertiesNames = {"http://xmlns.com/foaf/0.1/name", "http://schema.org/sameAs",
				"http://schema.org/birthDate", "http://schema.org/deathDate"};
		List<Property> properties = new ArrayList<>();
		for (int i = 0; i < propertiesNames.length; i++) {
			Property p = ResourceFactory.createProperty(propertiesNames[i]);
			properties.add(p);
		}
		
		DataSource source = new DataSource("Ecartico", ConnectionType.NT, Utils.ECARTICO, null);
		Model model = source.getRDFModel();
		SourceGenerator generator = new SourceGenerator(model);
		generator.run("src/main/resources/source-templates.json");
	}
	
	public static void checkProperties(Model model) {
		RDFModelHandler handler = new RDFModelHandler(model);
		Map<Property, List<Statement>> propertyMap = handler.mapStatementsToProperties();
		propertyMap.keySet().stream()
			.sorted(Comparator.comparing(key -> propertyMap.get(key).size()).reversed())
			.forEach(key -> {
				System.out.println(key + ", " + propertyMap.get(key).size());
			});
	}
	
	public static void listPropertiesInDomain(Model model) {
		String domainClass = "http://schema.org/Person";
		RDFModelHandler handler = new RDFModelHandler(model);
		System.out.println(handler.getPropertiesOfDomain(domainClass));
	}
}
