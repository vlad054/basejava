package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class MainStream {

    private static int minValue(int[] values) {
        int[] values2 = Arrays.stream(values).distinct().sorted().toArray();
        return (int) IntStream.range(0, values2.length).boxed().mapToDouble(ind -> values2[values2.length - ind - 1] * Math.pow(10, ind)).sum();
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        List<Integer> list = new ArrayList<>(List.copyOf(integers));
//      list.removeIf(x -> integers.stream().mapToInt(Integer::intValue).sum()%2 ==0?x%2 == 0:x%2 != 0);
        list.removeIf(x -> (integers.stream().mapToInt(Integer::intValue).sum() % 2 == 0) == (x % 2 == 0));
        return list;
    }

    public static void main(String[] args) {
        int[] values = {1, 2, 3, 3, 2, 3};
        System.out.println(minValue(values));
        values = new int[]{8, 9};
        System.out.println(minValue(values));

        List<Integer> list = List.of(1, 23, 4, 5, 3, 22, 11);
        System.out.println(Arrays.toString(oddOrEven(list).toArray()));

    }
}