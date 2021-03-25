import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileWriter;
import java.util.Formatter;
import java.util.ArrayList;

public class fileRW {
	
	private static String[] Days = { "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" };

	//Method that read the Lessons file.
	public ArrayList<Lesson> readLessons(String path) {
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		Scanner input;

		try {
			input = new Scanner(Paths.get(path));
			while (input.hasNext()) {
				String tline = input.nextLine();
				String[] line = tline.trim().split(" ", 4);
				lessons.add(new Lesson(line[1], line[0], line[2], Integer.parseInt(line[3])));

			}
			input.close();
		} catch (FileNotFoundException filenotfoundexception) {
			System.out.println("File not found.Terminating.");
		} catch (IOException ioException) {
			System.out.println("Error opening file.Terminating");
		} catch (NoSuchElementException nosuchelementexception) {
			System.err.println("File improperly format.Terminating.");
		}
		return lessons;
	}

	//Method that reads the Teachers file.
	public ArrayList<Teacher> readTeachers(String path) {

		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		Scanner input;

		try {
			input = new Scanner(Paths.get(path));
			while (input.hasNext()) {
				String tline = input.nextLine();
				String[] line = tline.trim().split(" ", 5);
				
				StringTokenizer codes = new StringTokenizer(line[2], ",");
				ArrayList<String> Lessonscode = new ArrayList<String>();
				while(codes.hasMoreTokens()) {
					Lessonscode.add(codes.nextToken());
				}
				
				teachers.add(
						new Teacher(line[0], line[1], Lessonscode, Integer.parseInt(line[3]), Integer.parseInt(line[4])));

			}
			input.close();
		} catch (FileNotFoundException filenotfoundexception) {
			System.out.println("File not found.Terminating.");
		} catch (IOException ioException) {
			System.out.println("Error opening file.Terminating");
		} catch (NoSuchElementException nosuchelementexception) {
			System.err.println("File improperly format.Terminating.");
		}
		return teachers;
	}
	
	//Method that print out the schedule.
	public void writer(String path, State state) {
		try {
			Formatter output;
			FileWriter f = new FileWriter(path);
			//Get max name length
			int max=-1;
			for(int t=0;t<Data.getNumberOfTeachers();t++) {
				if(Data.getTeacher(t).getNameT().length()>max)
					max=Data.getTeacher(t).getNameT().length();
			}
			int classorsubject =(state.getClass(0,0).length()>=state.getSubject(0,0).length())?state.getClass(0,0).length():state.getSubject(0, 0).length();
			//Print Days.
			output = new Formatter(f);
			output.format("%-"+max+"s||", "DAYS");
			for (int i = 0; i < Days.length; i++) {
				output.format("%-"+(7*classorsubject+6)+"s||", Days[i]);

			}
			//Print Hours.
			output.format("\n");
			output.format("%-"+max+"s||", "TEACHERS");
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 7; j++) {
					output.format("%-"+classorsubject+"d|", (j + 1));
				}
				output.format("|");
			}
			output.format("\n");
			
			//Print Lessons,Classes and Teachers.
			for (int i = 0; i < state.getWidthOfSchedule(); i++) {
				output.format("%-"+max+"s||", Data.getTeacher(i).getNameT());
				for (int j = 1; j <= state.getLengthOfSchedule(); j++) {
					output.format("%"+classorsubject+"s|", state.getClass(i, j - 1));
					
					if (j % 7 == 0) {
						output.format("|");
					}
				}
				output.format("\n");
				output.format("%-"+max+"s||", "");
				for (int j = 1; j <= state.getLengthOfSchedule(); j++) {
					output.format("%-"+classorsubject+"s|", state.getSubject(i, j - 1));
					
					if (j % 7 == 0) {
						output.format("|");
					}
				}
				output.format("\n");
			}
			
			//Close the file.
			output.close();

		} catch (IOException ioException) {
			System.err.println("Error opening file.Terminating");
		} catch (SecurityException securityexception) {
			System.err.println("Write permission denied. Terminating.");
			System.exit(1);
		}
	}
}
