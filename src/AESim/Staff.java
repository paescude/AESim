package AESim;

import java.util.ArrayList;

import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;


public abstract class Staff extends Agent {
	
	protected boolean available;
	protected int initPosX;
	protected int initPosY;
	protected int numAvailable;
	protected boolean inShift;
	protected double timeInitShift;
	protected float [][] myShiftMatrix;
	protected float [] durationOfShift = new float[7];;
	protected int requiredAtWork;
	protected double nextEndingTime;
	protected int multiTaskingFactor;
	protected ArrayList<Patient> patientsInMultitask;
	
	protected void moveOut() {
		resetVariables();
		System.out.println(this.getId()
				+ "  has finished his shift and has moved out to "
				+ this.getLoc().toString());
		
	}
	
	public void engageWithPatient(Patient patient){
		this.patientsInMultitask.add(patient);
		System.out.println(this.getId() + " has added from patientsInMultitask " + patient.getId());
		this.updateMultitask(true);
		System.out.println(this.getId()
				+ " is updating multitasking "
				+ patient.getId() + " is in multitasking.  ");

		System.out.println("My multitasking factor is "
				+ this.multiTaskingFactor);
		printElementsArray(this.patientsInMultitask,
				" patients in multitasking");
	}
	
	public void releaseFromPatient(Patient patient) {
		this.patientsInMultitask.remove(patient);
		System.out.println(this.getId() + " has removed from patientsInMultitask " + patient.getId());
		this.updateMultitask(false);
		System.out.println(this.getId()
				+ " is updating multitasking "
				+ patient.getId() + " is in multitasking.  ");

		System.out.println("My multitasking factor is "
				+ this.multiTaskingFactor);
		printElementsArray(this.patientsInMultitask,
				" patients in multitasking");
		System.out.println(this.getId() + " will decide what to do next");
		this.decideWhatToDoNext();
	}
	
	public void updateMultitask(boolean startSomethingWithAPatient) {
		if (this.isInShift()){
			if (startSomethingWithAPatient) {
				this.numAvailable -= 1;
				if (this.numAvailable == 0) {
					this.setAvailable(false);
				}
			} else {
				if (this.numAvailable < this.multiTaskingFactor) {
					this.numAvailable += 1;
					setAvailable(true);
				}
			}

		}
	}
	
	
	@ScheduledMethod(start = 0, interval = 60, priority = 90,  shuffle = false)
	public void scheduleWork() {
		int hour = getHour();
		int day = getDay();
		System.out.println("\n \n " + this.getId() + " is doing method : SCHEDULE WORK ");
		int requiredAtWork = (int) this.getMyShiftMatrix()[hour][day];
		if (requiredAtWork == 0) {
			System.out.println(this.getId() + " is not required at work at time " + getTime());
			printTime();
			if (this.isAvailable()) {
				this.canMoveOut();				
			} else {
				this.canNotMoveOut();				
			}
		} else if (requiredAtWork == 1) {
			this.requiredAtWork();
		}

	}
	
	protected void scheduleEndShift(double timeEnding) {
		System.out.println(" current time: " + getTime()  + " " +this.getId() + " is supposed to move out at: " + timeEnding);
		ISchedule schedule = repast.simphony.engine.environment.RunEnvironment
				.getInstance().getCurrentSchedule();
		ScheduleParameters scheduleParams = ScheduleParameters
				.createOneTime(timeEnding);
		Endshift actionEnd = new Endshift(this);

		schedule.schedule(scheduleParams, actionEnd);
	}
	
	protected static class Endshift implements IAction {
		private Staff staff;

		public Endshift(Staff staff) {
			this.staff = staff;
		}

		@Override
		public void execute() {
			staff.endShift();
		}

	}

	protected void endShift() {
		printTime();
		System.out.println(this.getId()
				+ " has finished the shift and will move out at " +getTime());
		this.moveOut();
	}

	protected void canMoveOut(){
		System.out
		.println(this.getId()
				+ " has finished his shift and is moving to not working area");
		this.moveOut();
	}
	
	protected void canNotMoveOut(){
		if (getTime() < this.nextEndingTime) {
			printTime();
			System.out
					.println(this.getId()
							+ " has finished his shift but needs to wait to leave because he still has work to do");
			double timeEnding = this.nextEndingTime;
			this.scheduleEndShift(timeEnding+5);
		}
	}
	
	public abstract void decideWhatToDoNext();
	
	public abstract void requiredAtWork();
	
	public abstract void resetVariables();
	
	
	
	protected Nurse findNurse() {
		Nurse nurse = null;
		for (int i = 7; i < 12; i++) {
			Object o = getGrid().getObjectAt(i, 2);
			if (o instanceof Nurse) {
				nurse = (Nurse) o;
				if (nurse.isAvailable()) {
					break;
				}

			}
		}
		return nurse;
	}
	
	protected Resource findBed(int triage){
	Resource resource = null;
	switch (triage) {
	case 1:
		resource = findResourceAvailable("minor cubicle ");
		break;
	case 2:
		resource = findResourceAvailable("minor cubicle ");
		break;
	case 3:
		resource = findResourceAvailable("major cubicle ");
		if (resource == null) {
			resource = findResourceAvailable("minor cubicle ");
		}
		break;
	case 4:
		double rndResus = RandomHelper.createUniform().nextDouble();
		if (rndResus < Patient.PROB_ORANGE_PATIENT_IN_RESUS_ROOM) {
			resource = findResourceAvailable("resus cubicle ");
			break;
		}
		resource = findResourceAvailable("major cubicle ");
		if (resource == null) {
			resource = findResourceAvailable("minor cubicle ");
		}
		break;
	case 5:
		resource = findResourceAvailable("resus cubicle ");
		break;
	}
	return resource;
}

	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public int getInitPosX() {
		return initPosX;
	}
	public void setInitPosX(int initPosX) {
		this.initPosX = initPosX;
	}
	public int getInitPosY() {
		return initPosY;
	}
	public void setInitPosY(int initPosY) {
		this.initPosY = initPosY;
	}
	public int getNumAvailable() {
		return numAvailable;
	}
	public void setNumAvailable(int numAvailable) {
		this.numAvailable = numAvailable;
	}
	public boolean isInShift() {
		return inShift;
	}
	public void setInShift(boolean inShift) {
		this.inShift = inShift;
	}
	public double getTimeInitShift() {
		return timeInitShift;
	}
	public void setTimeInitShift(double timeInitShift) {
		this.timeInitShift = timeInitShift;
	}
	public float[][] getMyShiftMatrix() {
		return myShiftMatrix;
	}
	public void setMyShiftMatrix(float[][] myShiftMatrix) {
		this.myShiftMatrix = myShiftMatrix;
	}
	public float[] getDurationOfShift() {
		return durationOfShift;
	}
	public void setDurationOfShift(float[] durationOfShift) {
		this.durationOfShift = durationOfShift;
	}
	public int getRequiredAtWork() {
		return requiredAtWork;
	}
	public void setRequiredAtWork(int requiredAtWork) {
		this.requiredAtWork = requiredAtWork;
	}
	public double getNextEndingTime() {
		return nextEndingTime;
	}
	public void setNextEndingTime(double nextEndingTime) {
		this.nextEndingTime = nextEndingTime;
	}
	public int getMultiTaskingFactor() {
		return multiTaskingFactor;
	}
	public void setMultiTaskingFactor(int multiTaskingFactor) {
		this.multiTaskingFactor = multiTaskingFactor;
	}
	public ArrayList<Patient> getPatientsInMultitask() {
		return patientsInMultitask;
	}
	public void setPatientsInMultitask(ArrayList<Patient> patientsInMultitask) {
		this.patientsInMultitask = patientsInMultitask;
	}
	
}
