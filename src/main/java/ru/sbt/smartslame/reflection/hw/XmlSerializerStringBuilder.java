package ru.sbt.smartslame.reflection.hw;

import java.lang.reflect.Array;

public class XmlSerializerStringBuilder implements SerializerStringBuilder {
    private final StringBuilder sb;

    public XmlSerializerStringBuilder() {
        sb = new StringBuilder();
    }

    @Override
    public void addFirstLine(String indent, String fieldName) {
        sb.append(indent + "<" + fieldName + ">\n");
    }

    @Override
    public void addSingleField(String indent, String fieldName, Object fieldValue) {
        sb.append(indent);
        sb.append("<" + fieldName + ">" + fieldValue + "</" + fieldName + ">\n");
    }

    @Override
    public void addCollection(String indent, String fieldName, Object fieldValue) {
        sb.append(indent);
        sb.append("<" + fieldName + ">\n");
        int i = 1;
        for (Object o : (Iterable) fieldValue) {
            sb.append(indent + '\t');
            sb.append("<" + i + ">" + o + "</" + i + ">\n");
            i++;
        }
        sb.append(indent);
        sb.append("</" + fieldName + ">\n");
    }

    @Override
    public void addArray(String indent, String fieldName, Object fieldValue) {
        sb.append(indent);
        sb.append("<" + fieldName + ">\n");
        int length = Array.getLength(fieldValue);
        for (int i = 0; i < length; i++) {
            Object arrayElement = Array.get(fieldValue, i);
            sb.append(indent + '\t');
            sb.append("<" + (i + 1) + ">" + arrayElement + "</" + (i + 1) + ">\n");
        }
        sb.append(indent);
        sb.append("</" + fieldName + ">\n");
    }

    @Override
    public void addOpenName(String indent, String fieldName) {
        sb.append(indent + "<" + fieldName + ">\n");
    }

    @Override
    public void addClosedName(String indent, String fieldName) {
        sb.append(indent + "</" + fieldName + ">\n");
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}

