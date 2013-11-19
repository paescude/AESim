package AESim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import repast.simphony.space.grid.Grid;

public class Sho extends Doctor {
	private static int count;

	public Sho(Grid<Object> grid, int initPosX, int initPosY,
			String doctorType, int idNum, int multiTasking) {
		this.idNum = idNum;
		this.grid = grid;
		this.numAvailable = 0;
		this.initPosX = initPosX;
		this.initPosY = initPosY;
		this.setId("doctor " + idNum);
		this.myPatientCalling = null;
		this.available = false;
		this.nextEndingTime = 0;
		this.timeEnterSimulation = getTime();
		this.myPatientsInBed = new PriorityQueue<Patient>(5,
				new PriorityQueueComparatorTime());
		this.myPatientsInTests = new ArrayList<Patient>();
		this.myPatientsBackInBed = new LinkedList<Patient>();
		this.doctorToHandOver = null;
		this.multiTaskingFactor = multiTasking;
		this.patientsInMultitask = new ArrayList<>();
		this.isAtDoctorArea = false;
	}

	public static void initSaticVars() {
		setCount(0);		
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Sho.count = count;
	}
}
