package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/*
 * This class ensures that vector is sorted by name
 */
class indexCmp implements Comparator<Person> {

    @Override
    public int compare(Person o1, Person o2) {
        return o1.name.compareTo(o2.name);
    }
}

/*
 * This method convert person(name) into object
 * to realize OOP
 */
class Person {

    String name;
    public static final int num = 7;
    public int score[] = new int[num];
    public int final_score;
    public char final_grade;
    
    /*
     * Constructor initializes data members in score[]
     * the replace method ensures that parseInt can be called
     */
    public Person(String str) {
        String[] tmp = str.split(",");
        name = tmp[0];
        for (int i = 1; i <= num; i++) {
//            System.out.printf("%d %s\n",i,tmp[i]);
            tmp[i] = tmp[i].replace(" ","");

            score[i-1] = Integer.parseInt(tmp[i]);
        }
    }

    /*
     * Letter grade is calculated on person
     */
    public void calcLetterGrade() {
        final_score = (int) (0.1 * (score[0] + score[1] + score[2] + score[3])
            + 0.2 * score[4]
            + 0.15 * score[5] + 0.25 * score[6]);
        if (final_score >= 90) {
            final_grade = 'A';
        } else if (final_score < 90 && final_score >= 80) {
            final_grade = 'B';
        } else if (final_score < 80 && final_score >= 70) {
            final_grade = 'C';
        } else if (final_score < 70 && final_score >= 60) {
            final_grade = 'D';
        } else {
            final_grade = 'F';
        }
    }
}

class LetterGrader {

    public static final int num = 7;
    int classMin[] = new int[num];
    int classMax[] = new int[num];
    float classAverage[] = new float[num];

    Vector<Person> p = new Vector<Person>();
    String[] names = {"Q1", "Q2", "Q3", "Q4", "Mid1", "Mid2", "Final"};
    int lines;

    /*
     * This method reads lines from input
     * and instantiate by lines
     */
    public void readScore(String input) throws IOException {
        File file = new File(input);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line = null;
        while ((line = br.readLine()) != null) {
            p.addElement(new Person(line));
        }
        br.close();
    }

    /*
     * This method calculates average, min and max based on the number of person (size of vector)
     */
    public void calcLetterGrade() {
        int num_person = p.size(); // number of person
        if (num_person == 0) {
            return;
        }

        for (int i = 0; i < num; i++) {
            classMax[i] = p.get(0).score[i];
            classMin[i] = p.get(0).score[i];
            classAverage[i] = p.get(0).score[i];
        }

        for (int i = 1; i < num_person; i++) {
            for (int j = 0; j < num; j++) {
                classMax[j] = Math.max(p.get(i).score[j], classMax[j]);
                classMin[j] = Math.min(p.get(i).score[j], classMin[j]);
                classAverage[j] += p.get(i).score[j];
            }
        }

        for (int i = 0; i < num; i++) {
            classAverage[i] /= num_person;
        }

        for (int i = 0; i < num_person; i++) {
            p.get(i).calcLetterGrade();
        }

        Comparator<Person> cmp = new indexCmp();
        Collections.sort(p, cmp);
    }

    /*
     * This method prints grades into the output file.
     */
    public void printGrade(String output) throws IOException {

        File file = new File(output);
        FileOutputStream os = new FileOutputStream(file);
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
        String line = null;
        int num_person = p.size();
        for (int i = 0; i < num_person; i++) {
            String name = p.get(i).name + ":";
            line = String.format("%-30s%c\n", name, p.get(i).final_grade);
            br.write(line);
        }
        br.close();
    }

    /*
     * This method prints class averages on the screen
     */
    public void displayAverages() {
        System.out.println("Here is the class averages:");
        System.out.printf("\t\t\t");
        for (int i = 0; i < num; i++) {
            System.out.printf("%15s", names[i]);
        }
        System.out.printf("\n");

        System.out.printf("%-5s", "Average:");
        for (int i = 0; i < num; i++) {
            System.out.printf("%15.2f", classAverage[i]);
        }
        System.out.printf("\n");

        System.out.printf("%-5s", "Minimum:");
        for (int i = 0; i < num; i++) {
            System.out.printf("%15d", classMin[i]);
        }
        System.out.printf("\n");

        System.out.printf("%-5s", "Maximum:");
        for (int i = 0; i < num; i++) {
            System.out.printf("%15d", classMax[i]);
        }
        System.out.printf("\n");
    }


}

/*
 * @param: args
 * This driver class reads input and calls previous functions 
 */
public class TestLetterGrader {

    public static void main(String[] args) throws IOException {
        // create a bufferedReader
        BufferedReader readInput = new BufferedReader(new InputStreamReader(System.in));
        String input;
        String output;
        input = args[0];
        output = args[1];

        if (output.isEmpty() || input.isEmpty()) {
            System.out.println("Usage: Java DiskIO inputFile outputFile");
            readInput.readLine();
        } else {
            LetterGrader letterGrader = new LetterGrader();
            letterGrader.readScore(input);
            letterGrader.calcLetterGrade();
            letterGrader.printGrade(output);
            letterGrader.displayAverages();
        }
        // deal with the two circumstances
    }//end of main
}