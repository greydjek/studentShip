package com.student.student.frontend;

import com.student.student.data.Company;
import com.student.student.exeption.ExceptionData;
import com.student.student.frontend.utils.ConfigFrontend;
import com.student.student.responce.company.CompanyProjectionAndId;
import com.student.student.service.CompanyService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Route(value = "companies", layout = MainLayout.class)
@PageTitle("Управление компаниями")
@Slf4j
public class CompanyView extends VerticalLayout {
    private final CompanyService companyService;
    private final Grid<CompanyProjectionAndId> grid = new Grid<>(CompanyProjectionAndId.class, false);
    private final TextField searchField = new TextField("Поиск по названию");
    private final Button addButton = new Button("Добавить компанию", VaadinIcon.PLUS.create());
    private final ConfigFrontend configFrontend;

    private String gridHeightSize;
    private String headerWidth;
    private int duration;

    public CompanyView(CompanyService companyService, ConfigFrontend configFrontend) {
        this.companyService = companyService;
        this.configFrontend = configFrontend;
        init();
    }

    public void init() {
        setConfigFrontend();
        configureGrid();
        setupToolbar();
        loadInitialData();
    }

    public void setConfigFrontend() {
        duration = configFrontend.getDuration().getIntShow();
        headerWidth = configFrontend.getHeader().getWidth();
        gridHeightSize = configFrontend.getGrid().getHeightSize();
    }

    private void configureGrid() {
        grid.setHeight(gridHeightSize);
        grid.removeAllColumns();
        grid.addColumn(CompanyProjectionAndId::getName).setHeader("Название").setSortable(true);
        grid.addColumn(CompanyProjectionAndId::getAddress).setHeader("Адрес");
        grid.addComponentColumn(company -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button editBtn = new Button(VaadinIcon.EDIT.create(), click ->
                    openEditDialog(company.getId()));
            editBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);

            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), click ->
                    deleteDialog(company.getId()));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY_INLINE);

            actions.add(editBtn, deleteBtn);
            return actions;
        }).setHeader("Действия").setWidth(headerWidth);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setMultiSort(true);
    }

    private void setupToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout(searchField, addButton);
        toolbar.setAlignItems(Alignment.BASELINE);

        searchField.setPlaceholder("Введите название...");
        searchField.addValueChangeListener(e -> searchCompanies(e.getValue()));

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openAddDialog());

        add(toolbar, grid);
    }

    private void loadInitialData() {
        try {
            grid.setItems(query -> {
                Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize(), Sort.by("name"));
                Page<CompanyProjectionAndId> page = companyService.findAll(pageable);
                return page.stream();
            });
        } catch (Exception e) {
            Notification.show("Ошибка при загрузке компании", duration,
                    Notification.Position.MIDDLE);
        }
    }

    private void searchCompanies(String name) {
        if (name == null || name.isBlank()) {
            loadInitialData();
        } else {
            try {
                grid.setItems(query -> {
                    Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize());
                    Page<CompanyProjectionAndId> page = companyService.findByLikeNameCompany(name, pageable);
                    log.info("фронт принял страницу с параметрами {} ", page.getContent());
                    return page.stream();
                });
            } catch (Exception e) {
                Notification.show("Ошибка при при поиске по имени", duration,
                        Notification.Position.MIDDLE);
            }
        }
    }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Новая компания");

        FormLayout form = new FormLayout();
        TextField nameField = new TextField("Название");
        TextField addressField = new TextField("Адрес");

        form.add(nameField, addressField);

        Button saveButton = new Button("Сохранить", event -> {
            Company newCompany = new Company();
            newCompany.setName(nameField.getValue());
            newCompany.setAddress(addressField.getValue());

            try {
                companyService.saveNewCompany(newCompany);
                Notification.show("Компания сохранена", duration, Notification.Position.MIDDLE);
                dialog.close();
                grid.getDataProvider().refreshAll();
            } catch (ExceptionData e) {
                Notification.show(e.getMessage(), duration, Notification.Position.MIDDLE);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(new VerticalLayout(form, saveButton));
        dialog.open();
    }

    private void openEditDialog(UUID companyId) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Редактирование компании");

        Company company = companyService.findById(companyId);

        FormLayout form = new FormLayout();
        TextField nameField = new TextField("Название", company.getName());
        TextField addressField = new TextField("Адрес", company.getAddress());

        form.add(nameField, addressField);

        Button saveButton = new Button("Сохранить", event -> {
            company.setName(nameField.getValue());
            company.setAddress(addressField.getValue());

            try {
                companyService.refactor(company);
                Notification.show("Изменения сохранены", duration, Notification.Position.MIDDLE);
                dialog.close();
                grid.getDataProvider().refreshAll();
            } catch (ExceptionData e) {
                Notification.show(e.getMessage(), duration, Notification.Position.MIDDLE);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Shortcuts.addShortcutListener(dialog,
                saveButton::click,
                Key.ENTER
        );

        dialog.add(new VerticalLayout(form, saveButton));
        dialog.open();
    }

    private void deleteDialog(UUID companyId) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Удаление компании");
        dialog.setText("Вы уверены, что хотите удалить эту компанию?");

        dialog.setCancelable(true);
        dialog.setConfirmText("Удалить");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(event -> {
            try {
                String deleted = (String) companyService.deleteById(companyId).getBody();
                Notification.show("Компания удалена: " + deleted, duration, Notification.Position.MIDDLE);
                grid.getDataProvider().refreshAll();
            } catch (ExceptionData e) {
                Notification.show(e.getMessage(), duration, Notification.Position.MIDDLE);
            }
        });

        dialog.open();
    }
}