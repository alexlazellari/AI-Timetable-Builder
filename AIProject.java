import java.util.ArrayList;
import java.util.Collections;

public class AIProject {

	public static void main(String args[]) {
		fileRW rw = new fileRW();

		Data.passData(rw.readTeachers("Teachers.txt"),rw.readLessons("Lessons.txt"));
		Data.whoMakes();
		Data.initializeTotalHours();
		
		
		//Data.workingHours();
		
		
		State initialState = new State();


		HillClimbing hillClimber = new HillClimbing();
		State x = new State();
		long start = System.currentTimeMillis(); 
		x = hillClimber.HC(initialState);
		long end = System.currentTimeMillis(); float sec = (end - start) / 1000F;
		System.out.println(sec + " seconds");





		System.out.println("Rule1and3 ERRORS: "+x.Rule1and3());
		System.out.println("Rule2 ERRORS: "+x.Rule2());
		System.out.println("Rule4 ERRORS: "+x.Rule4());
		System.out.println("Rule5 ERRORS: "+x.Rule5());
		System.out.println("Stict rule1 ERRORS: "+x.StrictRule1());
		System.out.println("Strict rule2 ERRORS: "+x.StrictRule2());
		
		System.out.println("Error Counter: " +x.getErrorCounter());
		rw.writer("SCHEDULE.txt", x);
		
		

		
		
		
		
		

	}

}