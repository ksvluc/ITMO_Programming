package utility;

import java.util.NoSuchElementException;
import java.lang.IllegalStateException;
import java.util.Scanner;

/**
 * Реализация интерфейса Console для взаимодействия с пользователем через консоль.
 */
public class StandardConsole implements Console {
    private static final String P = "$ "; // Устанавливаем вид prompt.
    private static Scanner fileScanner = null;
    private static Scanner defScanner = new Scanner(System.in);

    /**
     * Выводит obj.toString() в консоль.
     * @param obj Объект для печати.
     */
    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Выводит obj.toString() + \n в консоль.
     * @param obj Объект для печати.
     */
    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Выводит сообщение об ошибке в стандартный поток ошибок.
     * @param obj Объект для отображения в качестве ошибки.
     */
    @Override
    public void printError(Object obj) {
        System.err.println("Error: " + obj);
    }

    /**
     * Считывает строку из текущего источника ввода.
     * @return Считанная строка.
     * @throws NoSuchElementException если строка отсутствует.
     * @throws IllegalStateException если Scanner закрыт.
     */
    @Override
    public String readln() throws NoSuchElementException, IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).nextLine();
    }

    /**
     * Проверяет, есть ли следующая строка для считывания.
     * @return true, если можно считать строку.
     * @throws IllegalStateException если Scanner закрыт.
     */
    @Override
    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).hasNextLine();
    }

    /**
     * Отображает два элемента в формате таблицы.
     * @param elementLeft Левый элемент строки таблицы.
     * @param elementRight Правый элемент строки таблицы.
     */
    @Override
    public void printTable(Object elementLeft, Object elementRight) {
        System.out.printf(" %-35s%-1s%n", elementLeft, elementRight);
    }

    /**
     * Выводит текущий prompt в консоль.
     */
    @Override
    public void prompt() {
        print(P);
    }

    /**
     * Возвращает текущий prompt.
     * @return Строка prompt.
     */
    @Override
    public String getPrompt() {
        return P;
    }

    /**
     * Устанавливает Scanner для работы с файлом.
     * @param scanner Новый Scanner.
     */
    @Override
    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    /**
     * Переключает ввод обратно на консоль.
     */
    @Override
    public void selectConsoleScanner() {
        fileScanner = null;
    }
}
