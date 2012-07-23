package pgu.shared.dto;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SuggestionsResult implements IsSerializable {

    private ArrayList<Suggestion> suggestions;

    public ArrayList<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final ArrayList<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

}
