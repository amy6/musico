package example.com.musico;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

class SuggestionsAdapter extends CursorAdapter {

    private List<String> songNames;

    SuggestionsAdapter(Context context, MatrixCursor cursor, List<String> songNames) {
        super(context, cursor, false);
        this.songNames = songNames;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(songNames.get(cursor.getPosition()));
    }
}
