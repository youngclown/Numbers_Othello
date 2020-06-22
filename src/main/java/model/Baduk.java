package model;

public class Baduk {
	/*
	 * B : BLACK
	 * W : WHITE
	 * K : BLANK
	 * L : BLOCK
	 * BL : BLACK BLOCK
	 * WL : WHITE BLOCK
	 */
	String type = "";
	int i = 0;
	int j = 0;
	int value = 0;
	int[] tempIJ = new int[9];
	
	public int[] getTempIJ() {
		return tempIJ;
	}
	public void setTempIJ(int[] tempIJ) {
		this.tempIJ = tempIJ;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public int getJ() {
		return j;
	}
	public void setJ(int j) {
		this.j = j;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
