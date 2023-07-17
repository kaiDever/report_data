package xinmei.report.data.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import xinmei.report.data.bean.ApiToken;
import xinmei.report.data.bean.ApiTokenExample;

public interface ApiTokenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    long countByExample(ApiTokenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    int deleteByExample(ApiTokenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    int insert(ApiToken record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    int insertSelective(ApiToken record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    List<ApiToken> selectByExample(ApiTokenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    ApiToken selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    int updateByExampleSelective(@Param("record") ApiToken record, @Param("example") ApiTokenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    int updateByExample(@Param("record") ApiToken record, @Param("example") ApiTokenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    int updateByPrimaryKeySelective(ApiToken record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table api_token
     *
     * @mbg.generated Wed Jun 14 15:20:18 CST 2023
     */
    int updateByPrimaryKey(ApiToken record);
}