package com.student.student.frontend;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;

public class MainLayout extends AppLayout {
    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Система управления практиками");
        logo.addClassNames("text-l", "m-m");

        Header header = new Header(logo);
        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Список практик", ListView.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listLink,
                new RouterLink("Добавить практику", CreateView.class),
                new RouterLink("Список студентов", StudentView.class),
                new RouterLink("Список предприятий", CompanyView.class)
        ));
    }
}

