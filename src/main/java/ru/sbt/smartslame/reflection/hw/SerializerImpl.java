package ru.sbt.smartslame.reflection.hw;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SerializerImpl implements Serializer {
    private final SerializerStringBuilder sb;
    private final StringBuilder indent;

    public SerializerImpl(SerializerStringBuilder sb) {
        this.sb = sb;
        indent = new StringBuilder();
    }

    @Override
    public String serialize(Object o) {
        sb.addFirstLine(indent.toString(), o.getClass().getSimpleName());
        indent.append('\t');
        print(o);
        indent.deleteCharAt(indent.lastIndexOf("\t"));
        sb.addClosedName(indent.toString(), o.getClass().getSimpleName());
        return sb.toString();
    }

    private  <T> List<Field> getFields(T t) {
        List<Field> fields = new ArrayList<>();
        Class clazz = t.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public void print(Object p) {
        List<Field> fields = getFields(p);
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            Object fieldValue = null;

            try {
                fieldValue = field.get(p);
            } catch (IllegalAccessException ignored) {
            }

            if (fieldType.isPrimitive() || fieldType.equals(String.class)) {
                sb.addSingleField(indent.toString(), field.getName(), fieldValue);
                continue;
            }

            if (Collection.class.isAssignableFrom(fieldType)) {
                sb.addCollection(indent.toString(), field.getName(), fieldValue);
                continue;
            }

            if (fieldType.isArray()) {
                sb.addArray(indent.toString(), field.getName(), fieldValue);
                continue;
            }

            sb.addOpenName(indent.toString(), field.getName());
            indent.append('\t');
            print(fieldValue);
            indent.deleteCharAt(indent.lastIndexOf("\t"));
            sb.addClosedName(indent.toString(), field.getName());
        }
    }



    public static void main(String[] args) {
        Person p = new Person(
                "Ivan",
                "Ivanov",
                new Address("MSK", "111"),
                Arrays.asList("123", "456"),
                new int[]{1, 2, 3}
        );
        SerializerImpl jsonSerializer = new SerializerImpl(new JsonSerializerStringBuilder());
        System.out.println(jsonSerializer.serialize(p));
        SerializerImpl xmlSerializer = new SerializerImpl(new XmlSerializerStringBuilder());
        System.out.println(xmlSerializer.serialize(p));

    }
}
