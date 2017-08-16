package Source.ComparadorNombres;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;

public class ComparaNombres implements Initializable {

    @FXML
    Label lblTitulo;

    @FXML
    Label lblArchivo1Path;

    @FXML
    Label lblArchivo2Path;

    @FXML
    Button btoArchivo1;

    @FXML
    Button btoArchivo2;

    @FXML
    Button btoCompara_nombres ;

    @FXML
    TextField txtNombreArchivo;

    @FXML
    Pane pane;

    File file1 = null;
    File file2 = null;
    public void initialize(URL fxmlLocations, ResourceBundle resources){

        btoCompara_nombres.setOnAction(event -> {
            if(file1 == null || file2 == null){
                if(file1==null)
                    lblArchivo1Path.setText("Debe seleccionar un archivo");
                if(file2==null)
                    lblArchivo2Path.setText("Debe seleccionar un archivo");
                return;
            }
            ArrayList<NombreDescripcionEstatus> file2existencias = new ArrayList<>();
            ArrayList<String> file1existencias = new ArrayList<>();

            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Seleccione donde guardar el archivo.");
            String nuevoArchivo = dc.showDialog(null).getAbsolutePath();
            nuevoArchivo+='/'+txtNombreArchivo.getText()+".xls";
            File archivoResult = new File(nuevoArchivo);

            try{
                archivoResult.createNewFile();
            }catch (IOException ioe){
                ioe.printStackTrace();
            }

            ComparaNombres cn = new ComparaNombres();
            cn.readExcelFileFranc(file1,file1existencias);//puntarenas
            cn.readExcelFileSanJose(file2,file2existencias);//san jose
            lblTitulo.setText("Cargando...");
            cn.writeExcelFile(archivoResult,cn.nombresACorregir(file2existencias,file1existencias));//san jose y puntarenas
            lblTitulo.setText("Comparar nombres");
            limpiarVentana();
        });
        btoArchivo1.setOnAction(event -> {
            btoArchivo1.setDisable(true);
            file1 = getFile();
            if(file1!=null){
            String pathF1 = file1.getAbsolutePath();
            lblArchivo1Path.setText(pathF1);
            }
            btoArchivo1.setDisable(false);
        });
        btoArchivo2.setOnAction(event -> {
            btoArchivo2.setDisable(true);
            file2 = getFile();
            if(file2!=null) {
                String pathF2 = file2.getAbsolutePath();
                lblArchivo2Path.setText(pathF2);
            }
            btoArchivo2.setDisable(false);
        });
    }

    public File getFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione el archivo.");
        File file = fileChooser.showOpenDialog(null);
        return file;
    }
    public void limpiarVentana(){
        file1 = null;
        file2 = null;
        txtNombreArchivo.setText("Comparacion");
        lblArchivo1Path.setText("");
        lblArchivo2Path.setText("");
    }
    public ArrayList<NombreDescripcionEstatus> nombresACorregir(ArrayList<NombreDescripcionEstatus> SJ, ArrayList<String> PT){
        ArrayList<NombreDescripcionEstatus> nombreNuevoAntiguos = new ArrayList<>();
        for(int i=0;i<SJ.size();i++){
            if(!PT.contains(SJ.get(i).nombre)){
                nombreNuevoAntiguos.add(SJ.get(i));
            }
        }
        return nombreNuevoAntiguos;
    }
    public void writeExcelFile(File excelNewFile, ArrayList<NombreDescripcionEstatus> interseccion){
        OutputStream excelNewOutputStream = null;
        try{
            excelNewOutputStream = new FileOutputStream(excelNewFile);
            HSSFWorkbook hssfWorkbookNew = new HSSFWorkbook();
            HSSFSheet hssfSheetNew = hssfWorkbookNew.createSheet("Corregir Nombre");
            HSSFRow hssfRowNew;
            HSSFCell cellNewDesc;
            HSSFCell cellNewName;
            HSSFCell cellNewStat;
            for (int r = 0; r <= interseccion.size();r++){
                hssfRowNew = hssfSheetNew.createRow(r);
                cellNewName = hssfRowNew.createCell(0);
                cellNewDesc = hssfRowNew.createCell(1);
                cellNewStat = hssfRowNew.createCell(2);
                cellNewName.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNewDesc.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNewStat.setCellType(HSSFCell.CELL_TYPE_STRING);
                if(r==0){
                    cellNewName.setCellValue("Nombre");
                    cellNewDesc.setCellValue("Descripción");
                    cellNewStat.setCellValue("Estatus");
                }
                else{
                    cellNewName.setCellValue(interseccion.get(r-1).nombre);
                    cellNewDesc.setCellValue(interseccion.get(r-1).descripcion);
                    cellNewStat.setCellValue(interseccion.get(r-1).estatus);
                }
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
    public void readExcelFileFranc(File excelFile, ArrayList<String> existencias){
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
                if(r!=1) {
                    r++;
                }
                hssfRow = hssfSheet.getRow(r);
                if (hssfRow == null && r > 9){
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
                    }
                }
            }
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
    public void readExcelFileSanJose(File excelFile,ArrayList<NombreDescripcionEstatus> existencias){
        InputStream excelStream = null;
        try {
            excelStream = new FileInputStream(excelFile);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(excelStream);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
            HSSFRow hssfRow;
            int name = 0;
            int desc = 1;
            int stat = 3;
            int rows = hssfSheet.getLastRowNum();
            NombreDescripcionEstatus cellValue;
            String nombre;
            String descrip;
            String estatus;
            for (int r = 1; r < rows;r++) {
                hssfRow = hssfSheet.getRow(r);
                if (hssfRow == null && r > 9){
                    break;
                }else{
                    HSSFCell tmp = null;
                    if(hssfRow!=null)
                        tmp = hssfRow.getCell(2);
                    if(hssfRow!=null && tmp!=null && tmp.getCellType() != Cell.CELL_TYPE_BLANK && !(tmp.getCellType() == Cell.CELL_TYPE_STRING && tmp.getStringCellValue().isEmpty())) {
                        nombre = hssfRow.getCell(name) == null?"":
                                (hssfRow.getCell(name).getCellType() == Cell.CELL_TYPE_STRING)?hssfRow.getCell(name).getStringCellValue():
                                        (hssfRow.getCell(name).getCellType() == Cell.CELL_TYPE_NUMERIC)?"" + hssfRow.getCell(name).getNumericCellValue():
                                                (hssfRow.getCell(name).getCellType() == Cell.CELL_TYPE_BOOLEAN)?"" + hssfRow.getCell(name).getBooleanCellValue():
                                                        (hssfRow.getCell(name).getCellType() == Cell.CELL_TYPE_FORMULA)?"FORMULA":
                                                                (hssfRow.getCell(name).getCellType() == Cell.CELL_TYPE_ERROR)?"ERROR":"";
                        descrip = hssfRow.getCell(desc) == null?"":
                                (hssfRow.getCell(desc).getCellType() == Cell.CELL_TYPE_STRING)?hssfRow.getCell(desc).getStringCellValue():
                                        (hssfRow.getCell(desc).getCellType() == Cell.CELL_TYPE_NUMERIC)?"" + hssfRow.getCell(desc).getNumericCellValue():
                                                (hssfRow.getCell(desc).getCellType() == Cell.CELL_TYPE_BOOLEAN)?"" + hssfRow.getCell(desc).getBooleanCellValue():
                                                        (hssfRow.getCell(desc).getCellType() == Cell.CELL_TYPE_FORMULA)?"FORMULA":
                                                                (hssfRow.getCell(desc).getCellType() == Cell.CELL_TYPE_ERROR)?"ERROR":"";
                        estatus = hssfRow.getCell(stat) == null?"":
                                (hssfRow.getCell(stat).getCellType() == Cell.CELL_TYPE_STRING)?hssfRow.getCell(stat).getStringCellValue():
                                        (hssfRow.getCell(stat).getCellType() == Cell.CELL_TYPE_NUMERIC)?"" + hssfRow.getCell(stat).getNumericCellValue():
                                                (hssfRow.getCell(stat).getCellType() == Cell.CELL_TYPE_BOOLEAN)?"" + hssfRow.getCell(stat).getBooleanCellValue():
                                                        (hssfRow.getCell(stat).getCellType() == Cell.CELL_TYPE_FORMULA)?"FORMULA":
                                                                (hssfRow.getCell(stat).getCellType() == Cell.CELL_TYPE_ERROR)?"ERROR":"";
                        cellValue = new NombreDescripcionEstatus(nombre,descrip,estatus);
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
