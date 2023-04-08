package com.devash.venturenotify;
public class ModelClass {
    String Title, Content ;
    public ModelClass(String Title, String Content) {
        this.Title = Title;
        this.Content = Content;
    }
    public ModelClass() {
    }
    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }
    public String getContent() {
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }
}
