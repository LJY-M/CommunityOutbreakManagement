package com.example.communityoutbreakmanagement;

public class CommunityBlogs {

    private String houseNumber;
    private String residentName;
    private String blogLabel;
    private String blogTitle;
    private String blogContent;
    private String blogTime;

    public CommunityBlogs(String houseNumber, String residentName, String blogLabel, String blogTitle, String blogContent, String blogTime) {
        this.houseNumber = houseNumber;
        this.residentName = residentName;
        this.blogLabel = blogLabel;
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
        this.blogTime = blogTime;
    }

    @Override
    public String toString() {
        return "CommunityBlogs{" +
                "houseNumber='" + houseNumber + '\'' +
                ", residentName='" + residentName + '\'' +
                ", blogLabel='" + blogLabel + '\'' +
                ", blogTitle='" + blogTitle + '\'' +
                ", blogContent='" + blogContent + '\'' +
                ", blogTime='" + blogTime + '\'' +
                '}';
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getBlogLabel() {
        return blogLabel;
    }

    public void setBlogLabel(String blogLabel) {
        this.blogLabel = blogLabel;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public String getBlogTime() {
        return blogTime;
    }

    public void setBlogTime(String blogTime) {
        this.blogTime = blogTime;
    }
}
