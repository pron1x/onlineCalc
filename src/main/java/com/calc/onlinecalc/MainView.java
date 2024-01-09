package com.calc.onlinecalc;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.text.NumberFormat;

@Route
public class MainView extends VerticalLayout {
    private final EquationRepository equationRepository;
    private final EquationSolverService equationSolverService;
    private String equationString;
    final VirtualList<Equation> equationList;
    private final Button button;
    final TextField input;
    final Text output;
    final Notification notification;

    public MainView(EquationRepository equationRepository, EquationSolverService equationSolverService) {
        this.equationRepository = equationRepository;
        this.equationSolverService = equationSolverService;
        equationList = new VirtualList<>();
        button = new Button("Calculate", VaadinIcon.ABACUS.create());
        input = new TextField();
        output = new Text("");

        HorizontalLayout notificationLayout = new HorizontalLayout(new Div(new Text("Failed to calculate equation!")));
        notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(1500);
        notification.add(notificationLayout);

        Div outputDiv = new Div(output);
        outputDiv.addClassNames(LumoUtility.TextAlignment.CENTER, LumoUtility.AlignSelf.CENTER);

        HorizontalLayout actions = new HorizontalLayout(input,  outputDiv, button);

        equationList.setHeight("300px");
        equationList.setWidth("500px");
        equationList.setItems(equationRepository.findAll());
        equationList.setRenderer(new TextRenderer<>(Equation::getResolvedString));

        Header headerText = new Header();
        headerText.setText("History");
        headerText.addClassNames(LumoUtility.TextAlignment.CENTER, LumoUtility.FontSize.LARGE);
        Div header = new Div(headerText);
        VerticalLayout history = new VerticalLayout(header, equationList);
        history.setAlignItems(Alignment.CENTER);
        history.setWidth("600px");
        add(actions, history);

        input.setPlaceholder("Calculate...");
        input.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        input.addValueChangeListener(e -> equationString = e.getValue());

        setAlignItems(Alignment.CENTER);

        button.addClickListener(e ->
        {
            try {
                double result = equationSolverService.solveEquation(equationString);
                String text = (result % 1) == 0 ? String.valueOf((int)result) : String.valueOf(result);
                output.setText(text);
                equationRepository.save(new Equation(equationString, result));
                equationList.setItems(equationRepository.findAll());
            } catch (ArithmeticException ex) {
                notification.open();
            }
        });
    }
}
