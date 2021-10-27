package sort;

import common.ArrayGenerator;
import common.SortingHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MSDSort {

    private MSDSort(){}

    public static void sort(String[] arr){
        sort(arr,0,arr.length-1,0);
    }

    /**
     * @Description: 递归调用sort区间的方式
     *
     * @author: Veneno
     * @date: 2021/10/27 22:07
     * @param: [arr:源数组, left:包区间左, right:包区间右, r:目标字符串的索引]
     * @return: void
     */
    private static void sort(String[] arr, int left,int right,int r){

        //递归终止条件
        if(left>=right){
            return ;
        }
        int R = 256;
        // 字符char是8位的,所以有256个,但是每个字符串长度不一,所以预留一个0的位置来装缺少字符的位置
        int[] count = new int[R+1];
        // 同理index需要增加一位
        int[] index = new int[R+2];
        // 非原地排序,所以需要一个搬运空间,大小等于排序区间大小
        String[] temp = new String[right-left+1];

        //统计
        for (int i = left; i < right+1; i++) {
            String str = arr[i];
            // 如果该字符串长度不足r位,则视其ascii码为1,否则取其ascii码+1;
            count[str.length()<=r?0:(str.charAt(r)+1)]++;
        }

        //计算index
        for (int i = 0; i+1< index.length ; i++) {
            // 下一索引的起始位置= 当前索引+数量
            index[i+1] = index[i]+count[i];
        }

        // 按索引填充元素
        for (int i = left; i <right+1; i++) {
            String str = arr[i];
            int pos = index[str.length()<=r?0:(str.charAt(r)+1)];
            temp[pos] = str;
            index[str.length()<=r?0:(str.charAt(r)+1)]++;
        }

        // 搬运元素

        for (int i = left; i < right+1; i++) {
            arr[i] = temp[i-left];
        }

        // 处理第二个位置的字符r+1;
        for (int i = 1; i+1 < index.length; i++) {
            sort(arr,left+index[i],left+index[i+1]-1,r+1);
        }
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int amount = 100000,length = 10;
        String[] words = ArrayGenerator.generatorStrArray(amount, length);
        sortTest(MSDSort.class,"sort",words);
    }

    public static  void sortTest(Class<?> sortClass, String method, String[] arr) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {


        Method declaredMethod = sortClass.getMethod(method,String[].class);

        long start = System.nanoTime();
        // 因为invoke方法的入参是(object和一个参数的Object[],所以我们的目标参数Integer[]是一个参数需要转成object(而不是invoke的多个参数)
        declaredMethod.invoke(sortClass.getName()+"."+method,(Object)arr);
        long end = System.nanoTime();
        if(!isSorted(arr)){
            throw new RuntimeException(sortClass.getName()+"."+method+" : sort faild ");
        }
        double use = (end-start)/1_000_000_000.0;
        System.out.printf("%s sort %d data  use %f s %n",sortClass.getName()+"."+method,arr.length,use);
    }

    // 验证是否有虚
    private static <E extends Comparable<E>> boolean isSorted(E[] arr){

        for (int i =1;i<arr.length;i++){
            if(arr[i-1].compareTo(arr[i])>0){
                return false;
            }
        }
        return true;
    }

}
