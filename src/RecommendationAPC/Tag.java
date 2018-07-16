package RecommendationAPC;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class Tag {
	private String name;
	private int count;
	private	double perc;//percentuale di track con questo tag
	
	/*
	public Tag(String name, int perc){
		this.name = name;
		this.perc = perc;
	}*/
	public Tag(String name){
		this.name = name;
		this.perc = 0;
		this.count = 1;
	}
	public String getName (){
		return this.name;
	}
		
	public int getCount(){
		return this.count;
	}
	
	public void incrementCount(){
		count++;
	}
	public void setPerc(double perc){
		this.perc = perc;
	}
	public double getPerc(){
		return this.perc;
	}
	
	@Override
	public String toString() {
		return "Tag: "+name+" score: "+perc;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		Tag tagToCompare = (Tag)obj;
		if (this.name.equals(tagToCompare.getName())){
			return true; 
		}else{
			return false;
		}
	}
	
}
