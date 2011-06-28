package org.vaadin.marcus.clearablesearchfield;

import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractField;

/**
 * Server side component for the VClearableSearchField widget.
 */
@SuppressWarnings("serial")
@com.vaadin.ui.ClientWidget(org.vaadin.marcus.clearablesearchfield.client.ui.VClearableSearchField.class)
public class ClearableSearchField extends AbstractField {

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        target.addVariable(this, "searchterm", (String) getValue());
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
            setInternalValue(variables.get("searchterm"));
        }
    }

    public void setSearchTerm(String searchTerm) {
        setValue(searchTerm, true);
    }

    public String getSearchTerm() {
        return (String) getValue();
    }

}
