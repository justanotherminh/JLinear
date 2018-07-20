import java.util.Random;

public class Ops {
    static void Print(double[][] a) {
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[0].length; y++) {
                System.out.printf("%.6f\t", a[x][y]);
            }
            System.out.println();
        }
    }

    static double Norm(double[][] a) {
        double sum = 0;
        for (double[] i : a) {
            sum += i[0] * i[0];
        }
        return Math.sqrt(sum);
    }

    static double[][] Transpose(double[][] a) {
        double[][] result = new double[a[0].length][a.length];
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < a[0].length; y++) {
                result[y][x] = a[x][y];
            }
        }
        return result;
    }

    static double[][] Multiply(double[][] a, double[][] b) {
        assert a[0].length == b.length;
        double[][] result = new double[a.length][b[0].length];
        for (int x = 0; x < a.length; x++) {
            for (int y = 0; y < b[0].length; y++) {
                double sum = 0;
                for (int i = 0; i < a[0].length; i++) {
                    sum += a[x][i] * b[i][y];
                }
                result[x][y] = sum;
            }
        }
        return result;
    }

    static double[] Reduce(double[][] a, double[] b) {
        assert a.length == a[0].length;
        assert a.length == b.length;
        if (b.length == 1) {
            return new double[]{b[0] / a[0][0]};
        }
        double[][] a_sub = new double[a.length - 1][a.length - 1];
        double[] b_sub = new double[a.length - 1];
        for (int x = 1; x < a.length; x++) {
            double k = a[x][0] / a[0][0];
            for (int y = 1; y < a.length; y++) {
                a_sub[x - 1][y - 1] = a[0][y] * k - a[x][y];
            }
            b_sub[x - 1] = b[0] * k - b[x];
        }
        double[] result = new double[a.length];
        result[0] = b[0];
        System.arraycopy(Reduce(a_sub, b_sub), 0, result, 1, a.length - 1);
        for (int i = 1; i < a.length; i++) {
            result[0] -= result[i] * a[0][i];
        }
        result[0] /= a[0][0];
        return result;
    }

    static double[][] Invert(double[][] a) {
        assert a.length == a[0].length;
        double[][] result = new double[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            double[] eye = new double[a.length];
            eye[i] = 1;
            double[] _r = Reduce(a, eye);
            for (int r = 0; r < a.length; r++) {
                result[r][i] = _r[r];
            }
        }
        return result;
    }

    static double Det(double[][] a) {
        assert a.length == a[0].length;
        if (a.length == 1) {
            return a[0][0];
        }
        double result = 0;
        double[][] sub = new double[a.length - 1][a.length - 1];
        for (int i = 0; i < a.length; i++) {
            for (int x = 1; x < a.length; x++) {
                for (int y = 0; y < a.length; y++) {
                    if (y < i) {
                        sub[x - 1][y] = a[x][y];
                    } else if (y > i) {
                        sub[x - 1][y - 1] = a[x][y];
                    }
                }
            }
            result += (i % 2 == 0) ? a[0][i] * Det(sub) : -1 * a[0][i] * Det(sub);
        }
        return result;
    }

    // Power iteration
    static double[][] Eigen(double[][] a) {
        assert a.length == a[0].length;
        Random rand = new Random();
        double[][] b_k = new double[a.length][1];
        for (int i = 0; i < a.length; i++) {
            b_k[i][0] = rand.nextDouble();
        }
        double norm = Norm(b_k);
        for (int i = 0; i < a.length; i++) {
            b_k[i][0] /= norm;
        }
        double err = 1;
        while (err > 1e-8) {
            double[][] b_k1 = Multiply(a, b_k);
            norm = Norm(b_k1);
            for (int i = 0; i < a.length; i++) {
                b_k1[i][0] /= norm;
            }
            err = 0;
            for (int i = 0; i < a.length; i++) {
                err += Math.abs(b_k1[i][0] - b_k[i][0]);
            }
            b_k = b_k1;
        }
        return b_k;
    }

//    class USV {
//        double[][] U;
//        double[] S;
//        double[][] V;
//        public USV(double[][] U, double[] S, double[][] V) {
//            this.U = U;
//            this.S = S;
//            this.V = V;
//        }
//    }
//
//    static USV SVD(double[][]) {
//
//    }

    public static void main(String[] args) {
        Random rand = new Random();
        double[][] a = new double[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                a[x][y] = rand.nextInt(10);
            }
        }
        Print(a);
        double[][] eig = Eigen(a);
        Print(Multiply(Multiply(Transpose(eig), a), eig));
    }
}
