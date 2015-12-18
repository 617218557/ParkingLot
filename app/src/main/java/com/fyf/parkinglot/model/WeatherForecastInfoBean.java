package com.fyf.parkinglot.model;

import java.util.List;

/**
 * Created by fengyifei on 15/12/18.
 */
public class WeatherForecastInfoBean {

    private int error;
    private String status;
    private String date;
    private List<Results> results;

    public class Results {
        private String currentCity;
        private String pm25;
        private List<Index> index;
        private List<WeatherData> weather_data;

        public class Index {
            private String title;
            private String zs;
            private String tipt;
            private String des;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getZs() {
                return zs;
            }

            public void setZs(String zs) {
                this.zs = zs;
            }

            public String getTipt() {
                return tipt;
            }

            public void setTipt(String tipt) {
                this.tipt = tipt;
            }

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }
        }

       public class WeatherData {
            private String date;
            private String dayPictureUrl;
            private String nightPictureUrl;
            private String weather;
            private String wind;
            private String temperature;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getDayPictureUrl() {
                return dayPictureUrl;
            }

            public void setDayPictureUrl(String dayPictureUrl) {
                this.dayPictureUrl = dayPictureUrl;
            }

            public String getNightPictureUrl() {
                return nightPictureUrl;
            }

            public void setNightPictureUrl(String nightPictureUrl) {
                this.nightPictureUrl = nightPictureUrl;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }
        }
        public String getCurrentCity() {
            return currentCity;
        }

        public void setCurrentCity(String currentCity) {
            this.currentCity = currentCity;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public List<Index> getIndex() {
            return index;
        }

        public void setIndex(List<Index> index) {
            this.index = index;
        }

        public List<WeatherData> getWeather_data() {
            return weather_data;
        }

        public void setWeather_data(List<WeatherData> weather_data) {
            this.weather_data = weather_data;
        }

    }
    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

/*  json的格式
    {
        "error": 0,
            "status": "success",
            "date": "2015-12-18",
            "results": [
        {
            "currentCity": "西安",
                "pm25": "105",
                "index": [
            {
                "title": "穿衣",
                    "zs": "冷",
                    "tipt": "穿衣指数",
                    "des": "天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"
            },
            {
                "title": "洗车",
                    "zs": "较适宜",
                    "tipt": "洗车指数",
                    "des": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
            },
            {
                "title": "旅游",
                    "zs": "适宜",
                    "tipt": "旅游指数",
                    "des": "天气较好，气温稍低，会感觉稍微有点凉，不过也是个好天气哦。适宜旅游，可不要错过机会呦！"
            },
            {
                "title": "感冒",
                    "zs": "较易发",
                    "tipt": "感冒指数",
                    "des": "天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。"
            },
            {
                "title": "运动",
                    "zs": "较适宜",
                    "tipt": "运动指数",
                    "des": "阴天，较适宜进行各种户内外运动。"
            },
            {
                "title": "紫外线强度",
                    "zs": "最弱",
                    "tipt": "紫外线强度指数",
                    "des": "属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"
            }
            ],
            "weather_data": [
            {
                "date": "周五 12月18日 (实时：3℃)",
                    "dayPictureUrl": "http://api.map.baidu.com/images/weather/day/yin.png",
                    "nightPictureUrl": "http://api.map.baidu.com/images/weather/night/yin.png",
                    "weather": "阴",
                    "wind": "东风微风",
                    "temperature": "5 ~ -3℃"
            },
            {
                "date": "周六",
                    "dayPictureUrl": "http://api.map.baidu.com/images/weather/day/yin.png",
                    "nightPictureUrl": "http://api.map.baidu.com/images/weather/night/duoyun.png",
                    "weather": "阴转多云",
                    "wind": "东风微风",
                    "temperature": "4 ~ -3℃"
            },
            {
                "date": "周日",
                    "dayPictureUrl": "http://api.map.baidu.com/images/weather/day/qing.png",
                    "nightPictureUrl": "http://api.map.baidu.com/images/weather/night/yin.png",
                    "weather": "晴转阴",
                    "wind": "东风微风",
                    "temperature": "6 ~ -3℃"
            },
            {
                "date": "周一",
                    "dayPictureUrl": "http://api.map.baidu.com/images/weather/day/duoyun.png",
                    "nightPictureUrl": "http://api.map.baidu.com/images/weather/night/duoyun.png",
                    "weather": "多云",
                    "wind": "东风微风",
                    "temperature": "4 ~ -1℃"
            }
            ]
        }
        ]
    }
    */
}
