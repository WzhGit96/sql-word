package com.example.boot.controller;

import com.example.boot.service.SqlWordService;
import com.example.boot.util.ReadFileUtil;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author wzh
 * @since 2021/4/23
 */
@RestController
@RequestMapping("/sql-word/v1")
public class UploadFileController {
    @Resource(name = "sqlWordServiceImpl")
    private SqlWordService sqlWordService;

    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) throws IOException {
        Assert.isTrue(!multipartFile.isEmpty(), "file can not is empty");
        String fileStr = ReadFileUtil.readSqlFile(multipartFile.getInputStream());
        String filePath = sqlWordService.dealSqlStr(fileStr);
        File file = new File(filePath);
        Assert.isTrue(file.exists(), "word file not exists");
        try(FileInputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream()) {
            byte[] data = new byte[(int) file.length()];
            int length = inputStream.read(data);
            response.setContentType("application/word;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + "data.doc");
            outputStream.write(data, 0, length);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
