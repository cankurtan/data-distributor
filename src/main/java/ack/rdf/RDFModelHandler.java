package ack.rdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.shared.Lock;

public class RDFModelHandler {

	private final Model model;
	
	public RDFModelHandler(Model model) {
		this.model = model;
	}
	
	public Map<Property, List<Statement>> mapStatementsToProperties() {
		Map<Property, List<Statement>> statementMap = new HashMap<>();
		StmtIterator it = model.listStatements();
		while(it.hasNext()) {
			List<Statement> statements;
			Statement st = it.next();
			if((statements = statementMap.get(st.getPredicate())) == null){
				statements = new ArrayList<>();
				statementMap.put(st.getPredicate(), statements);
			}
			statements.add(st);
		}
		return statementMap;
	}
	
	public Map<Resource, List<Statement>> mapStatementsToSubjects() {
		Map<Resource, List<Statement>> statementMap = new HashMap<>();
		StmtIterator it = model.listStatements();
		while(it.hasNext()) {
			List<Statement> statements;
			Statement st = it.next();
			if((statements = statementMap.get(st.getSubject())) == null){
				statements = new ArrayList<>();
				statementMap.put(st.getSubject(), statements);
			}
			statements.add(st);
		}
		return statementMap;
	}
	
	public Model extractModelForProperty(Property p) {
		Model extracted = ModelFactory.createDefaultModel();
		Resource r = null;
		RDFNode n = null;
		StmtIterator it = model.listStatements(r, p, n);
		while(it.hasNext()) {
			extracted.add(it.next());
		}
		return extracted;
	}
	
	public List<String> getPropertiesOfDomain(String domainClass) {
		String query = "SELECT DISTINCT ?pred WHERE {?a a <" + domainClass + ">. ?a ?pred ?b}";
	    QueryExecution qexec = QueryExecutionFactory.create(query, model);
	    ResultSet results = qexec.execSelect();
	    List<String> answer = new ArrayList<String>();
	    while(results.hasNext()){
	    	QuerySolution t = results.nextSolution();
	    	RDFNode x  = t.get("pred");
	    	String s = x.toString();
	    	
	    	answer.add(s.substring(7));
	    }
	    qexec.close();
		return answer;
	}

	public void dump(String fileName) {
		model.enterCriticalSection(Lock.READ);
		try {
			File file = new File(fileName + ".nt");
			try(FileOutputStream out = new FileOutputStream(file)) {
				file.createNewFile();
				model.write(out, "N-TRIPLE");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			model.leaveCriticalSection();
		}
	}
}
