package org.vaadin.marcus.clearablesearchfield.client.ui;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.Field;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VClearableSearchField extends HorizontalPanel implements
        Paintable, ChangeHandler, Field, FocusHandler, BlurHandler,
        ClickHandler {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-clearable-searchfield";

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    protected ApplicationConnection client;

    private TextBox searchBox;

    public static final String SEARCH_IDENTIFIER = "searchterm";

    private Button searchButton;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VClearableSearchField() {

        setStyleName(CLASSNAME);

        sinkEvents(Event.FOCUSEVENTS);

        buildLayout();
    }

    private void buildLayout() {
        searchBox = new TextBox();
        searchButton = new Button();
        searchButton.addClickHandler(this);

        add(searchBox);
        add(searchButton);
    }

    /**
     * Called whenever an update is received from the server
     */
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first.
        // It handles sizes, captions, tooltips, etc. automatically.
        if (client.updateComponent(this, uidl, false)) {
            // If client.updateComponent returns true there has been no changes
            // and we
            // do not need to update anything.
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the client side identifier (paintable id) for the widget
        paintableId = uidl.getId();

        if (uidl.hasAttribute("prompt")) {
            searchBox.setValue(uidl.getStringAttribute("prompt"));
        }

        if (uidl.hasVariable(SEARCH_IDENTIFIER)) {
            searchBox.setText(uidl.getStringVariable(SEARCH_IDENTIFIER));
        } else {
            searchBox.setValue("");
        }

        searchButton.setText(uidl.getStringAttribute("caption"));
    }

    public void onBlur(BlurEvent event) {
        // TODO Auto-generated method stub

    }

    public void onFocus(FocusEvent event) {
        // TODO Auto-generated method stub

    }

    public void onChange(ChangeEvent event) {
        // TODO Auto-generated method stub

    }

    public void onClick(ClickEvent event) {
        client.updateVariable(paintableId, SEARCH_IDENTIFIER,
                searchBox.getText(), true);
    }
}
