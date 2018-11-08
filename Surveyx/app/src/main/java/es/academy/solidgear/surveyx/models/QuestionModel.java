package es.academy.solidgear.surveyx.models;

import java.io.Serializable;
import java.util.List;

public class QuestionModel implements Serializable {
    private int id;
    private String image;
    private String question_type;
    private String text;
    private String description;
    private List<OptionModel> choices;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return question_type;
    }

    public void setType(String type) {
        this.question_type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OptionModel> getChoices() {
        return choices;
    }

    public void setChoices(List<OptionModel> choices) {
        this.choices = choices;
    }
}
