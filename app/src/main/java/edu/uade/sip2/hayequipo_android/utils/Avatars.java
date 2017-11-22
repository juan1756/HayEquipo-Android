package edu.uade.sip2.hayequipo_android.utils;

import android.content.Context;
import android.content.res.Resources;

import edu.uade.sip2.hayequipo_android.R;



public class Avatars {
    // List of all available avatars
    private static final Integer[] AVATARS = {
            R.drawable.amigos, R.drawable.ciudad,
            R.drawable.desconocidos, R.drawable.dreamteam,
            R.drawable.oficina, R.drawable.parque,
            R.drawable.random, R.drawable.rapido,
            R.drawable.torneo
    };

    /**
     * Get a list of all avatar resource ids.
     *
     * @return Array containing all avatar resource ids.
     */
    public static Integer[] getAvatarResources() {
        return AVATARS;
    }

    /**
     * Get the resource name for a given avatar resource id.
     *
     * @param context
     * @param avatarResourceId resource id of the avatar
     * @return resource name of the avatar to be used for persistent storage.
     */
    public static String getAvatarResourceName(Context context, Integer avatarResourceId) {
        Resources resources = context.getResources();
        return resources.getResourceEntryName(avatarResourceId);
    }

    /**
     * Get the resource id for a given avatar name
     *
     * @param context
     * @param avatarResourceName name of the avatar
     * @return resource id
     */
    public static Integer getAvatarResourceId(Context context, String avatarResourceName) {
        Resources resources = context.getResources();
        return resources.getIdentifier(avatarResourceName, "drawable", context.getPackageName());
    }

    /**
     * @return resource name of the default avatar
     */
    public static String getDefaultAvatarResourceName() {
        return "anonymous";
    }

    /**
     * @return resource id of the default avatar
     */
    public static Integer getDefaultAvatarResourceId() {
        return R.drawable.anonymous;
    }
}
