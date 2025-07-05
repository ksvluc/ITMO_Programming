import java.util.Random;
public class Main {
    public static void main(String[] args) {

// Объявление одномерного массива z типа long размером 15 элементов(17-3+1 = 15) и заполнение массива от 3 до 17 включительно в порядке убывания
long[] z = new  long[15];
for (int i=0;i < 15;i++){
    z[i] = 17-i;
}
// Объявление одномерного массива x типа float размером 12 элементов и заполнение массива случайными числами в диапазоне от -4.0 до 2.0
float[] x = new float[12];
Random random = new Random();
for (int j = 0; j<12; j++){
	x[j] = -4 + (2+4)*random.nextFloat();
    }
// Объявление двумерного массива z1 размером 15x12 и вычисление элементов массива в соответствии с условиями задачи
double[][] z1 = new double[15][12];
for(int i1=0; i1 < 15; i1++){
	for(int j1=0; j1 < 12; j1++){
		z1[i1][j1] = unitOfMatrix(z,x,i1,j1);
	}
}
// Вывод массива z1 в соответствии с требованиями
printMatrix(z1,8,3);
}

public static double unitOfMatrix(long[] noise1, float[] noise2, int rplace, int lplace){
	double k;
	if (noise1[rplace] == 16) {
		k = Math.sin(Math.asin(0.25 * (noise2[lplace]-1.0)/6));
	}
	else if (noise1[rplace] == 3 || noise1[rplace] == 6 || noise1[rplace] == 7 || noise1[rplace] == 8 || noise1[rplace] == 9 || noise1[rplace] == 14 || noise1[rplace] == 17){
		k = Math.tan(Math.pow(Math.E,Math.log10(Math.abs(noise2[lplace]))));
	}
	else {
		k = Math.log10(Math.pow(Math.E,Math.log10(Math.abs(Math.sin(Math.cos(noise2[lplace]))))));
	}
	return k;
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
