/**
* Description:
* Author: jehuRen
* Date: 2019-08-26
* Time: 13:18
*/
import java.util.*;
public class DirectInsert{
    public static void sort(int[] a) {
        if (a == null || a.length == 0) {
            return;
        }

        for (int i = 1; i < a.length; i++) {
            int j = i - 1;
            int temp = a[i]; // 先取出待插入数据保存，因为向后移位过程中会把覆盖掉待插入数
            while (j >= 0 && a[j] > a[i]) { // 如果待是比待插入数据大，就后移
                a[j+1] = a[j];
                j--;
            }
            a[j+1] = temp; // 找到比待插入数据小的位置，将待插入数据插入
        }
    }

    public static void sort2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }

        for (int i = 1; i < arr.length; i ++) {
            int j = i - 1;
            while (j >= 0 && arr[j] > arr[i]) {
                arr[j + 1] = arr[j] + arr[j+1];      //只要大就交换操作
                arr[j] = arr[j + 1] - arr[j];
                arr[j + 1] = arr[j + 1] - arr[j];
                System.out.println("Sorting:  " + Arrays.toString(arr));
            }
        }
    }
}