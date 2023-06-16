package com.uima.joanne.testrepoapp;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The content this fragment is presenting.
     */
    private Client mClient;
    private DatabaseReference dbref;
    private String display;
    private View rootView;
    private TextView details;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbref = FirebaseDatabase.getInstance().getReference();
     //   mClient = new Client("Error", "xx", 0);

        Activity activity = this.getActivity();
        final CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            final String cid = getArguments().getString(ARG_ITEM_ID);
            Log.d("CID:", cid);
            Log.d("VAL:", dbref.child("clients").child(cid).toString());
            dbref.child("clients").child(cid).addListenerForSingleValueEvent( // or addValueEventListener
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            mClient = dataSnapshot.getValue(Client.class);
                            if (mClient == null) {
                                // client is null, error out
                                Log.e("DBREF:", "Client " + cid + " is unexpectedly null");
                            } else {
                                Log.d("DBREF:", "Client is " + mClient);
                                if (mClient != null) {
                                    // update what is displayed
                                    display = mClient.getId() + ":\n" +
                                            mClient.toString();
                                } else if (appBarLayout != null) {
                                    appBarLayout.setTitle("ERROR");
                                    display = "display error";
                                }
                                details.setText(display);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("DBREF:", "getClientr:onCancelled", databaseError.toException());
                        }
                    });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.item_detail, container, false);
        // Show the dummy content as text in a TextView.
       // if (mItem != null) {
       //     ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
       // }
        if (mClient != null) {
            display = mClient.toString();
        }
        details = ((TextView) rootView.findViewById(R.id.item_detail));
        details.setText(display);

        return rootView;
    }


}
