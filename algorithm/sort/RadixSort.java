/**
* Description:
* Author: jehuRen
* Date: 2019-08-26
* Time: 13:27
*/
import java.util.*;
public class RadixSort {

    public static void main(String[] args) {
        int[] array = {10, 20, 5, 4, 100};
        sort(array);

    }

    public static void sort(int[] a) {

        if (a == null || a.length < 0) {
            return;
        }

        int max = a[0];
        for (int i = 0; i <a.length; i++) {
            if (a[i] > max) {
                max = a[i];
            }
        }
        System.out.println("max, " + max);

        int maxDigit = 0;
        while (max != 0) {
            max = max / 10;
            maxDigit++;
        }
        System.out.println("maxDigit, " + maxDigit);

        int[][] buckets = new int[10][a.length];
        int base = 10;

        //从低位到高位，对每一位遍历，将所有元素分配到桶中
        for (int i = 0; i < maxDigit; i++) {
            int[] bucketLen = new int[10];  //存储各个桶中存储元素的数量

            //收集：将不同桶里数据挨个捞出来,为下一轮高位排序做准备,由于靠近桶底的元素排名靠前,因此从桶底先捞
            for (int j = 0; j < a.length; j++) {
                int whichBucket = (a[j] % base) / (base / 10);
                buckets[whichBucket][bucketLen[whichBucket]] = a[j];
                bucketLen[whichBucket]++;
            }

            int k = 0;
            //收集：将不同桶里数据挨个捞出来,为下一轮高位排序做准备,由于靠近桶底的元素排名靠前,因此从桶底先捞
            for (int l = 0; l < buckets.length; l++) {
                for (int m =0; m < bucketLen[l]; m++) {
                    a[k++] = buckets[l][m];
                }
            }
            System.out.println("Sorting: " + Arrays.toString(a));
            base *= 10;
        }
    }
}