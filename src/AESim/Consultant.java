package AESim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import Datos.Reader;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Consultant extends Doctor {
	private static int count;

	public Consultant(Grid<Object> grid, int initPosX, int initPosY, int idNum, int multiTasking) {
		this.idNum = idNum;
		this.grid = grid;
		this.numAvailable = 0;
		this.initPosX = initPosX;
		this.initPosY = initPosY;
		this.setId("Consultant doctor " + idNum);
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
		this.patientsInMultitask = new ArrayList<Patient>();
		this.allMyPatients = new ArrayList<Patient>();
		this.isAtDoctorArea = false;
	}
	
	public double[] firstAssessmentParameters(int triage) {
		double parameters[] = { 0, 0, 0 };
		switch (triage) {
		case 1:
			parameters[0] = 5;
			parameters[1] = 15;
			parameters[2] = 45;
			break;
		case 2:
			parameters[0] = 5;
			parameters[1] = 15;
			parameters[2] = 45;
			break;
		case 3:
			parameters[0] = 1;
			parameters[1] = 30;
			parameters[2] = 24;
			break;
		case 4:
			parameters[0] = 1;
			parameters[1] = 30;
			parameters[2] = 24;
			break;
		case 5:
			parameters[0] = 1;
			parameters[1] = 27;
			parameters[2] = 15;
			break;
		}
		return parameters;
	}
	
	@Override
	public void decideWhatToDo() {
		// TODO Auto-generated method stub
		System.out.println(this.getId()+ " is consultant and is checking any other sho is available");
		Doctor shoAvailable = checkForAnyAvailableDoctor();
		if (shoAvailable == null) {		
			System.out.println(this.getId()+ " is SHO and is checking if start init assessment");			
			if (!this.checkIfStartInitAssessment()) {
				if (!this.isAtDoctorArea) {
					this.moveToDoctorsArea();
				}
			}
		
		}
	}
	
	@Override
	public double[] reassessmentParameters(int Triage) {
		double parameters[] = { 0, 0, 0 };
		switch (Triage) {
		case 1:
			parameters[0] = 1;
			parameters[1] = 8;
			parameters[2] = 5;
			break;
		case 2:
			parameters[0] = 1;
			parameters[1] = 8;
			parameters[2] = 5;
			break;
		case 3:
			parameters[0] = 1;
			parameters[1] = 22;
			parameters[2] = 19;
			break;
		case 4:
			parameters[0] = 1;
			parameters[1] = 22;
			parameters[2] = 19;
			break;
		case 5:
			parameters[0] = 1;
			parameters[1] = 27;
			parameters[2] = 15;
			break;
		}
		return parameters;
	}

	private Doctor checkForAnyAvailableDoctor() {
		Doctor shoAvailable = null;
		System.out
				.println(" checks if there is any sho available to start init assessment ");
		for (Object sho : getContext().getObjects(Sho.class)) {
			Doctor shoToCheck = (Doctor) sho;
			if (shoToCheck.isAvailable()) {
				shoAvailable = shoToCheck;
				System.out.println(shoAvailable.getId() + " is available ");
				break;
			}
		}
		return shoAvailable;
	}

	@ScheduledMethod(start = 0, priority = 99, shuffle = false)
	public void initNumDocs() {
		printTime();
		System.out.println("When simulation starts, the conditions are "
				+ this.getId());
		GridPoint currentLoc = grid.getLocation(this);
		int currentX = currentLoc.getX();
		int currentY = currentLoc.getY();

		if (currentX == 19) {
			this.setAvailable(false);
			this.setInShift(false);
			System.out.println(this.getId()
					+ " is not in shift and is not available, time: "
					+ getTime());

		} else if (currentY == 4) {
			this.setAvailable(true);
			this.setInShift(true);
			System.out.println(this.getId()
					+ " is in shift and is available, time: " + getTime());
		}
		int id = this.getIdNum();
		float sum = 0;
		switch (id) {
			case 1:
			this.setMyShiftMatrix(Reader.getMatrixSHOD0());
			// this doctor is a consultant, minimum experience is 8 years
			for (int i = 0; i < 7; i++) {
				sum = 0;
				for (int j = 0; j < 23; j++) {
					sum = sum + Reader.getMatrixSHOD0()[j][i];
				}
				this.durationOfShift[i] = sum;
			}
			break;
		}
	}
	
	public static void initSaticVars() {
		setCount(0);		
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Consultant.count = count;
	}

}
