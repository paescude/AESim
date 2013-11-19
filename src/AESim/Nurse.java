package AESim;


import java.util.ArrayList;

import repast.simphony.space.grid.Grid;

public class Nurse extends Staff {
	public static final double[] NURSE_TRIAGE_PARAMS = { 5, 10, 12 };
	private static int count;
	private double[] timeTriage = new double[3];

	public Nurse(Grid<Object> grid, int x1, int y1, int idNum, int multiTasking) {
		this.grid = grid;
		this.available = true;
		this.setId("Nurse " + idNum);
		this.idNum = idNum;
		this.numAvailable = 0;
		this.initPosX = x1;
		this.initPosY = y1;
		this.myResource = null;
		this.multiTaskingFactor = multiTasking; 	
		this.patientsInMultitask = new ArrayList<Patient>();
		this.setTimeTriage(NURSE_TRIAGE_PARAMS);
	}

	public static void initSaticVars() {
		setCount(1);		
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Nurse.count = count;
	}

	public double[] getTimeTriage() {
		return timeTriage;
	}

	public void setTimeTriage(double[] timeTriage) {
		this.timeTriage = timeTriage;
	}
}
