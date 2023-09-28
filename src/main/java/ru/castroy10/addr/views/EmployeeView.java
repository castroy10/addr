package ru.castroy10.addr.views;

import ru.castroy10.addr.model.Employee;
import ru.castroy10.addr.services.EmployeeService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.LinkedHashMap;
import java.util.Map;

@AnonymousAllowed
@Route("employee")
public class EmployeeView extends VerticalLayout implements HasUrlParameter<Integer> {
    final private EmployeeService employeeService;
    final Grid<Map.Entry<String, String>> gridVertical = new Grid<>();
    Employee selectedEmployee;

    public EmployeeView(EmployeeService employeeService) {
        this.employeeService = employeeService;
        createScene();

        gridVertical.addItemClickListener(e -> { //открываем почтовый клиент при нажатии на строку email
            Map.Entry<String, String> map = e.getItem();
            if (map.getKey().equals("Email"))
                UI.getCurrent().getPage().open("mailto:" + selectedEmployee.getLastName() + " " + selectedEmployee.getFirstName() + " " + selectedEmployee.getMiddleName() + " " + selectedEmployee.getEmail());
        });

        add(new Button("Подразделение", e -> {
            getUI().ifPresent(ui -> ui.navigate(DepartmentView.class, selectedEmployee.getDepartment().getId()));
        }));

        add(new Button("Начальная страница", e -> {
            getUI().ifPresent(ui -> ui.navigate(MainView.class));
        }));
    }

    private void createScene() {
        setAlignItems(Alignment.CENTER);
        add(new H1("Карточка сотрудника"));
        gridVertical.setAllRowsVisible(true);
        gridVertical.setWidth("50%");
        gridVertical.addColumn(Map.Entry<String, String>::getKey);
        gridVertical.addColumn(Map.Entry<String, String>::getValue);
        gridVertical.getElement().getStyle().set("margin-left", "auto");
        gridVertical.getElement().getStyle().set("margin-right", "auto");
        add(gridVertical);
    }

    public void addGridEmployee(int id) {
        selectedEmployee = employeeService.findById(id);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("Фамилия", selectedEmployee.getLastName());
        map.put("Имя", selectedEmployee.getFirstName());
        map.put("Отчество", selectedEmployee.getMiddleName());
        map.put("Телефон", selectedEmployee.getPhone());
        map.put("Мобильный телефон", selectedEmployee.getMobilePhone());
        map.put("Email", selectedEmployee.getEmail());
        map.put("Должность", selectedEmployee.getPosition().getPosition_name());
        map.put("Подразделение", selectedEmployee.getDepartment().getDepartment_name());
        gridVertical.setItems(map.entrySet());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer employee_id) {
        addGridEmployee(employee_id);
    }
}
