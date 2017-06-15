package com.asking.pad.app.entity;

import java.util.List;

public class MySubjectEntity {

    /**
     * total : 3
     * list : [{"id":"1ADE62E8-E7E2-47E1-B6F4-FAD16238606E","subject_description":"3-2（x-1）＜1．<br>","subject_description_html":"3-2（x-1）＜1．<br>","right_answer":"先去括号、再移项、合并同类项、化系数为1即可求出x的取值范围．<br><\/p>","gkt_state":false,"zt_state":false,"zt_size":0,"autopost_size":0,"answer_size":0,"answer_right_size":0,"subject_type":{"type_id":"2","type_name":"解答题"},"difficulty":3,"column_num":2,"details_answer":"<p><br>去括号得，3-2x+2＜1，<br>移项得，-2x＜1-2-3，<br>合并同类项得，-2x＜-4，<br>化系数为1得，x＞2．<br>","details_answer_html":"<p><br>去括号得，3-2x+2＜1，<br>移项得，-2x＜1-2-3，<br>合并同类项得，-2x＜-4，<br>化系数为1得，x＞2．<br>","createDateStr":"2016-07-18","state":"1","random":0.5737381180105922},{"id":"F775585A-E274-4C34-A99B-7A5BF6472E2E","subject_description":"（2015·福建）下列函数为奇函数的是（　　 ）","subject_description_html":"<p>（2015·福建）下列函数为奇函数的是（　　 ）<\/p>","options_num":"1","options":[{"id":"1","option_name":"A","option_content":"$y=\\sqrt{x}$","option_content_html":"<p>$y=\\sqrt{x}$<\/p>"},{"id":"2","option_name":"B","option_content":"$y=|\\sin x|$","option_content_html":"<p>$y=|\\sin x|$<\/p>"},{"id":"3","option_name":"C","option_content":"$y=\\cos x$","option_content_html":"<p>$y=\\cos x$<\/p>"},{"id":"4","option_name":"D","option_content":"$y=e^{x}-e^{-x}$","option_content_html":"<p>$y=e^{x}-e^{-x}$<\/p>"}],"right_answer":"D","gkt_state":false,"zt_state":false,"zt_size":0,"autopost_size":0,"answer_size":0,"answer_right_size":0,"subject_type":{"type_id":"1","type_name":"选择题"},"difficulty":2,"subject_code":"M_03_17_03_A1_B1_005","column_num":2,"details_answer":"解：A．函数的定义域为$[0，+∞）$，定义域关于原点不对称，故A为非奇非偶函数．B．$f（-x）=|\\sin （-x）|=|\\sin x|=f（x）$，则$f（x）$为偶函数．C．$y=\\cos x$为偶函数．D．$f（-x）=e^{-x}-e^{x}=-（e^{x}-e^{-x}）=-f（x）$，则$f（x）$为奇函数，故选：D","details_answer_html":"<p>解：A．函数的定义域为$[0，+∞）$，定义域关于原点不对称，故A为非奇非偶函数．<br/>B．$f（-x）=|\\sin （-x）|=|\\sin x|=f（x）$，则$f（x）$为偶函数．<br/>C．$y=\\cos x$为偶函数．<br/>D．$f（-x）=e^{-x}-e^{x}=-（e^{x}-e^{-x}）=-f（x）$，则$f（x）$为奇函数，<br/>故选：D<\/p>","createDateStr":"2016-07-18","state":"1"},{"id":"9E777FCF-FD27-445D-B99D-92AA5E387700","subject_description":"（2013春·温江区校级月考）关于x的方程2x-5a=1的解是非负数，则满足条件的最小整数a是.","subject_description_html":"<p>（2013春·温江区校级月考）关于x的方程2x-5a=1的解是非负数，则满足条件的最小整数a是<span class=\"timuinputw\" contenteditable=\"true\"><\/span>.<\/p>","right_answer":"0","gkt_state":false,"zt_state":false,"zt_size":0,"autopost_size":0,"answer_size":0,"answer_right_size":0,"subject_type":{"type_id":"4","type_name":"填空题"},"difficulty":2,"subject_code":"M_40_01_09_A2_B4_002","column_num":2,"details_answer":"解：解方程得：x=$\\frac{5a+1}{2}$，则$\\frac{5a+1}{2}$≥0，解得：a≥-$\\frac{5a+1}{2}$．则满足条件的最小整数a是0．故答案是：0．","details_answer_html":"<p>解：解方程得：x=$\\frac{5a+1}{2}$，<\/p><p>则$\\frac{5a+1}{2}$≥0，<\/p><p>解得：a≥-$\\frac{5a+1}{2}$．<\/p><p>则满足条件的最小整数a是0．<br/>故答案是：0．<\/p><p><br/><\/p>","createDateStr":"2016-07-18","state":"1"}]
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1ADE62E8-E7E2-47E1-B6F4-FAD16238606E
         * subject_description : 3-2（x-1）＜1．<br>
         * subject_description_html : 3-2（x-1）＜1．<br>
         * right_answer : 先去括号、再移项、合并同类项、化系数为1即可求出x的取值范围．<br></p>
         * gkt_state : false
         * zt_state : false
         * zt_size : 0
         * autopost_size : 0
         * answer_size : 0
         * answer_right_size : 0
         * subject_type : {"type_id":"2","type_name":"解答题"}
         * difficulty : 3
         * column_num : 2
         * details_answer : <p><br>去括号得，3-2x+2＜1，<br>移项得，-2x＜1-2-3，<br>合并同类项得，-2x＜-4，<br>化系数为1得，x＞2．<br>
         * details_answer_html : <p><br>去括号得，3-2x+2＜1，<br>移项得，-2x＜1-2-3，<br>合并同类项得，-2x＜-4，<br>化系数为1得，x＞2．<br>
         * createDateStr : 2016-07-18
         * state : 1
         * random : 0.5737381180105922
         * options_num : 1
         * options : [{"id":"1","option_name":"A","option_content":"$y=\\sqrt{x}$","option_content_html":"<p>$y=\\sqrt{x}$<\/p>"},{"id":"2","option_name":"B","option_content":"$y=|\\sin x|$","option_content_html":"<p>$y=|\\sin x|$<\/p>"},{"id":"3","option_name":"C","option_content":"$y=\\cos x$","option_content_html":"<p>$y=\\cos x$<\/p>"},{"id":"4","option_name":"D","option_content":"$y=e^{x}-e^{-x}$","option_content_html":"<p>$y=e^{x}-e^{-x}$<\/p>"}]
         * subject_code : M_03_17_03_A1_B1_005
         */

        private String id;
        private String subject_description;
        private String subject_description_html;
        private String right_answer;
        private boolean gkt_state;
        private boolean zt_state;
        private int zt_size;
        private int autopost_size;
        private int answer_size;
        private int answer_right_size;
        private SubjectTypeBean subject_type;
        private int difficulty;
        private int column_num;
        private String details_answer;
        private String details_answer_html;
        private String createDateStr;
        private String state;
        private double random;
        private String options_num;
        private String subject_code;
        private List<OptionsBean> options;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubject_description() {
            return subject_description;
        }

        public void setSubject_description(String subject_description) {
            this.subject_description = subject_description;
        }

        public String getSubject_description_html() {
            return subject_description_html;
        }

        public void setSubject_description_html(String subject_description_html) {
            this.subject_description_html = subject_description_html;
        }

        public String getRight_answer() {
            return right_answer;
        }

        public void setRight_answer(String right_answer) {
            this.right_answer = right_answer;
        }

        public boolean isGkt_state() {
            return gkt_state;
        }

        public void setGkt_state(boolean gkt_state) {
            this.gkt_state = gkt_state;
        }

        public boolean isZt_state() {
            return zt_state;
        }

        public void setZt_state(boolean zt_state) {
            this.zt_state = zt_state;
        }

        public int getZt_size() {
            return zt_size;
        }

        public void setZt_size(int zt_size) {
            this.zt_size = zt_size;
        }

        public int getAutopost_size() {
            return autopost_size;
        }

        public void setAutopost_size(int autopost_size) {
            this.autopost_size = autopost_size;
        }

        public int getAnswer_size() {
            return answer_size;
        }

        public void setAnswer_size(int answer_size) {
            this.answer_size = answer_size;
        }

        public int getAnswer_right_size() {
            return answer_right_size;
        }

        public void setAnswer_right_size(int answer_right_size) {
            this.answer_right_size = answer_right_size;
        }

        public SubjectTypeBean getSubject_type() {
            return subject_type;
        }

        public void setSubject_type(SubjectTypeBean subject_type) {
            this.subject_type = subject_type;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(int difficulty) {
            this.difficulty = difficulty;
        }

        public int getColumn_num() {
            return column_num;
        }

        public void setColumn_num(int column_num) {
            this.column_num = column_num;
        }

        public String getDetails_answer() {
            return details_answer;
        }

        public void setDetails_answer(String details_answer) {
            this.details_answer = details_answer;
        }

        public String getDetails_answer_html() {
            return details_answer_html;
        }

        public void setDetails_answer_html(String details_answer_html) {
            this.details_answer_html = details_answer_html;
        }

        public String getCreateDateStr() {
            return createDateStr;
        }

        public void setCreateDateStr(String createDateStr) {
            this.createDateStr = createDateStr;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public double getRandom() {
            return random;
        }

        public void setRandom(double random) {
            this.random = random;
        }

        public String getOptions_num() {
            return options_num;
        }

        public void setOptions_num(String options_num) {
            this.options_num = options_num;
        }

        public String getSubject_code() {
            return subject_code;
        }

        public void setSubject_code(String subject_code) {
            this.subject_code = subject_code;
        }

        public List<OptionsBean> getOptions() {
            return options;
        }

        public void setOptions(List<OptionsBean> options) {
            this.options = options;
        }

        public static class SubjectTypeBean {
            /**
             * type_id : 2
             * type_name : 解答题
             */

            private String type_id;
            private String type_name;

            public String getType_id() {
                return type_id;
            }

            public void setType_id(String type_id) {
                this.type_id = type_id;
            }

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }
        }

        public static class OptionsBean {
            /**
             * id : 1
             * option_name : A
             * option_content : $y=\sqrt{x}$
             * option_content_html : <p>$y=\sqrt{x}$</p>
             */

            private String id;
            private String option_name;
            private String option_content;
            private String option_content_html;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOption_name() {
                return option_name;
            }

            public void setOption_name(String option_name) {
                this.option_name = option_name;
            }

            public String getOption_content() {
                return option_content;
            }

            public void setOption_content(String option_content) {
                this.option_content = option_content;
            }

            public String getOption_content_html() {
                return option_content_html;
            }

            public void setOption_content_html(String option_content_html) {
                this.option_content_html = option_content_html;
            }
        }
    }
}
