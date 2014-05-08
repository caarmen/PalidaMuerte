/**
 * Copyright 2014 Carmen Alvarez
 *
 * This file is part of Pálida Muerte.
 *
 * Pálida Muerte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Pálida Muerte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pálida Muerte. If not, see <http://www.gnu.org/licenses/>.
 */
package ca.rmen.android.palidamuerte.app.about;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ca.rmen.android.palidamuerte.MusicPlayer;
import ca.rmen.android.palidamuerte.MusicPlayer.Song;
import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.ui.Font;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutFragment extends Fragment {
    private static final String TAG = AboutFragment.class.getSimpleName();

    public AboutFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        TextView tvTitleView = (TextView) rootView.findViewById(R.id.author_name);
        Typeface font = Font.getTypeface(getActivity());
        tvTitleView.setTypeface(font);
        PackageInfo pInfo;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            TextView tvAppNameAndVersion = (TextView) rootView.findViewById(R.id.app_name_and_version);
            tvAppNameAndVersion.setText(getString(R.string.app_name_and_version, getString(R.string.app_name), pInfo.versionName));
            MusicPlayer musicPlayer = MusicPlayer.getInstance(getActivity());
            Song currentSong = musicPlayer.getCurrentSong();
            if (currentSong != null) {
                rootView.findViewById(R.id.divider2).setVisibility(View.VISIBLE);
                TextView tvCurrentSong = (TextView) rootView.findViewById(R.id.current_song);
                tvCurrentSong.setText(getString(R.string.current_song, currentSong.composer, currentSong.title));
                tvCurrentSong.setVisibility(View.VISIBLE);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return rootView;
    }
}