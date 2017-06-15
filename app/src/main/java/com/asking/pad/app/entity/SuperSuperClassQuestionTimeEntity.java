package com.asking.pad.app.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wxiao on 2016/11/30.
 * 超级辅导课--问答时间
 */

public class SuperSuperClassQuestionTimeEntity {

    private List<AttrListBean> attrList;

    public List<AttrListBean> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<AttrListBean> attrList) {
        this.attrList = attrList;
    }

    public AttrListBean createAttrListBean(){
        return new AttrListBean();
    }

    public static class AttrListBean {
        /**
         * tipId : 471
         * tipName : 正数、负数、有理数、数轴
         * titleName : “一个有理数既不是整数，又不是正数，那它是什么数呢？”，碰到这类问题，我们要如何处理呢？
         * contentHtml : <p>这类问题本质上就是有理数的分类问题.</p><p>有理数分类方式可以分为两种：</p><p>1、先按性质符号的正负分类，再按整数和分数分类</p><p>2、先按整数和分数分类，再按性质符号的正负分类</p><p>我们以“$250，\frac{4}{2}，-2015，-\frac{101}{5}，\frac{1}{5}，0，1.5，-0.\dot{1}$”这些有理数为例就行分类：</p><p><strong>1、先“性质符号”——后“整数分数”</strong></p><p><strong><img src="http://7xj9ur.com1.z0.glb.clouddn.com/2016-09-13/1473751321276085506.png" title="1473751196809073251.png" alt="blob.png"/></strong></p><p><strong><img src="http://7xj9ur.com1.z0.glb.clouddn.com/2016-09-13/1473751321464063177.png" title="1473751207874017015.png" alt="blob.png"/></strong></p><p>最后结果分为5类数，即<strong>正整数、正分数、0、负整数、负分数</strong>.</p><p><strong>2、先“整数分数”——后“性质符号”</strong></p><p><img src="http://7xj9ur.com1.z0.glb.clouddn.com/2016-09-13/1473751321648015246.png" title="1473751245809021503.png" alt="blob.png"/></p><p><img src="http://7xj9ur.com1.z0.glb.clouddn.com/2016-09-13/1473751321838073066.png" title="1473751255410089895.png" alt="blob.png"/></p><p>最后结果分为5类数，即<strong>正整数、0、负整数、正分数、负分数</strong>.</p><p><span style="text-indent: 2em;">比较以上两种分类方式，不管分类的顺序如何，最后都可以分为5类数，即<strong>正整数、负整数、正分数、负分数、还有既不是正数也不是负数的家伙0！</strong>按照上述这两种分类方式，我们就可以从容的处理有理数分类问题了.</span></p><p><span style="text-indent: 2em;"></span></p><p><br/></p><p>例题：</p><p>“一个有理数既不是整数，又不是正数，那它是什么数呢？”</p><p>解析：</p><p>有理数分为5类数“正整数、负整数、正分数、负分数、0”，</p><p>这个有理数既不是整数，我们先排除掉“正整数、负整数、0”，</p><p>剩下“正分数、负分数”，</p><p>又不是正数，再继续排除掉“正分数”，最后剩下的就只有“负分数”了.</p>
         */
        @SerializedName("tip_id")
        private String tipId;
        @SerializedName("tip_name")
        private String tipName;
        private String titleName;
        private String contentHtml;

        public String getTipId() {
            return tipId;
        }

        public void setTipId(String tipId) {
            this.tipId = tipId;
        }

        public String getTipName() {
            return tipName;
        }

        public void setTipName(String tipName) {
            this.tipName = tipName;
        }

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        public String getContentHtml() {
            return contentHtml;
        }

        public void setContentHtml(String contentHtml) {
            this.contentHtml = contentHtml;
        }
    }
}

