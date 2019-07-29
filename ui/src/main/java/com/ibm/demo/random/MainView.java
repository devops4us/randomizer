package com.ibm.demo.random;

import javax.inject.Inject;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	
	@Inject
    private RandomizerService randomizerService;

    public MainView() {
        Button button = new Button("Get Random Number from Server",
                event -> Notification.show(String.format("%d", randomizerService.getRandomNumber())));
        add(button);
    }    
}
