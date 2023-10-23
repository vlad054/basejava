package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;

public class MainStream {

    public static void main(String[] args) {
        int[] values = {1, 2, 3, 3, 2, 3};
        System.out.println(minValue(values));
        values = new int[]{8, 9};
        System.out.println(minValue(values));
        values = new int[]{8, 9, 1, 9, 8};
        System.out.println(minValue(values));

        List<Integer> list = List.of(1, 23, 4, 5, 3, 11, 11, 8);
        System.out.println(Arrays.toString(oddOrEven(list).toArray()));

    }

    private static int minValue(int[] values) {
        return Arrays.stream(values).distinct().sorted().reduce((x, y) -> x * 10 + y).getAsInt();
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        boolean bEven = integers.stream().mapToInt(Integer::intValue).sum() % 2 == 0;
        return integers.stream().filter(x -> bEven ? x % 2 != 0 : x % 2 == 0).toList();
    }
}