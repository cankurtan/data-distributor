package ack.distribution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

public class ResourceDistributor {

	private final Map<Resource, List<Statement>> resourceMap;

	public ResourceDistributor(Map<Resource, List<Statement>> resourceMap) {
		this.resourceMap = resourceMap;
	}
	
	public Map<Resource, List<Statement>> createRandomly(double chance) {
		Map<Resource, List<Statement>> selected = new HashMap<>();
		for(Entry<Resource, List<Statement>> entry : resourceMap.entrySet()){
			if(Math.random() <= chance) {
				selected.put(entry.getKey(), entry.getValue());
			}
		}
		return selected;
	}
	
	public Map<Resource, List<Statement>> createRandomlyInvolving(
			Map<Resource, List<Statement>> resourcesToInvolve, double chance) {
		Map<Resource, List<Statement>> selected = createRandomly(chance);
		selected.putAll(resourcesToInvolve);
		return selected;
	}
}
