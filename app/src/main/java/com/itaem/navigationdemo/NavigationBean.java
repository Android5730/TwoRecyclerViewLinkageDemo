package com.itaem.navigationdemo;

import java.io.Serializable;
import java.util.List;

public class NavigationBean implements Serializable {
    private List<DataBean> data;
    public List<DataBean> getData() {
        return data;
    }
    public static class DataBean implements Serializable {
        private String name;
        private List<ArticlesBean> articles;
        public String getName() {
            return name;
        }
        public List<ArticlesBean> getArticles() {
            return articles;
        }
        public static class ArticlesBean implements Serializable {
            private boolean isFirst;
            private boolean isLast;
            private String chapterName;
            private String title;
            public String getTitle() {
                return title;
            }
            public String getChapterName() {
                return chapterName;
            }
            public boolean isFirst() {
                return isFirst;
            }
            public boolean isLast() {
                return isLast;
            }
            public void setFirst(boolean first) {
                isFirst = first;
            }
            public void setLast(boolean last) {
                isLast = last;
            }
        }
    }
}
