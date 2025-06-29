package com.tasktracker;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    public static int count=1;
    private int id;
    private String description;
    private String status = "to-do";
    private LocalDate  createdAt = LocalDate.now();
    private LocalDate updateAt;

    public Task(String description){
        this.id = count++;
        this.description=description;
    }

}
