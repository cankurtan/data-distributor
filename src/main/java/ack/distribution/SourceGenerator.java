package ack.distribution;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import com.fasterxml.jackson.databind.ObjectMapper;

import ack.model.SourceTemplate;
import ack.rdf.RDFModelHandler;

public class SourceGenerator {
	
	private final Model model;

	public SourceGenerator(Model model) {
		this.model = model;
	}
	
	public void run(String filename) throws IOException {
		SourceTemplate[] templates = loadSourceTemplates(filename);
		splitForAllProperties(templates);
	}

	private SourceTemplate[] loadSourceTemplates(String filename) throws IOException {
		InputStream input = new FileInputStream(filename);
		ObjectMapper objectMapper = new ObjectMapper();
		SourceTemplate[] templates = objectMapper.readValue(input, SourceTemplate[].class);
		return templates;
	}
	
	public void splitForAllProperties(SourceTemplate[] templates) {
		RDFModelHandler handler = new RDFModelHandler(model);
		Map<Property, List<Statement>> propertyMap = handler.mapStatementsToProperties();
		List<Property> properties = new ArrayList<>(propertyMap.keySet());
		System.out.println(properties);
		splitForSelectedProperties(templates, properties);
	}

	public void splitForSelectedProperties(SourceTemplate[] templates, List<Property> properties) {
		RDFModelHandler handler = new RDFModelHandler(model);
		Map<Resource, List<Statement>> resourceMap = handler.mapStatementsToSubjects();
		ResourceDistributor ed = new ResourceDistributor(resourceMap);
		for (int i = 0; i < templates.length; i++) {
			Map<Resource, List<Statement>> selected = ed.createRandomly(0.3*(i+1));
			PropertyDistributor pd = new PropertyDistributor(properties, selected);
			Model sourceModel = pd.createSource(templates[i]);
			RDFModelHandler sourceModelHandler = new RDFModelHandler(sourceModel);
			sourceModelHandler.dump(String.format("src/main/resources/local-databases/Ecartico-%d", i));
		}
	}
}
