package sample;


import sample.ComparadorNombres.ComparaNombres;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> SanJoseExistencias = new ArrayList<>();
        ComparaNombres javaPoiUtils = new ComparaNombres();
        File excelFile = new File("/Users/iWorth/Desktop/Prueba/LISTA DE ARTICULOS PUNTARENAS.xls");
        File newExcelFile = new File("/Users/iWorth/Desktop/Prueba/nuevooo2.xls");
        try {
            newExcelFile.createNewFile();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        javaPoiUtils.readExcelFile(excelFile,SanJoseExistencias,1);
        System.out.println(javaPoiUtils.closer(SanJoseExistencias,"vainila"));
        ///javaPoiUtils.writeExcelFile(newExcelFile,SanJoseExistencias);
    }
}
