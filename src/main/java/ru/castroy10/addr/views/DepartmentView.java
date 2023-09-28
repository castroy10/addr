package ru.castroy10.addr.views;

import ru.castroy10.addr.model.Department;
import ru.castroy10.addr.model.Employee;
import ru.castroy10.addr.services.DepartmentService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.List;

@AnonymousAllowed
@Route("department")

public class DepartmentView extends VerticalLayout implements HasUrlParameter<Integer> {
    private final DepartmentService departmentService;
    private final Grid<Employee> grid = new Grid<>(Employee.class, false);
    private final Grid<Department> departmentGrid = new Grid<>(Department.class, false);
    private final VerticalLayout verticalLayout = new VerticalLayout(grid, departmentGrid);
    private List<Employee> list;
    private List<Department> departmentList;
    Text department = new Text("");

    public DepartmentView(DepartmentService departmentService) {
        this.departmentService = departmentService;
        createScene();

        grid.addItemClickListener(e -> {
            Employee employee = e.getItem();
            int findUserid = employee.getId();
            getUI().ifPresent(ui -> ui.navigate(EmployeeView.class, findUserid));
        });

        departmentGrid.addItemClickListener(e -> {
            Department department = e.getItem();
            getUI().ifPresent(ui -> ui.navigate(DepartmentView.class, department.getId()));
        });

        add(new Button("Вышестоящее подразделение", e -> {
            addGridDepartment(list.get(0).getDepartment().getParent_department_id());
            addGridSubDepartments(list.get(0).getDepartment().getId());
        }));

        add(new Button("Начальная страница", e -> {
            getUI().ifPresent(ui -> ui.navigate(MainView.class));
        }));
    }

    private void createScene() {
        setAlignItems(Alignment.CENTER);
        add(new H1("Карточка подразделения"));
        add(new H1(department));
        grid.setAllRowsVisible(true);
        grid.setWidthFull();
        grid.addColumn(employee -> employee.getLastName()).setHeader("Фамилия").setAutoWidth(true).setSortable(true);
        grid.addColumn(employee -> employee.getFirstName()).setHeader("Имя").setAutoWidth(true).setSortable(true);
        grid.addColumn(employee -> employee.getMiddleName()).setHeader("Отчество").setAutoWidth(true).setSortable(true);
        grid.addColumn(employee -> employee.getPhone()).setHeader("Телефон").setAutoWidth(true).setSortable(true);
        grid.addColumn(employee -> employee.getEmail()).setHeader("Email").setAutoWidth(true).setSortable(true);
        grid.addColumn(employee -> employee.getPosition().getPosition_name()).setHeader("Должность").setAutoWidth(true).setSortable(true);
        departmentGrid.setAllRowsVisible(true);
        departmentGrid.setWidth("40%");
        departmentGrid.addColumn(department -> department.getDepartment_name()).setHeader("Подразделения в составе:").setAutoWidth(true);
        verticalLayout.setWidth("80%");
        add(verticalLayout);
    }

    public void addGridDepartment(Integer id) { //метод получает список сотрудников департамента, на первое место ставит начальника, остальных сортирует по алфавиту
        list = departmentService.addGridDepartmentDepartmentView(id);
        grid.setItems(list);
        grid.setPageSize(grid.getPageSize()); //для того, что бы предотвратить lazy load для grid
        grid.recalculateColumnWidths();
        department.setText(list.get(0).getDepartment().getDepartment_name());
    }

    public void addGridSubDepartments(Integer id) { //метод получает список подчиненных департаментов, если их нет, скрывает departmentGrid
        departmentList = departmentService.addGridSubDepartmentsDepartmentView(id);
        if (departmentList.size() == 0) departmentGrid.setVisible(false);
        else departmentGrid.setVisible(true);
        departmentGrid.setItems(departmentList);

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer department_id) {
        addGridDepartment(department_id);
        addGridSubDepartments(department_id);
    }
}
