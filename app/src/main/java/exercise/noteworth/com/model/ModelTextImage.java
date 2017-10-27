package exercise.noteworth.com.model;

public class ModelTextImage {
    public String text;
    public Integer drawable;
    public Object object;

    public ModelTextImage() {
    }

    public ModelTextImage(String text, Integer drawable, Object object) {
        this.text = text;
        this.drawable = drawable;
        this.object = object;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getDrawable() {
        return drawable;
    }

    public void setDrawable(Integer drawable) {
        this.drawable = drawable;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
