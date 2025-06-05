package com.student.student.frontend;

import com.student.student.data.Company;
import com.student.student.frontend.utils.ConfigFrontend;
import com.student.student.request.InternshipDtoForFront;
import com.student.student.responce.student.StudentProjectionAndId;
import com.student.student.service.CompanyService;
import com.student.student.service.InternshipService;
import com.student.student.service.StudentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Добавить практику")
@Route(value = "create", layout = MainLayout.class)
public class CreateView extends VerticalLayout {
    private final InternshipService internshipService;
    private final StudentService studentService;
    private final CompanyService companyService;
    private final ConfigFrontend configFrontend;

    private int duration;
    private String phoneSize;
    private int column;

    public CreateView(InternshipService internshipService,
                      StudentService studentService,
                      CompanyService companyService, ConfigFrontend configFrontend) {
        this.internshipService = internshipService;
        this.studentService = studentService;
        this.companyService = companyService;
        this.configFrontend = configFrontend;
        getConfig();
        addClassName("create-view");
        setSizeFull();

        add(createForm());
    }

    private void getConfig() {
        duration = configFrontend.getDuration().getIntShow();
        phoneSize = configFrontend.getSize().getPhone();
        column = configFrontend.getQuantity().getColumn();
    }

    private Component createForm() {
        FormLayout formLayout = new FormLayout();

        ComboBox<StudentProjectionAndId> studentCombo = new ComboBox<>("Студент");
        studentCombo.setItems(studentService.findAllProjectionForFront());
        studentCombo.setItemLabelGenerator(StudentProjectionAndId::getProjectionAndId);

        ComboBox<Company> companyCombo = new ComboBox<>("Компания");
        companyCombo.setItems(companyService.findAllProjection());
        companyCombo.setItemLabelGenerator(Company::getName);

        DatePicker startDate = new DatePicker("Дата начала");
        DatePicker endDate = new DatePicker("Дата окончания");
        TextField position = new TextField("Должность");
        TextArea comments = new TextArea("Комментарии");

        formLayout.add(
                studentCombo, companyCombo,
                startDate, endDate,
                position, comments
        );
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep(phoneSize, column)
        );

        Button saveButton = new Button("Сохранить");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            if (studentCombo.getValue() == null || companyCombo.getValue() == null) {
                Notification.show("Заполните все обязательные поля", duration, Notification.Position.MIDDLE);
                return;
            }

            InternshipDtoForFront dto = new InternshipDtoForFront(
                    studentCombo.getValue().getUuid(),
                    companyCombo.getValue().getId(),
                    startDate.getValue(),
                    endDate.getValue(),
                    position.getValue(),
                    comments.getValue()
            );

            internshipService.createInternShip(dto);
            Notification.show("Практика создана");
            getUI().ifPresent(ui -> ui.navigate(ListView.class));
        });

        Button cancelButton = new Button("Отмена");
        cancelButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ListView.class)));

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);

        VerticalLayout layout = new VerticalLayout(formLayout, buttons);
        layout.setAlignItems(Alignment.CENTER);

        Shortcuts.addShortcutListener(layout,
                saveButton::click,
                Key.ENTER
        );

        return layout;
    }
}