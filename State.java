import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

public class State implements Comparable<State> {
	private static final int DAYS = 5;
	private static final int HOURS = 7;
	public String[][] schedule;
	private int length;
	private int width;
	private int errors;
	private HashSet<State> closedSet;
	
	public State() {
		schedule = new String[Data.getNumberOfTeachers()][Data.MAX_HOURS_OF_WEEK];
		this.length = Data.MAX_HOURS_OF_WEEK;
		this.width = Data.getNumberOfTeachers();
		this.Spaceinitializer();
		this.randomInitializer();
	}

	public State(String newSchedule[][],int errors) {
		schedule = new String[Data.getNumberOfTeachers()][Data.MAX_HOURS_OF_WEEK];
		this.length = Data.MAX_HOURS_OF_WEEK;
		this.width = Data.getNumberOfTeachers();
		for (int i = 0; i < this.getWidthOfSchedule(); i++) {

			for (int j = 0; j < this.getLengthOfSchedule(); j++) {
				this.schedule[i][j] = newSchedule[i][j];
			}
		}
		this.setErrorCounter(errors);
	}

	public void Spaceinitializer() {
		for (int i = 0; i < this.getWidthOfSchedule(); i++) {
			for (int j = 0; j < this.getLengthOfSchedule(); j++) {
				this.setClassAndSubject(i, j, "--", "----");
			}
		}
		this.errors = 0;
	}

	// Chromosome
	public void randomInitializer() {
		Random r = new Random();
		int randomteacher;
		int randomhour;
		boolean flag;

		for (int l = 0; l < Data.getNumberOfLessons(); l++) {
			for (int h = 0; h < Data.getLesson(l).getHpw(); h++) {
				
				flag = false;
				while (!flag) {
					randomteacher = Data.getWhoMakes(l).get(r.nextInt(Data.getWhoMakes(l).size()));
					randomhour = r.nextInt(Data.MAX_HOURS_OF_WEEK);
						if (this.getClass(randomteacher, randomhour).equals("--")) {
							this.setClassAndSubject(randomteacher, randomhour, Data.getLesson(l).getClas(),
									Data.getLesson(l).getCode());
							flag = true;
							break;
						}
					
				}

			}
		}
		Restrictions restrictions = new Restrictions(this);
		Thread a = new Thread(restrictions);
		a.start();
		this.setErrorCounter(restrictions.getChild().getErrorCounter());
		try {
			a.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public int getLengthOfSchedule() {
		return length;
	}

	public int getWidthOfSchedule() {
		return width;
	}

	public void setWidthOfSchedule(int width) {
		this.width = width;
	}

	public void setLengthOfSchedule(int length) {
		this.length = length;
	}

	public int getErrorCounter() {

		return this.errors;
	}

	public void setErrorCounter(int error) {

		this.errors = error;
	}

	public String getClass(int i, int j) {
		StringTokenizer clas = new StringTokenizer(schedule[i][j], ",");
		return clas.nextToken();
	}

	public String getSubject(int i, int j) {
		StringTokenizer clas = new StringTokenizer(schedule[i][j], ",");
		clas.nextToken();
		return clas.nextToken();
	}

	public String getClassAndSubject(int i, int j) {
		return schedule[i][j];
	}

	public void setClassAndSubject(int i, int j, String clas, String subject) {
		schedule[i][j] = clas + ',' + subject;
	}

	// Mutate by randomly changing the position of a queen
	public ArrayList<State> getChildren(int size) {
		ArrayList<State> children = new ArrayList<State>();
		ArrayList<Integer> teachers = null;
		Random r = new Random();
		int T1 = 0;
		int T2 = 0;
		int H1 = 0;
		int H2 = 0;
		String tempClass;
		String tempSubject;
		int counter = 0;
		int SR2Error=this.StrictRule2();
		Thread[] a = new Thread[size];
		Restrictions[] restrictions = new Restrictions[size];
		for (int s = 0; s < size; s++) {
			State child = new State(this.schedule,this.getErrorCounter());
			if (Math.random() <= 0.2 && SR2Error!=0) {
				boolean flag = true;
				while (flag) {
					
					T1 = r.nextInt(Data.getNumberOfTeachers());
					H1 = r.nextInt(Data.MAX_HOURS_OF_WEEK);
					H2 = r.nextInt(Data.MAX_HOURS_OF_WEEK);
					teachers = Data.getWhoMakes(child.getSubject(T1, H1));

					while (child.getSubject(T1, H1).equals("----") || teachers.size() == 1) {
						T1 = r.nextInt(Data.getNumberOfTeachers());
						H1 = r.nextInt(Data.MAX_HOURS_OF_WEEK);
						teachers = Data.getWhoMakes(child.getSubject(T1, H1));
					}
					counter = 0;
					for (int h = 0; h < Data.MAX_HOURS_OF_WEEK; h++) {
						if (child.getClassAndSubject(T1, h).equals(child.getClassAndSubject(T1, H1))) {
							counter++;
						}
					}
					if (counter != Data.getLessonHours(child.getSubject(T1, H1))) {
						flag = false;
					}else {
						flag=true;
					}
					counter = 0;
				}
				
				for (int t = 0; t < teachers.size(); t++) {
					if (T1 != teachers.get(t)) {
						for (int h = 0; h < Data.MAX_HOURS_OF_WEEK; h++) {
							if (child.getClassAndSubject(T1, H1).equals(child.getClassAndSubject(teachers.get(t), h))) {
								H2 = r.nextInt(Data.MAX_HOURS_OF_WEEK);
								while (child.getClassAndSubject(T1, H2).equals(child.getClassAndSubject(T1, H1))) {
									H2 = r.nextInt(Data.MAX_HOURS_OF_WEEK);
								}
								// for(int h1=(randomhour/HOURS)*HOURS;h1<
								// (((randomhour/HOURS)*HOURS)+HOURS);h1++) {
								tempClass = child.getClass(T1, H2);
								tempSubject = child.getSubject(T1, H2);
								child.setClassAndSubject(T1, H2, child.getClass(teachers.get(t), h),
										child.getSubject(teachers.get(t), h));
								child.setClassAndSubject(teachers.get(t), h, tempClass, tempSubject);
							}
						}
					}
				}
				
			} else {

				ArrayList<Integer> whoMakes;
				T1 = r.nextInt(Data.getNumberOfTeachers());
				H1 = r.nextInt(Data.MAX_HOURS_OF_WEEK);
				T2 = r.nextInt(Data.getNumberOfTeachers());
				H2 = r.nextInt(Data.MAX_HOURS_OF_WEEK);
				
				while(child.getClass(T1,H1).equals("--") && child.getClass(T2,H2).equals("--")) {
					T1 = r.nextInt(Data.getNumberOfTeachers());
					H1 = r.nextInt(Data.MAX_HOURS_OF_WEEK);
					T2 = r.nextInt(Data.getNumberOfTeachers());
					H2 = r.nextInt(Data.MAX_HOURS_OF_WEEK);
				}
				
				
				if (!child.getClass(T1, H1).equals("--")) {
					
						whoMakes = Data.getWhoMakes(child.getSubject(T1, H1));
						T2 = whoMakes.get(r.nextInt(whoMakes.size()));
					
					
				}else if (!child.getClass(T2, H2).equals("--")) {
				
						whoMakes = Data.getWhoMakes(child.getSubject(T2, H2));
						T1 = whoMakes.get(r.nextInt(whoMakes.size()));
					
				}
				
				String templesson = child.getClassAndSubject(T1, H1);
				child.schedule[T1][H1] = child.schedule[T2][H2];
				child.schedule[T2][H2] = templesson;
				
			
			}
			restrictions[s]=new Restrictions(child);
			if(!restrictions[s].getChild().equals(this)) {
				a[s]= new Thread(restrictions[s]);
				a[s].start();
			}else {
				restrictions[s].getChild().setErrorCounter(this.getErrorCounter());
				a[s]= new Thread(restrictions[s]);

			}
		}
		
		for(int i=0;i<size;i++) 
		{
			try {
				a[i].join();
				children.add(restrictions[i].getChild());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return children;
	}
	
	
	@Override
	public int compareTo(State x) {
		return (this.getErrorCounter()) - (x.getErrorCounter());
	}

	@Override
	public boolean equals(Object obj) {

		for (int i = 0; i < this.getWidthOfSchedule(); i++) {
			for (int j = 0; j < this.getLengthOfSchedule(); j++) {
				if (!this.schedule[i][j].equals(((State) obj).getClassAndSubject(i, j))) {
					return false;
				}
			}
		}
		return true;
	}

	public int Rule1and3() {
		int errorCounter = 0;
		int statehpd = 0;
		boolean flag = false;
		boolean flag1 = false;
		int counter;
		int result;
		for (int c = 0; c < Data.getClassLenght(); c++) {
			flag = false;
			int mhpd = Data.getInitTotalHours(c) / DAYS;
			if (Data.getInitTotalHours(c) % DAYS != 0) {
				flag1 = true;
			}
			for (int d = 0; d < DAYS; d++) {
				counter = 0;
				flag = false;
				statehpd = 0;
				for (int h = 0; h < HOURS; h++) {
					for (int t = 0; t < Data.getNumberOfTeachers(); t++) {
						if (this.getClass(t, HOURS * d + h).equals(Data.getXClass(c))) {
							counter++;
						}
						if (this.getClass(t, (h + HOURS * d)).equals(Data.getXClass(c))) {
							statehpd += 1;
						}
					}

				}
				if (flag1) {
					if (statehpd < mhpd - 1 || statehpd > (mhpd + 1)) {
						errorCounter += 1;
					}

				} else {
					if (statehpd != mhpd) {
						errorCounter += 1;
					}
				}
				result = HOURS - counter;
				for (int x = (HOURS * (d + 1) - 1); x > ((HOURS * (d + 1) - 1) - result); x--) {
					for (int t = 0; t < Data.getNumberOfTeachers(); t++) {
						if (this.getClass(t, x).equals(Data.getXClass(c))) {
							errorCounter ++;
						}
					}
				}
			}
		}
		return errorCounter;
	}

	public int Rule2() {
		int errorCounter = 0;
		for (int j = 0; j < this.getWidthOfSchedule(); j++) {
			for (int i = 0; i < DAYS; i++) {
				int ch = 0;
				for (int z = 0; z < HOURS; z++) {
					if (!this.getClass(j, i * HOURS + z).equals("--")) {
						ch = ch + 1;
						if (ch == 3) {
							break;
						}
					} else {
						ch = 0;
					}
				}
				if (ch == 3) {
					errorCounter++;
				}
			}

		}
		return errorCounter;
	}

	public int Rule4() {
		int errorCounter = 0;
		int statehpd = 0;
		boolean flag = false;
		int mhpd;
		int totalHours=0;
		for (int l = 0; l < Data.getNumberOfLessons(); l++) {
			if(Data.getLesson(l).getHpw()>1) {
			totalHours=0;
			mhpd = Data.getLesson(l).getHpw() / DAYS;
			if (Data.getLesson(l).getHpw() % DAYS != 0) {
				flag = true;
			}
			loop:
			for (int d = 0; d < DAYS; d++) {
				statehpd = 0;
				for (int h = 0; h < HOURS; h++) {
					for (int t = 0; t < Data.getWhoMakes(l).size(); t++) {
						if (this.getClassAndSubject(Data.getWhoMakes(l).get(t), (h + HOURS * d)).equals(Data.getLesson(l).getClas() + "," + Data.getLesson(l).getCode())) {
							statehpd++;
							totalHours++;
						}
					}

				}
				if (flag) {
					if (statehpd < (mhpd) || statehpd > mhpd+1) {
						errorCounter += 1;
					}
				} else if (statehpd != mhpd) {
					errorCounter += 1;
				}
				if(totalHours==Data.getLessonHours(Data.getLesson(l).getCode())) {
					break loop;
				}
			}
		 }
		}
		return errorCounter;
	}

	public int Rule5() {
		int errorCounter = 0;
		// ---- Rule 5 Starts Here !!! ----

		// From every teacher , total hours from his each of his lessons
		List<List<String>> lessonHours = new ArrayList<List<String>>();
		int idx = 0;
		// For every Teacher and Hour on Schedule
		for (int t = 0; t < Data.getNumberOfTeachers(); t++) {

			List<String> subjList = new ArrayList<String>(); // Lessons that this teacher has
			int max_hpw = Data.getTeacher(t).getMhpw();
			int max_hpd = Data.getTeacher(t).getMhpd();

			for (int h = 0; h < 35; h++) {

				if ((h + 1) % 7 == 0 ){max_hpd = Data.getTeacher(t).getMhpd();}

				String tempsubj = this.getSubject(t, h);

				if (!tempsubj.equals("----")) {
					max_hpw--;
					max_hpd--;

					if (max_hpd < -1) {errorCounter++;}


					subjList.add(tempsubj);
				}


			}

			Set<String> mySet = new HashSet<String>(subjList);
			for (String s : mySet) {

				lessonHours.add(new ArrayList<String>());
				lessonHours.get(idx).add(s);
				lessonHours.get(idx).add(Integer.toString(Collections.frequency(subjList, s)));

				idx++;
			}

			if (max_hpw < -1) {

				errorCounter++;
			}

		} // For every lesson
		for (Lesson lesson : Data.lessons) {
			String classroom = lesson.getClas();
			if (classroom.equals("A1") || classroom.equals("B1") || classroom.equals("C1")) {
				int min = 1000;
				int max = 0; // Min and Max of current lesson
				String lessonCode = lesson.getCode();

				// For every set of lessons/total hours
				for (int i = 0; i < lessonHours.size(); i++) {
					String tmpCode = lessonHours.get(i).get(0);

					// If they are equal
					if (lessonCode.equals(tmpCode)) {

						int hours = Integer.parseInt(lessonHours.get(i).get(1));

						// Update max or min
						if (hours > max) {
							max = hours;
						}
						if (hours < min) {
							min = hours;
						}

					}

				}
				if (max - 4 >= min) {

					errorCounter++;
				}

			}
		}

		// ---- Rule 5 Ends Here !!! ----
		return errorCounter;
	}

	public int StrictRule1() {
		int errorCounter = 0;
		ArrayList<String> samehours = new ArrayList<String>();
		for (int j = 0; j < this.getLengthOfSchedule(); j++) {
			samehours.clear();
			for (int i = 0; i < this.getWidthOfSchedule(); i++) {
				if ((!samehours.contains(this.getClass(i, j))) && (!this.getClass(i, j).equals("--"))) {
					samehours.add(this.getClass(i, j));
				} else if (samehours.contains(this.getClass(i, j))) {
					errorCounter += 2;
				}
			}
		}
		return errorCounter;
	}

	public int StrictRule2() {
		int errorCounter = 0;
		int counter1 = 0;
		ArrayList<Integer> whoMakes;
		for (int l = 0; l < Data.getNumberOfLessons(); l++) {
			counter1 = 0;
			whoMakes=Data.getWhoMakes(l);
			if(whoMakes.size()>1) {
				for (int t = 0; t <whoMakes.size()-1; t++) {
					for (int h = 0; h < Data.MAX_HOURS_OF_WEEK; h++) {
						if (this.getClassAndSubject(whoMakes.get(t), h).equals(Data.getLesson(l).getClas() + "," + Data.getLesson(l).getCode())) {
							counter1++;
						}
					}
					if (counter1 != 0 && counter1 < Data.getLesson(l).getHpw()) {
						errorCounter += 1;
						break;
					}
				}
			}
		}

		return errorCounter;
	}
}
