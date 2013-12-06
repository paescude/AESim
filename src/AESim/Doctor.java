package AESim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import cern.jet.random.Uniform;
import Datos.Reader;
import Funciones.MathFunctions;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.GridPoint;

public abstract class Doctor extends Staff {
	
	protected PriorityQueue<Patient> myPatientsInBed;
	protected LinkedList<Patient> myPatientsBackInBed;
	protected ArrayList<Patient> myPatientsInTests;
	protected ArrayList<Patient> allMyPatients;
	protected boolean isAtDoctorArea;
	protected double timeEnterSimulation;
	protected Patient myPatientCalling;
	protected Doctor doctorToHandOver;
	
	@Override
	public void requiredAtWork() {
		if (this.isInShift() == false) {
			this.setTimeInitShift(getTime());
			this.initializeDoctorShiftParams();
			this.doctorToHandOver= null;
			System.out.println(this.getId() + " will move to doctors area"
					+ " method: schedule work"
					+ " this method is being called by " + this.getId());
			this.numAvailable=this.multiTaskingFactor;
			this.setAvailable(true);
			if(this.isAtDoctorArea==false)
				this.moveToDoctorsArea();
			this.decideWhatToDoNext();
		} else {
			
		}
	}
	
	private void initializeDoctorShiftParams() {
		this.numAvailable = this.multiTaskingFactor;
		//Inicializar variables PECS
		
	}

	@Override
	public void decideWhatToDoNext() {
		System.out.println("when deciding what to do next, " + this.getId()
				+ " num of patients in multitask: "
				+ this.patientsInMultitask.size() + ", multitask factor: "
				+ this.multiTaskingFactor);
		if (this.patientsInMultitask.size() < this.multiTaskingFactor) {
			printTime();
			System.out.println(this.getId() + " decides what to do next");
			this.requiredAtWork = (int) this.getMyShiftMatrix()[getHour()][getDay()];
			if (requiredAtWork == 0) {
				this.moveOut();
			} else {
				if (this.available) {
					if (!this.checkIfStartReassessment()){
						this.decideWhatToDo();
					}
				}
			}
		} else {
			this.setAvailable(false);
		}
		
	}
	
	protected boolean checkIfStartInitAssessment() {
		boolean checked = false;
		Patient patient = null;
		Boolean flag = false;
		// The head of the queue is at (x,y-1)
		// Object o = grid.getObjectAt(locX, locY);
		int i = 1;
		while (i <= 5 && !flag) {
			// checking from left to right, which patient is at the head of
			// assessment (by each triage color) queue
			for (Object o : grid.getObjectsAt(i, 8)) {
				if (o instanceof Patient) {
					patient = (Patient) o;
					flag = true;
					break;
				}

			}
			i++;
			// checks if there is anyone to start the first assessment
		}

		if (patient != null) {
			System.out.println(this.getId()
					+ " decide to start first assessment with:"
					+ patient.getId());			
			this.checkConditionsForFirstAssessment(patient);
			checked = true;

		} else {
			System.out.println(this.getId() + "");
			if (this.allMyPatients != null){
				printElementsArray(allMyPatients,
						" does not start first assessment, all my patients:");
			}			
			System.out
					.println(this.getId()
							+ " is available and...decides what to do, moving to docs area when start shift");
			if (!this.isAtDoctorArea)
				this.moveToDoctorsArea();
		}
		return checked;
	}
	
	private boolean checkIfStartReassessment() {
		boolean check = false;
		if (this.patientsInMultitask.size() < this.multiTaskingFactor) {
			int doctorPatientsNum = this.getMyPatientsBackInBed().size();
			if (doctorPatientsNum > 0) {
				// TODO esto estaba con poll, yo lo voy a cambiar a peek Patient
				Patient patient = this.getMyPatientsBackInBed().peek();
				this.checkConditionsForReassessment(patient);
				check = true;
			}
		}
		return check;
	}



	public abstract void decideWhatToDo();
	
	@Watch(watcheeClassName = "AESim.Patient", watcheeFieldNames = "wasFirstforAsses", triggerCondition = "$watchee.getNumWasFstForAssess()>0 && $watcher.getNumAvailable()>0", scheduleTriggerPriority = 90, whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void checkConditionsForFirstAssessment(Patient patient){
		patient.setNumWasFstForAssess(0);
		printTime();
		System.out.println("Cheking conditions for first assessment ");
		Resource bed = findBed(patient.getTriageNum());
		Nurse nurse = findNurse();
		boolean conditionsOk = true;
		if (bed == null){
			System.out.println("There is no bed");
			conditionsOk = false;
		} else if (nurse == null) {
			System.out.println("There is no nurse");
			conditionsOk = true;
		}
		if (conditionsOk){
			System.out.println("Conditions are ok to start FirstAssessment");
			this.startFirstAssessment(patient, nurse, bed);
		}
	}
	
	private void startFirstAssessment(Patient patient, Nurse nurse, Resource bed) {
		System.out.println("\n\nSTART FIRST ASSESSMENT");
		GridPoint loc = bed.getLoc();
		int locX = loc.getX();
		int locY = loc.getY();
		grid.moveTo(this, locX, locY);
		if (nurse != null){
			grid.moveTo(nurse, locX, locY);
			patient.setMyNurse(nurse);
			nurse.engageWithPatient(patient);
		}
		
		grid.moveTo(patient, locX, locY);
		System.out.println(this.getId() + " & "
				+ patient.getId() + " have moved to "
				+ this.getLoc().toString() + " to "
				+ bed.getId());
		patient.setMyResource(bed);
		bed.setAvailable(false);
		
		patient.setMyDoctor(this);
		QueueSim queue = patient.getCurrentQueue();
		queue.removeFromQueue(patient);
		queue.elementsInQueue();
		this.myPatientsInBed.add(patient);		
		this.engageWithPatient(patient);
		
		printElementsQueue(this.myPatientsInBed,
				" my patients in bed time");
		printElementsArray(this.patientsInMultitask,
				" patients in multitasking");
		if (this.available){
			this.decideWhatToDoNext();
		}
		this.scheduleEndFirstAssessment(patient);
		printTime();
		System.out
				.println(this.getId()
						+ " schedules the end of first assessment and checks if there are more patients waiting at "
						+ queue.getId());

		if (queue.getSize() > 0) {
			System.out
					.println("there are patients waiting for first assessment at:"
							+ queue.getId());
			Patient patientToMove = queue.firstInQueue();
			System.out.println(patientToMove.getId()
					+ " will move to the head of " + queue.getId()
					+ " at:" + getTime());
			patientToMove.moveToHeadOfQ(queue);
		}
	}

	private void scheduleEndFirstAssessment(Patient patient) {
		System.out.println(this.getId()
				+ " is schedulling the end of first assessment of "
				+ patient.getId() );
		double parameters[] = this.firstAssessmentParameters(patient.getTriageNum());
		double serviceTime = 0;
		if ((patient.getTriage() == "Blue ")
				|| (patient.getTriage() == "Green ")) {
			serviceTime = MathFunctions.distTriangular(parameters[0], parameters[1],
					parameters[2]);
			// this.iniAssessmentTSampleTriang.add(serviceTime);
			// System.out.println(" init assessment sample triang: " +
			// this.iniAssessmentTSampleTriang);
		} else {
			serviceTime = MathFunctions.distLognormal(parameters[0], parameters[1],
					parameters[2]);
			// this.iniAssessmentTSampleLogn.add(serviceTime);
			// System.out.println(" init assessment sample logn: " +
			// this.iniAssessmentTSampleLogn);
		}

		// System.out.println("triangularOBS :   " + serviceTime);
		ISchedule schedule = repast.simphony.engine.environment.RunEnvironment
				.getInstance().getCurrentSchedule();

		// double timeEndService = schedule.getTickCount()
		// + serviceTime;
		patient.settFirstAssessment(serviceTime);
		double timeEndService = schedule.getTickCount() + serviceTime;
		this.setNextEndingTime(timeEndService);
		ScheduleParameters scheduleParams = ScheduleParameters
				.createOneTime(timeEndService);
		EndFirstAssessment action2 = new EndFirstAssessment(this, patient);
		patient.setTimeEndCurrentService(timeEndService);
		schedule.schedule(scheduleParams, action2);

		System.out.println("\n\tFirst assessment of " + patient.getId()
				+ " is schedulled to end at: " + timeEndService);

		
		System.out.println(this.getId() + " is available = "
				+ this.isAvailable() + " time " + getTime());

	}
	
	private static class EndFirstAssessment implements IAction {
		private Doctor doctor;
		private Patient patient;

		private EndFirstAssessment(Doctor doctor, Patient patient) {
			this.doctor = doctor;
			this.patient = patient;

		}

		@Override
		public void execute() {
			doctor.endFirstAssessment(this.patient);

		}

	}
	
	public void endFirstAssessment(Patient patient) {
		System.out.println("\n \t\t END FIRST ASSESSMENT");
		Doctor doctor = null;
		printTime();
		doctor = chooseDocFirstAssess(patient, doctor);
		if (doctor != null) {
			int totalProcess = patient.getTotalProcesses();
			patient.setTotalProcesses(totalProcess + 1);
			// doctor decides what to do
			int route[] = decideTests(patient.getTriageNum());
			/*
			 * Choose a route. A patient could go to test or not. 30% of
			 * patients are removed from department.
			 */
			if (patient.isGoToTreatRoom()) {
				chooseRoute(patient, doctor, route);
			} else {
				System.out.println(patient.getId() + " isn't going to TreatRoom and is leaving the department.");
				this.removePatientFromDepartment(patient);
			}
		} else {
			System.err
					.println(" ERROR: something is wrong here, no doctor to end fst assessment with "
							+ patient.getId());
			// this.doEndFstAssessment(this, patient);

		}
		System.out.println(doctor.getId() + "has available = "
				+ doctor.isAvailable());
		// Para mover paciente de la lista de espera.
		//movePatientBedReassessment(doctor);
		System.out.println(doctor.getId()
				+ " is moving to doctors area at end first assessment");
		doctor.releaseFromPatient(patient);

	}
	
	@Watch(watcheeClassName = "AESim.Patient", watcheeFieldNames = "backInBed",triggerCondition = "$watchee.getReassessmentDone()<1" /*&& $watcher.getStartedReassessment()<1"*/, whenToTrigger = WatcherTriggerSchedule.IMMEDIATE, scheduleTriggerPriority = 60/*, pick = 1*/)
	public void checkConditionsForReassessment(Patient patient) {
		if (this.isAvailable()) {
			if (this.myPatientsBackInBed.contains(patient)) {
				patient.setReassessmentDone(1);				
				this.startReassessment(patient);
			} else {
				this.decideWhatToDoNext();
			}	
		}
	}
	
	
	private void startReassessment(Patient patient) {
		this.myPatientsBackInBed.remove(patient);		
		this.myPatientsInBed.add(patient);
		System.out.println(this.getId() + " is DOING reassessment to "
				+ patient.getId());
		printTime();
		System.out.println("       \n \nSTART RE-ASSESSMENT  " + this.getId()
				+ " and " + patient.getId());
		if (patient.getMyBedReassessment() == null || patient.getId() == null ){
			System.out.println("Que paso con mi camitaaa :(");
		}
		System.out.println(patient.getId() + " is at "
				+ patient.getMyBedReassessment().getId() + " loc "
				+ patient.getLoc());
		Resource bedPatient = patient.getMyBedReassessment();
		printTime();
		System.out.println(this.getId() + " will go to " + bedPatient.getId());
		GridPoint loc = patient.getLoc();
		this.moveTo(grid, loc);
		printTime();
		System.out.println(this.getId() + " moves to " + bedPatient.getId());
		this.setMyResource(bedPatient);
		patient.setMyResource(bedPatient);
		System.out.println(this.getId() + " and " + patient.getId()
				+ " have as resource:" + bedPatient.getId());
		System.out.println(this.getId() + " is setting " + bedPatient.getId()
				+ " available= false");
		bedPatient.setAvailable(false);
		if(patient.isComingFromTest()){
			this.engageWithPatient(patient);	
		}		
		if (this.available)
			this.decideWhatToDoNext();
		System.out.println(bedPatient.getId() + " and " + this.getId()
				+ " are set not available");
		printTime();
		System.out.println(this.getId() + " will schedule end reassessment");
		this.scheduleEndReassessment(this, patient);
		
	}

	private void scheduleEndReassessment(Doctor doctor, Patient patient) {
		printTime();
		System.out.println(this.getId()
				+ " is scheduling the end of reassessment between: "
				+ doctor.getId() + " and " + patient.getId());

		ISchedule schedule = repast.simphony.engine.environment.RunEnvironment
				.getInstance().getCurrentSchedule();
		double serviceTime = getReassessmentTime(patient);
		patient.settReasssesment(serviceTime);
		double timeEndService = schedule.getTickCount()
				+ serviceTime;
		doctor.setNextEndingTime(timeEndService);
		ScheduleParameters scheduleParams = ScheduleParameters
				.createOneTime(timeEndService);
		patient.setTimeEndCurrentService(timeEndService);
		EndReassessment action2 = new EndReassessment(doctor, patient);

		schedule.schedule(scheduleParams, action2);
		System.out.println(this.getId()
				+ " has started re-assessment and  has added "
				+ patient.getId() + " to his multitasking.  ");
		System.out.println("My multitasking factor is "
				+ this.multiTaskingFactor);
		printElementsArray(this.patientsInMultitask,
				" patients in multitasking");
		System.out.println(this.getId() + " has available = "
				+ this.isAvailable());
		System.out.println(patient.getId()
				+ " expected to end reassessment at " + timeEndService);
		
	}
	
	private static class EndReassessment implements IAction {
		private Doctor doctor;
		private Patient patient;

		private EndReassessment(Doctor doctor, Patient patient) {
			this.doctor = doctor;
			this.patient = patient;

		}

		@Override
		public void execute() {
			doctor.endReassessment(this.patient);

		}

	}

	private double getReassessmentTime(Patient patient) {
		double time = 0;
		double parameters[] = this.reassessmentParameters(patient
				.getTriageNum());
		double min = parameters[0];
		double mean = parameters[1];
		double max = parameters[2];
		if (patient.isWasInTest() || patient.isWasInXray()) {
			// time=distExponential(min, mean, max);
			time = MathFunctions.exponential(mean).nextDouble();
			// this.reAssessmentTSampleExp.add(time);
			// System.out.println(" reassessment sample exponential: " +
			// reAssessmentTSampleExp);
		} else {
			time = MathFunctions.distLognormal(min, mean, max);
			// this.reAssessmentTSampleLogn.add(time);
			// System.out.println(" reassessment sample lognormal: " +
			// reAssessmentTSampleLogn);
		}

		return time;
	}

	public void endReassessment(Patient patient) {
		printTime();
		Doctor doctor = null;
		if (this.isInShift() == false) {
			doctor = this.doctorToTakeOver();
		} else {
			doctor = this;
		}
		if (doctor!= patient.getMyDoctor()){
			System.out.println(doctor.getId() + " is ending reassessment but the doctor who has the patient is: " +patient.getMyDoctor() );
		}
		int totalProcess = patient.getTotalProcesses();
		patient.setTotalProcesses(totalProcess + 1);

		printTime();
		
		if (patient.getMyBedReassessment()==null){
			System.out.println("Que pasoooo! Donde esta mi camita :(");
		}
		System.out
				.println("*****                               \n \n END RE-ASSESSMENT "
						+ doctor.getId()
						+ " and "
						+ patient.getId()

						+ " are at "
						+ patient.getMyBedReassessment().getId()
						+ " loc "
						+ patient.getLoc()
						+ "                  \n  ");

		Resource resourceToRelease = patient.getMyBedReassessment();
		System.out.println(" the bed to release is: "
				+ resourceToRelease.getId());
		doctor.setMyPatientCalling(null);

		int myPatientsInBed = doctor.getMyPatientsInBed().size();

		System.out.println(" method end reassessment ");
		if (this.myPatientsInBed.contains(patient)){
			this.myPatientsInBed.remove(patient);
		} else {
			Doctor doctorP= patient.getMyDoctor();
			if (doctorP.myPatientsInBed.contains(patient)){
				doctorP.myPatientsInBed.remove(patient);
			}
		}
		this.removePatientFromDepartment(patient);
		movePatientBedReassessment(doctor);
		System.out.println(doctor.getId()
				+ " is moving to doctors area at end re-assessment");
		//		doctor.setStartedReassessment(0);
	}

	private void movePatientBedReassessment(Doctor doctor) {
		if (patientsWaitingForCubicle.size() > 0) {
			Patient patientWaiting = patientsWaitingForCubicle.get(0);
			Resource bed = doctor.findBed(patientWaiting.getTriageNum());
			if (bed != null) {
				patientsWaitingForCubicle.remove(patientWaiting);
				patientWaiting.setMyBedReassessment(bed);
				patientWaiting.getMyBedReassessment().setAvailable(false);
				patientWaiting.moveBackToBed(bed);
			}
		}
	}

	protected abstract double[] reassessmentParameters(int triageNum);

	protected void removePatientFromDepartment(Patient patient) {
		if (patient.getMyDoctor()!=null){
			if (patient.getMyDoctor().myPatientsInBed.contains(patient))
			patient.getMyDoctor().myPatientsInBed.remove(patient);
			
			if (patient.getMyDoctor().myPatientsInTests.contains(patient))
			patient.getMyDoctor().myPatientsInTests.remove(patient);
			
			if (patient.getMyDoctor().allMyPatients.contains(patient))
			patient.getMyDoctor().allMyPatients.remove(patient);
			}
			
			if (patient.getMyDoctor().getMyPatientsBackInBed().contains(patient)){
				patient.getMyDoctor().myPatientsBackInBed.remove(patient);
				}
			
			printTime();
		
			System.out.println(patient.getId() + " IS LEAVING THE DEPARTMENT");
			System.out.println("method remove patient from dep " + this.getId()
					+ " is setting " + patient.getMyResource().getId()
					+ " available= true");
			System.out
					.println(patient.getId()
							+ " His time in system is:  "
							+ patient.getTimeInSystem());
			patient.getMyResource().setAvailable(true);
			patient.setMyResource(null);
			patient.setMyBedReassessment(null);
			if (patient.getMyNurse() != null){
				patient.getMyNurse().releaseFromPatient(patient);
			}			
			this.releaseFromPatient(patient);
			patient.setMyDoctor(null);
			printTime();
			System.out.println(patient.getId() + " goes to qTrolley");
			this.setMyResource(null);
			this.myPatientsInBed.remove(patient);
			printElementsQueue(myPatientsInBed,
					" my patients in bed ");
		
			patient.setInSystem(false);
			patient.getTimeInSystem();
			patient.addToQ("qTrolley ");
			System.out.println(patient.getId() + " has moved to trolley");
			System.out.println(patient.getId() + " has left  the department");
			System.out.println(patient.getId() + " is in system = " + patient.isInSystem());
			patient.getAllServicesTimes();
		
	}

	private void chooseRoute(Patient patient, Doctor doctor, int[] route) {
		if (route[0] == 0 && route[1] == 0) {
			patient.setMyBedReassessment(patient.getMyResource());
			System.out.println(patient.getId() + " starts reassessment immediately");
			patient.setNeedsTests(false);
			System.out.println(doctor.getId() + " is doing reassessment after first assessment " + patient.getId());
			patient.moveBackToBed(patient.getMyResource());			
		} else {
			System.out.println(patient.getId() + " needs tests ");
			patient.setNeedsTests(true);
			//10% de los pacientes no bloquean el cubiculo. Los rojos siempre la bloquean.
			if (Math.random() < 0.1 && patient.getMyResource().getResourceType().substring(0, 4).equals("resus")) {
				Resource resourceToRelease = patient.getMyResource();	
				System.out.println(patient.getId() + " does not wait for tests in bed and is releasing: " + resourceToRelease.getId());

				System.out.println("method end fst assessment " + this.getId()
						+ " is setting " + resourceToRelease.getId()
						+ " available= true, because " + patient.getId()
						+ " does not wait for test in bed");
				resourceToRelease.setAvailable(true);
				patient.setWaitInCublicle(false);
				patient.setIsWaitingBedReassessment(1);
			} else {
				System.out.println(patient.getId() + " waits in bed");
				Resource resourceToGo = patient.getMyResource();
				patient.setMyBedReassessment(resourceToGo);
				patient.setWaitInCublicle(true);
				patient.getMyBedReassessment().setAvailable(false);
				System.out.println(patient.getId() + " is keeping blocked as bed reassessment " + patient.getMyBedReassessment().getId());
			}
			
			printTime();
			System.out.println(patient.getId()
					+ " keeps in mind that his assigned doctor is  "
					+ patient.getMyDoctor().getId());

			doctor.setMyResource(null);
			doctor.myPatientsInBed.remove(patient);
			System.out.println(doctor.getId() + " will move to doctors area"
					+ " method: endFstAssessment"
					+ " this method is being called by " + this.getId());
			System.out.println(doctor.getId() + " decides for patient's test");
			if (route[0] == 1) {
				printTime();

				System.out.println(patient.getId() + " needs Xray ");
				System.out.println(patient.getId() + " goes to qXray");
				patient.addToQ("qXRay ");
				// patient.increaseTestCounterXray();
				patient.setTotalNumTest(patient.getTotalNumTest() + 1);
				System.out.println(doctor.getId()
						+ " adds to list of patients in test "
						+ patient.getId());
				System.out.println(" method end First assessment"
						+ doctor.getId() + " will add to his patients in test "
						+ patient.getId());
				doctor.myPatientsInTests.add(patient);
				printElementsArray(doctor.getMyPatientsInTests(),
						" my patients in test ");
				if (route[1] == 1) {
					printTime();
					patient.setNextProc(1);
					System.out.println(patient.getId()
							+ " will need test after Xray");
					// patient goes to test after xray
				} else {
					printTime();
					patient.setNextProc(2);
					System.out.println(patient.getId()
							+ " will go back to bed after Xray");
					// patient goes back to bed after xray
				}
			}

			else if (route[0] == 0 && route[1] == 1) {
				printTime();

				System.out.println(patient.getId()
						+ " needs test and didn't need xRay ");
				System.out.println(doctor.getId()
						+ " adds to list of patients in test "
						+ patient.getId());
				doctor.myPatientsInTests.add(patient);
				patient.addToQ("qTest ");
				// patient.increaseTestCounterTest();
				patient.setNextProc(2);
				patient.setTotalNumTest(patient.getTotalNumTest() + 1);
				System.out.println(" method end First assessment"
						+ doctor.getId()
						+ " has added to his patients in test "
						+ patient.getId());
				printElementsArray(doctor.getMyPatientsInTests(),
						" my patients in test ");

			}
		}
	}

	private int[] decideTests(int triageNum) {
		Uniform unif = RandomHelper.createUniform();
		double rndXRay = unif.nextDouble();
		double rndTest = unif.nextDouble();
		int testRoute[] = { 0, 0 };
		if (rndXRay <= Reader.getMatrixPropTest()[triageNum - 1][0]) {
			testRoute[0] = 1;

		}
		if (rndTest <= Reader.getMatrixPropTest()[triageNum - 1][1]) {
			testRoute[1] = 1;

		}
		return testRoute;
	}

	private Doctor chooseDocFirstAssess(Patient patient, Doctor doctor) {
		if (patient == null) {
			System.err
					.println("\n ERROR: Shouldn't be happening, patient is null at end of fst assessment");
		} else {
			if (patient.getMyDoctor() == this && (this.isInShift())) {
				System.out
						.println(" the method end fst assessment is being called by "
								+ this.getId()
								+ " that is the same doctor the patien had in mind ");
				System.out
						.println(this.getId()
								+ " is in shift, then it is possible to start end fst assessment");
				doctor = this;
			} else {
				if (patient.getMyDoctor() != null)
					if (patient.getMyDoctor().isInShift()) {
						System.out
								.println(patient.getMyDoctor().getId()
										+ " is not in shift but is ending the fst assessment with "
										+ patient.getId());
						doctor = patient.getMyDoctor();
					} else {
						doctor = patient.getMyDoctor();
					}
			}
		}
		return doctor;
	}

	public abstract double[] firstAssessmentParameters(int triageNum);
	

	protected void canMoveOut(){
		if(this.getAllMyPatients().size()>0){
			System.out.println(this.getId() + " STILL HAS PATIENTS TO SEE, CHECKING IF THERE ARE DOCTOR TO TAKE OVER");
			if (this.doctorToTakeOver() != null) {
				System.out.println(this.getId() + " has found a doctor to hand over his patients ");
				System.out.println(this.getId() + " IS MOVING OUT ");
				this.moveOut();
			}							
		} else {
			System.out.println(this.getId() + " has no patients left to see ");
			System.out.println(this.getId() + " IS MOVING OUT ");
			this.moveOut();
		}
	}
	
	protected void canNotMoveOut(){
		double maxEndingTime= 0; 
		for (Iterator<Patient> iterator = this.patientsInMultitask.iterator(); iterator
				.hasNext();) {
			Patient patient = (Patient) iterator.next();
			if(patient.getTimeEndCurrentService()>maxEndingTime){
				maxEndingTime= patient.getTimeEndCurrentService();
			}
		}
		if (getTime() < maxEndingTime) {
			printTime();
			System.out
					.println(this.getId()
							+ " has finished his shift but needs to wait to leave because he still has work to do");
			double timeEnding = maxEndingTime;
			this.scheduleEndShift(timeEnding+5);
		}
	}
	
	@Override
	public void resetVariables() {
		if (this.allMyPatients.size() > 0) {
			printTime();
			System.out
					.println(this.getId()
							+ " has finished his shift and still has patients, needs to hand over his patients  ");
			this.doctorToTakeOver();
	
		}			
		//TODO Resetear PECS
		this.setInShift(false);
		this.setAvailable(false);
		int i = this.getIdNum();
		int x = 19;
		int y = i + 4;
	
		grid.moveTo(this, x, y);
		this.isAtDoctorArea=false;
	}
	
	private Doctor doctorToTakeOver() {
		Doctor doctor = null;
		context = getContext();
		boolean enterIf = false;
		for (Object d : context.getObjects(Doctor.class)) {
			if (d != null) {
				doctor = (Doctor) d;
				int hour = getHour();
				int day = getDay();
				// Circular list for hour
				int nextHour = hour + 1;
				int nextDay = day;
				if (nextHour > 23) {
					nextHour = 0;
					nextDay = day + 1;
					if (nextDay > 6) {
						nextDay = 0;
					}
				}
				if (doctor.getClass() != Consultant.class) {
					if ((doctor.getMyShiftMatrix()[hour][day] == 1)
							&& (doctor.getMyShiftMatrix()[nextHour][nextDay] == 1)) {
						enterIf = true;
						printTime();
						System.out.println("there is a doctor to take over: "
								+ doctor.getId());
						this.handOver(doctor);
						doctor.decideWhatToDoNext();
						break;
					}

				} else
					doctor = null;
			}

		}

		if (doctor == null) {
			System.err.println("there is no doctor to take over: "
					+ this.getId() + " is leaving");
			System.out.println(this.getId()
					+ " search for somebody to stay at least when he leaves");
			for (Object d : context.getObjects(Doctor.class)) {
				if (d != null) {
					doctor = (Doctor) d;
					int hour = getHour();
					int day = getDay();
					// Circular list for hour
					int nextHour = hour + 1;
					int nextDay = day;
					if (nextHour > 23) {
						nextHour = 0;
						nextDay = day + 1;
						if (nextDay > 6) {
							nextDay = 0;
						}
					}
					if (doctor.getClass() != Consultant.class) {
						if ((doctor.getMyShiftMatrix()[hour][day] == 1)) {
							enterIf = true;
							printTime();
							System.out
									.println("there is a doctor to take over: "
											+ doctor.getId());
							this.handOver(doctor);
							doctor.decideWhatToDoNext();
							break;
						}
					}
				}

			}

		}

		return doctor;
	}
	private void handOver(Doctor doctor) {
		printTime();
		System.out.println(this.getId() + " has started method hand over "
				+ doctor.getId());
		PriorityQueue<Patient> newMyPatientsInBedTime = this
				.getMyPatientsInBed();
		ArrayList<Patient> newMyPatientsInTests = this.getMyPatientsInTests();
		LinkedList<Patient> myNewPatientsBackInBed = this.myPatientsBackInBed;

		System.out.println(doctor.getId()
				+ " before take over the patients had in all his list: \n");
		doctor.printElementsQueue(doctor.myPatientsInBed,
				" patients in bed \n");
		doctor.printElementsArray(doctor.myPatientsInTests,
				" patients in test \n");
		if (this.myPatientsInBed.size() > 0)
			doctor.myPatientsInBed.addAll(newMyPatientsInBedTime);
		if (this.myPatientsInTests.size() > 0)
			doctor.myPatientsInTests.addAll(newMyPatientsInTests);
		if (this.myPatientsBackInBed.size() > 0)
			doctor.myPatientsBackInBed.addAll(myNewPatientsBackInBed);

		for (int i = 0; i < this.getAllMyPatients().size(); i++) {
			Patient patient = this.getAllMyPatients().get(i);
			patient.setMyDoctor(doctor);
			printTime();
			System.out.println("/n " + patient.getId() + " has a new doctor: "
					+ patient.getMyDoctor().getId());
		}		
		this.myPatientsInBed.clear();
		this.myPatientsInTests.clear();
		this.allMyPatients.clear();
		this.doctorToHandOver = doctor;
		System.out.println(doctor.getId()
				+ " receiving after take over at time:  " + getTime());
		System.out.println(doctor.getId() + " has received in his list: \n");
		doctor.printElementsQueue(doctor.myPatientsInBed,
				" patients in bed \n");
		doctor.printElementsArray(doctor.myPatientsInTests,
				" patients in test \n");
		System.out.println(this.getId()
				+ " is leaving, after hand over at time:  " + getTime());
		System.out.println(this.getId() + " should have an empty list: \n");
		doctor.printElementsQueue(this.myPatientsInBed,
				" patients in bed \n");
		doctor.printElementsArray(this.myPatientsInTests,
				" patients in test \n");
		System.out.println(this.getId() + " handled to " + doctor.getId()
				+ " time " + getTime());
	
	}
	
	public void moveToDoctorsArea() {
	
		if (this.getClass() == Sho.class) {
			boolean flag = false;
			int y = 5;
			int x;
			for (int j = 0; j < 2; j++) {
				for (int i = 1; i < 6; i++) {
					Object o = grid.getObjectAt(i + 6, y + j);
					if (o == null) {
						x = i + 6;
						grid.moveTo(this, x, 5);
						this.isAtDoctorArea = true;
						System.out.println(this.getId()
								+ " has moved to doctors area "
								+ this.getLoc().toString() + " at time "
								+ getTime());
						flag = true;
						break;
					}
					if (flag) {
						break;
					}
				}
				if (flag) {
					break;
				}
			}
	
		} else if (this.getClass() == Consultant.class) {
	
			grid.moveTo(this, this.getInitPosX(), this.initPosY);
	
			System.out.println(this.getId() + " has moved to consultant area "
					+ this.getLoc().toString());
		}
	
		this.setInShift(true);
		this.setAvailable(true);
		System.out.println(this.getId() + " is in shift and is available at "
				+ getTime());
	}
	
	// implements comparator
	class PriorityQueueComparatorTriage implements Comparator<Patient> {
	
		@Override
		public int compare(Patient p1, Patient p2) {
	
			if (p1.getTriageNum() > p2.getTriageNum()) {
				return -1;
			}
			if (p1.getTriageNum() < p2.getTriageNum()) {
				return 1;
			} else {
				if (p1.getTimeInSystem() > p2.getTimeInSystem()) {
					return -1;
				}
				if (p1.getTimeInSystem() < p2.getTimeInSystem()) {
					return 1;
				} else
					return 0;
			}
	
		}
	}
	
	class PriorityQueueComparatorTime implements Comparator<Patient> {
	
		@Override
		public int compare(Patient p1, Patient p2) {
			if (p1.getTimeInSystem() > p2.getTimeInSystem()) {
				return -1;
			}
			if (p1.getTimeInSystem() < p2.getTimeInSystem()) {
				return 1;
			}
			return 0;
		}
	
	}
	
	public PriorityQueue<Patient> getMyPatientsInBed() {
		return myPatientsInBed;
	}
	
	public void setMyPatientsInBed(PriorityQueue<Patient> myPatientsInBed) {
		this.myPatientsInBed = myPatientsInBed;
	}
	
	public LinkedList<Patient> getMyPatientsBackInBed() {
		return myPatientsBackInBed;
	}
	
	public void setMyPatientsBackInBed(LinkedList<Patient> myPatientsBackInBed) {
		this.myPatientsBackInBed = myPatientsBackInBed;
	}
	
	public ArrayList<Patient> getMyPatientsInTests() {
		return myPatientsInTests;
	}
	
	public void setMyPatientsInTests(ArrayList<Patient> myPatientsInTests) {
		this.myPatientsInTests = myPatientsInTests;
	}
	
	public ArrayList<Patient> getAllMyPatients() {
		return allMyPatients;
	}
	
	public void setAllMyPatients(ArrayList<Patient> allMyPatients) {
		this.allMyPatients = allMyPatients;
	}
	
	public boolean isAtDoctorArea() {
		return isAtDoctorArea;
	}
	
	public void setAtDoctorArea(boolean isAtDoctorArea) {
		this.isAtDoctorArea = isAtDoctorArea;
	}
	
	public double getTimeEnterSimulation() {
		return timeEnterSimulation;
	}
	
	public void setTimeEnterSimulation(double timeEnterSimulation) {
		this.timeEnterSimulation = timeEnterSimulation;
	}
	
	public Patient getMyPatientCalling() {
		return myPatientCalling;
	}
	
	public void setMyPatientCalling(Patient myPatientCalling) {
		this.myPatientCalling = myPatientCalling;
	}
	
	public Doctor getDoctorToHandOver() {
		return doctorToHandOver;
	}
	
	public void setDoctorToHandOver(Doctor doctorToHandOver) {
		this.doctorToHandOver = doctorToHandOver;
	}
		
}
