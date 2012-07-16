package pgu.client.setup.ui;

import pgu.client.setup.SetupPresenter;
import pgu.client.setup.SetupView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SetupViewImpl extends Composite implements SetupView {

    private static SetupViewImplUiBinder uiBinder = GWT.create(SetupViewImplUiBinder.class);

    interface SetupViewImplUiBinder extends UiBinder<Widget, SetupViewImpl> {
    }

    private SetupPresenter presenter;

    public SetupViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setPresenter(final SetupPresenter presenter) {
        this.presenter = presenter;
    }

}
