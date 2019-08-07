package cash.app.com.mymvp;

import java.io.Serializable;

public class LoginBean  implements Serializable{

    /**
     * userId : 15
     * token : 666e05b3d1b942c8a9be31ebd0d32f61
     * refreshToken : 59f43b20f80241cabc74ba56cf12d7d3
     */

    private int userId;
    private String token;
    private String refreshToken;
    private String realName;
    private String headImg;
    private String langType;
    private int registerAddr;
    private int userRegisterCoordinate;

    public int getRegisterAddr() {
        return registerAddr;
    }

    public int getUserRegisterCoordinate() {
        return userRegisterCoordinate;
    }

    public int getUserId() {
        return userId;
    }

    public String getHeadImg() {
        return headImg == null ? "" : headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public String getRealName() {
        return realName == null ? "" : realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRefreshToken() {
        return refreshToken == null ? "" : refreshToken;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public String getLangType() {
        return langType == null ? "" : langType;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
