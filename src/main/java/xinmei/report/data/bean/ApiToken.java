package xinmei.report.data.bean;

public class ApiToken {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api_token.id
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api_token.name
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api_token.access_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    private String accessToken;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column api_token.refresh_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    private String refreshToken;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api_token.id
     *
     * @return the value of api_token.id
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api_token.id
     *
     * @param id the value for api_token.id
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api_token.name
     *
     * @return the value of api_token.name
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api_token.name
     *
     * @param name the value for api_token.name
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api_token.access_token
     *
     * @return the value of api_token.access_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api_token.access_token
     *
     * @param accessToken the value for api_token.access_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column api_token.refresh_token
     *
     * @return the value of api_token.refresh_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column api_token.refresh_token
     *
     * @param refreshToken the value for api_token.refresh_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}