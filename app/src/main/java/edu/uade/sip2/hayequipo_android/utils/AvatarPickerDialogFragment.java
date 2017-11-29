package edu.uade.sip2.hayequipo_android.utils;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;

import edu.uade.sip2.hayequipo_android.R;
import edu.uade.sip2.hayequipo_android.core.MainActivity;


public class AvatarPickerDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    // Use this interface to deliver action events
    AvatarPickerDialogListener mListener;
    // References to currently selected item_partido
    private String mSelectedAvatarResourceEntryName;
    private RoundedImageView mSelectedAvatarImageView = null;
    // Reference to the created AlertDialog instance
    private AlertDialog mCreatedDialog;

    /**
     * The activity that opens this dialog must implement AvatarPickerDialogListener.
     * This method stores the listener when the activity is attached.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Cast to AvatarPickerDialogListener so we can send events to the host
            mListener = (AvatarPickerDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    /**
     * Called to create the Dialog. Creates a custom AlertDialog.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

       // ActivityCompat.requestPermissions(getActivity(),
       //         new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.avatar_picker, null);
        builder.setView(view);

        GridView gridView = (GridView) view.findViewById(R.id.avatarGrid);
        gridView.setAdapter(new AvatarImageAdapter(getContext()));
        gridView.setOnItemClickListener(this);

        final AvatarPickerDialogFragment self = this;
        builder.setMessage(R.string.dialog_pick_avatar)
                .setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onAvatarSelected(self, mSelectedAvatarResourceEntryName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onCancelled(self);
                    }
                });

        // Create the AlertDialog object
        mCreatedDialog = builder.create();

        // Disable select button until an avatar is picked
        mCreatedDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mCreatedDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });

        return mCreatedDialog;
    }

    /**
     * This method is called when an avatar is selected in the dialog
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Avatar selected, allow clicking confirmation button
        mCreatedDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

        // Reset border on previously selected avatar when new one is picked
        if (mSelectedAvatarImageView != null) {
            mSelectedAvatarImageView.setBorderWidth(0.f);
        }

        // Use device independent pixels for uniform border width
        float borderWidthDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20.f, getResources().getDisplayMetrics());

        // Animate border on currently selected avatar
        RoundedImageView imageView = (RoundedImageView) view;
        ObjectAnimator anim = ObjectAnimator.ofFloat(imageView, "borderWidth", 0f, borderWidthDip);
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();

        int accentColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        imageView.setBorderColor(accentColor);

        // Store selected resource id
        Integer avatarResourceId = Avatars.getAvatarResources()[position];
        mSelectedAvatarResourceEntryName = Avatars.getAvatarResourceName(getContext(), avatarResourceId);
        mSelectedAvatarImageView = imageView;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                    Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show(); //a sample method called

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }


    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface AvatarPickerDialogListener {
        /**
         * Called when an avatar has been selected by the user.
         *
         * @param dialog       The dialog that trigerred the action.
         * @param resourceName The resource name of the selected avatar.
         */
        void onAvatarSelected(AvatarPickerDialogFragment dialog, String resourceName);

        /**
         * Called when the avatar selection has been aborted by the user.
         *
         * @param dialog The dialog that triggered the action.
         */
        void onCancelled(AvatarPickerDialogFragment dialog);
    }
}