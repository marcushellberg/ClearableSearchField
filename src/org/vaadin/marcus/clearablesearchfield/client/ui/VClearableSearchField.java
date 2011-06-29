package org.vaadin.marcus.clearablesearchfield.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Accessibility;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.Field;
import com.vaadin.terminal.gwt.client.ui.Icon;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VClearableSearchField extends FlowPanel implements Paintable,
        Field, FocusHandler, BlurHandler, KeyDownHandler, ClickHandler {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-clearable-searchfield";
    public static final String BUTTON_CLASSNAME = "search-button";
    public static final String BOX_CLASSNAME = "search-box";

    public static final String SEARCH_IDENTIFIER = "searchterm";
    protected static final String CLEAR_BUTTON_STYLE = "clear";
    protected static final int REVERT_DELAY = 200;

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    protected ApplicationConnection client;

    protected TextBox searchBox;
    protected Button searchButton;

    protected String currentSearchTerm = "";
    protected ResetSearchTimer resetTimer;

    protected boolean promptMode;
    protected boolean buttonInClearMode;

    protected HandlerRegistration boxFocusRegistration;
    protected HandlerRegistration boxBlurRegistration;
    protected HandlerRegistration buttonFocusRegistration;
    protected HandlerRegistration buttonBlurRegistration;
    protected HandlerRegistration boxKeyDownRegistration;
    protected String searchButtonCaption = "";
    protected String clearButtonCaption = "";
    protected String inputPrompt = "";

    // For button
    protected final Element buttonWrapper = DOM.createSpan();
    protected final Element buttonCaption = DOM.createSpan();
    protected Icon searchIcon;
    protected Icon clearIcon;

    public VClearableSearchField() {
        super();
        setStyleName(CLASSNAME);
        sinkEvents(Event.ONKEYDOWN);

        buildLayout();
    }

    private void buildLayout() {
        searchBox = new TextBox();
        searchBox.setStyleName(BOX_CLASSNAME);
        boxFocusRegistration = searchBox.addFocusHandler(this);
        boxBlurRegistration = searchBox.addBlurHandler(this);
        boxKeyDownRegistration = searchBox.addKeyDownHandler(this);

        searchButton = new Button();
        searchButton.setStyleName(BUTTON_CLASSNAME);
        buttonFocusRegistration = searchButton.addFocusHandler(this);
        buttonBlurRegistration = searchButton.addBlurHandler(this);
        searchButton.addClickHandler(this);
        Accessibility.setRole(searchButton.getElement(),
                Accessibility.ROLE_BUTTON);

        buttonWrapper.setClassName(BUTTON_CLASSNAME + "-wrap");
        buttonCaption.setClassName(BUTTON_CLASSNAME + "-caption");
        buttonWrapper.appendChild(buttonCaption);
        searchButton.getElement().appendChild(buttonWrapper);

        add(searchBox);
        add(searchButton);
    }

    /**
     * Called whenever an update is received from the server
     */
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first.
        // It handles sizes, captions, tooltips, etc. automatically.
        if (client.updateComponent(this, uidl, true)) {
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
            inputPrompt = uidl.getStringAttribute("prompt");
        }

        if (uidl.hasVariable(SEARCH_IDENTIFIER)) {
            currentSearchTerm = uidl.getStringVariable(SEARCH_IDENTIFIER);
            searchBox.setText(currentSearchTerm);
        }

        searchButtonCaption = uidl.getStringAttribute("searchButtonCaption");
        clearButtonCaption = uidl.getStringAttribute("clearButtonCaption");

        // if (uidl.hasAttribute("searchButtonIcon")) {
        // if (searchIcon == null) {
        // searchIcon = new Icon(client);
        // buttonWrapper.insertBefore(searchIcon.getElement(),
        // buttonCaption);
        // }
        // searchIcon.setUri(uidl.getStringAttribute("searchButtonIcon"));
        // } else {
        // if (searchIcon != null) {
        // buttonWrapper.removeChild(searchIcon.getElement());
        // searchIcon = null;
        // }
        // }
        //
        // if (uidl.hasAttribute("clearButtonIcon")) {
        // if (clearIcon == null) {
        // clearIcon = new Icon(client);
        //
        // }
        // clearIcon.setUri(uidl.getStringAttribute("searchButtonIcon"));
        // } else {
        // if (clearIcon != null) {
        // buttonWrapper.removeChild(clearIcon.getElement());
        // clearIcon = null;
        // }
        // }
        //

        updateButton();

    }

    // Search box logic
    private void clearSearchBox() {
        searchBox.setText("");
        searchBox.setFocus(true);
        setClearButton(false);
    }

    private void runSearch() {
        currentSearchTerm = searchBox.getText();
        searchBox.setFocus(false);
        client.updateVariable(paintableId, SEARCH_IDENTIFIER,
                searchBox.getText(), true);
    }

    private void revertSearch() {
        if (searchBox.getText().isEmpty()) {
            runSearch();
        } else if (!currentSearchTerm.equals(searchBox.getText())) {
            searchBox.setText(currentSearchTerm);
        }
        setClearButton(false);
    }

    // Button methods
    private void updateButton() {
        if (buttonInClearMode) {
            setButtonText(clearButtonCaption);
        } else {
            setButtonText(searchButtonCaption);
        }
    }

    protected void setButtonText(String text) {
        buttonCaption.setInnerText(text);
    }

    private void setClearButton(boolean clear) {
        if (clear) {
            buttonInClearMode = true;
            searchButton.addStyleName(CLEAR_BUTTON_STYLE);
        } else {
            buttonInClearMode = false;
            searchButton.removeStyleName(CLEAR_BUTTON_STYLE);
        }
        updateButton();
    }

    // Event handling
    public void onFocus(FocusEvent event) {
        if (event.getSource().equals(searchBox)) {
            if (!searchBox.getText().isEmpty()) {
                setClearButton(true);
            }

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

        if (buttonInClearMode) {
            clearSearchBox();
        } else {
            runSearch();
        }
    }

    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(searchBox)) {
            if (buttonInClearMode) {
                setClearButton(false);
            }
        }
    }

    @Override
    public void onBrowserEvent(com.google.gwt.user.client.Event event) {
        if (DOM.eventGetType(event) == Event.ONKEYDOWN
                && event.getKeyCode() == KeyCodes.KEY_ENTER) {
            runSearch();
        }

        super.onBrowserEvent(event);
    }

    private class ResetSearchTimer extends Timer {

        @Override
        public void run() {
            revertSearch();
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
        boxKeyDownRegistration.removeHandler();

        super.onDetach();
    }

}
