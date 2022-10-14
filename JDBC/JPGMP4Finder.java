package JDBC;

import java.io.*;

/**
 * @program: codeJDBC
 * @author: Ren
 * @create: 2022-10-13 21:59
 * @description:   检索src文件下的所有图片与视频，并将这些文件复制到dest文件目录下
 **/
public class JPGMP4Finder {
    static int i = 0;   //重写文件名需要的静态变量

    public static void main() {
        //含有图片文件的根文件夹绝对路径
        String src = "D:\\SteamGM\\steamapps\\workshop\\content\\431960";
        //要将图片复制到的根目录绝对路径
        String dest = "D:\\machs\\Pictures\\album\\wallpaper";
        finJPG(src,dest);
    }

    //将wallpaper中的所有图片文件 移植到album中
    public static void finJPG(String src,String dest) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File[] files = new File(src).listFiles();

        try {
            //遍历查找照片并进行保存
            if(files != null)
            for (File file : files) {
                if (file.isDirectory()) {
                    finJPG(file.getAbsolutePath(),dest);
                } else if (file.getName().endsWith(".jpg")) {
                    //添加输出流的位置  并将文件名重写
                    bos = new BufferedOutputStream(new FileOutputStream(dest + "\\Picture_" + (JPGMP4Finder.i++) + ".jpg"));
                    //将找到的后缀.jpg 的文件绝对路径设置为输入流
                    bis = new BufferedInputStream(new FileInputStream(file.getAbsoluteFile()));

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, len);
                    }
                    System.out.println(file.getAbsolutePath());   //wallpaper中图片文件的地址
                } else if (file.getName().endsWith(".mp4")) {
                    //添加输出流的位置
                    bos = new BufferedOutputStream(new FileOutputStream("D:\\machs\\Pictures\\album\\wallpaperMVI\\" + file.getName()));
                    //将找到的后缀.mp4 的文件绝对路径设置为输入流
                    bis = new BufferedInputStream(new FileInputStream(file.getAbsoluteFile()));

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, len);
                    }
                    System.out.println(file.getAbsolutePath());   //wallpaper中视频文件的地址
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
