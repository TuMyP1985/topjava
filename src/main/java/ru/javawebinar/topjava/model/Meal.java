package ru.javawebinar.topjava.model;

import lombok.*;

//import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

//@Entity
//@Table(name = "Meals")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Meal {
//    @Id
//    @Column(name="id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @Column(name = "dateTime")
    private LocalDateTime dateTime;
//    @Column (name = "description")
    private String description;
//    @Column (name = "calories")
    private int calories;
//    private boolean excess;

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }


}
