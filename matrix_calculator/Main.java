import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        boolean ok = true;
        double[] numbers;
        int rows = 0, hold;
        Matrix result;
        Matrix[] X = new Matrix[2];
        Scanner input = new Scanner(System.in);
        String str, question;
        String[] splitted;
        StringBuilder matrix;

        for (int i = 0 ; i < 2 ; i++)
        {
            ok = true;
            matrix = new StringBuilder();
            System.out.println(i == 0 ? "Enter first matrix" : "Enter second matrix");

            str = input.nextLine();
            if (str.isEmpty())
            {
                System.out.println("We consider a 1*1 zero matrix because you entered nothing :|");
                X[i] = new Matrix(1, 1, new double[] {0});
                continue;
            }
            splitted = str.split(",");
            hold = splitted.length;
            matrix.append(str+",");
            rows++;

            while(!(str = input.nextLine()).equals(""))
            {
                rows++;
                if (str.split(",").length != hold)
                    ok = false;
                matrix.append(str);
                matrix.append(",");
            }

            if (ok == false)
            {
                System.out.println("We consider a 1*1 zero matrix because input was not valid :|");
                X[i] = new Matrix(1, 1, new double[] {0});
                rows = 0;
                continue;
            }

            splitted = matrix.toString().split(",");
            numbers = new double[splitted.length];
            for (int j = 0 ; j < splitted.length ; j++)
                numbers[j] = Double.parseDouble(splitted[j]);
            X[i] = new Matrix(rows, splitted.length/rows, numbers);
            rows = 0;
            X[i].printMatrix();
        }

        while (true)
        {
            System.out.println("Calculate what ? (Enter nothing to exit)");
            question = input.nextLine();
            if (question.equals("")) break;

            result = Matrix.calculate(X[0], X[1], question);
            if (result == null)
                System.out.println("There is something wrong with the expression you entered. Check whether the matrixes are proper for the given " +
                        "operation or not...");
            else
                result.printMatrix();
        }

    }
}
