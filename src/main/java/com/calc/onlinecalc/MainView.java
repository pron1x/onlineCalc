package com.calc.onlinecalc;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {
    private final EquationRepository equationRepository;
    private String equationString;
    final Grid<Equation> grid;
    private final Button button;
    final TextField input;

    public MainView(EquationRepository equationRepository) {
        this.equationRepository = equationRepository;
        grid = new Grid<>(Equation.class);
        button = new Button("Calculate", VaadinIcon.ABACUS.create());
        input = new TextField();
        HorizontalLayout actions = new HorizontalLayout(input, button);
        add(actions, grid);

        grid.setHeight("300px");
        grid.setColumns("equation", "result");

        input.setPlaceholder("Calculate...");
        input.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        input.addValueChangeListener(e -> equationString = e.getValue());

        grid.setItems(equationRepository.findAll());
    }
}
