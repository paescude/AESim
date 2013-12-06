package Datos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class Reader {
	
	private static float[][] matrixNurse1;
	private static float[][] matrixNurse2;
	private static float[][] matrixNurse3;
	private static float[][] matrixNurse4;
	private static float[][] matrixNurse5;
	private static float[][] arrayDNW; 

	private static float[][] matrixClerk1;
	private static float[][] matrixClerk2;
	
	private static float[][] matrixArrivalWalkIn;
	private static float[][] matrixArrivalAmbulance;
	private static float[][] matrixTriagePropByArrival;
	private static float[][] matrixPropTest;

	private static float[][] matrixSHOD0;
	private static float[][] matrixSHOD1;
	private static float[][] matrixSHOD2;
	private static float[][] matrixSHOD3;
	private static float[][] matrixSHOD4;
	private static float[][] matrixSHOD5;
	private static float[][] matrixSHOD6;
	private static float[][] matrixSHOD7;
	private static float[][] matrixSHOD8;
	private static float[][] matrixSHOD9;
	
	public static void readAllData() throws IOException {
		setMatrixArrivalWalkIn(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\DatosIn.txt",
				24, 7));
		setMatrixArrivalAmbulance(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\DatosAmbulance.txt",
				24, 7));
		setMatrixClerk1(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosClerk1.txt",
				24, 7));
		setMatrixClerk2(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosClerk2.txt",
				24, 7));
		setMatrixNurse1(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosNurse1.txt",
				24, 7));
		setMatrixNurse2(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosNurse2.txt",
				24, 7));
		setMatrixNurse3(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosNurse3.txt",
				24, 7));
		setMatrixNurse4(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosNurse4.txt",
				24, 7));
		setMatrixNurse5(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosNurse5.txt",
				24, 7));
		setArrayDNW(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosDNW.txt",
				24,1));
		setMatrixSHOD0(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosSHO_D0.txt",
				24, 7));
		setMatrixSHOD1(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosSHO_D1.txt",
				24, 7));
		setMatrixSHOD2(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosSHO_D2.txt",
				24, 7));
		setMatrixSHOD3(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosSHO_D3.txt",
				24, 7));
		setMatrixSHOD4(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosSHO_D4.txt",
				24, 7));
		setMatrixSHOD5(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosSHO_D5.txt",
				24, 7));
		setMatrixSHOD6(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosSHO_D6.txt",
				24, 7));
		setMatrixSHOD7(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosSHO_D7.txt",
				24, 7));
		setMatrixSHOD8(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosSHO_D8.txt",
				24, 7));
		setMatrixSHOD9(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosSHO_D9.txt",
				24, 7));
		setMatrixTriagePropByArrival(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosTriageByArrival.txt",
				5, 2));
		// En la MatrixTriagePropArrival la primera columna corresponde a WalkIn
		// y la segunda a Ambulance, son las distribuciones acumuladas
		setMatrixPropTest(readFileIn(
				"C:\\RepastSimphony-2.1\\AESim\\src\\Datos\\datosProportionsTests.txt",
				5, 2));
		// En la MatrixPropTest la primera columna corresponde a XRay y la
		// segunda a Test
	}
	
	public static float[][] readFileIn(String fileName, int rows, int cols)
			throws IOException {
		String arrivalMatrix[][] = new String[rows][cols];
		float[][] arrivalMatrixfloat = new float[rows][cols];
		String line, token, delimiter = ",";

		StringTokenizer tokenizer;

		BufferedReader input = null;
		int i = 0;
		int j = 0;
		try {
			input = new BufferedReader(new FileReader(fileName));
			line = input.readLine(); // when printed gives first line in file
			// outer while (process lines)
			while (line != null) { // doesn't seem to start from first line
				tokenizer = new StringTokenizer(line, delimiter);

				while (tokenizer.hasMoreTokens()) {// process tokens in line
					token = tokenizer.nextToken();
					arrivalMatrix[i][j] = token;
					j++;
				}// close inner while
				j = 0;
				line = input.readLine(); // next line
				i++;
			}// close outer while

		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file " + fileName);
		} catch (IOException e) {
			System.out.println("Unable to read from file " + fileName);
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
				System.out.println("Unable to close file " + fileName);
			}
		}

		for (int a = 0; a < rows; a++) {
			for (int b = 0; b < cols; b++) {

				arrivalMatrixfloat[a][b] = Float
						.parseFloat(arrivalMatrix[a][b]);

			}


		}

		return arrivalMatrixfloat;
	}

	public static float[][] getMatrixNurse1() {
		return matrixNurse1;
	}

	public static void setMatrixNurse1(float[][] matrixNurse1) {
		Reader.matrixNurse1 = matrixNurse1;
	}

	public static float[][] getMatrixNurse2() {
		return matrixNurse2;
	}

	public static void setMatrixNurse2(float[][] matrixNurse2) {
		Reader.matrixNurse2 = matrixNurse2;
	}

	public static float[][] getMatrixNurse3() {
		return matrixNurse3;
	}

	public static void setMatrixNurse3(float[][] matrixNurse3) {
		Reader.matrixNurse3 = matrixNurse3;
	}

	public static float[][] getMatrixNurse4() {
		return matrixNurse4;
	}

	public static void setMatrixNurse4(float[][] matrixNurse4) {
		Reader.matrixNurse4 = matrixNurse4;
	}

	public static float[][] getMatrixNurse5() {
		return matrixNurse5;
	}

	public static void setMatrixNurse5(float[][] matrixNurse5) {
		Reader.matrixNurse5 = matrixNurse5;
	}

	public static float[][] getArrayDNW() {
		return arrayDNW;
	}

	public static void setArrayDNW(float[][] arrayDNW) {
		Reader.arrayDNW = arrayDNW;
	}

	public static float[][] getMatrixClerk1() {
		return matrixClerk1;
	}

	public static void setMatrixClerk1(float[][] matrixClerk1) {
		Reader.matrixClerk1 = matrixClerk1;
	}

	public static float[][] getMatrixClerk2() {
		return matrixClerk2;
	}

	public static void setMatrixClerk2(float[][] matrixClerk2) {
		Reader.matrixClerk2 = matrixClerk2;
	}

	public static float[][] getMatrixArrivalWalkIn() {
		return matrixArrivalWalkIn;
	}

	public static void setMatrixArrivalWalkIn(float[][] matrixArrivalWalkIn) {
		Reader.matrixArrivalWalkIn = matrixArrivalWalkIn;
	}

	public static float[][] getMatrixArrivalAmbulance() {
		return matrixArrivalAmbulance;
	}

	public static void setMatrixArrivalAmbulance(float[][] matrixArrivalAmbulance) {
		Reader.matrixArrivalAmbulance = matrixArrivalAmbulance;
	}

	public static float[][] getMatrixTriagePropByArrival() {
		return matrixTriagePropByArrival;
	}

	public static void setMatrixTriagePropByArrival(
			float[][] matrixTriagePropByArrival) {
		Reader.matrixTriagePropByArrival = matrixTriagePropByArrival;
	}

	public static float[][] getMatrixPropTest() {
		return matrixPropTest;
	}

	public static void setMatrixPropTest(float[][] matrixPropTest) {
		Reader.matrixPropTest = matrixPropTest;
	}

	public static float[][] getMatrixSHOD0() {
		return matrixSHOD0;
	}

	public static void setMatrixSHOD0(float[][] matrixSHOD0) {
		Reader.matrixSHOD0 = matrixSHOD0;
	}

	public static float[][] getMatrixSHOD1() {
		return matrixSHOD1;
	}

	public static void setMatrixSHOD1(float[][] matrixSHOD1) {
		Reader.matrixSHOD1 = matrixSHOD1;
	}

	public static float[][] getMatrixSHOD2() {
		return matrixSHOD2;
	}

	public static void setMatrixSHOD2(float[][] matrixSHOD2) {
		Reader.matrixSHOD2 = matrixSHOD2;
	}

	public static float[][] getMatrixSHOD3() {
		return matrixSHOD3;
	}

	public static void setMatrixSHOD3(float[][] matrixSHOD3) {
		Reader.matrixSHOD3 = matrixSHOD3;
	}

	public static float[][] getMatrixSHOD4() {
		return matrixSHOD4;
	}

	public static void setMatrixSHOD4(float[][] matrixSHOD4) {
		Reader.matrixSHOD4 = matrixSHOD4;
	}

	public static float[][] getMatrixSHOD5() {
		return matrixSHOD5;
	}

	public static void setMatrixSHOD5(float[][] matrixSHOD5) {
		Reader.matrixSHOD5 = matrixSHOD5;
	}

	public static float[][] getMatrixSHOD6() {
		return matrixSHOD6;
	}

	public static void setMatrixSHOD6(float[][] matrixSHOD6) {
		Reader.matrixSHOD6 = matrixSHOD6;
	}

	public static float[][] getMatrixSHOD7() {
		return matrixSHOD7;
	}

	public static void setMatrixSHOD7(float[][] matrixSHOD7) {
		Reader.matrixSHOD7 = matrixSHOD7;
	}

	public static float[][] getMatrixSHOD8() {
		return matrixSHOD8;
	}

	public static void setMatrixSHOD8(float[][] matrixSHOD8) {
		Reader.matrixSHOD8 = matrixSHOD8;
	}

	public static float[][] getMatrixSHOD9() {
		return matrixSHOD9;
	}

	public static void setMatrixSHOD9(float[][] matrixSHOD9) {
		Reader.matrixSHOD9 = matrixSHOD9;
	}
}
