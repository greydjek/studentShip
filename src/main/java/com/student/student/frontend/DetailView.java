package com.student.student.frontend;

import com.student.student.frontend.utils.ConfigFrontend;
import com.student.student.responce.internShip.InternShipProjection;
import com.student.student.service.InternshipService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@PageTitle("Просмотр практики")
@Route(value = "view/:internshipId", layout = MainLayout.class)
public class DetailView extends VerticalLayout implements BeforeEnterObserver {
    private final InternshipService internshipService;
    private final ConfigFrontend configFrontend;

    private UUID internshipId;
    private int duration;

    public DetailView(InternshipService internshipService, ConfigFrontend configFrontend) {
        this.internshipService = internshipService;
        this.configFrontend = configFrontend;
        getConfig();
        addClassName("detail-view");
        setSizeFull();
    }

    private void getConfig() {
        duration = configFrontend.getDuration().getIntShow();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        try {
            internshipId = event.getRouteParameters().get("internshipId")
                    .map(UUID::fromString)
                    .orElseThrow(() -> new IllegalArgumentException("Не указан ID практики"));

            loadInternshipData();
        } catch (IllegalArgumentException e) {
            showError("Некорректный ID практики");
            event.forwardTo(ListView.class);
        }
    }

    private void loadInternshipData() {
        try {
            InternShipProjection internship = internshipService.getInternShipFromId(internshipId).getBody();

            if (internship != null) {
                showInternshipDetails(internship);
            } else {
                showNotFound();
            }
        } catch (Exception e) {
            showError("Ошибка при загрузке данных");
        }
    }

    private void showInternshipDetails(InternShipProjection internship) {
        removeAll();

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(true);

        H2 title = new H2(String.format("Практика студента %s %s",
                internship.getLastName(),
                internship.getFirstName()));

        Div details = new Div();
        details.addClassName("details");
        details.add(
                createDetailParagraph("Компания:", internship.getNameCompany()),
                createDetailParagraph("Дата начала:",
                        internship.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))),
                createDetailParagraph("Дата окончания:",
                        internship.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        );

        HorizontalLayout buttons = createActionButtons();

        layout.add(title, details, buttons);
        layout.setAlignItems(Alignment.START);

        add(layout);
    }

    private Paragraph createDetailParagraph(String label, String value) {
        return new Paragraph(new Text(label + " " + value));
    }

    private HorizontalLayout createActionButtons() {
        Button editButton = new Button("Редактировать",
                e -> getUI().ifPresent(ui -> ui.navigate("edit/" + internshipId)));
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button backButton = new Button("Назад к списку",
                e -> getUI().ifPresent(ui -> ui.navigate(ListView.class)));

        return new HorizontalLayout(editButton, backButton);
    }

    private void showNotFound() {
        removeAll();
        add(new H2("Практика не найдена"));
        Button backButton = new Button("Вернуться к списку",
                e -> getUI().ifPresent(ui -> ui.navigate(ListView.class)));
        add(backButton);
        setAlignItems(Alignment.CENTER);
    }

    private void showError(String message) {
        Notification.show(message, duration, Notification.Position.MIDDLE);
    }
}