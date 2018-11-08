package es.academy.solidgear.surveyx.models;

public class SurveyModel {
    private int id;
    private boolean already_done;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private String image;
    private int questions[];
    private float distanceToCurrentPosition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getAlreadyDone() {
        return this.already_done;
    }

    public void setAlreadyDone(boolean already_done) {
        this.already_done = already_done;
    }

    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int[] getQuestions() {
        return questions;
    }

    public void setQuestions(int[] questions) {
        this.questions = questions;
    }

    public boolean hasCoordinates() {
        return (latitude != 0.0 && longitude != 0.0);
    }

    public float getDistanceToCurrentPosition() {
        return distanceToCurrentPosition;
    }

    public void setDistanceToCurrentPosition(float distanceToCurrentPosition) {
        this.distanceToCurrentPosition = distanceToCurrentPosition;
    }
}
