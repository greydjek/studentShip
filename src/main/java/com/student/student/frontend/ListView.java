package com.student.student.frontend;

import com.student.student.data.Company;
import com.student.student.frontend.utils.ConfigFrontend;
import com.student.student.request.RequestDtoInternShip;
import com.student.student.responce.internShip.DtoResponseInternShip;
import com.student.student.service.CompanyService;
import com.student.student.service.InternshipService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@PermitAll
@PageTitle("Список практик")
@Route(value = "listInternship", layout = MainLayout.class)
public class ListView extends VerticalLayout {
    private final InternshipService internshipService;
    private final Grid<DtoResponseInternShip> grid = new Grid<>(DtoResponseInternShip.class, false);
    private final CompanyService companyService;
    private final ConfigFrontend configFrontend;

    private String gridHeightSize;
    private String sizeForPhone;
    private int duration;
    private int column;

    public ListView(InternshipService internshipService, CompanyService companyService, ConfigFrontend configFrontend) {
        this.internshipService = internshipService;
        this.companyService = companyService;
        this.configFrontend = configFrontend;
        addClassName("list-view");
        setConfigFrontend();
        setSizeFull();
        configureGrid();
        add(getToolbar(), grid);
        lazyLoad();
    }

    public void setConfigFrontend() {
        duration = configFrontend.getDuration().getIntShow();
        sizeForPhone = configFrontend.getSize().getPhone();
        column = configFrontend.getQuantity().getColumn();
        gridHeightSize = configFrontend.getGrid().getHeightSize();
    }

    private void configureGrid() {
        grid.setHeight(gridHeightSize);
        grid.addClassNames("internship-grid");
        grid.setSizeFull();

        grid.addColumn(DtoResponseInternShip::getFirstName).setHeader("Имя").setSortable(true);
        grid.addColumn(DtoResponseInternShip::getLastName).setHeader("Фамилия").setSortable(true);
        grid.addColumn(DtoResponseInternShip::getCompanyName).setHeader("Компания").setSortable(true);
        grid.addColumn(DtoResponseInternShip::getPosition).setHeader("Должность");
        grid.addColumn(i -> i.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .setHeader("Дата начала");
        grid.addColumn(i -> i.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .setHeader("Дата окончания");
        grid.addComponentColumn(this::createActionButtons).setHeader("Действия");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout createActionButtons(DtoResponseInternShip internship) {
        Button commentsButton = new Button("комментарии");
        commentsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        commentsButton.addClickListener(e -> {
            showComments(internship);
        });

        Button editButton = new Button("Редактировать");
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        editButton.addClickListener(e -> {
            createFormRefactor(internship);
        });

        Button deleteButton = new Button(VaadinIcon.TRASH.create());
        deleteButton.addThemeVariants(
                ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY_INLINE
        );
        deleteButton.setTooltipText("Удалить");
        deleteButton.addClickListener(e -> {
            showDeleteConfirmation(internship.getId());
        });

        return new HorizontalLayout(commentsButton, editButton, deleteButton);
    }

    private void showComments(DtoResponseInternShip internship) {
        Dialog dialog = new Dialog();
        String comments = internshipService.getComments(internship.getId());
        Div commentsDiv = new Div();
        commentsDiv.setText(comments);
        dialog.add(commentsDiv);
        dialog.open();
        dialog.setCloseOnEsc(true);
    }

    private void showDeleteConfirmation(UUID internshipId) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Подтверждение удаления");
        dialog.setText("Вы уверены, что хотите удалить эту практику?");

        dialog.setCancelable(true);
        dialog.setConfirmText("Удалить");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(e -> {
            try {
                internshipService.deleteById(internshipId);
                lazyLoad();
                Notification.show("Практика успешно удалена");
            } catch (Exception ex) {
                Notification.show("Ошибка при удалении: " + ex.getMessage(),
                        duration, Notification.Position.BOTTOM_CENTER);
            }
        });

        dialog.open();
    }

    private HorizontalLayout getToolbar() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Поиск по фамилии студента...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addValueChangeListener(event -> {
            searchByLikeName(event.getValue());
        });

        Button addButton = new Button("Добавить практику");
        addButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(CreateView.class)));

        HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void searchByLikeName(String name) {
        if (name == null || name.isBlank()) {
            lazyLoad();
        } else {
            try {
                grid.setItems(query -> {
                    Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize());
                    Page<DtoResponseInternShip> page = internshipService.getLikeStudentName(name, pageable);
                    return page.stream();
                });
            } catch (Exception e) {
                Notification.show("Ошибка при при поиске по имени", duration,
                        Notification.Position.MIDDLE);
            }
        }
    }

    private GridLazyDataView<DtoResponseInternShip> lazyLoad() {
        return grid.setItems(query -> {
            Pageable pageable = PageRequest.of(
                    query.getPage(),
                    query.getPageSize(),
                    Sort.by("lastName")
            );
            Page<DtoResponseInternShip> page = internshipService.getAllInternShip(pageable);
            return page.stream();
        });
    }

    private Component createFormRefactor(DtoResponseInternShip internship) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField studentField = new TextField("Студент");
        studentField.setValue(internship.getFirstName() + internship.getLastName());
        studentField.setReadOnly(true);

        ComboBox<Company> companyCombo = new ComboBox<>("Компания");
        companyCombo.setItems(companyService.findAllProjection());
        companyCombo.setItemLabelGenerator(Company::getName);
        companyCombo.setValue(companyService.findById(internship.getCompanyId()));

        DatePicker startDate = new DatePicker("Дата начала");
        startDate.setValue(internship.getStartDate());

        DatePicker endDate = new DatePicker("Дата окончания");
        endDate.setValue(internship.getEndDate());

        TextField position = new TextField("Должность");
        position.setValue(internship.getPosition());

        TextArea comments = new TextArea("Комментарии");
        comments.setValue(internship.getComments());

        formLayout.add(
                studentField, companyCombo,
                startDate, endDate,
                position, comments
        );
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep(sizeForPhone, column)
        );

        Button saveButton = new Button("Сохранить");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> {
            RequestDtoInternShip dto = new RequestDtoInternShip(
                    internship.getId(),
                    internship.getStudentId(),
                    companyCombo.getValue().getId(),
                    startDate.getValue(),
                    endDate.getValue(),
                    position.getValue(),
                    comments.getValue()
            );
            internshipService.refactorInternShip(dto);
            Notification.show("Изменения сохранены");
            dialog.close();
            lazyLoad();
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(saveButton);

        HorizontalLayout fieldsRefactor = new HorizontalLayout(formLayout);

        VerticalLayout layout = new VerticalLayout();
        layout.add(fieldsRefactor, horizontalLayout);
        layout.setAlignItems(Alignment.CENTER);

        Shortcuts.addShortcutListener(dialog,
                saveButton::click,
                Key.ENTER
        );

        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        dialog.add(layout);
        dialog.open();
        return dialog;
    }
}