package ru.castroy10.addr.views.admin;

import ru.castroy10.addr.model.Department;
import ru.castroy10.addr.model.Employee;
import ru.castroy10.addr.services.DepartmentService;
import ru.castroy10.addr.services.EmployeeService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Admin Department")
@Route("admindepartment")
public class AdminViewDepartment extends Composite<VerticalLayout> {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    private final HorizontalLayout horizontalLayout1 = new HorizontalLayout();
    private final HorizontalLayout horizontalLayout2 = new HorizontalLayout();
    private final VerticalLayout verticalLayout1 = new VerticalLayout();
    private final VerticalLayout verticalLayout2 = new VerticalLayout();
    private final Grid<Department> basicGrid = new Grid<>(Department.class, false);
    private final Button buttonCreate = new Button();
    private final List<Department> departmentList = new ArrayList<>();
    private final List<Employee> employeeList = new ArrayList<>();


    public AdminViewDepartment(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;

        createScene();
        addGridListOfDepartment();

        buttonCreate.addClickListener(e -> {
            openCreateDialog();
        });

        basicGrid.addItemClickListener(e -> {
            Department department = e.getItem();
            openEditDialog(department);
        });
    }

    private void createScene() {
        Thread employeeThread = new Thread(() -> {
            employeeList.addAll(employeeService.findAll());
        });
        Thread departmentTread = new Thread(() -> {
            departmentList.addAll(departmentService.findAll());
        });
        employeeThread.start();
        departmentTread.start();
        try {
            employeeThread.join();
            departmentTread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getContent().setHeightFull();
        getContent().setWidthFull();
        horizontalLayout1.setWidthFull();
        horizontalLayout2.setWidthFull();
        verticalLayout1.setHeightFull();
        verticalLayout1.setWidth(null);
        verticalLayout2.setHeightFull();
        verticalLayout2.setWidth(null);
        getContent().setFlexGrow(1.0, horizontalLayout2);
        horizontalLayout2.setFlexGrow(1.0, verticalLayout2);

        buttonCreate.setText("Создание");
        getContent().add(horizontalLayout1);
        getContent().add(horizontalLayout2);
        horizontalLayout1.add(new H1("Департамент. Создание/редактирование"));
        horizontalLayout2.add(verticalLayout1);
        verticalLayout1.add(buttonCreate);
        horizontalLayout2.add(verticalLayout2);
        verticalLayout2.add(basicGrid);
        basicGrid.setAllRowsVisible(true);
        basicGrid.addColumn(department -> department.getId()).setHeader("id").setAutoWidth(true).setSortable(true);
        basicGrid.addColumn(department -> department.getDepartment_name()).setHeader("Наименование").setAutoWidth(true).setSortable(true);
        basicGrid.addColumn(department -> departmentService.getDepartmentName(departmentList, department.getParent_department_id())).setHeader("Вышестоящее подразделение").setAutoWidth(true).setSortable(true);
        basicGrid.addColumn(department -> employeeService.getEmployeeLastName(employeeList,department.getChief_employee_id())).setHeader("Начальник").setAutoWidth(true).setSortable(true);
    }

    private void addGridListOfDepartment() {
        basicGrid.setItems(departmentService.addGridDepartmentAdminDepartmentView());
    }

    private void openCreateDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("40%");
        TextField department_name = new TextField("Подразделение");

        Select<Department> departmentSelect = new Select<>();
        departmentSelect.setItems(departmentList);
        departmentSelect.setPlaceholder("Вышестоящее подразделение");

        Select<Employee> employeeSelect = new Select<>();
        employeeSelect.setItems(employeeService.getFreeEmployee(employeeList)); //выводим только сотрудников не имеющих ни департамента ни должности
        employeeSelect.setPlaceholder("Руководитель");

        Button saveButton = new Button("Save", event -> {
            Department newDepartment = new Department(department_name.getValue(), departmentSelect.getValue().getId(), employeeSelect.getValue().getId());
            departmentService.save(newDepartment);
            Employee employee = employeeSelect.getValue();
            employee.setDepartment(newDepartment);
            employeeService.save(employee);
            dialog.close();
            departmentList.add(newDepartment);
            employeeList.add(employee);
            addGridListOfDepartment();
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        VerticalLayout verticalLayout = createDialogLayout(department_name, departmentSelect, employeeSelect);
        dialog.add(verticalLayout);
        dialog.add(cancelButton, saveButton);
        dialog.open();
    }

    private void openEditDialog(Department department) {
        Dialog dialog = new Dialog();
        dialog.setWidth("40%");

        TextField department_name = new TextField("Подразделение");
        department_name.setValue(department.getDepartment_name());

        Select<Department> departmentSelect = new Select<>(); //выводим вышестоящее подразделение
        departmentSelect.setItems(departmentList);
        departmentSelect.setValue(departmentService.getDepartmentById(departmentList, department.getParent_department_id()));

        Select<Employee> employeeSelect = new Select<>(); //выводим руководителя
        employeeSelect.setItems(employeeList);
        employeeSelect.setValue(employeeService.getEmployeeById(employeeList,department.getChief_employee_id()));

        Button saveButton = new Button("Save", event -> {
            department.setDepartment_name(department_name.getValue());
            department.setParent_department_id(departmentSelect.getValue().getId());
            department.setChief_employee_id(employeeSelect.getValue().getId());
            departmentService.save(department);
            Employee employee = employeeSelect.getValue();
            employee.setDepartment(department);
            employeeService.save(employee);
            dialog.close();
            addGridListOfDepartment();
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        VerticalLayout verticalLayout = createDialogLayout(department_name, departmentSelect, employeeSelect);
        dialog.add(verticalLayout);
        dialog.add(cancelButton, saveButton);
        dialog.open();
    }

    private VerticalLayout createDialogLayout(TextField department_name, Select<Department> departmentSelect, Select<Employee> employeeSelect) {
        employeeSelect.setItemLabelGenerator(e -> e.getLastName() + " " + e.getFirstName() + " " + e.getMiddleName());
        departmentSelect.setItemLabelGenerator(d -> d.getDepartment_name());
        VerticalLayout verticalLayout = new VerticalLayout(department_name, departmentSelect, employeeSelect);
        department_name.setWidthFull();
        departmentSelect.setLabel("Вышестоящее подразделение");
        departmentSelect.setWidthFull();
        employeeSelect.setLabel("Руководитель");
        employeeSelect.setWidthFull();
        verticalLayout.setWidthFull();
        return verticalLayout;
    }
}
