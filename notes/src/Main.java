import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static Scanner scanner() {
        Scanner scanner = new Scanner(System.in);
        return scanner;
    }

    public static void println(String message) {
        System.out.println(message);
    }

    public static void print(String message) {
        System.out.print(message);
    }
   
    static void createNote(String name) {
        File file = new File("data/" + name);

        if (file.exists()) {
            println("File already exist. Overright? y/n");
            char response = scanner().next().charAt(0);
            if (response == 'n')
                return;
            file.delete();
        }
        try {
            boolean result = file.createNewFile();
            if (result)
                println("Successfully created file");
            else
                println("File creation failed.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void appendNote(String name, String data) {
        try (FileWriter fileWriter = new FileWriter("data/" + name, true)) {
            fileWriter.append(data);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    static void readNote(String name) {
        File file = new File("data/" + name);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                println(scanner.nextLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void searchNote(String keyWord) {
        File dir = new File("data");
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File currentFile) {
                try (Scanner scanner = new Scanner(currentFile)) {
                    String fileData = "";
                    while (scanner.hasNextLine()) {
                        fileData += scanner.nextLine();
                    }
                    if (fileData.contains(keyWord))
                        return true;
                    else
                        return false;
                } catch (Exception e) {
                    return false;
                }
            }
        };
        File[] files = dir.listFiles(fileFilter);

        if (files.length > 0) {
            println("Found keyword in these file: ");
            for (File file : files)
                println(file.toString());
        } else
            println("No results found");
    }

    static void deleteNote(String name) {
        if (name.equalsIgnoreCase("all")) {
            File directory = new File("data");
            File[] files = directory.listFiles();
            if (files == null)
                return;
            for (File file : files) {
                println(file.toString());
                file.delete();
            }
        }
        File file = new File("data/" + name);
        file.delete();
    }

    static void prompt() {

        System.out.print("Enter Command: ");
        String givenCommand = scanner().nextLine();

        String[] commandStrings = givenCommand.split("\\s+", 3);
        String command = commandStrings.length > 0 ? commandStrings[0].trim().toLowerCase() : " ";
        String argument = commandStrings.length > 1 ? commandStrings[1].trim().toLowerCase() : " ";
        String data = commandStrings.length > 2 ? commandStrings[2].trim().toLowerCase() : " ";

        if (command.equalsIgnoreCase("exit"))
            return;

        switch (command) {
            case "create" -> createNote(argument);
            case "append" -> appendNote(argument, data);
            case "read" -> readNote(argument);
            case "search" -> searchNote(argument);
            case "delete" -> deleteNote(argument);
            default -> System.out.println("Unknown command");
        }

        // clear data on stack and reruns
        givenCommand = null;
        commandStrings = new String[0];
        command = null;
        argument = null;
        data = null;
        prompt();

    }

    public static void main(String[] args) {

        System.out.println("Welcome to command notes, All commands:");
        System.out.println(
                "create [filename.extension], append [filename] [textdata], read [all/filename.ext] search [text] delete [all/filename.ext]");

        prompt();
    }
}
