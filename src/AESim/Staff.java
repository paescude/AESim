package AESim;

import java.util.ArrayList;

public class Staff extends Agent {
	
	protected boolean available;
	protected int initPosX;
	protected int initPosY;
	protected int numAvailable;
	protected boolean inShift;
	protected double timeInitShift;
	protected float [][] myShiftMatrix;
	protected float [] durationOfShift;
	protected int requiredAtWork;
	protected double nextEndingTime;
	protected int multiTaskingFactor;
	protected ArrayList<Patient> patientsInMultitask;
	
}
