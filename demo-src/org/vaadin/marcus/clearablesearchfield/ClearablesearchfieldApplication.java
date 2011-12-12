package org.vaadin.marcus.clearablesearchfield;

import org.vaadin.marcus.clearablesearchfield.data.PersonContainer;

import com.vaadin.Application;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ClearablesearchfieldApplication extends Application {

    private static final long serialVersionUID = 1L;
    private Table personTable;
    private PersonContainer personContainer;

    @Override
    public void init() {
        final Window mainWindow = new Window("Clearablesearchfield Application");
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSpacing(true);
        rootLayout.setMargin(true);
        mainWindow.setContent(rootLayout);
        setMainWindow(mainWindow);

        createUI();
    }

    private void createUI() {

        final ClearableSearchField clearableSearchField = new ClearableSearchField(
                "Filter", "Clear");

        clearableSearchField.addListener(new ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            public void valueChange(ValueChangeEvent event) {
                filterFirstNameBy((String) event.getProperty().getValue());
            }
        });
        clearableSearchField.setInputPrompt("Filter by first name..");

        personTable = new Table();
        personTable.setContainerDataSource(getContainer());

        getMainWindow().addComponent(clearableSearchField);
        getMainWindow().addComponent(personTable);
    }

    protected void filterFirstNameBy(String filterTerm) {
        getContainer().removeAllContainerFilters();
        Filter filter = new SimpleStringFilter("firstName", filterTerm, true,
                false);

        getContainer().addContainerFilter(filter);
    }

    private Container.Filterable getContainer() {
        if (personContainer == null) {
            personContainer = PersonContainer.createWithTestData();
        }

        return personContainer;
    }

}
