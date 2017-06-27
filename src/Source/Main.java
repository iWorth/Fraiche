package Source;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ComparadorNombres/ComparaNombresFXML.fxml"));
        primaryStage.setTitle("Ssssssss");
        primaryStage.setScene(new Scene(root, 990, 428));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        /*ArrayList<String> SanJoseExistencias = new ArrayList<>();
        ArrayList<String> PuntarenasExistencias = new ArrayList<>();
        ComparaNombres javaPoiUtils = new ComparaNombres();
        File excelFile = new File("/Users/iWorth/Desktop/Prueba/LISTA DE ARTICULOS PUNTARENAS.xls");
        File excelFile2 = new File("/Users/iWorth/Desktop/Prueba/LISTA DE ARTICULOS SAN JOSE.xls");
        File newExcelFile = new File("/Users/iWorth/Desktop/Prueba/nuevooo2.xls");
        try {
            newExcelFile.createNewFile();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        javaPoiUtils.readExcelFile(excelFile,PuntarenasExistencias,1);
        javaPoiUtils.readExcelFile(excelFile2,SanJoseExistencias,2);
        javaPoiUtils.writeExcelFile(newExcelFile,javaPoiUtils.nombresACorregir(SanJoseExistencias,PuntarenasExistencias));*/
    }

}
