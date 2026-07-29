package org.kdkbuilds;

import org.kdkbuilds.repository.TodoManager;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final TodoManager todoManager = new TodoManager();
    private static final Scanner scanner = new Scanner(System.in);

    private static void add() {
        System.out.print("Task: ");
        String task = scanner.nextLine();
        todoManager.add(task.trim());
    }

    private static void displayAll() {
        todoManager.display();
    }

    private static void displayByID() {
        System.out.print("\nEnter Task ID: ");
        String input = scanner.nextLine();

        int id = Integer.parseInt(input.trim());
        todoManager.display(id);
    }

    private static void update() {
        System.out.print("\nEnter Task ID: ");
        String input = scanner.nextLine();

        int id = Integer.parseInt(input.trim());
        System.out.print("Enter Updated Description: ");
        String task = scanner.nextLine();

        todoManager.update(task, id);
    }

    private static void delete() {
        System.out.print("\nEnter Task ID: ");
        String input = scanner.nextLine();

        int id = Integer.parseInt(input.trim());
        todoManager.delete(id);
    }

    private static void run() {
        System.out.println("------------TERMINALLY------------");

        boolean runApp = true;
        while (runApp) {
            System.out.println("\n1.Add   2.Display All   3.Display by ID   4.Update   5.Delete   6.Terminate");
            int input = -1;
            try {
                String userInput = scanner.nextLine();
                input = Integer.parseInt(userInput.trim());
            } catch (NumberFormatException e) {
                System.out.println("Unexpected token received. Please try again.\n");
                continue;
            }

            if (input >= 1 && input <= 6) {
                switch (input) {
                    case 1:
                        add();
                        break;
                    case 2:
                        displayAll();
                        break;
                    case 3:
                        displayByID();
                        break;
                    case 4:
                        update();
                        break;
                    case 5:
                        delete();
                        break;
                    case 6:
                        runApp = false;
                        break;
                }
            } else {
                System.out.println("Only enter integers between [1, 6]. Please try again.\n");
            }
        }
    }

    // Independent Run for DEBUG
    public static void main(String[] args) {
        run();
    }
}
