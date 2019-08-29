/**
* Description:
* Author: jehuRen
* Date: 2019-08-26
* Time: 13:26
*/
public class swap{
    /**
     * 通过临时变量交换数组array的i和j位置的数据
     * 最常用
     */
    public static void swapByTemp(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    /**
     * 通过算术法交换数组array的i和j位置的数据（有可能溢出）
     */
    public static void  swapByArithmetic(int[] array, int i, int j) {
        array[i] = array[i] + array[j];
        array[j] = array[i] - array[j];
        array[i] = array[i] - array[j];
    }
    /**
     * 通过位运算法交换数组array的i和j位置的数据
     */
    public static void  swapByBitOperation(int[] array, int i, int j) {
        array[i] = array[i]^array[j];
        array[j] = array[i]^array[j]; //array[i]^array[j]^array[j]=array[i]
        array[i] = array[i]^array[j]; //array[i]^array[j]^array[i]=array[j]
    }
}