package com.employee.project.controller;

import com.employee.project.entity.Employee;
import com.employee.project.entity.EmployeeTaxDTO;
import com.employee.project.repo.EmployeeRepo;
import com.employee.project.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {

    @Autowired
    public EmployeeService employeeService;

    @Autowired
    public EmployeeRepo employeeRepo;
//    @PostMapping("/employee")
//    public String addEmployee(@RequestBody Employee employee){
//       employeeRepo.save(employee);
//       return " employee details added";
//    }

    @GetMapping("/employee")
    public List<Employee> getEmployee() {
        return employeeService.getEmployees();
    }

    @PostMapping("/employee")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee employee) {
        employeeService.addEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee added successfully...");
    }

    @GetMapping("/tax-deduction/{id}")
    public ResponseEntity<?> getTaxDeductions(@PathVariable int id ) {
        return employeeService.taxCalculation(id);

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errors = new ArrayList<>();
        for(var e : bindingResult.getAllErrors()){
            errors.add("message: " + e.getDefaultMessage() + "field: " + ((FieldError) e).getField());
        }
        return ResponseEntity.badRequest().body(getErrorsMap(errors));
    }
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }



}
