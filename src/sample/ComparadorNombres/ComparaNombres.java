package sample.ComparadorNombres;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;

public class ComparaNombres {

    public ArrayList<NombreNuevoAntiguo> nombresACorregir(ArrayList<String> SJ,ArrayList<String> PT){
        ArrayList<NombreNuevoAntiguo> nombreNuevoAntiguos = new ArrayList<>();
        for(int i=0;i<PT.size();i++){
            if(!SJ.contains(PT.get(i))){
                nombreNuevoAntiguos.add(new NombreNuevoAntiguo(closer(SJ,PT.get(i)),PT.get(i)));
            }
        }
        return nombreNuevoAntiguos;
    }
    public String closer(ArrayList<String> SJ,String nombre) {
        ArrayList<Integer> distancias = new ArrayList<>();
        for(int i=0;i<SJ.size();i++){
            distancias.add(distance(SJ.get(i),nombre));
        }
        Collections.sort(distancias);
        for(int i=0;i<SJ.size();i++){
            if(distance(SJ.get(i),nombre)==distancias.get(0)){
                return SJ.get(i);
            }
        }
        return "";

    }
    public static int distance(String a, String b)
    {
        a = a.toLowerCase();
        b = b.toLowerCase();
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++)
        {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++)
            {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
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
    public void readExcelFile(File excelFile, ArrayList<String> existencias,int tipo){
        //tipo 1 para puntarenas, 2 para san jose
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
                if(tipo==1 && r!=1){
                    r++;
                }
                System.out.println(r);
                hssfRow = hssfSheet.getRow(r);
                if (hssfRow == null && r > 9){
                    System.out.println("a");
                    break;
                }else{
                    HSSFCell tmp = null;
                    if(hssfRow!=null)
                        tmp = hssfRow.getCell(2);
                    if(hssfRow!=null && tmp!=null && tmp.getCellType() != Cell.CELL_TYPE_BLANK && !(tmp.getCellType() == Cell.CELL_TYPE_STRING && tmp.getStringCellValue().isEmpty())) {
                        cellValue = hssfRow.getCell(c) == null?"":
                                (hssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_STRING)?hssfRow.getCell(c).getStringCellValue():
                                        (hssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_NUMERIC)?"" + hssfRow.getCell(c).getNumericCellValue():
                                                (hssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_BOOLEAN)?"" + hssfRow.getCell(c).getBooleanCellValue():
                                                        (hssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_FORMULA)?"FORMULA":
                                                                (hssfRow.getCell(c).getCellType() == Cell.CELL_TYPE_ERROR)?"ERROR":"";
                        existencias.add(cellValue);
                        System.out.println("entro");
                    }
                }
            }
            if(tipo==1)
                existencias.remove(existencias.get(0));
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
