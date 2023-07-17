package xinmei.report.data.bean;

import xinmei.report.data.enums.RTBType;
import xinmei.report.data.enums.State;
import xinmei.report.data.enums.TimeZone;

public class PartnerInformation {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_information.id
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_information.type
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    private RTBType type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_information.name
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_information.state
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    private State state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_information.utc
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    private TimeZone utc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_information.app_key
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    private String appKey;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_information.app_key2
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    private String appKey2;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_information.id
     *
     * @return the value of partner_information.id
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_information.id
     *
     * @param id the value for partner_information.id
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_information.type
     *
     * @return the value of partner_information.type
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public RTBType getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_information.type
     *
     * @param type the value for partner_information.type
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public void setType(RTBType type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_information.name
     *
     * @return the value of partner_information.name
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_information.name
     *
     * @param name the value for partner_information.name
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_information.state
     *
     * @return the value of partner_information.state
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public State getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_information.state
     *
     * @param state the value for partner_information.state
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_information.utc
     *
     * @return the value of partner_information.utc
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public TimeZone getUtc() {
        return utc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_information.utc
     *
     * @param utc the value for partner_information.utc
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public void setUtc(TimeZone utc) {
        this.utc = utc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_information.app_key
     *
     * @return the value of partner_information.app_key
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_information.app_key
     *
     * @param appKey the value for partner_information.app_key
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_information.app_key2
     *
     * @return the value of partner_information.app_key2
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public String getAppKey2() {
        return appKey2;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_information.app_key2
     *
     * @param appKey2 the value for partner_information.app_key2
     *
     * @mbg.generated Thu Jun 29 10:40:43 CST 2023
     */
    public void setAppKey2(String appKey2) {
        this.appKey2 = appKey2;
    }
}