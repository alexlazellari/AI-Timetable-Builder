import java.util.ArrayList;
import java.util.Random;

public class Data {

	// Fields
	private static String[] classes = { "A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3" };
	public static Lesson[] lessons;
	private static Teacher[] teachers;
	private static int[] totalHours;
	private static ArrayList<ArrayList<Integer>> whoMakes;
	public static final  int  MAX_HOURS_OF_WEEK=35;
	public static final int HOURS = 7;
	public static final int DAYS = 5;

	public static void passData(ArrayList<Teacher> teachers, ArrayList<Lesson> lessons) {
		Data.teachers = new Teacher[teachers.size()];
		for (int i = 0; i < teachers.size(); i++) {
			Data.teachers[i] = teachers.get(i);
		}
		Data.lessons = new Lesson[lessons.size()];
		for (int i = 0; i < lessons.size(); i++) {
			Data.lessons[i] = lessons.get(i);
		}

	}

	public static void whoMakes() {
		whoMakes = new ArrayList<ArrayList<Integer>>();

		for (int l = 0; l < Data.getNumberOfLessons(); l++) {
			whoMakes.add(new ArrayList<Integer>());
			for (int t = 0; t < Data.getNumberOfTeachers(); t++) {
				for (int s = 0; s < Data.getTeacher(t).getCodeL().size(); s++) {
					if (Data.getLesson(l).getCode().equals(Data.getTeacher(t).getCodeL().get(s))) {
						whoMakes.get(l).add(t);
						break;
					}
				}
			}
		}
	}

	public static ArrayList<Integer> getWhoMakes(int l) {
		return whoMakes.get(l);
	}

	public static void initializeTotalHours() {
		totalHours = new int[classes.length];
		for (int i = 0; i < classes.length; i++) {
			for (Lesson lesson : lessons) {
				if (lesson.getClas().equals(classes[i])) {
					totalHours[i] += lesson.getHpw();
				}
			}
		}
	}

	public static ArrayList<Integer> getWhoMakes(String code) {
		int index = 0;
		for (int i = 0; i < Data.lessons.length; i++) {
			if (lessons[i].getCode().equals(code)) {
				index = i;
				break;
			}
		}
		return whoMakes.get(index);
	}

	public static int getNumberOfTeachers() {
		return Data.teachers.length;
	}

	public static int getNumberOfLessons() {
		return Data.lessons.length;
	}

	public static Teacher getTeacher(int i) {
		return Data.teachers[i];
	}

	public static Lesson getLesson(int i) {
		return Data.lessons[i];
	}

	public static int getInitTotalHours(int i) {
		return totalHours[i];
	}

	public static String getXClass(int X) {
		return Data.classes[X];
	}

	public static int getClassLenght() {
		return Data.classes.length;
	}
	public static int getLessonHours(String code) {
		for(int l =0;l<Data.getNumberOfLessons();l++) {
			if(Data.getLesson(l).getCode().equals(code)) {
				return Data.getLesson(l).getHpw();
			}
		}
		return 0;
	}

}
