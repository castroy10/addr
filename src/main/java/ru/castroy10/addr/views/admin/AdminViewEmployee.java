package ru.castroy10.addr.views.admin;

import ru.castroy10.addr.model.Department;
import ru.castroy10.addr.model.Employee;
import ru.castroy10.addr.model.Position;
import ru.castroy10.addr.services.DepartmentService;
import ru.castroy10.addr.services.EmployeeService;
import ru.castroy10.addr.services.PositionService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Admin Employee")
@Route("adminemployee")
public class AdminViewEmployee extends Composite<VerticalLayout> {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PositionService positionService;

    private final HorizontalLayout horizontalLayout1 = new HorizontalLayout();
    private final HorizontalLayout horizontalLayout2 = new HorizontalLayout();
    private final VerticalLayout verticalLayout1 = new VerticalLayout();
    private final VerticalLayout verticalLayout2 = new VerticalLayout();
    private final Grid<Employee> basicGrid = new Grid<>(Employee.class, false);
    private final Button buttonCreate = new Button();
    private List<Department> departmentList = new ArrayList<>();
    private List<Position> positionList = new ArrayList<>();
    private List<Employee> employeeList = new ArrayList<>();


    public AdminViewEmployee(EmployeeService employeeService, DepartmentService departmentService, PositionService positionService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.positionService = positionService;

        createScene();
        addGridListOfEmployee();

        basicGrid.addItemClickListener(e -> {
            Employee employee = e.getItem();
            openEditDialog(employee);
        });
    }

    private void createScene() {
        Thread employeeThread = new Thread(() -> {
            employeeList.addAll(employeeService.findAll());
        });
        employeeThread.start();
        Thread departmentTread = new Thread(() -> {
            departmentList.addAll(departmentService.findAll());
        });
        departmentTread.start();
        Thread positionTread = new Thread(() -> {
            positionList.addAll(positionService.findAll());
        });
        positionTread.start();
        try {
            employeeThread.join();
            departmentTread.join();
            positionTread.join();
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
//        добавляем в горизонтальный лэйаут2 два вертикальных лэйаут. В первый вертикальный добавляем кнопки, во второй грид
        horizontalLayout2.add(verticalLayout1);
        verticalLayout1.add(buttonCreate);
        horizontalLayout2.add(verticalLayout2);
        verticalLayout2.add(basicGrid);
        basicGrid.setAllRowsVisible(true);
//        basicGrid.setPageSize(30);
        basicGrid.addColumn(employee -> employee.getLastName()).setHeader("Фамилия").setAutoWidth(true).setSortable(true);
        basicGrid.addColumn(employee -> employee.getFirstName()).setHeader("Имя").setAutoWidth(true).setSortable(true);
        basicGrid.addColumn(employee -> employee.getMiddleName()).setHeader("Отчество").setAutoWidth(true).setSortable(true);
        basicGrid.addColumn(employee -> departmentService.getDepartmentById(departmentList, employee.getDepartment().getId()).getDepartment_name()).setHeader("Подразделение").setAutoWidth(true).setSortable(true);
        basicGrid.addColumn(employee -> positionService.getPositionById(positionList, employee.getPosition().getId()).getPosition_name()).setHeader("Вышестоящее подразделение").setAutoWidth(true).setSortable(true);
    }

    private void openEditDialog(Employee employee) {
        Dialog dialog = new Dialog();
        dialog.setWidth("40%");

        Select<Employee> employeeSelect = new Select<>();
        employeeSelect.setItems(employeeList);
        employeeSelect.setValue(employee);

        Select<Position> positionSelect = new Select<>(); //выводим должность
        positionSelect.setItems(positionList);
        positionSelect.setValue(positionService.getPositionById(positionList,employee.getPosition().getId()));

        Select<Department> departmentSelect = new Select<>(); //выводим подразделение
        departmentSelect.setItems(departmentList);
        departmentSelect.setValue(departmentService.getDepartmentById(departmentList, employee.getDepartment().getId()));

        Button saveButton = new Button("Save", event -> {
            employee.setPosition(positionSelect.getValue());
            employee.setDepartment(departmentSelect.getValue());
            employeeService.save(employee);
            dialog.close();
            addGridListOfEmployee();
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        VerticalLayout verticalLayout = createDialogLayout(employeeSelect, departmentSelect, positionSelect);
        dialog.add(verticalLayout);
        dialog.add(cancelButton, saveButton);
        dialog.open();
    }

    private void addGridListOfEmployee() {
        basicGrid.setItems(employeeList);
    }

    private VerticalLayout createDialogLayout(Select<Employee> employeeSelect, Select<Department> departmentSelect, Select<Position> positionSelect) {
        employeeSelect.setItemLabelGenerator(e -> e.getLastName() + " " + e.getFirstName() + " " + e.getMiddleName());
        positionSelect.setItemLabelGenerator(p -> p.getPosition_name());
        departmentSelect.setItemLabelGenerator(d -> d.getDepartment_name());
        VerticalLayout verticalLayout = new VerticalLayout(employeeSelect, departmentSelect, positionSelect);
        employeeSelect.setLabel("Сотрудник");
        employeeSelect.setWidthFull();
        positionSelect.setLabel("Должность");
        positionSelect.setWidthFull();
        departmentSelect.setLabel("Подразделение");
        departmentSelect.setWidthFull();
        verticalLayout.setWidthFull();
        return verticalLayout;
    }
}
