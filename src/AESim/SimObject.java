package AESim;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class SimObject {
	
	protected String id;
	protected int idNum;	
	protected Grid<Object> grid;
	protected GridPoint loc;
	protected Context<Object> context;
	protected static double minute;
	protected static int hour;
	protected static int day;
	protected static int week;
	protected static int dayTotal;
	
	public double getTime() {
		double time = (RunEnvironment.getInstance().getCurrentSchedule()
				.getTickCount());
		return time;
	}
	
	public static void initSaticVar() {
		setMinute(0);
		setHour(0);
		setDay(0);
		setWeek(0);
		setDayTotal(0);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIdNum() {
		return idNum;
	}

	public void setIdNum(int idNum) {
		this.idNum = idNum;
	}

	public Grid<Object> getGrid() {
		return grid;
	}

	public void setGrid(Grid<Object> grid) {
		this.grid = grid;
	}

	public GridPoint getLoc() {
		return loc;
	}

	public void setLoc(GridPoint loc) {
		this.loc = loc;
	}

	public Context<Object> getContext() {
		return context;
	}

	public void setContext(Context<Object> context) {
		this.context = context;
	}

	public static double getMinute() {
		return minute;
	}

	public static void setMinute(double minute) {
		SimObject.minute = minute;
	}

	public static int getHour() {
		return hour;
	}

	public static void setHour(int hour) {
		SimObject.hour = hour;
	}

	public static int getDay() {
		return day;
	}

	public static void setDay(int day) {
		SimObject.day = day;
	}

	public static int getWeek() {
		return week;
	}

	public static void setWeek(int week) {
		SimObject.week = week;
	}

	public static int getDayTotal() {
		return dayTotal;
	}

	public static void setDayTotal(int dayTotal) {
		SimObject.dayTotal = dayTotal;
	}
	
}
