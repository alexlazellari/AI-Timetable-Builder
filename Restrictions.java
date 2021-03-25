import java.util.*;

public class Restrictions implements Runnable {
	public State child;

	public Restrictions(State child) {
		this.child = child;
	}

	@Override
	public void run() {
		int errorCounter = 0;
		int statehpd = 0;
		boolean flag = false;
		boolean flag1 = false;
		int counter;
		int result;
		for (int c = 0; c < Data.getClassLenght(); c++) {
			flag = false;
			int mhpd = Data.getInitTotalHours(c) / Data.DAYS;
			if (Data.getInitTotalHours(c) % Data.DAYS != 0) {
				flag1 = true;
			}
			for (int d = 0; d < Data.DAYS; d++) {
				counter = 0;
				flag = false;
				statehpd = 0;
				for (int h = 0; h < Data.HOURS; h++) {
					for (int t = 0; t < Data.getNumberOfTeachers(); t++) {
						if (child.getClass(t, Data.HOURS * d + h).equals(Data.getXClass(c))) {
							counter++;
						}
						if (child.getClass(t, (h + Data.HOURS * d)).equals(Data.getXClass(c))) {
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
				result = Data.HOURS - counter;
				for (int x = (Data.HOURS * (d + 1) - 1); x > ((Data.HOURS * (d + 1) - 1) - result); x--) {
					for (int t = 0; t < Data.getNumberOfTeachers(); t++) {
						if (child.getClass(t, x).equals(Data.getXClass(c))) {
							errorCounter+=1;
						}
					}
				}
			}
		}

		for (int j = 0; j < Data.getNumberOfTeachers(); j++) {
			for (int i = 0; i < Data.DAYS; i++) {
				int ch = 0;
				for (int z = 0; z < Data.HOURS; z++) {
					if (!child.getClass(j, i * Data.HOURS + z).equals("--")) {
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

		statehpd = 0;
		flag = false;
		int mhpd;
		int totalHours = 0;
		for (int l = 0; l < Data.getNumberOfLessons(); l++) {
			if (Data.getLesson(l).getHpw() >1) {
				totalHours = 0;
				mhpd = Data.getLesson(l).getHpw() / Data.DAYS;
				if (Data.getLesson(l).getHpw() % Data.DAYS != 0) {
					flag = true;
				}
				loop: for (int d = 0; d < Data.DAYS; d++) {
					statehpd = 0;
					for (int h = 0; h < Data.HOURS; h++) {
						for (int t = 0; t < Data.getWhoMakes(l).size(); t++) {
							if (child.getClassAndSubject(Data.getWhoMakes(l).get(t), (h + Data.HOURS * d))
									.equals(Data.getLesson(l).getClas() + "," + Data.getLesson(l).getCode())) {
								statehpd++;
								totalHours++;
							}
						}

					}
					if (flag) {
						if (statehpd < (mhpd) || statehpd > mhpd + 1) {
							errorCounter += 1;
						}

					} else if (statehpd != mhpd) {
						errorCounter += 1;
					}
					if (totalHours == Data.getLessonHours(Data.getLesson(l).getCode())) {
						break loop;
					}
				}
			}
		}

		ArrayList<String> samehours = new ArrayList<String>();
		for (int j = 0; j < Data.MAX_HOURS_OF_WEEK; j++) {
			samehours.clear();
			for (int i = 0; i < Data.getNumberOfTeachers(); i++) {
				if ((!samehours.contains(child.getClass(i, j))) && (!child.getClass(i, j).equals("--"))) {
					samehours.add(child.getClass(i, j));
				} else if (samehours.contains(child.getClass(i, j))) {
					errorCounter +=2;
				}
			}
		}

		int counter1 = 0;
		ArrayList<Integer> whoMakes;
		for (int l = 0; l < Data.getNumberOfLessons(); l++) {
			counter1 = 0;
			whoMakes = Data.getWhoMakes(l);
			if (whoMakes.size() > 1) {
				for (int t = 0; t < whoMakes.size() - 1; t++) {
					for (int h = 0; h < Data.MAX_HOURS_OF_WEEK; h++) {
						if (child.getClassAndSubject(whoMakes.get(t), h)
								.equals(Data.getLesson(l).getClas() + "," + Data.getLesson(l).getCode())) {
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
		// ---- Rule 5 Starts Here !!! ----

		// From every teacher , total hours from his each of his lessons
		List<List<String>> lessonHours = new ArrayList<List<String>>();
		int idx = 0;
		// For every Teacher and Hour on Schedule
		for (int t = 0; t < Data.getNumberOfTeachers(); t++) {

			List<String> subjList = new ArrayList<String>(); // Lessons that this teacher has
			int max_hpw = Data.getTeacher(t).getMhpw();
			for (int h = 0; h < 35; h++) {
				String tempsubj = child.getSubject(t, h);

				if (!tempsubj.equals("----")) {
					max_hpw--;
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
		child.setErrorCounter(errorCounter);

	}
	public State getChild() {
		return child;
	}

}
