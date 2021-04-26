package com.example.boot.util;

import com.example.boot.entity.Table;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wzh
 * @since 2021/4/25
 */
public final class WordUtil {
    private WordUtil() {
        super();
    }

    public static String createWord(List<Table> tables) {
        Map<String, Object> dataMap = new HashMap<>();
        List<Map<String, Object>> tableInfo = new ArrayList<>();
        tables.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", item.getName());
            map.put("desc", StringUtils.isEmpty(item.getDesc()) ? "手动补录" : item.getDesc());
            map.put("encode", StringUtils.isEmpty(item.getEncode()) ? "手动补录" : item.getEncode());
            List<Map<String, Object>> listInfo = new ArrayList<>();
            item.getFields().forEach(fields -> {
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("name", fields.getName());
                fieldMap.put("comment",  StringUtils.isEmpty(fields.getComment()) ? "无" : fields.getComment());
                if (StringUtils.isEmpty(fields.getLength())) {
                    fieldMap.put("type", fields.getType());
                } else {
                    fieldMap.put("type", fields.getType() + '(' + fields.getLength() + ')');
                }
                listInfo.add(fieldMap);
            });
            map.put("listInfo", listInfo);
            tableInfo.add(map);
        });
        dataMap.put("tableInfo", tableInfo);
        Random r = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        StringBuilder builder = new StringBuilder();
        builder.append(sdf.format(new Date()));
        builder.append("-");
        builder.append(r.nextInt(100));

        try {
            String filePath = ResourceUtils.getURL("/").getPath() + "static";
            String fileName = builder.toString() + ".doc";
            return buildWord(dataMap, "model.ftl", filePath, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "FAIL";
    }

    private static String buildWord(Map<String, Object> dataMap, String modelName, String filePath, String fileName) {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setClassForTemplateLoading(WordUtil.class,"/static");
        Writer out = null;
        try {
            Template template = configuration.getTemplate(modelName);

            File outFile = new File(filePath + File.separator + fileName);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }

            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8));
            template.process(dataMap, out);
            out.flush();
            return outFile.getCanonicalPath();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
