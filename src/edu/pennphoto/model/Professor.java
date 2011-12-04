package edu.pennphoto.model;

public class Professor extends User {
	String researchArea;
	String title;

	public String getResearchArea() {
		return researchArea;
	}

	public void setResearchArea(String researchArea) {
		this.researchArea = researchArea;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString(){
		return super.toString() + "\n" + title + "\n"+researchArea;
	}
}
