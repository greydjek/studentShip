package com.student.student.service.UtilsSevices;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.util.function.Consumer;
import lombok.Getter;

@Getter
public class CustomPagination extends HorizontalLayout {
    private Button firstPageButton;
    private Button prevPageButton;
    private Button nextPageButton;
    private Button lastPageButton;
    private Span pageInfoLabel;

    private int currentPage = 1;
    private int totalPages = 1;
    private Consumer<Integer> pageChangeListener;

    public CustomPagination() {
        initComponents();
        updateButtons();
    }

    private void initComponents() {
        firstPageButton = new Button("<<", e -> setCurrentPage(1));
        prevPageButton = new Button("<", e -> setCurrentPage(currentPage - 1));
        nextPageButton = new Button(">", e -> setCurrentPage(currentPage + 1));
        lastPageButton = new Button(">>", e -> setCurrentPage(totalPages));

        pageInfoLabel = new Span();

        add(firstPageButton, prevPageButton, pageInfoLabel, nextPageButton, lastPageButton);
        setAlignItems(Alignment.CENTER);
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        updateButtons();
    }

    public void setCurrentPage(int page) {
        if (page >= 1 && page <= totalPages) {
            this.currentPage = page;
            updateButtons();
            if (pageChangeListener != null) {
                pageChangeListener.accept(page);
            }
        }
    }

    public void addPageChangeListener(Consumer<Integer> listener) {
        this.pageChangeListener = listener;
    }

    private void updateButtons() {
        firstPageButton.setEnabled(currentPage > 1);
        prevPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < totalPages);
        lastPageButton.setEnabled(currentPage < totalPages);

        pageInfoLabel.setText(String.format("Page %d of %d", currentPage, totalPages));
    }
}