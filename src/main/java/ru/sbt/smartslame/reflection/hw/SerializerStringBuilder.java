package ru.sbt.smartslame.reflection.hw;

public interface SerializerStringBuilder {
    void addSingleField(String indent, String fieldName, Object fieldValue);

    void addCollection(String indent, String fieldName, Object fieldValue);

    void addArray(String indent, String fieldName, Object fieldValue);

    void addOpenName(String indent, String fieldName);

    void addClosedName(String indent, String fieldName);

    void addFirstLine(String indent, String fieldName);
}
