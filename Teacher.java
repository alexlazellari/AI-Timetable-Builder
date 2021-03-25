import java.util.ArrayList;

public class Teacher {

	String codeT;
	String nameT;
	ArrayList<String> codeL;
	int mhpd;
	int mhpw;

	Teacher() {
		codeT = "";
		nameT = "";
		codeL = null;
		mhpd = -1;
		mhpw = -1;

	}

	Teacher(String codeT, String nameT, ArrayList<String> codeL, int mhpd, int mhpw) {
		this.codeT = codeT;
		this.nameT = nameT;
		this.codeL = codeL;
		this.mhpd = mhpd;
		this.mhpw = mhpw;
	}

	// Getters and Setters	
	public String getCodeT() {
		return codeT;
	}

	public void setCodeT(String codeT) {
		this.codeT = codeT;
	}

	public String getNameT() {
		return nameT;
	}

	public void setNameT(String nameT) {
		this.nameT = nameT;
	}

	public ArrayList<String> getCodeL() {
		return codeL;
	}

	public void setCodeL(ArrayList<String> codeL) {
		this.codeL = codeL;
	}

	public int getMhpd() {
		return mhpd;
	}

	public void setMhpd(int mhpd) {
		this.mhpd = mhpd;
	}

	public int getMhpw() {
		return mhpw;
	}

	public void setMhpw(int mhpw) {
		this.mhpw = mhpw;
	}
}