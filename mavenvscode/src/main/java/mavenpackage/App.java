package mavenpackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.*;

import java.util.*;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    public static void main(String[] args) throws IOException {
        ArrayList<StudentInfo> studentList = new ArrayList<>();
        Map<Integer, Integer> testScores = new HashMap<>();
        Map<Integer, Integer> retakeScores = new HashMap<>();
        try{
            File studentInfo = new File("mavenvscode/Student Info.xlsx");
            FileInputStream studentInfoStream = new FileInputStream(studentInfo);

            XSSFWorkbook wb1 = new XSSFWorkbook(studentInfoStream);
            XSSFSheet sheet1 = wb1.getSheetAt(0);
            Iterator<Row> itr1 = sheet1.iterator();
            Row skip = itr1.next(); //skip first row
            while (itr1.hasNext()){
                StudentInfo student = new StudentInfo(); //intitialize student object
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

            File testScoresXLSX = new File("mavenvscode/Test Scores.xlsx");
            FileInputStream testScoresStream = new FileInputStream(testScoresXLSX);

            XSSFWorkbook wb2 = new XSSFWorkbook(testScoresStream);
            XSSFSheet sheet2 = wb2.getSheetAt(0);
            Iterator<Row> itr2 = sheet2.iterator();
            Row tmp = itr2.next(); //skip first row
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
            Row tmp2 = itr3.next(); // skip first row
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
        
        for(int i =0; i < studentList.size(); i++){
            System.out.println(studentList.get(i).studentID + " | " + studentList.get(i).major + " | " + studentList.get(i).gender);
        }
        System.out.println("Test Scores: ");
        for (Map.Entry<Integer, Integer> entry : testScores.entrySet()){
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue());
        }
        System.out.println("Retake Scores: ");
        for (Map.Entry<Integer, Integer> entry : testScores.entrySet()){
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue());
        }
        //using student ids, set test score for each student object
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
        ArrayList<Integer> femaleIDs = new ArrayList<>();
        double sum =0;
        String major = studentList.get(2).major;
        for(int i =0; i < studentList.size(); i++){
            System.out.println("Student ID: " + studentList.get(i).studentID);
            System.out.println("Major: " + studentList.get(i).major);
            System.out.println("Gender: " + studentList.get(i).gender);
            System.out.println("Test Score: " + studentList.get(i).testScore);
            System.out.println("Retake Score: " + studentList.get(i).retakeScore);
            System.out.println("Final Test Score: " + studentList.get(i).getFinalTestScore());
            sum += studentList.get(i).getFinalTestScore();
            if((studentList.get(i).gender == 'F') && (studentList.get(i).major == major)){
                System.out.println(studentList.get(i).studentID);
                femaleIDs.add(studentList.get(i).studentID);
            }
            System.out.println(" ");
        }
        Collections.sort(femaleIDs);
        System.out.println("");
        System.out.println("Sorted Female IDs: ");
        int femaleArr[] = new int [femaleIDs.size()];
        for(int i =0; i < femaleIDs.size(); i++){
            femaleArr[i] = femaleIDs.get(i);
        }
        int classAverage = (int) sum / studentList.size();
        JSONObject obj = new JSONObject();
        obj.put("id", "nbullock1@luc.edu");
        obj.put("name", "Nathan Bullock");
        obj.put("average", classAverage);
        obj.put("studentIds", femaleArr);
        URL url = new URL ("http://54.90.99.192:5000/challenge");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        System.out.println("Class Average: " + classAverage);
    }
}
