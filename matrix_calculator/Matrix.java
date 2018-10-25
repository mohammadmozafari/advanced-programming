/**
 * This class holds a matrix and is able to do simple mathematical operations on it.
 *
 * @author Mohammad Mozafari
 */
public class Matrix implements MatrixOperation
{
    //fields
    private double elements[][];
    private int rows, columns;

    /**
     * This constructor sets the number of rows and columns of the matrix and also takes the elements of the matrix
     * as a one dimention array.
     * @param rows the number of rows
     * @param columns the number of columns
     * @param numbers the elements of the matrix
     */
    public Matrix(int rows, int columns, double[] numbers)
    {
        int matrixIndex = 0;
        this.rows = rows;
        this.columns = columns;
        elements = new double[rows][columns];

        for (int i = 0 ; i < rows ; i++)
            for (int j = 0 ; j < columns ; j++)
                elements[i][j] = numbers[matrixIndex++];
    }

    /**
     * This static method takes two matrixes and returns a new matrix which is the result of the sum of two matrixes.
     * Warning : The number of rows and columns of the two matrixes must be equal. Otherwise the method returns null.
     * @param m1 the first matrix
     * @param m2 the second matrix
     * @return the sum of the two matrixes
     */
    public static Matrix add(Matrix m1, Matrix m2)
    {
        if (m1.rows != m2.rows || m1.columns != m2.columns)
        {
            System.out.println("The number of rows and columns of the two matrixes must be equal.");
            return null;
        }

        int matrixIndex = 0;
        double[] numbers = new double[m1.rows * m1.columns];

        for (int i = 0 ; i < m1.rows ; i++)
            for (int j = 0 ; j < m1.columns ; j++)
                numbers[matrixIndex++] = m1.elements[i][j] + m2.elements[i][j];

        return new Matrix(m1.rows, m1.columns, numbers);
    }

    /**
     * This static method takes two matrixes and returns a new matrix which is the result of the multiplication of two matrixes.
     * Warning : The number of columns of the first must be equal to the number of rows of the second. Otherwise the method retunrs null.
     * @param m1 the first matrix
     * @param m2 the second matrix
     * @return the sum of the two matrixes
     */
    public static Matrix mult(Matrix m1, Matrix m2)
    {
        if (m1.columns != m2.rows)
        {
            System.out.println("The number of columns of the first must be equal to the number of rows of the second.");
            return null;
        }

        int matrixIndex = 0;
        double[] numbers = new double[m1.rows*m2.columns];

        for (int i = 0 ; i < m1.rows ; i++)
        {
            for (int j = 0; j < m2.columns; j++)
            {
                numbers[matrixIndex] = 0;
                for (int k = 0; k < m1.columns; k++)
                {
                    numbers[matrixIndex] += m1.elements[i][k] * m2.elements[k][j];
                }
                matrixIndex++;
            }
        }
        return new Matrix(m1.rows, m2.columns, numbers);
    }

    /**
     * This method creates a new matrix that is the product of a scalar and a matrix.
     * @param r the scalar
     * @param m the matrix
     * @return the multiplication of the scalar and matrix as a new matrix
     */
    public static Matrix scalarMult(int r, Matrix m)
    {
        int matrixIndex = 0;
        double[] numbers = new double[m.rows*m.columns];

        for (int i = 0 ; i < m.rows ; i++)
            for (int j = 0 ; j < m.columns ; j++)
                numbers[matrixIndex++] = m.elements[i][j] * r;

        return new Matrix(m.rows, m.columns, numbers);
    }

    /**
     * Takes a string and tokenizes it to do the operations on matrixes then returns the result.
     * @param x first matrix
     * @param y second matrix
     * @param str the stirng
     * @return the result of the calculations
     */
    public static Matrix calculate(Matrix x, Matrix y, String str)
    {
        boolean sign;
        char op = '+';
        int a, b;
        String breaker, copy = str;
        String[] splitted;
        String[] mainParts = new String[2];
        Matrix firstMatrix, secondMatrix;

        if (str.indexOf('X') == -1 && str.indexOf('Y') == -1)
        {
            return new Matrix(1, 1, new double[] {0});
        }

        else if (str.indexOf('X') == -1 || str.indexOf('Y') == -1)
        {
            str = str.trim();
            str = str.substring(0, str.length()-1).trim();
            if (str.isEmpty()) a = 1;
            else
            {
                if (str.charAt(0) == '(')
                    str = str.substring(1,str.length()-1);
                if (str.equals("+")) str = "1";
                else if (str.equals("-")) str = "-1";
                a = Integer.parseInt(str);
            }
            return (copy.indexOf('X') == -1) ? scalarMult(a, y) : scalarMult(a, x);
        }

        else
        {
            breaker = str.indexOf("X") < str.indexOf("Y") ? "X" : "Y";

            splitted = str.split(breaker);
            mainParts[0] = splitted[0].trim();
            if (mainParts[0].isEmpty()) mainParts[0] = "1";
            splitted[1] = splitted[1].trim();
            op = splitted[1].charAt(0);
            splitted[1] = splitted[1].substring(1);
            splitted[1] = splitted[1].trim();
            mainParts[1] = splitted[1].substring(0, splitted[1].length() - 1).trim();
            if (mainParts[1].isEmpty()) mainParts[1] = "1";

            if (mainParts[0].charAt(0) == '(') mainParts[0] = mainParts[0].substring(1, mainParts[0].length()-1);
            if (mainParts[1].charAt(0) == '(') mainParts[1] = mainParts[1].substring(1, mainParts[1].length()-1);

            if (mainParts[0].equals("+")) mainParts[0] = "1";
            else if (mainParts[0].equals("-")) mainParts[0] = "-1";

            if (mainParts[1].equals("+")) mainParts[1] = "1";
            else if (mainParts[1].equals("-")) mainParts[1] = "-1";

            if (breaker.equals("X"))
            {
                a = Integer.parseInt(mainParts[0]);
                b = Integer.parseInt(mainParts[1]);
                if (op == '-')
                {
                    op = '+';
                    b *= -1;
                }
                firstMatrix = scalarMult(a, x);
                secondMatrix = scalarMult(b, y);
            }
            else
            {
                a = Integer.parseInt(mainParts[1]);
                b = Integer.parseInt(mainParts[0]);
                if (op == '-')
                {
                    op = '+';
                    a *= -1;
                }
                firstMatrix = scalarMult(b, y);
                secondMatrix = scalarMult(a, x);
            }
        }

        switch (op)
        {
            case '+':
                return add(firstMatrix, secondMatrix);

            case '*':
                return mult(firstMatrix, secondMatrix);

            default:
                System.out.println("Wrong operator entered.");
                return null;
        }
    }


    /**
     * Prints a matrix.
     */
    public void printMatrix()
    {
        for (int i = 0 ; i < rows ; i++)
        {
            for (int j = 0 ; j < columns ; j++)
                System.out.printf("%-10.2f", elements[i][j]);
            System.out.println();
        }
        System.out.println("------------------");
    }
}

interface MatrixOperation
{
    void printMatrix();
}
