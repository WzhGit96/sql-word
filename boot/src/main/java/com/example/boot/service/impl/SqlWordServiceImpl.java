package com.example.boot.service.impl;

import com.example.boot.entity.SqlField;
import com.example.boot.entity.Table;
import com.example.boot.service.SqlWordService;
import com.example.boot.util.WordUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wzh
 * @since 2021/4/25
 */
@Service
public class SqlWordServiceImpl implements SqlWordService {
    private static final String PREFIX = "CREATE TABLE";
    private static final String SUFFIX = "Dynamic";
    private static final String SUFFIX2 = "DYNAMIC";

    private static List<String> fieldType = new ArrayList<>();

    static {
        fieldType.addAll(Arrays.asList(new String[]{"varchar", "int", "datetime", "decimal", "bigint"}));
    }

    @Override
    public String dealSqlStr(String sqlStr) {
        List<String> tableStr = getTableStr(sqlStr.replaceAll(SUFFIX, SUFFIX2));
        List<Table> tables = new ArrayList<>();
        tableStr.forEach(item -> {
            Table table = new Table();
            table.setFields(new ArrayList<>());
            String lineStr[] = item.split("\n");
            dealTableStr(lineStr, table, 0);
            tables.add(table);
        });
        return WordUtil.createWord(tables);
    }

    private List<String> getTableStr(String sqlStr) {
        List<Integer> startIndex = new ArrayList<>();
        List<Integer> endIndex = new ArrayList<>();
        int index = sqlStr.indexOf(PREFIX);
        while (index != -1) {
            startIndex.add(index);
            int next = index + PREFIX.length();
            index = sqlStr.indexOf(PREFIX, next);
        }
        index = sqlStr.indexOf(SUFFIX2);
        while (index != -1) {
            endIndex.add(index);
            int next = index + SUFFIX2.length();
            index = sqlStr.indexOf(SUFFIX2, next);
        }
        Assert.isTrue(startIndex.size() == endIndex.size(), "start size must equals end size");
        endIndex = endIndex.stream().sorted().collect(Collectors.toList());
        List<String> tableStr = new ArrayList<>();
        int length = startIndex.size();
        for (int i = 0; i < length; i++) {
            tableStr.add(sqlStr.substring(startIndex.get(i), endIndex.get(i) + SUFFIX2.length()));
        }
        return tableStr;
    }

    private void dealTableStr(String tableStr[], Table table, int n) {
        if (n == tableStr.length) {
            return;
        }
        String line = tableStr[n];
        // 表名
        if (n == 0) {
            table.setName(getName(line));
            n++;
            dealTableStr(tableStr, table, n);
            return;
        }
        // 表编码和描述
        if (n == tableStr.length - 1) {
            String encodeAndDesc[] = getTableEncodeAndDesc(line);
            table.setEncode(encodeAndDesc[0]);
            table.setDesc(encodeAndDesc[1]);
            return;
        }
        // 主键
        if (n == tableStr.length - 2) {
            String filter = getName(line);
            table.getFields().forEach(item -> {
                if (item.getName().equals(filter)) {
                    item.setPrimaryKey(true);
                }
            });
            n++;
            dealTableStr(tableStr, table, n);
            return;
        }
        dealFiledsStr(line, table);
        n++;
        dealTableStr(tableStr, table, n);
    }

    private String getName(String line) {
        return line.substring(line.indexOf("`") + 1, line.lastIndexOf("`"));
    }

    private String[] getTableEncodeAndDesc(String line) {
        String result[] = new String[2];
        String prefix = "CHARACTER SET =";
        String suffix = "COLLATE";
        result[0] = line.substring(line.indexOf(prefix) + prefix.length(), line.indexOf(suffix));
        if (line.contains("COMMENT")) {
            prefix = "COMMENT = '";
            suffix = "' ROW_FORMAT";
            result[1] = line.substring(line.indexOf(prefix) + prefix.length(), line.indexOf(suffix));
        } else {
            result[1] = "";
        }
        return result;
    }

    private void dealFiledsStr(String line, Table table) {
        SqlField field = new SqlField();
        String name = getName(line);
        String type = fieldType.stream().filter(item -> line.contains(item)).findFirst().orElse("");
        int startIndex = line.indexOf("(") + 1;
        int endIndex = line.indexOf(")");
        String length = line.substring(startIndex, endIndex);
        if (line.contains("COMMENT")) {
            startIndex = line.indexOf("'") + 1;
            endIndex = line.lastIndexOf("'");
            String comment = line.substring(startIndex, endIndex);
            field.setComment(comment);
        }
        field.setName(name);
        field.setType(type);
        field.setLength(length);
        table.getFields().add(field);
    }
}
