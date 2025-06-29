package com.tasktracker;

import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Service service = new Service();

    private static final Set<String> TOKENS = Set.of(
            "add",
            "list",
            "update",
            "delete",
            "mark-in-progress",
            "mark-done");

    public static void main(String[] args) {
        service.load();
        Scanner e = new Scanner(System.in);
        System.out.print("task-cli ");
        while (e.hasNext()) {
            String clave = e.next().toLowerCase();
            if (TOKENS.contains(clave)) {
                switch (clave) {
                    case "add":
                        String description = e.nextLine();
                        Task task = new Task(description);
                        service.addTask(task);
                        break;
                    case "list":
                        String filter = e.nextLine();
                        if (!filter.isBlank()) {
                            switch (filter.trim()) {
                                case "done":
                                    System.out.println("Done Tasks:");
                                    service.listDoneTasks();
                                    break;
                                case "in-progress":
                                    System.out.println("In Progress Tasks:");
                                    service.listInProgressTasks();
                                    break;
                                case "to-do":
                                    System.out.println("To Do Tasks:");
                                    service.listToDoTasks();
                                    break;
                                default:
                                    System.out.println("Invalid filter");
                            }
                        } else {
                            System.out.println("Tasks:");
                            service.listAllTasks();
                        }
                        break;
                    case "update":
                        int idToUpdate = e.nextInt();
                        String newDescription = e.nextLine().trim();
                        service.updateTask(idToUpdate, newDescription);
                        break;
                    case "delete":
                        int idToDelete = e.nextInt();
                        service.deleteTask(idToDelete);
                        break;
                    case "mark-in-progress":
                        int idToMarkInProgress = e.nextInt();
                        service.markInProgress(idToMarkInProgress);
                        break;
                    case "mark-done":
                        int idToMarkDone = e.nextInt();
                        service.markDone(idToMarkDone);
                        break;
                    default:
                        System.out.println("Invalid command");
                }
            } else {
                System.out.println("Invalid command");
            }
            System.out.print("task-cli ");
        }
        e.close();
    }

}