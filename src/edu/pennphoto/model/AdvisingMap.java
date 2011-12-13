package edu.pennphoto.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AdvisingMap{

	private HashMap<Integer, Set<Integer>> map;
	
	public AdvisingMap(){
		map = new HashMap<Integer, Set<Integer>>();
	}
	
	public boolean addAdvising(int advisorId, int adviseeId){
		if(!map.containsKey(advisorId)){
			Set<Integer> advisees = new HashSet<Integer>();
			map.put(advisorId, advisees);
		}
		return map.get(advisorId).add(adviseeId);
	}
	
	@Override
	public String toString(){
		return "AdvisingMap: " + map.toString();
	}
}