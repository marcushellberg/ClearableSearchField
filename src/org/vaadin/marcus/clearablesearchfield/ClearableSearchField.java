package org.vaadin.marcus.clearablesearchfield;

import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.AbstractField;

/**
 * Server side component for the VClearableSearchField widget.
 */
@SuppressWarnings("serial")
@com.vaadin.ui.ClientWidget(org.vaadin.marcus.clearablesearchfield.client.ui.VClearableSearchField.class)
public class ClearableSearchField extends AbstractField {

    private String inputPrompt = "";
    private String searchButtonCaption = "";
    private String clearButtonCaption = "";
    private Resource searchButtonIcon;
    private Resource clearButtonIcon;

    public ClearableSearchField(String searchButtonCaption,
            String clearButtonCaption) {
        this.searchButtonCaption = searchButtonCaption;
        this.clearButtonCaption = clearButtonCaption;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        target.addVariable(this, "searchterm", (String) getValue());

        target.addAttribute("prompt", inputPrompt);
        target.addAttribute("searchButtonCaption", searchButtonCaption);
        target.addAttribute("clearButtonCaption", clearButtonCaption);

        if (searchButtonIcon != null) {
            target.addAttribute("searchButtonIcon", searchButtonIcon);
        }

        if (clearButtonIcon != null) {
            target.addAttribute("clearButtonIcon", clearButtonIcon);
        }
    }

    @Override
    public Class<?> getType() {
        return String.class;
    }

    /**
     * Receive and handle events and other variable changes from the client.
     * 
     * {@inheritDoc}
     */
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        if (variables.containsKey("searchterm")) {
            setValue(variables.get("searchterm"));
        }
    }

    public void setSearchTerm(String searchTerm) {
        setInternalValue(searchTerm);
        requestRepaint();
    }

    public String getSearchTerm() {
        return (String) getValue();
    }

    public String getSearchButtonCaption() {
        return searchButtonCaption;
    }

    public void setSearchButtonCaption(String searchButtonCaption) {
        this.searchButtonCaption = searchButtonCaption;
    }

    public String getClearButtonCaption() {
        return clearButtonCaption;
    }

    public void setClearButtonCaption(String clearButtonCaption) {
        this.clearButtonCaption = clearButtonCaption;
    }

    public Resource getSearchButtonIcon() {
        return searchButtonIcon;
    }

    public void setSearchButtonIcon(Resource searchButtonIcon) {
        this.searchButtonIcon = searchButtonIcon;
    }

    public Resource getClearButtonIcon() {
        return clearButtonIcon;
    }

    public void setClearButtonIcon(Resource clearButtonIcon) {
        this.clearButtonIcon = clearButtonIcon;
    }

    public String getInputPrompt() {
        return inputPrompt;
    }

    public void setInputPrompt(String prompt) {
        inputPrompt = prompt;
    }
}
