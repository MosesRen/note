/**
* Description:
* Author: jehuRen
* Date: 2019-08-26
* Time: 13:23
*/
import java.util.*;
public class ShellSort{
    public static void sort(int[] arr) {
        int gap = arr.length / 2;
        for (;gap > 0; gap = gap/2) {
            for (int j = 0; (j + gap) < arr.length; j++) { //不断缩小gap，直到1为止
                for (int k = 0; (k + gap) < arr.length; k+=gap) { //使用当前gap进行组内插入排序
                    if (arr[k] > arr[k+gap]) { //交换操作
                        arr[k] = arr[k] + arr[k+gap];
                        arr[k+gap] = arr[k] - arr[k+gap];
                        arr[k] = arr[k] - arr[k+gap];
                        System.out.println("    Sorting:  " + Arrays.toString(arr));
                    }
                }
            }
        }
    }
}