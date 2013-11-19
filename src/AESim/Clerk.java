package AESim;

import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Clerk extends Staff {
	private static int count;

	public Clerk(Grid<Object> grid, int idNum, int x, int y) {
		this.grid = grid;
		this.id = "Clerk " + idNum;
		this.idNum = idNum;
		this.numAvailable = 1;
		this.initPosX = x;
		this.initPosY = y;
		this.available = true;
		this.inShift = false;
		this.getLoc(grid);
	}

	private GridPoint getLoc(Grid<Object> grid) {
		loc = grid.getLocation(this);
		return loc;
	}

	public static void initSaticVars() {
		setCount(0);		
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Clerk.count = count;
	}
}
