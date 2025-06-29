package com.tasktracker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Service {
    static List<Task> taskList = new ArrayList<>();

    public void addTask(Task task) {
        taskList.add(task);
        save();
        System.out.println("Task added succesfully " + "(ID: " + task.getId() + ")");
    }

    public void listAllTasks() {
        if (taskList.isEmpty()) {
            System.out.println("No hay tareas en la lista.");
        } else {
            printList(taskList);
            System.out.println("All Tasks Printed");
        }
    }

    public void updateTask(int id, String description) {
        Task task = getTask(id);
        if (!Objects.isNull(task)) {
            task.setDescription(description);
            task.setUpdateAt(LocalDate.now());
            save();
            System.out.println("Task update succesfully " + "(ID: " + task.getId() + ")");
        } else {
            System.out.println("Task " + id + " is not found");
        }

    }

    public void deleteTask(int id) {
        Task task = getTask(id);
        taskList.remove(task);
        save();
        System.out.println("Task delete succesfully " + "(ID: " + task.getId() + ")");
    }

    public void markInProgress(int id) {
        Task task = getTask(id);
        task.setStatus("in-progress");
        task.setUpdateAt(LocalDate.now());
        System.out.println("Task ID: " + task.getId() + " update succesfully");
        save();
    }

    public void markDone(int id) {
        Task task = getTask(id);
        task.setStatus("done");
        task.setUpdateAt(LocalDate.now());
        System.out.println("Task ID: " + task.getId() + " update succesfully");
        save();
    }

    public void listDoneTasks() {
        var list = taskList.stream().filter(task -> task.getStatus().equals("done")).toList();
        if (list.isEmpty()) {
            System.out.println("There are no tasks");
            return;
        }
        printList(list);
        System.out.println("Tasks Done Printed");
    }

    public void listInProgressTasks() {
        var list = taskList.stream().filter(task -> task.getStatus().equals("in-progress")).toList();
        if (list.isEmpty()) {
            System.out.println("There are no tasks");
            return;
        }
        printList(list);
        System.out.println("Tasks In-Progress Printed");
    }

    public void listToDoTasks() {
        var list = (taskList.stream().filter(task -> task.getStatus().equals("to-do")).toList());
        if (list.isEmpty()) {
            System.out.println("There are no tasks");
            return;
        }
        printList(taskList);
        System.out.println("Tasks To-Do Printed");
    }

    public Task getTask(int id) {
        return taskList.stream().filter(task -> task.getId() == id).findFirst().get();
    }

    public void save() {
        try (FileWriter file = new FileWriter("tasks.json")) {
            JSONArray arrayJSON = new JSONArray();
            taskList.stream().forEach(t -> {
                JSONObject obj = new JSONObject();
                obj.put("id", t.getId());
                obj.put("description", t.getDescription());
                obj.put("status", t.getStatus());
                obj.put("createAt", t.getCreatedAt().toString());
                obj.put("updateAt", t.getUpdateAt() != null ? t.getUpdateAt().toString() : "");
                arrayJSON.add(obj);
            });
            JSONArray.writeJSONString(arrayJSON, file);
            // ESCRIBIENDO EN FILE
            file.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void load() {
        try (FileReader file = new FileReader("tasks.json")) {
            JSONParser parser = new JSONParser();
            JSONArray arrayJSON = (JSONArray) parser.parse(file);
            taskList.clear(); // Limpiar la lista antes de cargar
            arrayJSON.stream().forEach(obj -> {
                JSONObject jsonObj = (JSONObject) obj;

                // EXTRAYENDO OBJETOS
                int id = Integer.parseInt(jsonObj.get("id").toString());
                String description = jsonObj.get("description").toString();
                String status = jsonObj.get("status").toString();
                LocalDate createdAt = LocalDate.parse(jsonObj.get("createAt").toString());
                LocalDate updateAt = null;

                // VALIDANDO UPDATEAT
                String updateAtStr = jsonObj.get("updateAt").toString();
                if (!updateAtStr.isEmpty()) {
                    updateAt = LocalDate.parse(updateAtStr);
                }

                // CARGANDO LISTA
                Task task = new Task(id, description, status, createdAt, updateAt);
                taskList.add(task);
            });

            // Actualizar el contador estático para evitar IDs duplicados
            if (!taskList.isEmpty()) {
                int maxId = taskList.stream().mapToInt(Task::getId).max().getAsInt();
                Task.count = maxId + 1;
            }

            System.out.println("Tareas cargadas desde el archivo: " + taskList.size() + " tareas");
        } catch (Exception e) {
            System.out.println("Error al cargar tareas: " + e.getMessage());
        }
    }

    public void printList(List<Task> list) {
        list.forEach(task -> {
            String t = "Id: " + task.getId() + "\n"
                    + "Description: " + task.getDescription() + "\n"
                    + "Status: " + task.getStatus() + "\n"
                    + "CreateAt: " + task.getCreatedAt() + "\n"
                    + "UpdateAt: " + task.getUpdateAt() + "\n";
            System.out.println(t);
        });
    }

}
