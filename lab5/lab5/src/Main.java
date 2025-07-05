import managers.*;
import utility.*;
import commands.*;

public class Main {
    public static void main(String[] args) {
        var console = new StandardConsole();

        if (args.length == 0) {
            console.println("Введите имя загружаемого файла как аргумент командной строки");
            System.exit(1);
        }

        var dumpManager = new DumpManager(args[0], console);
        var collectionManager = new CollectionManager(dumpManager);
        if (!collectionManager.init()) {
            System.exit(1);
        }

        var commandManager = new CommandManager() {{
            register("help", new Help(console, this));
            register("info", new Info(console, collectionManager));
            register("show", new Show(console, collectionManager));
            register("add", new Add(console, collectionManager));
            register("update", new Update(console, collectionManager));
            register("remove_by_id", new RemoveById(console, collectionManager));
            register("clear", new Clear(console, collectionManager));
            register("save", new Save(console, collectionManager));
            register("execute_script", new ExecuteScript(console));
            register("exit", new Exit(console));
            register("head", new Head(console, collectionManager));
            register("remove_head", new RemoveHead(console, collectionManager));
            register("remove_lower", new RemoveLower(console, collectionManager));
            register("max_by_creation_date", new MaxByCreationDate(console, collectionManager));
            register("print_descending", new PrintDescending(console, collectionManager));
            register("print_unique_distance", new PrintUniqueDistance(console, collectionManager));
        }};

        new Runner(console, commandManager).interactiveMode();
    }
}
