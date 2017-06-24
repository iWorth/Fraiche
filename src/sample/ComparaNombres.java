package sample;

import java.io.*;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;

public class ComparaNombres {

    public void writeExcelFile(File excelNewFile, ArrayList<String> interseccion){
        OutputStream excelNewOutputStream = null;
        try{
            excelNewOutputStream = new FileOutputStream(excelNewFile);
            HSSFWorkbook hssfWorkbookNew = new HSSFWorkbook();
            HSSFSheet hssfSheetNew = hssfWorkbookNew.createSheet("Corregir Nombre");
            HSSFRow hssfRowNew;
            HSSFCell cellNew;
            for (int r = 0; r < interseccion.size();r++){
                hssfRowNew = hssfSheetNew.createRow(r);
                cellNew = hssfRowNew.createCell(0);
                cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNew.setCellValue(interseccion.get(r));
            }
            hssfWorkbookNew.write(excelNewOutputStream);
            excelNewOutputStream.close();
        }
        catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void readExcelFile(File excelFile, ArrayList<String> existencias){
        InputStream excelStream = null;
        try {
            excelStream = new FileInputStream(excelFile);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(excelStream);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
            HSSFRow hssfRow;
            int c = 0;
            int rows = hssfSheet.getLastRowNum();
            String cellValue;
            for (int r = 1; r < rows;r++) {
                hssfRow = hssfSheet.getRow(r);
                if (hssfRow == null){
                    break;
                }else{
                    HSSFCell tmp = hssfRow.getCell(1);
                    if(hssfRow!=null && tmp.getCellType() != Cell.CELL_TYPE_BLANK && !(tmp.getCellType() == Cell.CELL_TYPE_STRING && tmp.getStringCellValue().isEmpty())) {
                        cellValue = hssfRow.getCell(c) == null?"":
                                (hssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_STRING)?hssfRow.getCell(c).getStringCellValue():
                                        (hssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_NUMERIC)?"" + hssfRow.getCell(c).getNumericCellValue():
                                                (hssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_BOOLEAN)?"" + hssfRow.getCell(c).getBooleanCellValue():
                                                        (hssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_FORMULA)?"FORMULA":
                                                                (hssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_ERROR)?"ERROR":"";
                        existencias.add(cellValue);
                    }
                }
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                excelStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
