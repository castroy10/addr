package ru.castroy10.addr.views;

import ru.castroy10.addr.model.Department;
import ru.castroy10.addr.model.Employee;
import ru.castroy10.addr.services.DepartmentService;
import ru.castroy10.addr.services.EmployeeService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.List;

@Route("")
@AnonymousAllowed
public class MainView extends VerticalLayout {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final TextField filter = new TextField();
    private final HorizontalLayout layout = new HorizontalLayout(filter);
    List<Employee> list;
    List<Department> listDepartment;
    final Grid<Employee> grid = new Grid<>(Employee.class, false);
    final Grid<Department> gridDepartment = new Grid<>(Department.class, false);
    private final VerticalLayout mainLayout = new VerticalLayout(grid, gridDepartment);

    public MainView(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;

        createScene();

        grid.addItemClickListener(e -> {
            Employee employee = e.getItem();
            int findUserid = employee.getId();
            getUI().ifPresent(ui -> ui.navigate(EmployeeView.class, findUserid));
        });

        gridDepartment.addItemClickListener(e -> {
            Department department = e.getItem();
            getUI().ifPresent(ui -> ui.navigate(DepartmentView.class, department.getId()));
        });

        filter.addValueChangeListener(field -> {
            if (field.getValue().length() == 0) addGridEmployee();
            else if (field.getValue().length() > 2) {
                grid.setItems(employeeService.findByName(field.getValue().toLowerCase()));
                grid.setPageSize(grid.getPageSize()); //для того, что бы предотвратить lazy load для grid
                grid.recalculateColumnWidths();
            }
        });
    }

    public void addGridEmployee() {
        list = employeeService.addGridEmployeeMainView();
        grid.setItems(list);
    }

    public void addGridDepartment() {
        listDepartment = departmentService.addGridDepartmentMainView();
        gridDepartment.setItems(listDepartment);
    }

    public void createScene() {
        Thread employeeThread = new Thread(() -> addGridEmployee());
        employeeThread.start();
        Thread departmentTread = new Thread(() -> addGridDepartment());
        departmentTread.start();
        try {
            employeeThread.join();
            departmentTread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setAlignItems(Alignment.CENTER);
        grid.setAllRowsVisible(true);
        grid.addColumn(employee -> employee.getLastName()).setHeader("Фамилия").setAutoWidth(true).setSortable(true);
        grid.addColumn(employee -> employee.getFirstName()).setHeader("Имя").setAutoWidth(true).setSortable(true);
        grid.addColumn(employee -> employee.getMiddleName()).setHeader("Отчество").setAutoWidth(true).setSortable(true);
        grid.addColumn(employee -> employee.getDepartment().getDepartment_name()).setHeader("Подразделение").setAutoWidth(true).setSortable(true);
        grid.addColumn(employee -> employee.getPosition().getPosition_name()).setHeader("Должность").setAutoWidth(true).setSortable(true);

        filter.setPlaceholder("Type to filter");
        filter.setValueChangeMode(ValueChangeMode.EAGER);

        gridDepartment.setAllRowsVisible(true);
        gridDepartment.setWidth("40%");
        gridDepartment.addColumn(department -> department.getDepartment_name()).setHeader("Подразделения:").setAutoWidth(true);

        mainLayout.setWidth("80%");
        add(new H1("Телефонный справочник"));
        add(layout);
        add(mainLayout);
    }
}

