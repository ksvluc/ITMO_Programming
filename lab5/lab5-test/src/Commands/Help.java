package Commands;

public class Help {
    public static void execute() {
        System.out.println("""
            Список команд:
            help - вывести справку по доступным командам
            info - информация о коллекции
            show - вывести все элементы коллекции
            add - добавить новый элемент в коллекцию
            update id - обновить элемент по ID
            remove_by_id id - удалить элемент по ID
            clear - очистить коллекцию
            save - сохранить коллекцию в файл
            execute_script file_name - считать и выполнить скрипт
            exit - завершить программу
            head - вывести первый элемент коллекции
            remove_head - вывести первый элемент коллекции и удалить его
            remove_lower {element} - удалить элементы коллекции меньше заданного
            max_by_creation_date - вывести элемент коллекции с максимальной датой создания
            print_descending - вывести элементы коллекции в порядке убывания
            print_unique_distance - уникальные значения distance
            """);
    }
}