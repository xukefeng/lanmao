package cash.app.com.mymvp;

public class UploadImgBean {
    private String headImg;

    public String getHeadImg() {
        return headImg == null ? "" : headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
