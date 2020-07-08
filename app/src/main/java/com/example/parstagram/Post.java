package com.example.parstagram;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

@Parcel(analyze = {Post.class})
@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_USERS_WHO_LIKED = "usersWhoLiked";

    public Post() {
        super();
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public JSONArray getUsersWhoLiked() {return getJSONArray(KEY_USERS_WHO_LIKED); }

    public int isLiked(ParseUser user) throws JSONException {
        // Returns index of user if they liked the post, -1 if they haven't
        JSONArray array = getJSONArray(KEY_USERS_WHO_LIKED);
        for (int i = 0; i < array.length(); i++) {
            if (array.get(i).equals(user.getObjectId())) {
                return i;
            }
        }
        return -1;
    }

    public void setUsersWhoLiked(JSONArray array) {put(KEY_USERS_WHO_LIKED, array); }

    public void addUserToLikes(ParseUser user) {
        setUsersWhoLiked(getJSONArray(KEY_USERS_WHO_LIKED).put(user.getObjectId()));
    }

    public void removeUserFromLikes(int index) {
        JSONArray array = getJSONArray(KEY_USERS_WHO_LIKED);
        array.remove(index);
        setUsersWhoLiked(array);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public ParseFile getProfilePic() {return getParseUser(KEY_USER).getParseFile("profilePic"); }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public int getLikes() { return getInt(KEY_LIKES); }

    public void addLike() { put(KEY_LIKES, getLikes() + 1); }

    public void subLike() { put(KEY_LIKES, getLikes() - 1); }

}
