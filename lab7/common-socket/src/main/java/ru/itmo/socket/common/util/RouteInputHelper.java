package ru.itmo.socket.common.util;

import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.common.entity.*;

import java.util.Scanner;

public class RouteInputHelper {

    public static UserDto readUser(Scanner scanner) {
        UserDto userDto = new UserDto();
        userDto.setLogin(inputLogin(scanner));
        userDto.setPassword(inputPassword(scanner));
        return userDto;

    }

    private static String inputLogin(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите login");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Введено пустое значение");
                }
                return input;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    private static String inputPassword(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите password");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Введено пустое значение");
                }
                return input;
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    public static Route readRoute(Scanner scanner) {
        return readRoute(scanner, false);
    }

    // Основной публичный метод, который собирает все части объекта Route.
    public static Route readRoute(Scanner scanner, boolean update) {

        Route route = new Route();

        if (update) {
            route.setId(inputId(scanner));
        }
        else {
            route.setId(route.generateId());
        }
        route.setName(inputName(scanner));
        route.setCoordinates(inputCoordinates(scanner));
        route.setFrom(inputFrom(scanner));
        route.setTo(inputTo(scanner));
        route.setDistance(inputDistance(scanner));

        return route;
    }

    public static long inputId(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите id");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equals("null")) {
                    throw new IllegalArgumentException("Введено пустое значение или 'null'");
                }

                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введены не цифры. Попробуйте еще раз.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение названия route.
    public static String inputName(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите название маршрута:");
                String name = scanner.nextLine().trim();
                if (name.isEmpty() || name.equals("null")) {
                    throw new IllegalArgumentException("Название не может быть пустым");
                }
                return name;
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение координат.
    public static Coordinates inputCoordinates(Scanner scanner) {
        Coordinates coordinates = new Coordinates(0, 0);
        coordinates.setX(inputCoordinateX(scanner));
        coordinates.setY(inputCoordinateY(scanner));
        return coordinates;
    }

    // Чтение координаты X.
    private static double inputCoordinateX(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите координату X (double, > -68):");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equals("null")) {
                    throw new IllegalArgumentException("Введено пустое значение или 'null'");
                }

                float x = Float.parseFloat(input);

                if (x <= -68) {
                    throw new IllegalArgumentException("Координата X должна быть больше -68");
                }

                return x;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введены не цифры. Попробуйте еще раз.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение координаты Y.
    private static int inputCoordinateY(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите координату Y:");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equals("null")) {
                    throw new IllegalArgumentException("Введено пустое значение или 'null'");
                }

                int y = Integer.parseInt(input);
                return y;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введены не цифры. Попробуйте еще раз.");
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение начальной и конечной локации локации.
    public static Location inputFrom(Scanner scanner) {
        System.out.print("Ввести начало маршрута? (Y/N): ");
        while (true) {
            String ans = scanner.nextLine().trim();
            if (ans.equals("Y")) {
                System.out.println("Введите начальную локацию");
                Location from = new Location("temporary", 0, 0, 0);
                from.setName(inputFromName(scanner));
                from.setX(inputFromX(scanner));
                from.setY(inputFromY(scanner));
                from.setZ(inputFromZ(scanner));
                return from;
            }
            if (ans.equals("N")) {
                return null;
            } else {
                System.out.print("Введите 'Y' или 'N': ");
            }
        }
    }
    public static Location inputTo(Scanner scanner) {
        System.out.print("Ввести конец маршрута? (Y/N): ");
        while (true) {
            String ans = scanner.nextLine().trim();
            if (ans.equals("Y")) {
                System.out.println("Введите конечную локацию");
                Location to = new Location("temporary", 0, 0, 0);
                to.setName(inputFromName(scanner));
                to.setX(inputFromX(scanner));
                to.setY(inputFromY(scanner));
                to.setZ(inputFromZ(scanner));
                return to;
            }
            if (ans.equals("N")) {
                return null;
            } else {
                System.out.print("Введите 'Y' или 'N': ");
            }
        }
    }
    public static String inputFromName(Scanner scanner) {
        while (true) {
            try{
            System.out.print("Введите название локации from: ");
            String name = scanner.nextLine();
            if (name == null || name.isEmpty()) {
                System.out.println("Имя не может быть пустым или null");
            }
            return name;
            }
            catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение координаты X локации.
    public static int inputFromX(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Введите координату x для from (не null): ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty() || input.equalsIgnoreCase("null")) {
                    throw new IllegalArgumentException("Координата X локации не может быть пустой или равной null");
                }
                int x = Integer.parseInt(input);
                return x;

            } catch (NumberFormatException e) {
                System.out.println("Вводить надо int");
            }
            catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение координаты Y локации.
    public static double inputFromY(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Введите координату y для from: ");
                double y = Double.parseDouble(scanner.nextLine());
                return y;
            } catch (NumberFormatException e) {
                System.out.println("Вводить надо int");
            }
            catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }

    // Чтение координаты Z локации.
    public static int inputFromZ(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Введите координату z для from: ");
                int z = Integer.parseInt(scanner.nextLine());
                return z;
            } catch (NumberFormatException e) {
                System.out.println("Вводить надо int");
            }
            catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }
    // Чтение дистанции.
    public static int inputDistance(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Введите дистанцию (> 1): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty() || input.equalsIgnoreCase("null")) {
                    throw new IllegalArgumentException("Дистанция не может быть пустой или равной null");
                }
                int distance = Integer.parseInt(input);
                if (distance <= 1){
                    System.out.println("Дистанция должна быть > 1");
                }
                return distance;

            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage() + ". Попробуйте еще раз.");
            }
        }
    }
    public static Route readRouteBody(Scanner scanner) {
        Route route = new Route();
        route.setName(inputName(scanner));
        route.setCoordinates(inputCoordinates(scanner));
        route.setFrom(inputFrom(scanner));
        route.setTo(inputTo(scanner));
        route.setDistance(inputDistance(scanner));
        return route;
    }


}
