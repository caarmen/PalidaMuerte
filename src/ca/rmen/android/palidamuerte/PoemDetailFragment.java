package ca.rmen.android.palidamuerte;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ca.rmen.android.palidamuerte.provider.poem.PoemCursor;
import ca.rmen.android.palidamuerte.provider.poem.PoemSelection;

/**
 * A fragment representing a single poem detail screen.
 * This fragment is either contained in a {@link PoemListActivity} in two-pane mode (on tablets) or a {@link PoemDetailActivity} on handsets.
 */
public class PoemDetailFragment extends Fragment { // NO_UCD (use default)
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PoemDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_poem_detail, container, false);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            long poemId = getArguments().getLong(ARG_ITEM_ID);
            PoemCursor poemCursor = new PoemSelection().id(poemId).query(getActivity().getContentResolver());
            if (poemCursor.moveToFirst()) ((TextView) rootView.findViewById(R.id.poem_detail)).setText(poemCursor.getContent());
            poemCursor.close();
        }

        return rootView;
    }
}
