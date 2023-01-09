import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

        String path = askForTheFile();

        MatrixFileHandler fileHandler = new MatrixFileHandler(path);
        double[][] matrix = fileHandler.getMatrix();

        Gauss g = new Gauss(matrix);
        g.printAandB();
        double[] solution = g.sovle(0.00000000000000001, 2000);

        System.out.println("solution:");
        for (int i = 0; i < solution.length; i++) {
            System.out.println(solution[i]);
        }


    }



    static String askForTheFile() {
        System.out.println("Introduzca el nombre del archivo");
        Scanner s = new Scanner(System.in);
        String dir = s.nextLine();
        s.close();
        return dir;
    }

}
