package ru.castroy10.addr.views.admin;

import ru.castroy10.addr.services.UsrService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Admin Users")
@Route("adminusr")
public class AdminViewUsr extends Composite<VerticalLayout> {

    private final VerticalLayout verticalLayout = new VerticalLayout();
    private final TextField loginField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField confirmPasswordField = new PasswordField();
    private final Button buttonOk = new Button();
    private final Button buttonCancel = new Button();
    private final UsrService usrService;

    public AdminViewUsr(UsrService usrService) {
        this.usrService = usrService;
        createScene();

        buttonOk.addClickListener(e -> {
            createUser();
        });

        buttonCancel.addClickListener(e->{
            History history = UI.getCurrent().getPage().getHistory();
            history.back();
        });
    }

    private void createUser() {
        if (usrService.findUserByUsername(loginField.getValue()))
            Notification.show("Такой пользователь уже есть в базе данных");
        else if (!passwordField.getValue().equals(confirmPasswordField.getValue()))
            Notification.show("Пароли не совпадают");
        else if (passwordField.getValue().isEmpty()|| loginField.getValue().isEmpty())
            Notification.show("Поле не может быть пустым");
        else {
            //usrService.createUser(textField.getValue(), passwordField.getValue()); //запись пользователя в базу данных
            Notification.show("Пользователь создан");
            loginField.setValue("");
            passwordField.setValue("");
            confirmPasswordField.setValue("");
        }
    }

    public void createScene() {
        getContent().setHeightFull();
        verticalLayout.setHeightFull();
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); //вертикальное выравнивание по центру страницы
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER); //горизонтальное выравнивание по центру страницы
        loginField.setLabel("New user name");
        passwordField.setLabel("Password field");
        confirmPasswordField.setLabel("Confirm password");
        buttonOk.setText("Ok");
        buttonCancel.setText("Cancel");
        getContent().add(verticalLayout);
        verticalLayout.add(loginField, passwordField, confirmPasswordField, buttonOk, buttonCancel);
    }
}
