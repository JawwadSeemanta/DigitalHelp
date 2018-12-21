package com.drifters.help.DataModel;

public class RequestModel {
    private String Request_ID;
    private String Requester_Name;
    private String Requester_Phone;
    private double lat, lang;
    private String Request_type;
    private int UserID;
    private String status;
    private String submission_time;
    private String ImageURL;

    public RequestModel() {
    }

    public RequestModel(String request_ID, String requester_Name, String requester_Phone, double lat, double lang, String request_type, int userID, String status, String submission_time, String imageURL) {
        Request_ID = request_ID;
        Requester_Name = requester_Name;
        Requester_Phone = requester_Phone;
        this.lat = lat;
        this.lang = lang;
        Request_type = request_type;
        UserID = userID;
        this.status = status;
        this.submission_time = submission_time;
        ImageURL = imageURL;
    }

    public String getRequest_ID() {
        return Request_ID;
    }

    public void setRequest_ID(String request_ID) {
        Request_ID = request_ID;
    }

    public String getRequester_Name() {
        return Requester_Name;
    }

    public void setRequester_Name(String requester_Name) {
        Requester_Name = requester_Name;
    }

    public String getRequester_Phone() {
        return Requester_Phone;
    }

    public void setRequester_Phone(String requester_Phone) {
        Requester_Phone = requester_Phone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public String getRequest_type() {
        return Request_type;
    }

    public void setRequest_type(String request_type) {
        Request_type = request_type;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmission_time() {
        return submission_time;
    }

    public void setSubmission_time(String submission_time) {
        this.submission_time = submission_time;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
