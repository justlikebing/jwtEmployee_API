//package com.devrezaur.main.controller;
//
//
//import com.devrezaur.main.repository.EmployeeRepository;
//import com.devrezaur.main.service.Employee;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.http.HttpStatus;
//
//import org.springframework.http.ResponseEntity;
//
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//import java.util.Optional;
//
//@RestController
//
//@RequestMapping("/employees")
//
//public class EmployeeController {
//
//    @Autowired
//
//    private EmployeeRepository employeeRepository;
//
//    // Get all employees
//
//    @GetMapping
//
//    public ResponseEntity<List<Employee>> getAllEmployees() {
//
//        List<Employee> employees = employeeRepository.findAll();
//
//        return new ResponseEntity<>(employees, HttpStatus.OK);
//
//    }
//
//    // Get employee by ID
//
//    @GetMapping("/{id}")
//
//    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
//
//        Optional<Employee> employeeOptional = employeeRepository.findById(id);
//
//        return employeeOptional.map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
//
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//
//    }
//
//    // Create a new employee
//
//    @PostMapping
//
//    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
//
//        Employee savedEmployee = employeeRepository.save(employee);
//
//        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
//
//    }
//
//    // Update an existing employee
//
//    @PutMapping("/{id}")
//
//    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
//
//        if (!employeeRepository.existsById(id)) {
//
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//        }
//
//        employee.setId(id);
//
//        Employee updatedEmployee = employeeRepository.save(employee);
//
//        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
//
//    }
//
//    // Delete an employee
//
//    @DeleteMapping("/{id}")
//
//    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
//
//        if (!employeeRepository.existsById(id)) {
//
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//        }
//
//        employeeRepository.deleteById(id);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//    }
//
//}