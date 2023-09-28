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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@PageTitle("Admin Position")
@Route("adminposition")
public class AdminViewPosition extends Composite<VerticalLayout> {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PositionService positionService;
    private final HorizontalLayout horizontalLayout1 = new HorizontalLayout();
    private final HorizontalLayout horizontalLayout2 = new HorizontalLayout();
    private final VerticalLayout verticalLayout1 = new VerticalLayout();
    private final VerticalLayout verticalLayout2 = new VerticalLayout();
    private final Grid<Position> basicGrid = new Grid<>(Position.class, false);
    private final Button buttonCreate = new Button("Создание");
    List<Employee> employeeList = new ArrayList<>();
    List<Department> departmentList = new ArrayList<>();
    List<Position> positionList = new ArrayList<>();

    public AdminViewPosition(EmployeeService employeeService, DepartmentService departmentService, PositionService positionService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.positionService = positionService;

        createScene();
        addGridListOfPosition();

        buttonCreate.addClickListener(e -> {
            openCreateDialog();
        });

        basicGrid.addItemClickListener(e -> {
            Position position = e.getItem();
            openEditDialog(position);
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
        horizontalLayout1.add(new H1("Должность. Создание/редактирование"));
        horizontalLayout2.add(verticalLayout1);
        verticalLayout1.add(buttonCreate);
        horizontalLayout2.add(verticalLayout2);
        verticalLayout2.add(basicGrid);
        basicGrid.setAllRowsVisible(true);
        basicGrid.addColumn(position -> position.getId()).setHeader("id").setAutoWidth(true).setSortable(true);
        basicGrid.addColumn(position -> position.getPosition_name()).setHeader("Наименование").setAutoWidth(true).setSortable(true);
        basicGrid.addColumn(position -> positionService.getPositionName(positionList, position.getParent_position_id())).setHeader("Вышестоящая должность").setAutoWidth(true).setSortable(true);
        basicGrid.addColumn(position -> departmentService.getDepartmentName(departmentList, position.getDepartment_id())).setHeader("Подразделение").setAutoWidth(true).setSortable(true);

    }

    private void addGridListOfPosition() {
        basicGrid.setItems(positionList);
    }

    private void openCreateDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("40%");

        TextField position_name = new TextField("Должность");

        Select<Position> positionSelect = new Select<>();
        positionSelect.setItems(positionList);
        positionSelect.setValue(positionService.getPositionById(positionList, 1));

        Select<Department> departmentSelect = new Select<>();
        departmentSelect.setItems(departmentList);
        departmentSelect.setValue(departmentService.getDepartmentById(departmentList, 1));

        Button saveButton = new Button("Save", event -> {
            int chief_employee_id = 0;
            chief_employee_id = (employeeService.getEmployeeByPositionId(employeeList, positionSelect.getValue().getId())).getId(); //получаем id руководителя на вышестоящей должности
            Position position = new Position(position_name.getValue(), positionSelect.getValue().getId(), departmentSelect.getValue().getId(), chief_employee_id); //создаем должность с полями из формы
            positionService.save(position);
            positionList.add(position);
            dialog.close();
            addGridListOfPosition();
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        VerticalLayout verticalLayout = createDialogLayout(position_name, positionSelect, departmentSelect);
        dialog.add(verticalLayout);
        dialog.add(cancelButton, saveButton);
        dialog.open();
    }

    private void openEditDialog(Position position) {
        Dialog dialog = new Dialog();
        dialog.setWidth("40%");

        TextField position_name = new TextField("Должность");
        position_name.setValue(position.getPosition_name());

        Select<Position> positionSelect = new Select<>(); //выводим вышестоящую должность
        positionSelect.setItems(positionList);
        positionSelect.setValue(positionService.getPositionById(positionList, position.getParent_position_id()));

        Select<Department> departmentSelect = new Select<>(); //выводим подразделение
        departmentSelect.setItems(departmentList);
        departmentSelect.setValue(departmentService.getDepartmentById(departmentList, position.getDepartment_id()));

        Button saveButton = new Button("Save", event -> {
            position.setPosition_name(position_name.getValue());
            position.setParent_position_id(positionSelect.getValue().getId());
            position.setDepartment_id(departmentSelect.getValue().getId());
            positionService.save(position);
            dialog.close();
            addGridListOfPosition();
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        VerticalLayout verticalLayout = createDialogLayout(position_name, positionSelect, departmentSelect);
        dialog.add(verticalLayout);
        dialog.add(cancelButton, saveButton);
        dialog.open();
    }

    private VerticalLayout createDialogLayout(TextField position_name, Select<Position> positionSelect, Select<Department> departmentSelect) {
        positionSelect.setItemLabelGenerator(p -> p.getPosition_name());
        departmentSelect.setItemLabelGenerator(d -> d.getDepartment_name());
        VerticalLayout verticalLayout = new VerticalLayout(position_name, positionSelect, departmentSelect);
        position_name.setWidthFull();
        positionSelect.setLabel("Вышестоящая должность");
        positionSelect.setWidthFull();
        departmentSelect.setLabel("Подразделение");
        departmentSelect.setWidthFull();
        verticalLayout.setWidthFull();
        return verticalLayout;
    }
}
