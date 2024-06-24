package utils;

public class DataStore {
    private static DataStore dataStoreInstance = null;
    private String multimediaAssignmentDoc;
    private String multimediaAssignmentNameDoc;
    private String multimediaAssignmentYouTube;
    private String multimediaAssignmentNameYouTube;
    private String mediaIDYouTube;
    private String mediaIDWebLink;
    private String vqAssignmentIDYouTube;
    private String vqAssignmentNameYouTube;
    private String vqAssignmentIDVideo;
    private String vqAssignmentNameVideo;
    private String mediaIDDoc;
    private String mediaIDImage;
    private String mediaIDVideo;
    private String question1ID;
    private String question2ID;

    private DataStore() {
    }

    public static DataStore getInstance() {
        if (dataStoreInstance == null) {
            dataStoreInstance = new DataStore();
        }
        return dataStoreInstance;
    }


    public String getMediaIDYouTube() {
        return mediaIDYouTube;
    }

    public void setMediaIDYouTube(String mediaIDYouTube) {
        this.mediaIDYouTube = mediaIDYouTube;
    }


    public String getMediaIDWebLink() {
        return mediaIDWebLink;
    }

    public void setMediaIDWebLink(String mediaIDWebLink) {
        this.mediaIDWebLink = mediaIDWebLink;
    }

    public String getMediaIDDoc() {
        return mediaIDDoc;
    }

    public void setMediaIDDoc(String mediaIDDoc) {
        this.mediaIDDoc = mediaIDDoc;
    }

    public String getMediaIDImage() {
        return mediaIDImage;
    }

    public void setMediaIDImage(String mediaIDImage) {
        this.mediaIDImage = mediaIDImage;
    }

    public String getMediaIDVideo() {
        return mediaIDVideo;
    }

    public void setMediaIDVideo(String mediaIDVideo) {
        this.mediaIDVideo = mediaIDVideo;
    }

    public String getVqAssignmentIDYouTube() {
        return vqAssignmentIDYouTube;
    }

    public void setVqAssignmentIDYouTube(String vqAssignmentIDYouTube) {
        this.vqAssignmentIDYouTube = vqAssignmentIDYouTube;
    }

    public String getVqAssignmentNameYouTube() {
        return vqAssignmentNameYouTube;
    }

    public void setVqAssignmentNameYouTube(String vqAssignmentNameYouTube) {
        this.vqAssignmentNameYouTube = vqAssignmentNameYouTube;
    }

    public String getVqAssignmentIDVideo() {
        return vqAssignmentIDVideo;
    }

    public void setVqAssignmentIDVideo(String vqAssignmentIDVideo) {
        this.vqAssignmentIDVideo = vqAssignmentIDVideo;
    }

    public String getVqAssignmentNameVideo() {
        return vqAssignmentNameVideo;
    }

    public void setVqAssignmentNameVideo(String vqAssignmentNameVideo) {
        this.vqAssignmentNameVideo = vqAssignmentNameVideo;
    }

    public String getMultimediaAssignmentDoc() {
        return multimediaAssignmentDoc;
    }

    public void setMultimediaAssignmentDoc(String multimediaAssignmentDoc) {
        this.multimediaAssignmentDoc = multimediaAssignmentDoc;
    }

    public String getMultimediaAssignmentNameDoc() {
        return multimediaAssignmentNameDoc;
    }

    public void setMultimediaAssignmentNameDoc(String multimediaAssignmentNameDoc) {
        this.multimediaAssignmentNameDoc = multimediaAssignmentNameDoc;
    }

    public String getMultimediaAssignmentYouTube() {
        return multimediaAssignmentYouTube;
    }

    public void setMultimediaAssignmentYouTube(String multimediaAssignmentYouTube) {
        this.multimediaAssignmentYouTube = multimediaAssignmentYouTube;
    }

    public String getMultimediaAssignmentNameYouTube() {
        return multimediaAssignmentNameYouTube;
    }

    public void setMultimediaAssignmentNameYouTube(String multimediaAssignmentNameYouTube) {
        this.multimediaAssignmentNameYouTube = multimediaAssignmentNameYouTube;
    }


    public String getQuestion1ID() {
        return question1ID;
    }

    public void setQuestion1ID(String question1ID) {
        this.question1ID = question1ID;
    }

    public String getQuestion2ID() {
        return question2ID;
    }

    public void setQuestion2ID(String question2ID) {
        this.question2ID = question2ID;
    }
}


