package org.vaadin.marcus.clearablesearchfield;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

public class ClearablesearchfieldApplication extends Application {

    private static final long serialVersionUID = 1L;

    @Override
    public void init() {
        final Window mainWindow = new Window("Clearablesearchfield Application");
        setMainWindow(mainWindow);

        final Panel valueChanges = new Panel("ValueChanges");

        final ClearableSearchField clearableSearchField = new ClearableSearchField(
                "Search", "Clear");
        clearableSearchField.addListener(new ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            public void valueChange(ValueChangeEvent event) {
                valueChanges.addComponent(new Label((String) event
                        .getProperty().getValue()));
            }
        });
        clearableSearchField.setCaption("Search");
        clearableSearchField.setInputPrompt("Search..");

        mainWindow.addComponent(clearableSearchField);
        mainWindow.addComponent(valueChanges);

        Button serverStateButton = new Button("Check server state");
        serverStateButton.addListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent event) {
                mainWindow.showNotification("Server value for search field",
                        "\"" + clearableSearchField.getSearchTerm() + "\"");
            }
        });

        mainWindow.addComponent(serverStateButton);

        Button setSearchTermButton = new Button("Set search term");
        setSearchTermButton.addListener(new ClickListener() {

            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent event) {
                clearableSearchField.setSearchTerm("searchterm");
            }
        });

        mainWindow.addComponent(setSearchTermButton);
    }
}
