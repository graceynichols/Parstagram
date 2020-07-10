package com.example.parstagram

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import org.json.JSONArray
import org.json.JSONException
import org.parceler.Parcel

@Parcel(analyze = [Post::class])
@ParseClassName("Post")
class Post : ParseObject() {
    var description: String?
        get() = getString(KEY_DESCRIPTION)
        set(description) {
            put(KEY_DESCRIPTION, description!!)
        }

    var image: ParseFile?
        get() = getParseFile(KEY_IMAGE)
        set(parseFile) {
            put(KEY_IMAGE, parseFile!!)
        }

    var user: ParseUser?
        get() = getParseUser(KEY_USER)
        set(user) {
            put(KEY_USER, user!!)
        }

    val profilePic: ParseFile?
        get() = getParseUser(KEY_USER)!!.getParseFile("profilePic")

    var comments: JSONArray?
        get() = getJSONArray(KEY_COMMENTS)
        set(array) {
            put(KEY_COMMENTS, array!!)
        }

    // Add a comment to the comment array and return the resulting array
    @Throws(JSONException::class)
    fun addComment(comment: String?): JSONArray {
        val array = comments!!.put(0, comment)
        comments = array
        return array
    }

    // Functions related to liking
    var usersWhoLiked: JSONArray?
        get() = getJSONArray(KEY_USERS_WHO_LIKED)
        set(array) {
            put(KEY_USERS_WHO_LIKED, array!!)
        }

    @Throws(JSONException::class)
    fun isLiked(user: ParseUser): Int {
        // Returns index of user if they liked the post, -1 if they haven't
        val array = getJSONArray(KEY_USERS_WHO_LIKED)
        for (i in 0 until array!!.length()) {
            if (array[i] == user.objectId) {
                return i
            }
        }
        return -1
    }

    fun addUserToLikes(user: ParseUser): Int {
        usersWhoLiked = getJSONArray(KEY_USERS_WHO_LIKED)!!.put(user.objectId)
        // Return index of user in likes array
        return getJSONArray(KEY_USERS_WHO_LIKED)!!.length() - 1
    }

    fun removeUserFromLikes(index: Int) {
        val array = getJSONArray(KEY_USERS_WHO_LIKED)
        array!!.remove(index)
        usersWhoLiked = array
    }

    val likes: Int
        get() = getInt(KEY_LIKES)

    fun addLike() {
        put(KEY_LIKES, likes + 1)
    }

    fun subLike() {
        put(KEY_LIKES, likes - 1)
    }

    companion object {
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
        const val KEY_CREATED_KEY = "createdAt"
        const val KEY_LIKES = "likes"
        const val KEY_USERS_WHO_LIKED = "usersWhoLiked"
        const val KEY_COMMENTS = "comments"
    }
}