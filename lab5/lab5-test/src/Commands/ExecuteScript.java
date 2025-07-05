package Commands;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;


public class ExecuteScript {
    private final CommandManager commandManager;
    private static final Set<String> executingScripts = new HashSet<>();
    private static final int MAX_RECURSION_DEPTH = 10;
    private int currentDepth = 0;


    public ExecuteScript(CommandManager commandManager) {
        this.commandManager = commandManager;
    }


    public void execute(String fileName) {
        // Преобразуем относительный путь в абсолютный
        File scriptFile = new File(fileName);
        if (!scriptFile.exists()) {
            System.out.println("Файл не найден по пути: " + scriptFile.getAbsolutePath());
            return;
        }
        // Проверка на рекурсию
        if (executingScripts.contains(fileName)) {
            System.out.println("Ошибка: Обнаружена рекурсия в скрипте '" + fileName + "'");
            return;
        }

        // Проверка глубины рекурсии
        if (currentDepth >= MAX_RECURSION_DEPTH) {
            System.out.println("Ошибка: Превышена максимальная глубина рекурсии (" + MAX_RECURSION_DEPTH + ")");
            return;
        }

        // Добавляем файл в выполняемые
        executingScripts.add(fileName);
        currentDepth++;

        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            System.out.println("Выполнение скрипта: " + fileName);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    commandManager.executeCommand(line);
                }
            }

            System.out.println("Скрипт успешно выполнен: " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: Файл скрипта не найден: " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода при выполнении скрипта: " + e.getMessage());
        } finally {
            executingScripts.remove(fileName);
            currentDepth--;
        }
    }
}