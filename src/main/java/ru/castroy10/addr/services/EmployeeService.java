package ru.castroy10.addr.services;

import ru.castroy10.addr.model.Employee;
import ru.castroy10.addr.repo.EmployeeRepo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;

    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    public Employee findById(int id) {
        return employeeRepo.findById(id).orElse(new Employee());
    }

    public List<Employee> findByName(String name) {
        return employeeRepo.findByName(name);
    }

    public List<Employee> findByDepartmentId(int id) {
        return employeeRepo.findByDepartmentId(id);
    }

    public List<Employee> addGridEmployeeMainView() {
        return employeeRepo.findAll()
                .stream()
                .filter(employee -> employee.getPosition().getParent_position_id() == 2 && employee.getPosition().getDepartment_id() != 1)
                .sorted(Comparator.comparing(employee -> employee.getPosition().getDepartment_id()))
                .toList();
    }

    public Employee save(Employee employee) {
        return employeeRepo.save(employee);
    }

    public String getEmployeeLastName(List<Employee> list, int id) {
        return list.stream()
                .filter(e -> e.getId() == id)
                .map(Employee::getLastName)
                .findAny().orElse("");
    }

    public List<Employee> getFreeEmployee(List<Employee> list) {
        return list.stream()
                .filter(e -> e.getDepartment().getId() == 1 && e.getPosition().getId() == 1)
                .sorted(Comparator.comparing(Employee::getLastName))
                .toList();
    }

    public Employee getEmployeeById(List<Employee> list, int id){
        return list.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(new Employee());
    }

    public Employee getEmployeeByPositionId(List<Employee> list, int id){
        return list.stream()
                .filter(e -> e.getPosition().getId() == id)
                .findFirst()
                .orElse(new Employee());
    }
}
