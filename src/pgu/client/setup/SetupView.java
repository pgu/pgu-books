package pgu.client.setup;

import com.google.gwt.user.client.ui.IsWidget;

public interface SetupView extends IsWidget {

    public interface ResultsPerPage {

        void setResultsNb(int resultsPerPage);

    }

    void setPresenter(SetupPresenter presenter);

    ResultsPerPage getResultsPerPage();

}
