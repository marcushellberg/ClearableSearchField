package org.vaadin.marcus.clearablesearchfield.client.ui;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.Field;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VClearableSearchField extends FlowPanel implements Paintable,
        Field, FocusHandler, BlurHandler, ClickHandler {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-clearable-searchfield";

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    protected ApplicationConnection client;

    private TextBox searchBox;

    public static final String SEARCH_IDENTIFIER = "searchterm";

    private static final int REVERT_DELAY = 200;

    private Button searchButton;

    private VerticalPanel messagesPanel;

    private String currentSearchTerm = "";

    private boolean promptMode;

    private ResetSearchTimer resetTimer;

    private HandlerRegistration boxFocusRegistration;

    private HandlerRegistration boxBlurRegistration;

    private HandlerRegistration buttonFocusRegistration;

    private HandlerRegistration buttonBlurRegistration;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VClearableSearchField() {

        setStyleName(CLASSNAME);

        buildLayout();
    }

    private void buildLayout() {

        searchBox = new TextBox();
        boxFocusRegistration = searchBox.addFocusHandler(this);
        boxBlurRegistration = searchBox.addBlurHandler(this);

        searchButton = new Button();
        buttonFocusRegistration = searchButton.addFocusHandler(this);
        buttonBlurRegistration = searchButton.addBlurHandler(this);
        searchButton.addClickHandler(this);

        messagesPanel = new VerticalPanel();

        add(searchBox);
        add(searchButton);
        add(messagesPanel);
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
            searchBox.setText(uidl.getStringAttribute("prompt"));
            promptMode = true;
        }

        if (uidl.hasVariable(SEARCH_IDENTIFIER)) {
            currentSearchTerm = uidl.getStringVariable(SEARCH_IDENTIFIER);
            searchBox.setText(currentSearchTerm);
        }

        searchButton.setHTML("<b>" + uidl.getStringAttribute("caption")
                + "</b>");
    }

    public void onFocus(FocusEvent event) {
        if (event.getSource().equals(searchBox)) {
        } else if (event.getSource().equals(searchButton)) {
            cancelResetTimer();
        }
    }

    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(searchBox)) {
            startResetTimer();
        } else if (event.getSource().equals(searchButton)) {
            startResetTimer();
        }
    }

    public void onClick(ClickEvent event) {
        cancelResetTimer();

        currentSearchTerm = searchBox.getText();
        client.updateVariable(paintableId, SEARCH_IDENTIFIER,
                searchBox.getText(), true);
    }

    private class ResetSearchTimer extends Timer {

        @Override
        public void run() {
            searchBox.setText(currentSearchTerm);
        }
    }

    private void startResetTimer() {
        if (resetTimer != null) {
            resetTimer.cancel();
        }
        resetTimer = new ResetSearchTimer();
        resetTimer.schedule(REVERT_DELAY);
    }

    private void cancelResetTimer() {
        if (resetTimer != null) {
            resetTimer.cancel();
            resetTimer = null;
        }
    }

    @Override
    protected void onDetach() {
        if (resetTimer != null) {
            resetTimer.cancel();
            resetTimer = null;
        }

        boxBlurRegistration.removeHandler();
        boxFocusRegistration.removeHandler();
        buttonBlurRegistration.removeHandler();
        buttonFocusRegistration.removeHandler();

        super.onDetach();
    }
}
