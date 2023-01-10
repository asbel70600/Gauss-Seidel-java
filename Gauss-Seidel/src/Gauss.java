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

    public Gauss(double[][] ExtenedMatrix) {
        
        A = new double[ExtenedMatrix.length][ExtenedMatrix.length];
        b = new double[ExtenedMatrix.length];

        for (int i = 0; i < ExtenedMatrix.length; i++)
            for (int j = 0; j < ExtenedMatrix[0].length; j++)
                if (j < ExtenedMatrix[0].length - 1)
                    A[i][j] = ExtenedMatrix[i][j];
                else
                    b[i] = ExtenedMatrix[i][j];


    }

    public void printAandB() {

        System.out.println("A:");

        for (int i = 0; i < A.length; i++) {

            for (int j = 0; j < A.length; j++)
                System.out.print("'" + A[i][j] + "' ");

            System.out.println("\n");
        }

        System.out.println("\nb:");

        for (int i = 0; i < b.length; i++)
            System.out.print("'" + b[i] + "' ");

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

    /**
     * 
     * In every new iteration it is resset, because every iteration of i-loop
     * returns the value of one element of the approximation vector
     * 
     * As burden said
     * x0 +=|Σ aij*x(k)[j], i<j
     * |Σ aij*xj(k-1)[j], i>j
     * 
     * That means:
     * x0 = (Σ aij*x(k)[j], i<j) + (Σ aij*xj(k-1)[j], i>j)
     * 
     * 
     * xi = 1/aii (-x0 + bi)
     * that is:
     * xi = 1/aii [ -(Σ aij*x(k)[j], i<j) - (Σ aij*xj(k-1)[j], i>j) + bi ]
     * 
     * @param precision
     * @param maxIterations
     * @return
     */
    public double[] sovle(double precision, int maxIterations) {

        // Initial approximation
        double[] approximation;

        // If there is no initial approximation then, 0 vector will be
        if (initialAprox == null) {
            approximation = new double[b.length];
            Arrays.fill(approximation, 0);
        } else
            approximation = initialAprox;

        // The vector that will hold the last approximation
        double[] lApprox = new double[approximation.length];

        // If it's not diagonal Dominant it can still converge
        if (!isDiagonalDominant())
            System.err.println("Esta matriz no es diagonalmente dominante");

        // Burden method
        for (int k = 0; k < maxIterations; k++) {

            // Copy last approximation to hold it
            for (int i = 0; i < approximation.length; i++)
                lApprox[i] = approximation[i];


            for (int i = 0; i < A.length; i++) {

                double x0 = 0;

                for (int j = 0; j < A.length; j++)
                    if (i != j)
                        x0 += A[i][j] * approximation[j];

                approximation[i] = (-x0 + b[i]) / A[i][i];
            }

            // PRINTING STUFFFFFFF ///////////////////////////////////////////
            System.out.print("iteration: #" + k + ": ");
            for (int i = 0; i < lApprox.length; i++) {
                System.out.print(approximation[i] + ", ");
            }
            System.out.println();
            /////////////////////////////////////////////////////////////////

            if (relativeError(lApprox, approximation) < precision)
                break;

            if (k == maxIterations - 1)
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
    public double relativeError(double[] lApprox, double[] cApprox) {

        double greatestDiffTop = Math.abs(lApprox[0] - cApprox[0]);
        double greatestDiffBottom = Math.abs(cApprox[0]);

        for (int i = 0; i < cApprox.length; i++) {

            double elemDiff = Math.abs(cApprox[i] - lApprox[i]);

            if (greatestDiffTop < elemDiff)
                greatestDiffTop = elemDiff;
        }

        for (int i = 0; i < cApprox.length; i++)
            if (greatestDiffBottom < Math.abs(cApprox[i]))
                greatestDiffBottom = Math.abs(cApprox[i]);

        return greatestDiffTop / greatestDiffBottom;
    }

    public double infiintyNorm(double[] x) {
        double norm = Math.abs(x[0]);
        double absoluteXi;

        for (int i = 0; i < x.length; i++) {

            absoluteXi = Math.abs(x[i]);

            if (norm < absoluteXi)
                norm = absoluteXi;
        }

        return norm;
    }

    public double infiintyNorm(double[][] Q) {
        double norm = Math.abs(Q[0][0]);
        double absoluteXi;

        for (int i = 0; i < Q.length; i++)
            for (int j = 0; j < Q[0].length; j++) {

                absoluteXi = Math.abs(Q[i][j]);

                if (norm < absoluteXi)
                    norm = absoluteXi;
            }

        return norm;
    }

    public boolean nonZeroDiagonal() {
        for (int i = 0; i < A.length; i++)
            if (A[i][i] == 0)
                return false;

        return true;
    }

}

// public Matriz valoresPropios(double[] valores, int maxIter)throws
// ValoresException{
// final double CERO=1e-8;
// double maximo, tolerancia, sumsq;
// double x, y, z, c, s;
// int contador=0;
// int i, j, k, l;
// Matriz a=(Matriz)clone(); //copia de this
// Matriz p=new Matriz(n);
// Matriz q=new Matriz(n);
// //matriz unidad
// for(i=0; i<n; i++){
// q.x[i][i]=1.0;
// }
// do{
// k=0; l=1;
// maximo=Math.abs(a.x[k][1]);
// for(i=0; i<n-1; i++){
// for(j=i+1; j<n; j++){
// if(Math.abs(a.x[i][j])>maximo){
// k=i; l=j;
// maximo=Math.abs(a.x[i][j]);
// }
// }
// }
// sumsq=0.0;
// for(i=0; i<n; i++){
// sumsq+=a.x[i][i]*a.x[i][i];
// }
// tolerancia=0.0001*Math.sqrt(sumsq)/n;
// if(maximo<tolerancia) break;
// //calcula la matriz ortogonal de p
// //inicialmente es la matriz unidad
// for(i=0; i<n; i++){
// for(j=0; j<n; j++){
// p.x[i][j]=0.0;
// }
// }
// for(i=0; i<n; i++){
// p.x[i][i]=1.0;
// }
// y=a.x[k][k]-a.x[l][l];
// if(Math.abs(y)<CERO){
// c=s=Math.sin(Math.PI/4);
// }else{
// x=2*a.x[k][l];
// z=Math.sqrt(x*x+y*y);
// c=Math.sqrt((z+y)/(2*z));
// s=signo(x/y)*Math.sqrt((z-y)/(2*z));
// }
// p.x[k][k]=c;
// p.x[l][l]=c;
// p.x[k][l]=s;
// p.x[l][k]=-s;
// a=Matriz.producto(p, Matriz.producto(a, Matriz.traspuesta(p)));
// q=Matriz.producto(q, Matriz.traspuesta(p));
// contador++;
// }while(contador<maxIter);

// if(contador==maxIter){
// throw new ValoresExcepcion("No se han podido calcular los valores propios");
// }
// //valores propios
// for(i=0; i<n; i++){
// valores[i]=(double)Math.round(a.x[i][i]*1000)/1000;
// }
// //vectores propios
// return q;
// }

// public double spectralRatio(){

// double[] initialAprox = new double[A.length];

// double[][] currentApprox = new double[A.length][A.length];
// double[][] lastApprox = new double[A.length][A.length];

// double[][] Axk = new double[A.length][A.length];

// Axk = dot(A,initialAprox);
// currentApprox = dot(Axk,(1/infiintyNorm(Axk)));

// do {
// Axk = dot(A,lastApprox);
// currentApprox = dot(Axk,(1/infiintyNorm(Axk)));

// } while
// (Math.abs(infiintyNorm(currentApprox)-infiintyNorm(lastApprox))>0.0000000000001);

// }

// public double[][] dot(double [][] Q,double[][] Y){

// for (int i = 0; i < Q.length; i++)
// for (int j = 0; j < Q[0].length; j++)
// Q[i][j] *= Y[j][i];

// return Q;
// }
// public double[][] dot(double [][] Q,double[] Y){

// for (int i = 0; i < Q.length; i++)
// for (int j = 0; j < Q[0].length; j++)
// Q[i][j] *= Y[j];

// return Q;
// }
// public double[][] dot(double [][] Q,double Y){

// for (int i = 0; i < Q.length; i++)
// for (int j = 0; j < Q[0].length; j++)
// Q[i][j] *= Y;

// return Q;
// }