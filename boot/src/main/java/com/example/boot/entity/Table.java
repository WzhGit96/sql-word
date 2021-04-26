package com.example.boot.entity;

import java.util.List;

/**
 * @author wzh
 * @since 2021/4/25
 */
public class Table {
    private String name;

    private String desc;

    private String encode;

    private List<SqlField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public List<SqlField> getFields() {
        return fields;
    }

    public void setFields(List<SqlField> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", encode='" + encode + '\'' +
                ", fields=" + fields +
                '}';
    }
}
