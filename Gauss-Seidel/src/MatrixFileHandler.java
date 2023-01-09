import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MatrixFileHandler {
    private String path;

    public MatrixFileHandler(String path) {
        this.path = path;
    }


    /**
     * @return int[][]
     * @throws IOException
     * @throws Exception
     */
    public double[][] getMatrix() throws IOException {

        // The lines of the file
        Object[] lines = getLines();
        // Individual Line as array
        String[] line;
        // The final Matrix
        ArrayList<ArrayList<Object>> matrix = new ArrayList<>();




        for (int i = 0; i < lines.length; i++) {

            // Create an array with every non-blank value of the line
            line = String.valueOf(lines[i]).split(" ");
            ArrayList<Object> row = new ArrayList<>();

            for (int j = 0; j < line.length; j++) {

                // If there is a value in the line array element
                if (!line[j].isBlank() && !line[j].isEmpty()) {

                    try {

                        // Add it as an integer to the row of the matrix
                        row.add(Integer.valueOf(line[j]));

                    } catch (Exception e) {
                        System.out.println("El archivo está mal formado");
                        System.exit(1);
                    }
                }
            }

            if (!row.isEmpty())
                matrix.add(row);
        }

        double[][] finalMatrix = new double[matrix.size()][matrix.get(0).size()];

        for (int i = 0; i < matrix.size(); i++)
            for (int j = 0; j < matrix.get(i).size(); j++)
                finalMatrix[i][j] = Double.parseDouble(String.valueOf(matrix.get(i).get(j)));

        return finalMatrix;
    }



    
    /**
     * This method reads the file and adds every line to an array 
     * @return 
     * @throws IOException
     */
    public Object[] getLines() throws IOException {

        Object[] lines;

        // stuff to read the file
        File f = new File(path);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        // File lines to array
        lines = br.lines().toArray();
        br.close();

        return lines;
    }

}