package example.com.musico.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import example.com.musico.R;

import static example.com.musico.activity.MainActivity.FRAGMENT_TAG;

public class SampleFragment extends Fragment {

    private OnExploreListener listener;

    public SampleFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnExploreListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(R.string.no_favorites);

        Button button = view.findViewById(R.id.explore);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onExplore(getArguments().getString(FRAGMENT_TAG));
            }
        });
        return view;
    }

    /**
     * interface to define communication between fragment and activity
      */
    public interface OnExploreListener {
        void onExplore(String tag);
    }
}
