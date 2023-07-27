package com.employee.project.service;

import com.employee.project.entity.Employee;
import com.employee.project.entity.EmployeeTaxDTO;
import com.employee.project.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    public EmployeeRepo employeeRepo;
    private Employee employee;


//    @Autowired
//    public EmployeeService employee;

    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public List<Employee> getEmployees() {
        return employeeRepo.findAll();
    }

    public Employee addEmployees() {
        return employeeRepo.save(employee);
    }


    public ResponseEntity<?> addEmployee(Employee employee) {
        // Check for blank fields
        if (employee.getMail().isBlank() || employee.getFirstName().isBlank() || employee.getLastName().isBlank() || employee.getPhonenumbers().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide all the required details...");
        } else if (employee.getSalary() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Give the salary in positive numbers...");
        } else {
            // Check for duplicate name, and email
            if (employeeRepo.existsByFirstName(employee.getFirstName())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Employee with the given name already exists...");
            }
            if (employeeRepo.existsByMail(employee.getMail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Employee with the given email already exists...");
            }
            // Save the employee if no duplicates are found
            employeeRepo.save(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body("Employee added successfully...");
        }
    }

    // calculating tax
    double totalSalary = 0.0;
    double salaryPerDay = 0.0;
    double taxAmount = 0.0;
    double cessAmount = 0.0;

    double salary = 0.0;

    public ResponseEntity<?> taxCalculation(int id) {
        Employee employee = employeeRepo.findById(id).get();
        salary = employee.getSalary();
        //   totalSalary = salary * 12;
        salaryPerDay = salary / 30;
        LocalDate doj = employee.getDateOfJoining();
        totalSalary = calculateFinancialYearSalary(doj, salaryPerDay);
        if (totalSalary <= 250000) {
            taxAmount = 0.0;
        } else if (totalSalary > 250000 && totalSalary <= 500000) {
            taxAmount = (totalSalary - 250000) * 0.05;
            cessAmount = (totalSalary - 250000) * 0.02;
        } else if (totalSalary > 500000 && totalSalary <= 1000000) {
            taxAmount = 250000 * 0.05 + (totalSalary - 500000) * 0.10;
            cessAmount = (totalSalary - 250000) * 0.02;
        } else {
            taxAmount = 250000 * 0.05 + 500000 * 0.10 + (totalSalary - 1000000) * 0.20;
            cessAmount = (totalSalary - 250000) * 0.02;
        }
        EmployeeTaxDTO employeeTaxDTO = new EmployeeTaxDTO();
        employeeTaxDTO.setId(employee.getId());
        employeeTaxDTO.setFirstname(employee.getFirstName());
        employeeTaxDTO.setLastname(employee.getLastName());
      //  employeeTaxDTO.setDateofjoining();
        employeeTaxDTO.setYearlySalary(totalSalary);
        employeeTaxDTO.setTaxAmount(taxAmount);
        employeeTaxDTO.setCessAmount(cessAmount);
        return new ResponseEntity<>(employeeTaxDTO, HttpStatus.OK);
    }

    public static double calculateFinancialYearSalary(LocalDate doj, double salaryPerDay) {
        LocalDate financialDate = LocalDate.of(2023, 03, 31);
        long numberOfDays = 0;
        double yearlySalary = 0.0;
        LocalDate dateOfJoining = doj;
        numberOfDays = ChronoUnit.DAYS.between(dateOfJoining,financialDate);
        long days = doj.getDayOfMonth();
        numberOfDays -= days;
        return numberOfDays*salaryPerDay;
        }

    }














