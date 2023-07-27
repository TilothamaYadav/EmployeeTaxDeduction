package com.employee.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="EmployeeDetails")
public class Employee {

    @Id
    @Column(unique = true)
    private int id;

    @NotBlank
    @NotNull
    @Column(name="firstname")
    private String firstName;

    @NotBlank
    @NotNull
    @Column(name="lastname")
    private String lastName;

    @Email(message = "Invalid mail ")
    @Column(name = "mailid",unique=true)
    private String mail;

//    @NotBlank
    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "phone_numbers", joinColumns = @JoinColumn(name = "id"))
    @Column(name="phonenumber",unique=true)
    private List<String> phonenumbers;


    @NotNull
    @Column(name="dateofjoining")
    @Past
    private LocalDate dateOfJoining;

    @NotNull
    @Min(value = 15000)
    @Column(name="salary")
    private double salary;

}
