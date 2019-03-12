package com.example.kys_8.easyforest.plant;

import java.util.List;

/**
 * Created by kys-8 on 2018/11/1.
 */

public class IdentifyResult {

    /**
     * log_id : 17090108923949697
     * result : [{"score":0.17789168655872,"name":"杉木","baike_info":{"baike_url":"http://baike.baidu.com/item/æ\u009d\u0089æ\u009c¨/1008926","image_url":"http://imgsrc.baidu.com/baike/pic/item/d8f9d72a6059252d43688697389b033b5bb5b922.jpg","description":"杉木，学名：Cunninghamia lanceolata (Lamb.)Hook.，又名：沙木、沙树等，属柏目，柏科乔木，高达30米，胸径可达2.5-3米；树皮灰褐色；冬芽近圆形，雄球花圆锥状，雌球花单生，球果卵圆形，长2.5-5厘米，径3-4厘米；种子扁平，遮盖着种鳞，长卵形或矩圆形，花期4月，球果10月下旬成熟。主要可用于祛风止痛，散瘀止血。以及建筑、桥梁、造船、矿柱、家具等。"}}]
     */

    private long log_id;
    private List<ResultBean> result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * score : 0.17789168655872
         * name : 杉木
         * baike_info : {"baike_url":"http://baike.baidu.com/item/æ\u009d\u0089æ\u009c¨/1008926","image_url":"http://imgsrc.baidu.com/baike/pic/item/d8f9d72a6059252d43688697389b033b5bb5b922.jpg","description":"杉木，学名：Cunninghamia lanceolata (Lamb.)Hook.，又名：沙木、沙树等，属柏目，柏科乔木，高达30米，胸径可达2.5-3米；树皮灰褐色；冬芽近圆形，雄球花圆锥状，雌球花单生，球果卵圆形，长2.5-5厘米，径3-4厘米；种子扁平，遮盖着种鳞，长卵形或矩圆形，花期4月，球果10月下旬成熟。主要可用于祛风止痛，散瘀止血。以及建筑、桥梁、造船、矿柱、家具等。"}
         */

        private double score;
        private String name;
        private BaikeInfoBean baike_info;

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BaikeInfoBean getBaike_info() {
            return baike_info;
        }

        public void setBaike_info(BaikeInfoBean baike_info) {
            this.baike_info = baike_info;
        }

        public static class BaikeInfoBean {
            /**
             * baike_url : http://baike.baidu.com/item/ææ¨/1008926
             * image_url : http://imgsrc.baidu.com/baike/pic/item/d8f9d72a6059252d43688697389b033b5bb5b922.jpg
             * description : 杉木，学名：Cunninghamia lanceolata (Lamb.)Hook.，又名：沙木、沙树等，属柏目，柏科乔木，高达30米，胸径可达2.5-3米；树皮灰褐色；冬芽近圆形，雄球花圆锥状，雌球花单生，球果卵圆形，长2.5-5厘米，径3-4厘米；种子扁平，遮盖着种鳞，长卵形或矩圆形，花期4月，球果10月下旬成熟。主要可用于祛风止痛，散瘀止血。以及建筑、桥梁、造船、矿柱、家具等。
             */

            private String baike_url;
            private String image_url;
            private String description;

            public String getBaike_url() {
                return baike_url;
            }

            public void setBaike_url(String baike_url) {
                this.baike_url = baike_url;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
    }
}
