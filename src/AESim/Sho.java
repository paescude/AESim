package AESim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import Datos.Reader;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Sho extends Doctor {
	private static int count;

	public Sho(Grid<Object> grid, int initPosX, int initPosY,
			int idNum, int multiTasking) {
		this.idNum = idNum;
		this.grid = grid;
		this.numAvailable = 0;
		this.initPosX = initPosX;
		this.initPosY = initPosY;
		this.setId("SHO doctor " + idNum);
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
	
	@Override
	public double[] firstAssessmentParameters(int triage) {
		double parameters[] = { 0, 0, 0 };
		switch (triage) {
		case 1:
			parameters[0] = 5;
			parameters[1] = 20;
			parameters[2] = 55;
			break;
		case 2:
			parameters[0] = 5;
			parameters[1] = 20;
			parameters[2] = 55;
			break;
		case 3:
			parameters[0] = 1;
			parameters[1] = 38;
			parameters[2] = 30;
			break;
		case 4:
			parameters[0] = 1;
			parameters[1] = 38;
			parameters[2] = 30;
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
		System.out.println(" checks if there is any sho available to start init assessment ");
		if (!this.checkIfStartInitAssessment()) {
			if (!this.isAtDoctorArea) {
				System.out.println(this.getId()+" is moving to docs area because when decide what to do has nothing to do ");
				this.moveToDoctorsArea();
			}
		}
	}	
	
	@Override
	public double[] reassessmentParameters(int Triage) {
		double parameters[] = { 0, 0, 0 };
		switch (Triage) {
		case 1:
			parameters[0] = 1;
			parameters[1] = 10;
			parameters[2] = 8;
			break;
		case 2:
			parameters[0] = 1;
			parameters[1] = 10;
			parameters[2] = 8;
			break;
		case 3:
			parameters[0] = 1;
			parameters[1] = 28;
			parameters[2] = 22;
			break;
		case 4:
			parameters[0] = 1;
			parameters[1] = 28;
			parameters[2] = 22;
			break;
		case 5:
			parameters[0] = 1;
			parameters[1] = 27;
			parameters[2] = 15;
			break;
		}
		
		return parameters;
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
			this.setMyShiftMatrix(Reader.getMatrixSHOD1());
			// sho minim. exp is 2 years (middle grade 6)
			// doctor middle experience
			for (int i = 0; i < 7; i++) {
				sum = 0;
				for (int j = 0; j < 23; j++) {
					sum = sum + Reader.getMatrixSHOD1()[j][i];
				}
				this.durationOfShift[i] = sum;
			}
			break;

		case 2:
			this.setMyShiftMatrix(Reader.getMatrixSHOD2());

			for (int i = 0; i < 7; i++) {
				sum = 0;
				for (int j = 0; j < 23; j++) {
					sum = sum + Reader.getMatrixSHOD2()[j][i];
				}
				this.durationOfShift[i] = sum;
			}
			break;

		case 3:
			this.setMyShiftMatrix(Reader.getMatrixSHOD3());

			for (int i = 0; i < 7; i++) {
				sum = 0;
				for (int j = 0; j < 23; j++) {
					sum = sum + Reader.getMatrixSHOD3()[j][i];
				}
				this.durationOfShift[i] = sum;
			}
			break;

		case 4:
			this.setMyShiftMatrix(Reader.getMatrixSHOD4());

			for (int i = 0; i < 7; i++) {
				sum = 0;
				for (int j = 0; j < 23; j++) {
					sum = sum + Reader.getMatrixSHOD4()[j][i];
				}
				this.durationOfShift[i] = sum;
			}
			break;

		case 5:
			this.setMyShiftMatrix(Reader.getMatrixSHOD5());

			for (int i = 0; i < 7; i++) {
				sum = 0;
				for (int j = 0; j < 23; j++) {
					sum = sum + Reader.getMatrixSHOD5()[j][i];
				}
				this.durationOfShift[i] = sum;
			}
			break;

		case 6:
			this.setMyShiftMatrix(Reader.getMatrixSHOD6());

			for (int i = 0; i < 7; i++) {
				sum = 0;
				for (int j = 0; j < 23; j++) {
					sum = sum + Reader.getMatrixSHOD6()[j][i];
				}
				this.durationOfShift[i] = sum;
			}
			break;

		case 7:
			this.setMyShiftMatrix(Reader.getMatrixSHOD7());

			for (int i = 0; i < 7; i++) {
				sum = 0;
				for (int j = 0; j < 23; j++) {
					sum = sum + Reader.getMatrixSHOD7()[j][i];
				}
				this.durationOfShift[i] = sum;
			}
			break;

		case 8:
			this.setMyShiftMatrix(Reader.getMatrixSHOD8());

			for (int i = 0; i < 7; i++) {
				sum = 0;
				for (int j = 0; j < 23; j++) {
					sum = sum + Reader.getMatrixSHOD8()[j][i];
				}
				this.durationOfShift[i] = sum;
			}
			break;

		case 9:
			this.setMyShiftMatrix(Reader.getMatrixSHOD9());

			for (int i = 0; i < 7; i++) {
				sum = 0;
				for (int j = 0; j < 23; j++) {
					sum = sum + Reader.getMatrixSHOD9()[j][i];
				}
				this.durationOfShift[i] = sum;
			}
			break;
		}

		System.out.println(this.getId() + " shift's duration ["
				+ this.durationOfShift[0] + " ," + this.durationOfShift[1]
				+ "," + this.durationOfShift[2] + " ,"
				+ this.durationOfShift[3] + " ," + this.durationOfShift[4]
				+ ", " + this.durationOfShift[5] + ", "
				+ this.durationOfShift[6] + "]");
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
