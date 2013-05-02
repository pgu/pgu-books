package pgu.shared.dto;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SuggestionsResult implements IsSerializable {

    private boolean hasMoreThanOneThousandSuggestions = false;

    private ArrayList<Suggestion> suggestions;

    public ArrayList<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final ArrayList<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

    public boolean hasMoreThanOneThousandSuggestions() {
        return hasMoreThanOneThousandSuggestions;
    }

    public void setHasMoreThanOneThousandSuggestions(final boolean hasMoreThanOneThousandSuggestions) {
        this.hasMoreThanOneThousandSuggestions = hasMoreThanOneThousandSuggestions;
    }

}
