package com.renchi.search;

import java.util.Objects;

/**
 * @author ivan_liang
 * @describe
 * @createTime 2023/05/24
 */
public class BinarySearch {

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8,9,10};
        int value = binary_search(arr, 6);
        System.out.println(value);
    }

    /**
     * @Function: 二分查找
     * @author: ivan_liang
     * @Date: 2023/5/24 11:37
      * @param arr : 数组
     * @param item : 查找数
     * @return: int
     */
    public static int binary_search(int[] arr,int item){
        int low = 0;
        int high = arr.length-1;

        while (low<=high){
            int mid = (low+high)/2;
            int guess = arr[mid];
            if (Objects.equals(guess,item)){
                return mid;
            }
            if (guess>item){
                high = mid-1;
            }
            if (guess<item){
                low = mid+1;
            }
        }
        return -1;
    }
}
