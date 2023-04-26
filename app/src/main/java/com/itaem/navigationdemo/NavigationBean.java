package com.itaem.navigationdemo;

import java.io.Serializable;
import java.util.List;

public class NavigationBean implements Serializable {
    private List<DataBean> data;
    public List<DataBean> getData() {
        return data;
    }
    public static class DataBean implements Serializable {
        // 导航
        private String name;
        private List<ArticlesBean> articles;
        // 导航页选定判断
        private Boolean select = false;
        public String getName() {
            return name;
        }

        public Boolean getSelect() {
            return select;
        }

        public void setSelect(Boolean select) {
            this.select = select;
        }

        public List<ArticlesBean> getArticles() {
            return articles;
        }
        public static class ArticlesBean implements Serializable {
            // 是否在同组首位
            private boolean isFirst;
            // 是否在同组末尾
            private boolean isLast;
            // 组名——对应导航
            private String chapterName;
            // 组内的选项名
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
