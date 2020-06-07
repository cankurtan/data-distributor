package ack.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Environment {
	
	private final Set<Integer> idSet;
	private final Map<String, Map<String, Set<Integer>>> agentMap;
	private final Map<String, Set<String>> taskMap = new LinkedHashMap<>();
	private final Map<String, Map<String, Set<String>>> bestTeamMap = new LinkedHashMap<>();
	
	public Environment(@JsonProperty(value = "idSet", required = true) Set<Integer> idSet, 
			@JsonProperty(value = "agentMap", required = true) Map<String, Map<String, Set<Integer>>> agentMap,
			@JsonProperty(value = "taskMap", required = true) Map<String,Set<String>> taskMap,
			@JsonProperty(value = "bestTeamMap", required = false) Map<String,Map<String,Set<String>>> bestTeamMap) {
		this.idSet = idSet;
		this.agentMap = agentMap;
		addTasks(taskMap);
		if(bestTeamMap != null) {
			this.bestTeamMap.putAll(bestTeamMap);
		}
	}
	public Map<String, Set<Integer>> getAgent(String agentID) {
		return agentMap.get(agentID);
	}
	public Set<Integer> getIdSet() {
		return idSet;
	}
	public Map<String, Map<String, Set<Integer>>> getAgentMap() {
		return agentMap;
	}
	public Map<String, Set<String>> getTaskMap() {
		return taskMap;
	}
	public void addTasks(Map<String, Set<String>> taskMap) {
		this.taskMap.putAll(taskMap);
	}
	public Map<String, Set<String>> getBestTeam(String taskID) {
		return bestTeamMap.get(taskID);
	}
	public Map<String, Map<String, Set<String>>> getBestTeamMap() {
		return bestTeamMap;
	}
	@Override
	public String toString() {
		return String.format("Environment includes %d entities, %d agents, %d tasks, and %d stored best teams!=",
				idSet.size(), agentMap.size(), taskMap.size(), bestTeamMap.size());
	}
}
