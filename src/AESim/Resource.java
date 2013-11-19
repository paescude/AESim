package AESim;

import repast.simphony.space.grid.Grid;

public class Resource extends SimObject {

	private static int count;
	private boolean available;
	private String resourceType;
	private static int countTypeXRay = 1;
	private static int countTypeTest = 1;
	private static int countTypeTriage = 1;
	private static int countTypeTrolley = 1;
	private static int countTypeMinor = 1;
	private static int countTypeMajor = 1;
	private static int countTypeResus = 1;
	
	public Resource(String resourceType, String rName, Grid<Object> grid) {
		this.available = true;
		this.resourceType = resourceType;
		switch (resourceType) {
		case "triage cubicle ":
			this.setId(resourceType + countTypeTriage++);
			break;
		case "trolley ":
			this.setId(resourceType + countTypeTrolley++);
			break;
		case "minor cubicle ":
			this.setId(resourceType + countTypeMinor++);
			break;
		case "major cubicle ":
			this.setId(resourceType + countTypeMajor++);
			break;
		case "resus cubicle ":
		this.setId(resourceType + countTypeResus++);
			break;
		default:
			break;
		}
		
//
//		if (resourceType.equals("xRayRoom ")) {
//			this.setId(resourceType + countTypeXRay++);
//			this.setNumAvailableXray(1);
//			processTime[0] = 20;
//			processTime[1] = 40;
//			processTime[2] = 32;
//		} else if (resourceType.equals("testRoom ")) {
//			this.setId(resourceType + countTypeTest++);
//			this.setNumAvailableTest(1);
//			processTime[0] = 10;
//			processTime[1] = 25;
//			processTime[2] = 20;
//		} else {
//			processTime[0] = 0;
//			processTime[1] = 0;
//			processTime[2] = 0;
//		}
//		this.typeResource = 0;
//		this.grid = grid;
	}

	public static void initSaticVars() {
		count = 0;		
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Resource.count = count;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public static int getCountTypeXRay() {
		return countTypeXRay;
	}

	public static void setCountTypeXRay(int countTypeXRay) {
		Resource.countTypeXRay = countTypeXRay;
	}

	public static int getCountTypeTest() {
		return countTypeTest;
	}

	public static void setCountTypeTest(int countTypeTest) {
		Resource.countTypeTest = countTypeTest;
	}

	public static int getCountTypeTriage() {
		return countTypeTriage;
	}

	public static void setCountTypeTriage(int countTypeTriage) {
		Resource.countTypeTriage = countTypeTriage;
	}

	public static int getCountTypeTrolley() {
		return countTypeTrolley;
	}

	public static void setCountTypeTrolley(int countTypeTrolley) {
		Resource.countTypeTrolley = countTypeTrolley;
	}

	public static int getCountTypeMinor() {
		return countTypeMinor;
	}

	public static void setCountTypeMinor(int countTypeMinor) {
		Resource.countTypeMinor = countTypeMinor;
	}

	public static int getCountTypeMajor() {
		return countTypeMajor;
	}

	public static void setCountTypeMajor(int countTypeMajor) {
		Resource.countTypeMajor = countTypeMajor;
	}

	public static int getCountTypeResus() {
		return countTypeResus;
	}

	public static void setCountTypeResus(int countTypeResus) {
		Resource.countTypeResus = countTypeResus;
	}
}
