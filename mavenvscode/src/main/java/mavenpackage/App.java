package mavenpackage;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }
    public static void main(String[] args) {
        ArrayList<StudentInfo> studentList = new ArrayList<>();
        Map<Integer, Integer> testScores = new HashMap<>();
        Map<Integer, Integer> retakeScores = new HashMap<>();
        try{
            File studentInfo = new File("mavenvscode/Student Info.xlsx");
            FileInputStream studentInfoStream = new FileInputStream(studentInfo);

            XSSFWorkbook wb1 = new XSSFWorkbook(studentInfoStream);
            XSSFSheet sheet1 = wb1.getSheetAt(0);
            Iterator<Row> itr1 = sheet1.iterator();
            
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
            Row tmp = itr2.next();
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
            Row tmp2 = itr3.next();
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
        for (Map.Entry<Integer, Integer> entry : testScores.entrySet()){
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue());
        }
        for (Map.Entry<Integer, Integer> entry : testScores.entrySet()){
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue());
        }
    }
}
