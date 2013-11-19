package AESim;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Doctor extends Staff {
	
	protected PriorityQueue<Patient> myPatientsInBed;
	protected LinkedList<Patient> myPatientsBackInBed;
	protected ArrayList<Patient> myPatientsInTests;
	protected ArrayList<Patient> allMyPatients;
	protected boolean isAtDoctorArea;
	protected double timeEnterSimulation;
	protected Patient myPatientCalling;
	protected Doctor doctorToHandOver;
	
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
}
