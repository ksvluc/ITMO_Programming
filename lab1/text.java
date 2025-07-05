import java.util.Random;
public class text {
    public static void main(String[] args) {

// Объявление одномерного массива z типа long размером 15 элементов(17-3+1 = 15) и заполнение массива от 3 до 17 включительно в порядке убывания
long[] z = new  long[15];
for (int i=0;i < 15;i++){
    z[i] = 17-i;

}
for(int j = 0; j<15;j++){
    System.out.println(z[j]);
}
}
}