/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.enums;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
public enum Asses {
	Asse1(1), Asse2(2), Asse3(3), Asse4(4), Asse5(5), Asse6(6), Asse7(7), Asse8(8), Asse9(9), Asse10(10), Asse11(
			11), Asse12(12), Asse13(13);

	private Asses(int value) {
		this.asse = value;
	}

	public int getAsse() {
		return this.asse;
	}

	private int asse;

	@Override
	public String toString() {
		return String.valueOf(this.asse);
	}
}
