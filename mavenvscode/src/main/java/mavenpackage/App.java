package mavenpackage;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }
    public static void main(String[] args) {
        ArrayList<StudentInfo> studentList = new ArrayList<>();

        try{
            File studentInfo = new File("mavenvscode/Student Info.xlsx");
            FileInputStream stream = new FileInputStream(studentInfo);

            XSSFWorkbook wb = new XSSFWorkbook(stream);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext()){
                StudentInfo student = new StudentInfo();
                Row row = itr.next();
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
        }
        catch(Exception e){
                e.printStackTrace();
        }
        for(int i =0; i < studentList.size(); i++){
            System.out.println(studentList.get(i).studentID + " | " + studentList.get(i).major + " | " + studentList.get(i).gender);
        }
        
    }
}
