package exercise.noteworth.com.model;


public class ModelTextSpaceLeft {
    public String text;

    public ModelTextSpaceLeft() {
    }

    public String getTime() {
        String[] separated = text.split(":");
        return "<b>" + separated[0] + "</b> " + separated[1] + ":" + separated[2] + ":" + separated[3];
    }

    public ModelTextSpaceLeft(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
