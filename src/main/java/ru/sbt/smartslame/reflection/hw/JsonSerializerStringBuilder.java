package ru.sbt.smartslame.reflection.hw;

import java.lang.reflect.Array;

public class JsonSerializerStringBuilder implements SerializerStringBuilder {
    private final StringBuilder sb;

    public JsonSerializerStringBuilder() {
        sb = new StringBuilder();
    }

    @Override
    public void addFirstLine(String indent, String fieldName) {
        sb.append(indent + "{\n");
    }

    @Override
    public void addSingleField(String indent, String fieldName, Object fieldValue) {
        sb.append(indent);
        sb.append("\"" + fieldName + "\": \"" + fieldValue + "\",\n");
    }

    @Override
    public void addCollection(String indent, String fieldName, Object fieldValue) {
        sb.append(indent);
        sb.append("\"" + fieldName + "\": [\n");
        for (Object o : (Iterable) fieldValue) {
            sb.append(indent + '\t');
            sb.append("\"" + o + "\",\n");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(indent);
        sb.append("],\n");
    }

    @Override
    public void addArray(String indent, String fieldName, Object fieldValue) {
        sb.append(indent);
        sb.append("\"" + fieldName + "\": [\n");
        int length = Array.getLength(fieldValue);
        for (int i = 0; i < length; i++) {
            Object arrayElement = Array.get(fieldValue, i);
            sb.append(indent + '\t');
            sb.append("\"" + arrayElement + "\",\n");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(indent);
        sb.append("],\n");
    }

    @Override
    public void addOpenName(String indent, String fieldName) {
        sb.append(indent + "\"" + fieldName + "\": {\n");
    }

    @Override
    public void addClosedName(String indent, String fieldName) {
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(indent + "},\n");
    }

    @Override
    public String toString() {
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }
}
