public class Lesson{
	String code;
	String lesson;
	String clas;
	int hpw;
	
	Lesson(){
		code="";
		lesson="";
		clas="";
		hpw=-1;
		
	}
	Lesson(String code , String lesson , String clas, int hpw){
		this.code = code;
		this.lesson=lesson;
		this.clas=clas;
		this.hpw=hpw;
	}

	//Getters and Setters
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLesson() {
		return lesson;
	}
	public void setLesson(String lesson) {
		this.lesson = lesson;
	}
	public String getClas() {
		return clas;
	}
	public void setClas(String clas) {
		this.clas = clas;
	}
	public int getHpw() {
		return hpw;
	}
	public void setHpw(int hpw) {
		this.hpw = hpw;
	}


}