package ru.sbt.smartslame.generics.hw;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<T> implements CountMap<T> {
    private final Map<T, Integer> map;

    public CountMapImpl() {
        this.map = new HashMap<>();
    }

    @Override
    public void add(T key) {
        map.compute(key, (k, v) -> v == null ? 1 : v + 1);
    }

    @Override
    public int getCount(T key) {
        return map.getOrDefault(key, 0);
    }

    @Override
    public int remove(T key) {
        int count = getCount(key);
        map.remove(key);
        return count;
    }

    @Override
    public int size() {
        return map.keySet().size();
    }

    @Override
    public void addAll(CountMap<? extends T> source) {
        for (Map.Entry<? extends T, Integer> entries : source.toMap().entrySet()) {
            T addKey = entries.getKey();
            int addValue = entries.getValue();
            map.compute(addKey, (key, oldValue) -> oldValue == null ? 1 : oldValue + addValue);
        }
    }

    @Override
    public Map<T, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(Map<? super T, Integer> destination) {
        destination.putAll(map);
    }

    public static void main(String[] args) {
        CountMap<Integer> map = new CountMapImpl<>();

        map.add(10);
        map.add(10);
        map.add(5);
        map.add(6);
        map.add(5);
        map.add(10);
        System.out.println(map.getCount(5)); // 2
        System.out.println(map.getCount(6)); // 1
        System.out.println(map.getCount(10)); // 3

    }
}
