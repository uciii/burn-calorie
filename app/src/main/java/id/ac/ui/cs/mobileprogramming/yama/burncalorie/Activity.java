package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

public class Activity {
    String title;
    String info;
    String description;
    String type;
    String url;
    Integer calorie;

    public Activity(String title, String description, String type, String url, Integer calorie) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.url = url;
        this.calorie = calorie;

        if (description.length() < 150) this.info = description + "...";
        else this.info = description.substring(0, 150) + "...";
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public Integer getCalorie() {
        return calorie;
    }
}
