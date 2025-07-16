package com.student.student.frontend;

import com.student.student.data.Company;
import com.student.student.data.Student;
import com.student.student.exeption.ExceptionData;
import com.student.student.frontend.utils.ConfigFrontend;
import com.student.student.request.InternshipDtoForFront;
import com.student.student.responce.StudentFullProjection;
import com.student.student.service.CompanyService;
import com.student.student.service.InternshipService;
import com.student.student.service.StudentService;
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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import static org.reflections.Reflections.log;

@Route(value = "students", layout = MainLayout.class)
@PageTitle("Student Management")
public class StudentView extends VerticalLayout {
    private final StudentService studentService;
    private final CompanyService companyService;
    private final InternshipService internshipService;
    private final Grid<StudentFullProjection> grid = new Grid<>(StudentFullProjection.class, false);
    private final TextField searchField = new TextField("Поиск по имени");
    private final Button addStudentBtn = new Button("Создать нового студента");
    private final ConfigFrontend configFrontend;

    private String headerWidth;
    private String gridHeightSize;
    private int duration;
    private String commentsHeight;
    private String commentsWeight;

    public StudentView(StudentService studentService, CompanyService companyService, InternshipService internshipService, ConfigFrontend configFrontend) {
        this.studentService = studentService;
        this.companyService = companyService;
        this.internshipService = internshipService;
        this.configFrontend = configFrontend;
        initView();
        loadInitialData();
    }

    private void initView() {
        setConfigFrontend();
        configureGrid();
        setupSearch();
        setupAddButton();
        add(createToolbar(), grid);
    }

    public void setConfigFrontend() {
        this.duration = configFrontend.getDuration().getIntShow();
        headerWidth = configFrontend.getGrid().getHeight();
        commentsHeight = configFrontend.getGrid().getHeight();
        commentsWeight = configFrontend.getGrid().getHeight();
        gridHeightSize = configFrontend.getGrid().getHeightSize();
    }

    private HorizontalLayout createToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.setAlignItems(Alignment.BASELINE);

        searchField.setPlaceholder("Поиск по фамилии...");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));

        toolbar.add(searchField, addStudentBtn);
        toolbar.expand(searchField);
        return toolbar;
    }

    private void configureGrid() {
        grid.setHeight(gridHeightSize);
        grid.removeAllColumns();
        grid.addComponentColumn(student -> {
            Button buttonInfo = new Button("полная инф о студенте", click -> {
                log.info(student.getUuid().toString() + " ищем инф по студенту");
                showStudentFullInfoDialog(student.getUuid());
            });
            buttonInfo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            return buttonInfo;
        }).setHeader("информация");
        grid.addComponentColumn(studentFullProjection -> {

            Button addInternship = new Button(VaadinIcon.EDIT.create(), buttonClickEvent ->
                    openInternshipDialog(studentFullProjection.getUuid()));
            addInternship.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_PRIMARY);
            return addInternship;
        }).setHeader("добавить практику");
        grid.addColumn(StudentFullProjection::getFullName).setHeader("ФИО");
        grid.addColumn(StudentFullProjection::getSpecialization).setHeader("специальность");
        grid.addColumn(StudentFullProjection::getCourse).setHeader("курс");
        grid.addComponentColumn(student -> {
            HorizontalLayout action = new HorizontalLayout();

            Button editBtn = new Button(VaadinIcon.EDIT.create(), click ->
                    openEditDialog(student.getUuid()));
            editBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
            editBtn.setTooltipText("Редактировать");

            Button deleteBtn = new Button(VaadinIcon.TRASH.create(), click ->
                    deleteStudent(student.getUuid()));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY_INLINE);

            action.add(editBtn, deleteBtn);
            return action;
        }).setHeader("Действия").setWidth(headerWidth);
        grid.getColumns().forEach(c -> c.setAutoWidth(true));
        grid.setMultiSort(true);
    }

    private void openInternshipDialog(UUID uuid) {
        Dialog dialog = new Dialog();
        Student student = studentService.findById(uuid).getBody();
        dialog.setHeaderTitle(String.format("Практикa студента %s\n", student.getFio()));
        ComboBox<Company> companyComboBox = new ComboBox<>("выберите компанию");
        List<Company> companies = companyService.findAllProjection();
        companyComboBox.setItems(companies);
        companyComboBox.setItemLabelGenerator(Company::getName);
        companyComboBox.addValueChangeListener(event -> {
            Company company = event.getValue();
        });

        VerticalLayout formLayout = new VerticalLayout();
        DatePicker dateStart = new DatePicker("дата начала");
        DatePicker dateEnd = new DatePicker("дата окончания");
        TextField positionField = new TextField("должность на практике");
        TextArea commentsField = new TextArea("Комментарий");
        commentsField.setHeight(commentsHeight);
        commentsField.setWidth(commentsWeight);
        formLayout.add(companyComboBox, dateStart, dateEnd, positionField, commentsField);

        Button saveButton = new Button("Сохранить", e -> {
            Company company = companyComboBox.getValue();

            InternshipDtoForFront createInternship = new InternshipDtoForFront(
                    student.getId(),
                    company.getId(),
                    dateStart.getValue(),
                    dateEnd.getValue(),
                    positionField.getValue(),
                    commentsField.getValue()
            );

            internshipService.createInternShip(createInternship);
            dialog.close();
        });

        Button cancelButton = new Button("Отмена", e -> dialog.close());

        dialog.add(formLayout);
        dialog.getFooter().add(cancelButton, saveButton);

        dialog.open();
    }

    private void showStudentFullInfoDialog(UUID studentId) {
        try {
            ResponseEntity<Student> response = studentService.findById(studentId);
            Student student = response.getBody();
            if (student != null) {
                log.info(student + " студент получен из БД");
                Dialog dialog = new Dialog();

                VerticalLayout layout = new VerticalLayout();
                layout.add(new H1(student.getFio()));

                Div infoDiv = new Div();
                infoDiv.setText(String.format(
                        "Специальность: %s\n" +
                        "Курс: %d\n" +
                        "контакт(телефон): %s\n" +
                        "Место работы: %s",
                        student.getSpecialization(),
                        student.getCourse(),
                        student.getMobilePhone(),
                        student.getWorker()

                ));
                infoDiv.getStyle().set("white-space", "pre-line");

                layout.add(infoDiv);

                Button closeButton = new Button("Закрыть", e -> dialog.close());
                layout.add(closeButton);

                dialog.add(layout);
                dialog.open();
            }
        } catch (Exception e) {
            Notification.show("Ошибка при загрузке информации: " + e.getMessage(),
                    duration, Notification.Position.MIDDLE);
        }
    }

    private void openEditDialog(UUID studentId) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Редактирование данных студента");

        Student student = studentService.findById(studentId).getBody();
        log.info("студент полученный из БД для редактирования {}", student);

        FormLayout form = new FormLayout();
        TextField firstNameField = new TextField("Имя", student.getFirstName());
        TextField lastNameField = new TextField("Фамилия", student.getLastName());
        TextField middleNameField = new TextField("Отчество", student.getMiddleName());
        TextField mobilePhoneField = new TextField("Телефон", student.getMobilePhone());
        IntegerField courseField = new IntegerField("Курс");
        courseField.setValue(student.getCourse());
        TextField workerField = new TextField("Место работы", student.getWorker());
        TextField specializationField = new TextField("Специальность", student.getSpecialization());

        form.add(firstNameField, lastNameField, middleNameField, mobilePhoneField, courseField, workerField, specializationField);
        Button saveButton = new Button("Сохранить", event -> {
            student.setFirstName(firstNameField.getValue());
            student.setLastName(lastNameField.getValue());
            student.setMiddleName(middleNameField.getValue());
            student.setMobilePhone(mobilePhoneField.getValue());
            student.setCourse(courseField.getValue());
            student.setWorker(workerField.getValue());
            student.setSpecialization(specializationField.getValue());
            try {
                studentService.refreshDataStudent(student);
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

    private void deleteStudent(UUID id) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Удаление студента");
        dialog.setText("Вы уверены, что хотите удалить этого студента");

        dialog.setCancelable(true);
        dialog.setConfirmText("Удалить");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(event -> {
            try {
                Student student = studentService.deleteStudentById(id).getBody();
                assert student != null;
                Notification.show("студент удален: " + student.getLastName() + student.getFirstName(), duration, Notification.Position.MIDDLE);
                grid.getDataProvider().refreshAll();
            } catch (ExceptionData e) {
                Notification.show(e.getMessage(), duration, Notification.Position.MIDDLE);
            }
        });

        dialog.open();
    }

    private void setupSearch() {
        searchField.setPlaceholder("Введите фамилию...");
        searchField.addValueChangeListener(e -> {
            if (e.getValue().isEmpty()) {
                loadInitialData();
            } else {
                try {
                    List<StudentFullProjection> students = studentService
                            .findByLikeNameFullProjectionStudent(e.getValue())
                            .getBody();
                    grid.setItems(students);
                } catch (Exception ex) {
                    Notification.show("Ошибка при поиске", duration,
                            Notification.Position.MIDDLE);
                }
            }
        });
    }

    private void setupAddButton() {
        addStudentBtn.addClickListener(e -> {
            Dialog dialog = new Dialog();
            TextField firstNameField = new TextField("Имя");
            TextField lastNameField = new TextField("Фамилия");
            TextField middleNameField = new TextField("Отчество");
            TextField mobilePhoneField = new TextField("Контакт(телефон)");
            TextField specializationField = new TextField("Специальность");
            IntegerField courseField = new IntegerField("Курс");
            TextField workerField = new TextField("Место работы");
            Button saveBtn = new Button("Сохранить", event -> {

                Student student = new Student();
                student.setFirstName(firstNameField.getValue());
                student.setLastName(lastNameField.getValue());
                student.setMiddleName(middleNameField.getValue());
                student.setMobilePhone(mobilePhoneField.getValue());
                student.setSpecialization(specializationField.getValue());
                student.setCourse(courseField.getValue());
                student.setWorker(workerField.getValue());

                try {
                    studentService.saveNewStudent(student);
                    Notification.show("Студент успешно добавлен", duration, Notification.Position.MIDDLE);
                    dialog.close();
                } catch (ExceptionData ex) {
                    Notification.show("Error saving student", duration,
                            Notification.Position.MIDDLE);
                } finally {
                    loadInitialData();
                }
            });
            dialog.add(new VerticalLayout(
                    firstNameField,
                    lastNameField,
                    middleNameField,
                    mobilePhoneField,
                    courseField,
                    specializationField,
                    workerField,
                    saveBtn));

            Shortcuts.addShortcutListener(dialog,
                    saveBtn::click,
                    Key.ENTER
            );

            dialog.open();
        });
    }

    private void loadInitialData() {
        try {
            grid.setItems(query -> {
                Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize(),
                        Sort.by("firstName"));
                Page<StudentFullProjection> page = studentService.findAllStudentForMainPage(pageable);
                return page.stream();
            });
        } catch (Exception e) {
            Notification.show("Error loading students", duration,
                    Notification.Position.MIDDLE);
        }
    }
}