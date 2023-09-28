package ru.castroy10.addr.views.admin;

import ru.castroy10.addr.security.SecurityService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import ru.castroy10.addr.views.MainView;

import java.util.List;

@PermitAll
@PageTitle("Admin view")
@Route("admin")
public class AdminView extends Composite<VerticalLayout> {

    private final VerticalLayout verticalLayout = new VerticalLayout();
    private final HorizontalLayout horizontalLayout = new HorizontalLayout();
    private final Grid<String> gridOfAdminPages = new Grid<>(String.class, false);
    private final List<String> listOfAdminPages = List.of("Администрирование департаментов", "Администрирование должностей", "Администрирование сотрудников", "Заведение пользователя");
    private final Button buttonOk = new Button();
    private final Button buttonLogout = new Button();
    private final Button buttonMainPage = new Button();
    private String resultOfChoiceGrid;
    private final SecurityService securityService;

    public AdminView(SecurityService securityService) {
        this.securityService = securityService;
        createScene();

        buttonOk.addClickListener(e -> {
            if (resultOfChoiceGrid != null) redirectToAdminPage(resultOfChoiceGrid);
        });

        buttonMainPage.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(MainView.class));
        });

        buttonLogout.addClickListener(e -> {
            securityService.logout();
        });

        gridOfAdminPages.addItemClickListener(e -> {
            resultOfChoiceGrid = e.getItem();
        });
    }


    private void createScene() {
        getContent().setHeightFull();
        verticalLayout.setHeightFull();
        gridOfAdminPages.setAllRowsVisible(true);
//        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); //вертикальное выравнивание по центру страницы
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER); //горизонтальное выравнивание по центру страницы
        buttonOk.setText("Ok");
        buttonMainPage.setText("Main Page");
        buttonLogout.setText("Logout");
        gridOfAdminPages.addColumn(String::toString).setHeader("Страницы администрирования").setAutoWidth(true);
        gridOfAdminPages.setItems(listOfAdminPages);
        horizontalLayout.add(gridOfAdminPages);
        verticalLayout.add(horizontalLayout, buttonOk, buttonMainPage, buttonLogout);
        getContent().add(verticalLayout);
    }

    private void redirectToAdminPage(String result) {
        switch (result) {
            case "Администрирование департаментов":
                getUI().ifPresent(ui -> ui.navigate(AdminViewDepartment.class));
                break;
            case "Администрирование должностей":
                getUI().ifPresent(ui -> ui.navigate(AdminViewPosition.class));
                break;
            case "Администрирование сотрудников":
                getUI().ifPresent(ui -> ui.navigate(AdminViewEmployee.class));
                break;
            case "Заведение пользователя":
                getUI().ifPresent(ui -> ui.navigate(AdminViewUsr.class));
                break;
            default:
                break;
        }
    }

    
}
