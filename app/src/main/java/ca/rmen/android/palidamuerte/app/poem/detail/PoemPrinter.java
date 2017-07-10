/**
 * Copyright 2017 Carmen Alvarez
 * <p>
 * This file is part of Pálida Muerte.
 * <p>
 * Pálida Muerte is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * Pálida Muerte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Pálida Muerte. If not, see <http://www.gnu.org/licenses/>.
 */
package ca.rmen.android.palidamuerte.app.poem.detail;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ca.rmen.android.palidamuerte.R;
import ca.rmen.android.palidamuerte.provider.poem.PoemCursor;
import ca.rmen.android.palidamuerte.provider.poem.PoemSelection;

@TargetApi(Build.VERSION_CODES.KITKAT)
final class PoemPrinter {
    private PoemPrinter() {
        // prevent instantiation
    }

    static void print(final Context context, final long poemId) {
        new AsyncTask<Void, Void, PoemCursor>() {

            @Override
            protected PoemCursor doInBackground(Void... params) {
                final PoemSelection poemSelection = new PoemSelection().id(poemId);
                PoemCursor cursor = poemSelection.query(context.getContentResolver());
                if (cursor.moveToFirst()) {
                    return cursor;
                }
                return null;
            }

            @Override
            protected void onPostExecute(final PoemCursor poemCursor) {
                if (poemCursor == null) return;
                final WebView webView = new WebView(context);
                final String title = poemCursor.getTitle();
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        @SuppressWarnings("deprecation")
                        PrintDocumentAdapter printDocumentAdapter =
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? webView.createPrintDocumentAdapter(title)
                                        : webView.createPrintDocumentAdapter();
                        PrintAttributes printAttributes = new PrintAttributes.Builder().build();
                        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
                        printManager.print(title, printDocumentAdapter, printAttributes);
                    }
                });
                try {
                    String preContent = poemCursor.getPreContent();
                    if (preContent == null) preContent = "";
                    String formattedText = context.getString(R.string.print_preview_html,
                            poemCursor.getTitle(),
                            preContent,
                            poemCursor.getContent(),
                            Poems.getPoemNumberString(context, poemCursor),
                            Poems.getLocationDateString(context, poemCursor));
                    webView.loadDataWithBaseURL(null, formattedText, "text/html", "UTF-8", null);
                } finally {
                    poemCursor.close();
                }
            }
        }.execute();

    }
}
