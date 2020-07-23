/**
 * Program by Nathan Bullock
 * Email: nbullock1@luc.edu
 * Phone: 317-224-9823
 */
package mavenpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Student> studentList = new ArrayList<>();
        ArrayList<Integer> femaleIDs = new ArrayList<>();
        Map<Integer, Integer> testScores = new HashMap<>();
        Map<Integer, Integer> retakeScores = new HashMap<>();
        try{
            File studentInfo = new File("mavenvscode/Student Info.xlsx");
            FileInputStream studentInfoStream = new FileInputStream(studentInfo);

            XSSFWorkbook wb1 = new XSSFWorkbook(studentInfoStream);
            XSSFSheet sheet1 = wb1.getSheetAt(0);
            Iterator<Row> itr1 = sheet1.iterator();
            itr1.next(); //skip first row
            while (itr1.hasNext()){
                Student student = new Student(); //intitialize student object
                Row row = itr1.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()){
                        case Cell.CELL_TYPE_STRING:
                            if(cell.getStringCellValue().length() > 1){
                                student.major = cell.getStringCellValue();
                            }
                            else{
                                char c = cell.getStringCellValue().charAt(0);
                                student.gender = c;
                            }
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            student.studentID = (int) cell.getNumericCellValue();
                            break;
                        default:
                    }
                }
                studentList.add(student);
            }
            wb1.close();
            //populate map with test scores from xlsx
            File testScoresXLSX = new File("mavenvscode/Test Scores.xlsx");
            FileInputStream testScoresStream = new FileInputStream(testScoresXLSX);
            XSSFWorkbook wb2 = new XSSFWorkbook(testScoresStream);
            XSSFSheet sheet2 = wb2.getSheetAt(0);
            Iterator<Row> itr2 = sheet2.iterator();
            itr2.next(); //skip first row
            while (itr2.hasNext()){
                Row row = itr2.next();
                ArrayList<Integer> arr = new ArrayList<>();
                for(Cell cell : row){
                    arr.add((int) cell.getNumericCellValue());
                }
                testScores.put(arr.get(0), arr.get(1));
            }
            wb2.close();

            File retakeScoresXLSX = new File("mavenvscode/Test Retake Scores.xlsx");
            FileInputStream retakeScoresStream = new FileInputStream(retakeScoresXLSX);

            XSSFWorkbook wb3 = new XSSFWorkbook(retakeScoresStream);
            XSSFSheet sheet3 = wb3.getSheetAt(0);
            Iterator<Row> itr3 = sheet3.iterator();
            itr3.next(); // skip first row
            while (itr3.hasNext()){
                Row row = itr3.next();
                ArrayList<Integer> arr = new ArrayList<>();
                for(Cell cell : row){
                    arr.add((int) cell.getNumericCellValue());
                }
                retakeScores.put(arr.get(0), arr.get(1));
            }
            wb3.close();
        }
        catch(Exception e){
                e.printStackTrace();
        }
        //using student ids, set test score and retake score for each student object
        for(int i =0; i < studentList.size(); i++){
            int id = studentList.get(i).studentID;
            for(Map.Entry<Integer, Integer> entry : testScores.entrySet()){
                if(entry.getKey() == id){
                    studentList.get(i).testScore = entry.getValue();
                }
            }
            for(Map.Entry<Integer, Integer> entry : retakeScores.entrySet()){
                if(entry.getKey() == id){
                    studentList.get(i).retakeScore = entry.getValue();
                }
            }
        }
        System.out.println(" ");
        double sum =0; //sum of final test scores
        String major = studentList.get(2).major; //To get comparable string value of computer science major 
        //print out all student objects and final test scores while also adding female computer science majors to array
        for(int i =0; i < studentList.size(); i++){
            System.out.println("Student ID: " + studentList.get(i).studentID);
            System.out.println("Major: " + studentList.get(i).major);
            System.out.println("Gender: " + studentList.get(i).gender);
            System.out.println("Test Score: " + studentList.get(i).testScore);
            System.out.println("Retake Score: " + studentList.get(i).retakeScore);
            System.out.println("Final Test Score: " + studentList.get(i).getFinalTestScore());
            sum += studentList.get(i).getFinalTestScore();
            if((studentList.get(i).gender == 'F') && (studentList.get(i).major == major)){
                femaleIDs.add(studentList.get(i).studentID);
            }
            System.out.println(" ");
        }

        Collections.sort(femaleIDs);
        int femaleArr[] = new int [femaleIDs.size()];
        for(int i =0; i < femaleIDs.size(); i++){
            femaleArr[i] = femaleIDs.get(i);
        }
        System.out.println("");
        System.out.println("Sorted Female IDs: ");
        for(int i =0; i < femaleArr.length; i++){
            System.out.println(femaleArr[i]);
        }
        System.out.println("");
        int classAverage = (int) sum / studentList.size();

        System.out.println("Class Average: " + classAverage);
        System.out.println(" ");

        //create JSONObject with data
        JSONObject obj = new JSONObject();
        obj.put("id", "nbullock1@luc.edu");
        obj.put("name", "Nathan Bullock");
        obj.put("average", classAverage);
        obj.put("studentIds", femaleArr);


        URL url = new URL ("http://54.90.99.192:5000/challenge");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        String jsonString = obj.toString();
        System.out.println("JSON Request String: ");
        System.out.println(jsonString);
        con.connect();
        try(OutputStream os = con.getOutputStream()){
            byte[] input = jsonString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        System.out.println("");
        System.out.println("Server Response: ");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }
}
