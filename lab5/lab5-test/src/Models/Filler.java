package Models;

import java.util.Scanner;

public class Filler {
    public void fill(Route route) {
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.print("Введите имя маршрута: ");
            String name = sc.nextLine();
            if (name == null || name.isEmpty()) {
                System.out.println("Имя маршрута не может быть пустым или null");
            }
            else{
                route.setName(name);
                break;
            }
        }


        Coordinates coordinates = new Coordinates();

        while (true){
            try{
                System.out.print("Введите координату x (должна быть > -68): ");
                Double x = Double.parseDouble(sc.nextLine());
                if (x <= -68) {
                    System.out.println("x должен быть больше -68");
                }
                else {
                    coordinates.setX(x);
                    break;
                }
            }
            catch (NumberFormatException e){
                System.out.println("Вводить надо int");
            }
        }
        while (true){
            try{
                System.out.print("Введите координату y: ");
                coordinates.setY(Integer.parseInt(sc.nextLine()));
                route.setCoordinates(coordinates);
                break;
            }
            catch (NumberFormatException e) {
                System.out.println("Вводить надо int");
            }
        }
        System.out.print("Ввести начало маршрута? (Y/N): ");
        while (true) {
            String ans = sc.nextLine().trim();
            if (ans.equals("Y")){
                System.out.println("Введите начальную локацию");
                Location from = new Location();
                while (true){
                    try {
                        System.out.print("Введите координату x для from (не null): ");
                        Integer x = Integer.parseInt(sc.nextLine());
                        from.setX(x);
                        break;

                    }
                    catch (NumberFormatException e) {
                        System.out.println("Вводить надо int");
                    }
                }

                while (true){
                    try{
                        System.out.print("Введите координату y для from: ");
                        from.setY(Double.parseDouble(sc.nextLine()));
                        break;
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Вводить надо int");
                    }
                }

                while (true){
                    try{
                        System.out.print("Введите координату z для from: ");
                        from.setZ(Integer.parseInt(sc.nextLine()));
                        break;
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Вводить надо int");
                    }
                }

                while (true){
                    System.out.print("Введите название локации from: ");
                    String name = sc.nextLine();
                    if (name == null || name.isEmpty()) {
                        System.out.println("Имя не может быть пустым или null");
                    }
                    else{
                        from.setName(name);
                        break;
                    }
                }
                route.setFrom(from);

            }
            if (ans.equals("N")){
                break;
            }
            else{
                System.out.print("Введите 'Y' или 'N': ");
            }
        }

        System.out.print("Ввести конец маршрута? (Y/N): ");
        while (true) {
            String ans = sc.nextLine().trim();
            if (ans.equals("Y")){
                System.out.println("Введите конечную локацию");
                Location to = new Location();
                while (true){
                    try{
                        System.out.print("Введите координату x для to (не null): ");
                        Integer x = Integer.parseInt(sc.nextLine());
                        to.setX(x);
                        break;
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Вводить надо int");
                    }
                }

                while (true){
                    try{
                        System.out.print("Введите координату y для to: ");
                        to.setY(Double.parseDouble(sc.nextLine()));
                        break;
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Вводить надо int");
                    }
                }

                while (true){
                    try{
                        System.out.print("Введите координату z для to: ");
                        to.setZ(Integer.parseInt(sc.nextLine()));
                        break;
                    }
                    catch (NumberFormatException e) {
                        System.out.println("Вводить надо int");
                    }
                }

                while (true){
                    System.out.print("Введите название локации to: ");
                    String name = sc.nextLine();
                    if (name == null || name.isEmpty()) {
                        System.out.println("Имя не может быть пустым или null");
                    }
                    else{
                        to.setName(name);
                        break;
                    }
                }

                route.setTo(to);
                break;
            }
            if (ans.equals("N")){
                break;
            }
            else{
                System.out.print("Введите 'Y' или 'N': ");
            }
        }


        while (true){
            try{
                System.out.print("Введите дистанцию (должна быть > 1): ");
                Integer distance = Integer.parseInt(sc.nextLine());
                if (distance <= 1){
                    System.out.println("Дистанция должна быть > 1");
                }
                else{
                    route.setDistance(distance);
                    break;
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Вводить надо int");
            }
        }


    }
}