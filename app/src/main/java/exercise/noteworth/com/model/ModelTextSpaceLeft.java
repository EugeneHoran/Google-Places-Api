package exercise.noteworth.com.model;


public class ModelTextSpaceLeft {
    public String text;

    public ModelTextSpaceLeft() {
    }

    public String getTime() {
        if (text == null) {
            return "";
        }
        String[] separated = text.split(":");
        String day = separated[0];
        String dayTimeOrOpen = separated[1];
        if (separated.length == 2) {
            return "<b>" + day + "</b> " + dayTimeOrOpen;
        } else if (separated.length == 4) {
            return "<b>" + day + "</b> " + dayTimeOrOpen + ":" + separated[2] + ":" + separated[3];
        }
        return text;
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
