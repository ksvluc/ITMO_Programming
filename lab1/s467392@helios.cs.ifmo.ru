import java.util.Random;
public class Main {
    public static void main(String[] args) {

// Объявление одномерного массива z типа long размером 15 элементов(17-3+1 = 15) и заполнение массива от 3 до 17 включительно в порядке убывания
long[] z = new  long[15];
for (int i=14;i>=0;i--){
    z[i] = i+3;
}
// Объявление одномерного массива x типа float размером 12 элементов и заполнение массива случайными числами в диапазоне от -4.0 до 2.0
float[] x = new float[12];
Random random = new Random();
for (int j = 0; j<12; j++){
	x[j] = -4 + (2+4)*random.nextFloat();
    }
// Объявление двумерного массива z1 размером 15x12 и вычисление элементов массива в соответствии с условиями задачи
double[][] z1 = initMatrix(15,12,z,x);

// Вывод массива z1 в соответствии с требованиями
printMatrix(z1,8,3);
}

public static double[][] initMatrix(int rows, int columns, long[] noise1, float[] noise2){
	double[][] z1 = new double[rows][columns];
	for (int i = 0; i < rows;i++){
		for (int j = 0; j < columns; j++){
			if (noise1[i] == 16) {
				z1[i][j] = Math.sin(Math.asin(0.25 * (noise2[j]-1.0)/6));
			}
			else if (noise1[i] == 3 || noise1[i] == 6 || noise1[i] == 7 || noise1[i] == 8 || noise1[i] == 9 || noise1[i] == 14 || noise1[i] == 17){
				z1[i][j] = Math.tan(Math.pow(Math.E,Math.log10(Math.abs(noise2[j]))));
			}
			else {
				z1[i][j] = Math.log10(Math.pow(Math.E,Math.log10(Math.abs(Math.sin(Math.cos(noise2[j]))))));
			}
		}
	}
	return z1;
}

public static void printMatrix(double[][] matrix, int itemLength, int countNumbersAfterPoint){
	for(double[] line : matrix){
		for(double item : line){
			System.out.printf("%" + itemLength + "." + countNumbersAfterPoint + "f ", item);
	}
	System.out.println();
}
}
}