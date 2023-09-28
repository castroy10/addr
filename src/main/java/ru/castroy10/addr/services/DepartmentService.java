package ru.castroy10.addr.services;

import ru.castroy10.addr.model.Department;
import ru.castroy10.addr.model.Employee;
import ru.castroy10.addr.repo.DepartmentRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepo departmentRepo;
    private final EmployeeService employeeService;


    public DepartmentService(DepartmentRepo departmentRepo, EmployeeService employeeService) {
        this.departmentRepo = departmentRepo;
        this.employeeService = employeeService;
    }

    public List<Department> findAll() {
        return departmentRepo.findAll();
    }

    public List<Department> addGridDepartmentMainView() {
        return departmentRepo.findAll()
                .stream()
                .filter(department -> department.getId() != 1)
                .filter(department -> department.getParent_department_id() == 2)
                .toList();
    }

    public List<Employee> addGridDepartmentDepartmentView(int id) {
        List<Employee> list = employeeService.findByDepartmentId(id);
        List<Employee> sortedList = list.stream()
                .sorted(Comparator.comparing(Employee::getLastName)).toList();
        List<Employee> filteredList = sortedList.stream()
                .filter(employee -> employee.getDepartment().getChief_employee_id() == employee.getId()).toList();
        List<Employee> finalList = new ArrayList<>();
        if (filteredList.size() != 0) finalList.add(filteredList.get(0));
        finalList.addAll(sortedList.stream()
                .filter(employee -> employee.getDepartment().getChief_employee_id() != employee.getId()).toList());
        return finalList;
    }

    public List<Department> addGridSubDepartmentsDepartmentView(int id) {
        return departmentRepo.findAll().stream()
                .filter(d -> (d.getParent_department_id() == id))
                .filter(d -> d.getId() != 1)
                .filter(d -> d.getId() != 2)
                .toList();
    }

    public List<Department> addGridDepartmentAdminDepartmentView() {
        return departmentRepo.findAll().stream()
                .sorted(Comparator.comparing(Department::getId))
                .toList();
    }

    public Department save(Department department) {
        return departmentRepo.save(department);
    }

    public String getDepartmentName(List<Department> list, int id) {
        return list.stream()
                .filter(d -> d.getId() == id)
                .map(Department::getDepartment_name)
                .findAny().orElse("");
    }

    public Department getDepartmentById(List<Department> list, int id) {
        return list.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(new Department());
    }
}

