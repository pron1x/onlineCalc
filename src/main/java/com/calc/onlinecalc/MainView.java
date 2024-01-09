package com.calc.onlinecalc;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {
    private final EquationRepository equationRepository;
    private final EquationSolverService equationSolverService;
    private String equationString;
    final Grid<Equation> grid;
    private final Button button;
    final TextField input;
    final Text output;
    final Notification notification;

    public MainView(EquationRepository equationRepository, EquationSolverService equationSolverService) {
        this.equationRepository = equationRepository;
        this.equationSolverService = equationSolverService;
        grid = new Grid<>(Equation.class);
        button = new Button("Calculate", VaadinIcon.ABACUS.create());
        input = new TextField();
        output = new Text("");

        notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(1500);
        HorizontalLayout notificationLayout = new HorizontalLayout(new Div(new Text("Failed to calculate equation!")));
        notification.add(notificationLayout);
        notification.setPosition(Notification.Position.TOP_CENTER);

        HorizontalLayout actions = new HorizontalLayout(input,  new Div(output), button);
        add(actions, grid);

        grid.setHeight("300px");
        grid.setColumns("equation", "result");

        input.setPlaceholder("Calculate...");
        input.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        input.addValueChangeListener(e -> equationString = e.getValue());

        button.addClickListener(e ->
        {
            try {
                double result = equationSolverService.solveEquation(equationString);
                String text = (result % 1) == 0 ? String.valueOf((int)result) : String.valueOf(result);
                output.setText(text);
            } catch (ArithmeticException ex) {
                notification.open();
            }
        });

        grid.setItems(equationRepository.findAll());
    }
}
