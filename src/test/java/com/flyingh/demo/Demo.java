package com.flyingh.demo;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class Demo {
    private static final int[][] array = {
            {1, 5, 2, 0, 0, 0},
            {7, 3, 0, 0, 1, 2},
            {0, 0, 0, 0, 8, 1},
            {8, 7, 0, 6, 7, 0},
            {9, 6, 0, 9, 1, 0}
    };

    @Test
    public void test() {
        List<Set<Point>> pointsList = new ArrayList<>();
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                int value = array[row][col];
                if (value == 0) {
                    continue;
                }
                Point point = new Point(row, col, value);
                List<Point> list = getNearPoints(row, col);
                Optional<Set<Point>> optional = pointsList.stream().filter(point::nearAny).findFirst();
                if (optional.isPresent()) {
                    Set<Point> points = optional.get();
                    points.add(point);
                    points.addAll(list);
                } else {
                    Set<Point> set = new HashSet<>(Arrays.asList(point));
                    set.addAll(list);
                    pointsList.add(optional.orElse(set));
                }
            }
        }
        pointsList.stream().forEach(System.out::println);
        System.out.println(pointsList.stream().mapToInt(points -> points.stream().collect(Collectors.summingInt(Point::getValue))).max().orElse(0));
    }

    private List<Point> getNearPoints(int row, int col) {
        List<Point> result = new ArrayList<>();
        if (row > 0) {
            if (array[row - 1][col] != 0) {
                result.add(new Point(row - 1, col, array[row - 1][col]));
            }
        }
        if (row < array.length - 1) {
            if (array[row + 1][col] != 0) {
                result.add(new Point(row + 1, col, array[row + 1][col]));
            }
        }
        if (col > 0) {
            if (array[row][col - 1] != 0) {
                result.add(new Point(row, col - 1, array[row][col - 1]));
            }
        }
        if (col < array[0].length - 1) {
            if (array[row][col + 1] != 0) {
                result.add(new Point(row, col + 1, array[row][col + 1]));
            }
        }
        return result;
    }

    class Point {
        int row;
        int col;
        int value;

        public Point(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public boolean isNear(Point point) {
            return row == point.row && Math.abs(col - point.col) == 1 ||
                    col == point.col && Math.abs(row - point.row) == 1;
        }

        public boolean nearAny(Set<Point> points) {
            return points.stream().filter(this::isNear).findFirst().isPresent();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (row != point.row) return false;
            if (col != point.col) return false;
            return value == point.value;

        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            result = 31 * result + value;
            return result;
        }

        @Override
        public String toString() {
            return "" + row + "," + col + "," + value;
        }
    }
}