import java.util.Arrays;

public class Gauss {

    private double[][] A;
    private double[] b;
    private double[] initialAprox;

    public Gauss(double[][] A, double[] b) {
        if (A == null || b == null || A.length != b.length)
            throw new IllegalArgumentException();

        this.A = A;
        this.b = b;
    }

    public Gauss(double[][] A, double[] b, double[] initialprox) {
        if (A == null || b == null || initialAprox == null || A.length != b.length || initialprox.length != b.length)
            throw new IllegalArgumentException();

        this.A = A;
        this.b = b;
    }

    public Gauss(double[][] ExtenedMatrix){
        A = new double[ExtenedMatrix.length][ExtenedMatrix.length];
        b = new double[ExtenedMatrix.length];    
        
        for (int i = 0; i < ExtenedMatrix.length; i++) {
            for (int j = 0; j < ExtenedMatrix[0].length; j++) {
                if(j < ExtenedMatrix[0].length-1)
                    A[i][j] = ExtenedMatrix[i][j];
                else
                    b[i] = ExtenedMatrix[i][j];

            }
        }

        
    
    }

    public void printAandB(){
        System.out.println("A:");
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                System.out.print("'"+A[i][j]+"' ");
            }
            System.out.println();
        }

        System.out.println("\n");

        System.out.println("b:");
        for (int i = 0; i < b.length; i++) {
            System.out.print("'"+b[i]+"' ");
        }
        System.out.println("\n");
    }

    public boolean isDiagonalDominant() {

        boolean isDiagonalDominant = true;
        double diagonal;
        double tmpSum;

        for (int i = 0; i < A.length; i++) {

            // diagonal is the diagonal value of this iteration
            diagonal = Math.abs(A[i][i]);

            // this variable will hold the sum of all the elements in the row but the
            // diagonal
            tmpSum = 0;

            // Right now we are in the "i" row
            // Sum all the elements "j" in the current row
            for (int j = 0; j < A.length; j++)
                // if the actual position is not the diagonal, sum the current element
                if (i != j)
                    tmpSum += Math.abs(A[i][j]);

            // if it's not diagonal dominant
            if (diagonal < tmpSum) {
                isDiagonalDominant = false;
                break;
            }

        }

        return isDiagonalDominant;
    }



    public double[] sovle(double precision, int maxIterations) {
        // Initial approximation
        double[] approximation;

        // If there is no initial approximation then, 0 vector will be
        if (initialAprox == null) {
            approximation = new double[b.length];
            Arrays.fill(approximation, 0);
        }
        else
            approximation = initialAprox;
        
        // The vector that will hold the last approximation
        double[] lApprox = new double[approximation.length];

        // If it's not diagonal Dominant it can still converge
        if (!isDiagonalDominant())
            System.err.println("Esta matriz no es diagonalmente dominante");


        //Burden method
        for (int k = 0; k < maxIterations; k++) {
            
            // Copy last approximation to hold it
            for (int i = 0; i < approximation.length; i++) {
                lApprox[i] = approximation[i];
            }

            // Sumator
            for (int i = 0; i < A.length; i++) {

                // In every new iteration it is resset, because every iteration of i-loop returns the value of
                // one element of the approximation vector
                double x0 = 0;

                //  x0 = Σ aij*xj , V i!=j;
                // As burden said
                for (int j = 0; j < A.length; j++)
                    if(i!=j)
                        x0 += A[i][j] * approximation[j];
                
                // xi = 1/aii [ bi - Σ aij*xj ]
                // Again, as burden said
                approximation[i] = (b[i] - x0)/A[i][i];
            }

// PRINTING STUFFFFFFF  ///////////////////////////////////////////
            System.out.print("iteration: #"+k+": ");
            for (int i = 0; i < lApprox.length; i++) {
                System.out.print(approximation[i] + ", ");
            }
            System.out.println();
/////////////////////////////////////////////////////////////////


            if(infinityNorm(lApprox, approximation)<precision)
                break;

            if (k == maxIterations-1)
                System.out.println("Max it reached");
            
        }
        
        return approximation;
    }

    /**
     * @implNote 
     * @param lApprox Last approximation vector
     * @param cApprox Lurrent approximation vector
     * @return
     */
    public double infinityNorm(double[] lApprox,double[] cApprox){
        
        double bdiff = Math.abs(lApprox[0]) - Math.abs(cApprox[0]);
        bdiff = Math.abs(bdiff);


        for (int i = 0; i < cApprox.length; i++) {
            
            double elemDif = Math.abs(lApprox[i]) - Math.abs(cApprox[i]);
            elemDif = Math.abs(elemDif);

            if(bdiff < elemDif)
                bdiff = elemDif;
        }

        return bdiff;
    }
}
