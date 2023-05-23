package com.renchi.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author ivan_liang
 * @describe
 * @createTime 2023/05/23
 */
public class CatchExcetion {

    public static void main(String[] args) {

        //Java7之前捕获异常方式
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(""));
            while (scanner.hasNext()){
                System.out.println(scanner.nextLine());
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if (scanner != null){
                scanner.close();
            }
        }


        //Java7之后捕获异常方式,try-with-resources会自动关闭资源
        try(Scanner scanner1 = new Scanner(new File(""))){
            while (scanner1.hasNext()){
                System.out.println(scanner1.nextLine());
            }
        }catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
    }
}
