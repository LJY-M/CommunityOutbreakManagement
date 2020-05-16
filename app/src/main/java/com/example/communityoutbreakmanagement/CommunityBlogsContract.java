package com.example.communityoutbreakmanagement;

import android.net.Uri;
import android.provider.BaseColumns;

public class CommunityBlogsContract {

    public static final String AUTHORITY = "com.example.communityoutbreakmanagementBlogs";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" +AUTHORITY);

    public static final String PATH_COMMUNITY_BLOGS = "communityBlog";

    public static final class CommunityBlogsEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMUNITY_BLOGS).build();

        public static final String TABLE_NAME = "communityBlog";
        public static final String COLUMN_HOUSE_NUMBER = "houseNumber";
        public static final String COLUMN_RESIDENT_NAME = "residentName";
        public static final String COLUMN_BLOG_LABEL = "blogLabel";
        public static final String COLUMN_BLOG_TITLE = "blogTitle";
        public static final String COLUMN_BLOG_CONTENT = "blogContent";
        public static final String COLUMN_BLOG_TIME = "blogTime";
    }
}
