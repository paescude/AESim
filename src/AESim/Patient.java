package AESim;

import repast.simphony.space.grid.Grid;

public class Patient extends Agent {
	
	public static final double PROB_BLUE_PATIENT_IN_TREATMENT = 0.7;
	public static final double PROB_ORANGE_PATIENT_IN_RESUS_ROOM = 0.8;
	private static int count;
	private int testCountX;
	private int testCountT;	
	private int totalProcesses;
	private int weekOutSystem;
	private int dayOutSystem;
	private int hourOutSystem;	
	private Doctor myDoctor;
	private Nurse myNurse;
	private int weekInSystem;
	private int dayInSystem;
	private int hourInSystem;
	private int totalNumTest; 
	private boolean hasReachedtarget;	
	private boolean isInSystem;
	private boolean isEnteredSystem;
	private boolean wasInTest;
	private boolean wasInXray;
	private boolean waitInCublicle;
	private boolean backInBed;
	private String triage;
	private int triageNum;
	private double queuingTimeQr;
	private double queuingTimeQt;
	private double queuingTimeQa;
	private double timeOfArrival;
	private double timeInSystem;
	private QueueSim currentQueue;
	private double timeEnteredCurrentQ;
	private double timeOutCurrentQueue;
	private int nextProc;
	private double timeEndCurrentService;
	// 1 if goes to test and 2 if goes back to bed
	private String typeArrival;
	private boolean wasFirstInQr;
	private double testRatio;
	private boolean wasFirstInQueueTriage;
	private boolean wasFirstInQueueXRay;
	private boolean wasFirstInQueueTest;
	private boolean wasFirstforAsses;	
	private int isWaitingBedReassessment;
	private Resource myBedReassessment;	
	private int numWasFstForAssess;
	private boolean goToResusRoom;
	private boolean goToTreatRoom;
	private boolean needsTests;

	public Patient(Grid<Object> grid, String typeArrival, double time) {
		count++;
		this.setId("Patient " + count);
		this.idNum = count;
		this.grid = grid;
		this.queuingTimeQr = 0;
		this.triage = " has not been triaged ";
		this.triageNum = 0;
		this.wasFirstforAsses = false;
		this.wasFirstInQr = false;
		this.hasReachedtarget = false;
		this.wasFirstInQueueXRay = false;
		this.wasFirstInQueueTest = false;
		this.typeArrival = typeArrival;
		this.timeOfArrival = time;
		this.timeInSystem = 0;
		this.queuingTimeQr = 0;
		this.queuingTimeQt = 0;
		this.queuingTimeQa = 0;
		this.currentQueue = null;
		this.timeEnteredCurrentQ = 0;
		this.timeOutCurrentQueue = 0;
		this.myResource = null;
		this.isInSystem = true;
		this.myBedReassessment = null;
		this.myDoctor = null;
		this.nextProc = 0;
		this.backInBed = false;
		this.isEnteredSystem = true;
		this.wasInTest = false;
		this.wasInXray = false;
		this.numWasFstForAssess = 0;
		this.testCountX = 0;
		this.testCountT = 0;
		this.totalProcesses = 0;
		this.timeEndCurrentService = 0;
		this.myNurse = null;
		this.waitInCublicle = true;
		this.goToResusRoom = false;
		this.goToTreatRoom = false;
		this.isWaitingBedReassessment = 0;
		this.needsTests = false;
	}

	public static void initSaticVars() {
		setCount(0);		
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Patient.count = count;
	}

	public int getTestCountX() {
		return testCountX;
	}

	public void setTestCountX(int testCountX) {
		this.testCountX = testCountX;
	}

	public int getTestCountT() {
		return testCountT;
	}

	public void setTestCountT(int testCountT) {
		this.testCountT = testCountT;
	}

	public int getTotalProcesses() {
		return totalProcesses;
	}

	public void setTotalProcesses(int totalProcesses) {
		this.totalProcesses = totalProcesses;
	}

	public int getWeekOutSystem() {
		return weekOutSystem;
	}

	public void setWeekOutSystem(int weekOutSystem) {
		this.weekOutSystem = weekOutSystem;
	}

	public int getDayOutSystem() {
		return dayOutSystem;
	}

	public void setDayOutSystem(int dayOutSystem) {
		this.dayOutSystem = dayOutSystem;
	}

	public int getHourOutSystem() {
		return hourOutSystem;
	}

	public void setHourOutSystem(int hourOutSystem) {
		this.hourOutSystem = hourOutSystem;
	}

	public Doctor getMyDoctor() {
		return myDoctor;
	}

	public void setMyDoctor(Doctor myDoctor) {
		this.myDoctor = myDoctor;
	}

	public Nurse getMyNurse() {
		return myNurse;
	}

	public void setMyNurse(Nurse myNurse) {
		this.myNurse = myNurse;
	}

	public int getWeekInSystem() {
		return weekInSystem;
	}

	public void setWeekInSystem(int weekInSystem) {
		this.weekInSystem = weekInSystem;
	}

	public int getDayInSystem() {
		return dayInSystem;
	}

	public void setDayInSystem(int dayInSystem) {
		this.dayInSystem = dayInSystem;
	}

	public int getHourInSystem() {
		return hourInSystem;
	}

	public void setHourInSystem(int hourInSystem) {
		this.hourInSystem = hourInSystem;
	}

	public int getTotalNumTest() {
		return totalNumTest;
	}

	public void setTotalNumTest(int totalNumTest) {
		this.totalNumTest = totalNumTest;
	}

	public boolean isHasReachedtarget() {
		return hasReachedtarget;
	}

	public void setHasReachedtarget(boolean hasReachedtarget) {
		this.hasReachedtarget = hasReachedtarget;
	}

	public boolean isInSystem() {
		return isInSystem;
	}

	public void setInSystem(boolean isInSystem) {
		this.isInSystem = isInSystem;
	}

	public boolean isEnteredSystem() {
		return isEnteredSystem;
	}

	public void setEnteredSystem(boolean isEnteredSystem) {
		this.isEnteredSystem = isEnteredSystem;
	}

	public boolean isWasInTest() {
		return wasInTest;
	}

	public void setWasInTest(boolean wasInTest) {
		this.wasInTest = wasInTest;
	}

	public boolean isWasInXray() {
		return wasInXray;
	}

	public void setWasInXray(boolean wasInXray) {
		this.wasInXray = wasInXray;
	}

	public boolean isWaitInCublicle() {
		return waitInCublicle;
	}

	public void setWaitInCublicle(boolean waitInCublicle) {
		this.waitInCublicle = waitInCublicle;
	}

	public boolean isBackInBed() {
		return backInBed;
	}

	public void setBackInBed(boolean backInBed) {
		this.backInBed = backInBed;
	}

	public String getTriage() {
		return triage;
	}

	public void setTriage(String triage) {
		this.triage = triage;
	}

	public int getTriageNum() {
		return triageNum;
	}

	public void setTriageNum(int triageNum) {
		this.triageNum = triageNum;
	}

	public double getQueuingTimeQr() {
		return queuingTimeQr;
	}

	public void setQueuingTimeQr(double queuingTimeQr) {
		this.queuingTimeQr = queuingTimeQr;
	}

	public double getQueuingTimeQt() {
		return queuingTimeQt;
	}

	public void setQueuingTimeQt(double queuingTimeQt) {
		this.queuingTimeQt = queuingTimeQt;
	}

	public double getQueuingTimeQa() {
		return queuingTimeQa;
	}

	public void setQueuingTimeQa(double queuingTimeQa) {
		this.queuingTimeQa = queuingTimeQa;
	}

	public double getTimeOfArrival() {
		return timeOfArrival;
	}

	public void setTimeOfArrival(double timeOfArrival) {
		this.timeOfArrival = timeOfArrival;
	}

	public double getTimeInSystem() {
		return timeInSystem;
	}

	public void setTimeInSystem(double timeInSystem) {
		this.timeInSystem = timeInSystem;
	}

	public QueueSim getCurrentQueue() {
		return currentQueue;
	}

	public void setCurrentQueue(QueueSim currentQueue) {
		this.currentQueue = currentQueue;
	}

	public double getTimeEnteredCurrentQ() {
		return timeEnteredCurrentQ;
	}

	public void setTimeEnteredCurrentQ(double timeEnteredCurrentQ) {
		this.timeEnteredCurrentQ = timeEnteredCurrentQ;
	}

	public double getTimeOutCurrentQueue() {
		return timeOutCurrentQueue;
	}

	public void setTimeOutCurrentQueue(double timeOutCurrentQueue) {
		this.timeOutCurrentQueue = timeOutCurrentQueue;
	}

	public int getNextProc() {
		return nextProc;
	}

	public void setNextProc(int nextProc) {
		this.nextProc = nextProc;
	}

	public double getTimeEndCurrentService() {
		return timeEndCurrentService;
	}

	public void setTimeEndCurrentService(double timeEndCurrentService) {
		this.timeEndCurrentService = timeEndCurrentService;
	}

	public String getTypeArrival() {
		return typeArrival;
	}

	public void setTypeArrival(String typeArrival) {
		this.typeArrival = typeArrival;
	}

	public boolean isWasFirstInQr() {
		return wasFirstInQr;
	}

	public void setWasFirstInQr(boolean wasFirstInQr) {
		this.wasFirstInQr = wasFirstInQr;
	}

	public double getTestRatio() {
		return testRatio;
	}

	public void setTestRatio(double testRatio) {
		this.testRatio = testRatio;
	}

	public boolean isWasFirstInQueueTriage() {
		return wasFirstInQueueTriage;
	}

	public void setWasFirstInQueueTriage(boolean wasFirstInQueueTriage) {
		this.wasFirstInQueueTriage = wasFirstInQueueTriage;
	}

	public boolean isWasFirstInQueueXRay() {
		return wasFirstInQueueXRay;
	}

	public void setWasFirstInQueueXRay(boolean wasFirstInQueueXRay) {
		this.wasFirstInQueueXRay = wasFirstInQueueXRay;
	}

	public boolean isWasFirstInQueueTest() {
		return wasFirstInQueueTest;
	}

	public void setWasFirstInQueueTest(boolean wasFirstInQueueTest) {
		this.wasFirstInQueueTest = wasFirstInQueueTest;
	}

	public boolean isWasFirstforAsses() {
		return wasFirstforAsses;
	}

	public void setWasFirstforAsses(boolean wasFirstforAsses) {
		this.wasFirstforAsses = wasFirstforAsses;
	}

	public int getIsWaitingBedReassessment() {
		return isWaitingBedReassessment;
	}

	public void setIsWaitingBedReassessment(int isWaitingBedReassessment) {
		this.isWaitingBedReassessment = isWaitingBedReassessment;
	}

	public Resource getMyBedReassessment() {
		return myBedReassessment;
	}

	public void setMyBedReassessment(Resource myBedReassessment) {
		this.myBedReassessment = myBedReassessment;
	}

	public int getNumWasFstForAssess() {
		return numWasFstForAssess;
	}

	public void setNumWasFstForAssess(int numWasFstForAssess) {
		this.numWasFstForAssess = numWasFstForAssess;
	}

	public boolean isGoToResusRoom() {
		return goToResusRoom;
	}

	public void setGoToResusRoom(boolean goToResusRoom) {
		this.goToResusRoom = goToResusRoom;
	}

	public boolean isGoToTreatRoom() {
		return goToTreatRoom;
	}

	public void setGoToTreatRoom(boolean goToTreatRoom) {
		this.goToTreatRoom = goToTreatRoom;
	}

	public boolean isNeedsTests() {
		return needsTests;
	}

	public void setNeedsTests(boolean needsTests) {
		this.needsTests = needsTests;
	}
}
