package com.example.raj_liangjian.controller;

import com.example.raj_liangjian.common.R;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${Raj.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //截取原文件后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //用UUID生成文件名防止重复
        String s = UUID.randomUUID() + suffix;

        //创建一个目录是否存在
        File dir = new File(basePath);
        //判断目录是否存在
        if (!dir.exists()) {
            //如果不存在创建一个
            dir.mkdirs();
        }

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(s);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse Response) {
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = Response.getOutputStream();
            Response.setContentType("image/jpeg");

             IOUtils.copy(fileInputStream, outputStream);

            /*int len = 0;
            byte[] bytes = new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
               outputStream.write(bytes,0,len);
                outputStream.flush();
        }*/

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
