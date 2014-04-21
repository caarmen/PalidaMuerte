package ca.rmen.android.palidamuerte;

import java.util.Calendar;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ca.rmen.android.palidamuerte.app.PoemTypes;
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
            if (poemCursor.moveToFirst()) {
                TextView tvTitleView = (TextView) rootView.findViewById(R.id.title);
                Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "dancing_script.ttf");
                tvTitleView.setTypeface(font);
                tvTitleView.setText(poemCursor.getTitle());
                String preContent = poemCursor.getPreContent();
                TextView preContentView = (TextView) rootView.findViewById(R.id.pre_content);
                preContentView.setVisibility(TextUtils.isEmpty(preContent) ? View.GONE : View.VISIBLE);
                preContentView.setText(preContent);
                ((TextView) rootView.findViewById(R.id.content)).setText(poemCursor.getContent());
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, poemCursor.getYear());
                calendar.set(Calendar.MONTH, poemCursor.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, poemCursor.getDay());
                String dateString = DateUtils.formatDateTime(getActivity(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);
                String locationDateString = String.format("%s, %s", poemCursor.getLocation(), dateString);
                ((TextView) rootView.findViewById(R.id.location_and_date)).setText(locationDateString);

                Integer poemNumber = poemCursor.getPoemNumber();
                String poemTypeAndNumber = "";
                if (poemNumber != null) {
                    String poemTypeName = PoemTypes.getPoemTypeName(getActivity(), poemCursor.getPoemTypeId());
                    poemTypeAndNumber = getActivity().getString(R.string.poem_type_and_number, poemTypeName, poemNumber);
                }
                TextView tvPoemTypeAndNumber = (TextView) rootView.findViewById(R.id.poem_type_and_number);
                tvPoemTypeAndNumber.setVisibility(TextUtils.isEmpty(poemTypeAndNumber) ? View.GONE : View.VISIBLE);
                tvPoemTypeAndNumber.setText(poemTypeAndNumber);

            }
            poemCursor.close();
        }

        return rootView;
    }
}
