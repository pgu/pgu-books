package pgu.client.setup.ui;

import pgu.client.setup.SetupPresenter;
import pgu.client.setup.SetupView;

import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.SubmitButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SetupViewImpl extends Composite implements SetupView {

    private static SetupViewImplUiBinder uiBinder = GWT.create(SetupViewImplUiBinder.class);

    interface SetupViewImplUiBinder extends UiBinder<Widget, SetupViewImpl> {
    }

    @UiField
    RadioButton                 s10, s20, s30, s50;
    @UiField
    SubmitButton                saveBtn;

    private SetupPresenter      presenter;
    private final RadioButton[] radios;

    public SetupViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        radios = new RadioButton[] { s10, s20, s30, s50 };
    }

    @Override
    public void setPresenter(final SetupPresenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("saveBtn")
    public void clickSave(final ClickEvent e) {
        int resultsPerPage = 0;
        for (final RadioButton r : radios) {
            if (r.getValue()) {
                resultsPerPage = Integer.valueOf(r.getText());
                break;
            }
        }
        presenter.updateResultsPerPage(resultsPerPage);
    }

    @Override
    public ResultsPerPage getResultsPerPage() {
        return new ResultsPerPage() {

            @Override
            public void setResultsNb(final int resultsPerPage) {
                if (10 == resultsPerPage) {
                    s10.setValue(true);

                } else if (20 == resultsPerPage) {
                    s20.setValue(true);

                } else if (30 == resultsPerPage) {
                    s30.setValue(true);

                } else if (50 == resultsPerPage) {
                    s50.setValue(true);

                }
            }
        };
    }

}
